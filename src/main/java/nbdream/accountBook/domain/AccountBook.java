package nbdream.accountBook.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbdream.common.entity.BaseEntity;
import nbdream.member.domain.Member;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AccountBook extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "accountBook", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccountBookHistory> accountBookHistoryList = new ArrayList<>();

    public AccountBook(final Member member) {
        this.member = member;
    }

    // 장부 로직 관련 메서드를 여기에
    //총 수입
    public Long getTotalRevenue(List<AccountBookHistory> list){
        Long totalAmount = getTotalAmount(list, TransactionType.REVENUE);
        return totalAmount;
    }
    //총 지출
    public Long getTotalExpense(List<AccountBookHistory> list){
        Long totalAmount = getTotalAmount(list, TransactionType.EXPENSE);
        return -totalAmount;
    }

    public Long getTotalAmount(List<AccountBookHistory> list, TransactionType transactionType){
        Long totalAmount = 0L;
        for (AccountBookHistory history : list) {
            if(history.getTransactionType() == transactionType){
                totalAmount += history.getAmount();
            }
        }
        return totalAmount;
    }
}
