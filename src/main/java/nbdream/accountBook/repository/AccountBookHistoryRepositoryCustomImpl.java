package nbdream.accountBook.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import nbdream.accountBook.domain.*;
import nbdream.accountBook.service.dto.GetAccountBookListReqDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RequiredArgsConstructor
public class AccountBookHistoryRepositoryCustomImpl implements AccountBookHistoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<AccountBookHistory> findByMemberIdAndCursor(Long memberId, Long cursorId, int maxResults, GetAccountBookListReqDto reqDto) {
        QAccountBookHistory accountBookHistory = QAccountBookHistory.accountBookHistory;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(accountBookHistory.accountBook.member.id.eq(memberId));

        if (reqDto.getCategory() != null && StringUtils.isNotBlank(reqDto.getCategory())) {
            AccountBookCategory categoryEnum = AccountBookCategory.fromValue(reqDto.getCategory());
            builder.and(accountBookHistory.accountBookCategory.eq(categoryEnum));
        }

        if (reqDto.getStart() != null && StringUtils.isNotBlank(reqDto.getStart())) {
            LocalDate startDate = LocalDate.parse(reqDto.getStart());
            LocalDateTime startDateTime = startDate.atStartOfDay();
            builder.and(accountBookHistory.dateTime.goe(startDateTime));
        }
        if (reqDto.getEnd() != null && StringUtils.isNotBlank(reqDto.getEnd())) {
            LocalDate endDate = LocalDate.parse(reqDto.getEnd());
            LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
            builder.and(accountBookHistory.dateTime.loe(endDateTime));
        }

        if (reqDto.getTransactionType() != null && StringUtils.isNotBlank(reqDto.getTransactionType())) {
            TransactionType transactionTypeEnum = TransactionType.fromValue(reqDto.getTransactionType());
            builder.and(accountBookHistory.transactionType.eq(transactionTypeEnum));
        }

        boolean isDESC = true;  // 내림차순 or 오름차순 확인
        if (reqDto.getSort() != null && StringUtils.isNotBlank(reqDto.getSort())) {
            if (Sort.fromValue(reqDto.getSort()) == Sort.EARLIEST) {
                isDESC = false;
            }
        }

        JPAQuery<AccountBookHistory> query = queryFactory.selectFrom(accountBookHistory).where(builder);

        if (cursorId == null || cursorId == 0) {
            query.orderBy(accountBookHistory.id.desc()).limit(2);
        } else {
            if (isDESC) {
                builder.and(accountBookHistory.id.lt(cursorId));
                query.orderBy(accountBookHistory.dateTime.desc());
            } else {
                builder.and(accountBookHistory.id.gt(cursorId));
                query.orderBy(accountBookHistory.dateTime.asc());
            }

            query.where(builder).limit(maxResults);
        }

        return query.fetch();
    }
}
