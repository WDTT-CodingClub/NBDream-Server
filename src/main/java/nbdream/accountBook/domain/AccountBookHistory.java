package nbdream.accountBook.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbdream.common.domain.BaseEntity;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AccountBookHistory extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ledger_id")
    private AccountBook accountBook;

    @Enumerated(EnumType.STRING)
    private AccountBookCategory accountBookCategory;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    private int amount;

    private String content;

    private LocalDate date;

    public AccountBookHistory(final AccountBook accountBook, final AccountBookCategory accountBookCategory, final TransactionType transactionType, final int amount, final String content, final LocalDate date) {
        this.accountBook = accountBook;
        this.accountBookCategory = accountBookCategory;
        this.transactionType = transactionType;
        this.amount = amount;
        this.content = content;
        this.date = date;
    }
}