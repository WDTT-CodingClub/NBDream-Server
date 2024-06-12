package nbdream.accountBook.repository.specifications;

import nbdream.accountBook.domain.AccountBookCategory;
import nbdream.accountBook.domain.AccountBookHistory;
import nbdream.accountBook.domain.TransactionType;
import nbdream.accountBook.domain.Sort;
import nbdream.accountBook.service.dto.GetAccountBookListReqDto;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AccountBookHistorySpecifications {

    public static Specification<AccountBookHistory> withFilters(GetAccountBookListReqDto reqDto, Long memberId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("accountBook").get("member").get("id"), memberId));

            // 카테고리
            if (reqDto.getCategory() != null) {
                AccountBookCategory categoryEnum = AccountBookCategory.fromValue(reqDto.getCategory());
                predicates.add(criteriaBuilder.equal(root.get("accountBookCategory"), categoryEnum));
            }

            // 날짜
            if (reqDto.getStart() != null) {
                LocalDate startDate = LocalDate.parse(reqDto.getStart());
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), startDate));
            }
            if (reqDto.getEnd() != null) {
                LocalDate endDate = LocalDate.parse(reqDto.getEnd());
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("date"), endDate));
            }

            // 수입/지출
            if (reqDto.getCost() != null) {
                TransactionType transactionTypeEnum = TransactionType.fromValue(reqDto.getCost());
                predicates.add(criteriaBuilder.equal(root.get("transactionType"), transactionTypeEnum));
            }

            // 날짜순
            if (reqDto.getSort() != null) {
                Sort sortEnum = Sort.fromValue(reqDto.getSort());
                if (sortEnum == Sort.EARLIEST) {
                    query.orderBy(criteriaBuilder.desc(root.get("date")));
                } else if (sortEnum == Sort.OLDEST) {
                    query.orderBy(criteriaBuilder.asc(root.get("date")));
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
