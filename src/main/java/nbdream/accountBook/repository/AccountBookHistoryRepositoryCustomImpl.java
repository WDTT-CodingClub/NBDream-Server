package nbdream.accountBook.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import nbdream.accountBook.domain.*;
import nbdream.accountBook.service.dto.GetAccountBookListReqDto;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;



@RequiredArgsConstructor
public class AccountBookHistoryRepositoryCustomImpl implements AccountBookHistoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public List<AccountBookHistory> findByFilterAndCursor(Long memberId, Long cursorId, int PAGE_SIZE, GetAccountBookListReqDto request) {
        QAccountBookHistory accountBookHistory = QAccountBookHistory.accountBookHistory;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(accountBookHistory.accountBook.member.id.eq(memberId));
        applyFilters(builder, request);

        if (request.getTransactionType() != null && StringUtils.isNotBlank(request.getTransactionType())) {
            builder.and(accountBookHistory.transactionType.eq(TransactionType.fromValue(request.getTransactionType())));
        }

        boolean isDESC = true;  // 내림차순 or 오름차순 확인
        if (request.getSort() != null && StringUtils.isNotBlank(request.getSort())) {
            if (Sort.fromValue(request.getSort()) == Sort.OLDEST) {
                isDESC = false;
            }
        }

        JPAQuery<AccountBookHistory> query = queryFactory.selectFrom(accountBookHistory).where(builder);
        //커서가 0이면 처음 요청한것
            //0이고 최신순 or 오래된 순
        if (cursorId == null || cursorId == 0) {
            if (isDESC) {
                query.orderBy(accountBookHistory.dateTime.desc(), accountBookHistory.id.desc()).limit(PAGE_SIZE);
            } else {
                query.orderBy(accountBookHistory.dateTime.asc(), accountBookHistory.id.asc()).limit(PAGE_SIZE);
            }
        } else {
            // cursorId에 해당하는 데이터 찾기
            AccountBookHistory cursorHistory = queryFactory.selectFrom(accountBookHistory)
                    .where(accountBookHistory.id.eq(cursorId))
                    .fetchOne();
            if (cursorHistory != null) {
                LocalDateTime cursorDateTime = cursorHistory.getDateTime();
                if (isDESC) {
                    builder.and(accountBookHistory.dateTime.lt(cursorDateTime)
                            .or(accountBookHistory.dateTime.eq(cursorDateTime).and(accountBookHistory.id.lt(cursorId))));
                    query.orderBy(accountBookHistory.dateTime.desc(), accountBookHistory.id.desc());
                } else {
                    builder.and(accountBookHistory.dateTime.gt(cursorDateTime)
                            .or(accountBookHistory.dateTime.eq(cursorDateTime).and(accountBookHistory.id.gt(cursorId))));
                    query.orderBy(accountBookHistory.dateTime.asc(), accountBookHistory.id.asc());
                }
                query.where(builder).limit(PAGE_SIZE);
            }
        }

        return query.fetch();
    }

    @Override
    public List<AccountBookHistory> findAllByFilter(Long memberId, GetAccountBookListReqDto request){
        QAccountBookHistory accountBookHistory = QAccountBookHistory.accountBookHistory;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(accountBookHistory.accountBook.member.id.eq(memberId));

        applyFilters(builder, request);

        return  queryFactory.selectFrom(accountBookHistory)
                .where(builder)
                .fetch();
    }

    private void applyFilters(BooleanBuilder builder, GetAccountBookListReqDto request) {
        QAccountBookHistory accountBookHistory = QAccountBookHistory.accountBookHistory;

        if (request.getCategory() != null && StringUtils.isNotBlank(request.getCategory())) {
            AccountBookCategory categoryEnum = AccountBookCategory.fromValue(request.getCategory());
            builder.and(accountBookHistory.accountBookCategory.eq(categoryEnum));
        }

        if (request.getStart() != null && StringUtils.isNotBlank(request.getStart())) {
            LocalDate startDate = LocalDate.parse(request.getStart());
            LocalDateTime startDateTime = startDate.atStartOfDay();
            builder.and(accountBookHistory.dateTime.goe(startDateTime));
        }
        if (request.getEnd() != null && StringUtils.isNotBlank(request.getEnd())) {
            LocalDate endDate = LocalDate.parse(request.getEnd());
            LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
            builder.and(accountBookHistory.dateTime.loe(endDateTime));
        }
    }
}
