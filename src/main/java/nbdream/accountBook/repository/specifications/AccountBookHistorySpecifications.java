package nbdream.accountBook.repository.specifications;

import nbdream.accountBook.domain.AccountBookCategory;
import nbdream.accountBook.domain.AccountBookHistory;
import nbdream.accountBook.domain.TransactionType;
import nbdream.accountBook.domain.Sort;
import nbdream.accountBook.service.dto.GetAccountBookListReqDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AccountBookHistorySpecifications {

    public static Specification<AccountBookHistory> withFilters(GetAccountBookListReqDto reqDto, Long memberId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("accountBook").get("member").get("id"), memberId));

            // 카테고리
            if (reqDto.getCategory() != null && StringUtils.isNotBlank(reqDto.getCategory())) {
                AccountBookCategory categoryEnum = AccountBookCategory.fromValue(reqDto.getCategory());
                predicates.add(criteriaBuilder.equal(root.get("accountBookCategory"), categoryEnum));
            }

            // 날짜
            if (reqDto.getStart() != null && StringUtils.isNotBlank(reqDto.getStart())) {
                LocalDate startDate = LocalDate.parse(reqDto.getStart());
                LocalDateTime startDateTime = startDate.atStartOfDay();
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dateTime"), startDateTime));
            }
            if (reqDto.getEnd() != null && StringUtils.isNotBlank(reqDto.getEnd())) {
                LocalDate endDate = LocalDate.parse(reqDto.getEnd());
                LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dateTime"), endDateTime));
            }

            // 수입/지출
            if (reqDto.getTransactionType() != null && StringUtils.isNotBlank(reqDto.getTransactionType())) {
                TransactionType transactionTypeEnum = TransactionType.fromValue(reqDto.getTransactionType());
                predicates.add(criteriaBuilder.equal(root.get("transactionType"), transactionTypeEnum));
            }

            // 날짜순
            if (reqDto.getSort() != null && StringUtils.isNotBlank(reqDto.getSort())) {
                Sort sortEnum = Sort.fromValue(reqDto.getSort());
                if (sortEnum == Sort.EARLIEST) {
                    query.orderBy(criteriaBuilder.desc(root.get("dateTime")));
                } else if (sortEnum == Sort.OLDEST) {
                    query.orderBy(criteriaBuilder.asc(root.get("dateTime")));
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
