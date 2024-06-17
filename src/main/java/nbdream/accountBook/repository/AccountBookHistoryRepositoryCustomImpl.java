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
    public List<AccountBookHistory> findByMemberIdAndCursor(Long memberId, Long cursorId, int PAGE_SIZE, GetAccountBookListReqDto request) {
        QAccountBookHistory accountBookHistory = QAccountBookHistory.accountBookHistory;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(accountBookHistory.accountBook.member.id.eq(memberId));

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

        if (request.getTransactionType() != null && StringUtils.isNotBlank(request.getTransactionType())) {
            TransactionType transactionTypeEnum = TransactionType.fromValue(request.getTransactionType());
            builder.and(accountBookHistory.transactionType.eq(transactionTypeEnum));
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
            if(isDESC){
                query.orderBy(accountBookHistory.id.desc()).limit(PAGE_SIZE);
            }else{
                query.orderBy(accountBookHistory.id.asc()).limit(PAGE_SIZE);
            }
        } else {
            if (isDESC) {
                builder.and(accountBookHistory.id.lt(cursorId));
                query.orderBy(accountBookHistory.id.desc());
            } else {
                builder.and(accountBookHistory.id.gt(cursorId));
                query.orderBy(accountBookHistory.id.asc());
            }

            query.where(builder).limit(PAGE_SIZE);
        }

        return query.fetch();
    }


    @Override
    public Long getTotalRevenue(Long memberId, GetAccountBookListReqDto request) {
        QAccountBookHistory accountBookHistory = QAccountBookHistory.accountBookHistory;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(accountBookHistory.accountBook.member.id.eq(memberId))
                .and(accountBookHistory.transactionType.eq(TransactionType.REVENUE));

        applyFilters(builder, request);

        Long totalRevenue = queryFactory.select(accountBookHistory.amount.sum())
                .from(accountBookHistory)
                .where(builder)
                .fetchOne();

        return totalRevenue != null ? totalRevenue : 0L;
    }

    @Override
    public Long getTotalExpense(Long memberId, GetAccountBookListReqDto request) {
        QAccountBookHistory accountBookHistory = QAccountBookHistory.accountBookHistory;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(accountBookHistory.accountBook.member.id.eq(memberId))
                .and(accountBookHistory.transactionType.eq(TransactionType.EXPENSE));

        applyFilters(builder, request);

        Long totalExpense = queryFactory.select(accountBookHistory.amount.sum())
                .from(accountBookHistory)
                .where(builder)
                .fetchOne();

        return totalExpense != null ? totalExpense : 0L;
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
