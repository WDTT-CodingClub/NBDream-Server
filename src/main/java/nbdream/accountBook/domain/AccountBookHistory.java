package nbdream.accountBook.domain;

import jakarta.persistence.*;
import lombok.*;
import nbdream.common.entity.BaseEntity;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class AccountBookHistory extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountBook_id")
    private AccountBook accountBook;

    @Enumerated(EnumType.STRING)
    private AccountBookCategory accountBookCategory;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    private Long amount;

    private String content;

    private LocalDateTime dateTime;

    @Builder
    public AccountBookHistory(final AccountBook accountBook, final AccountBookCategory accountBookCategory, final TransactionType transactionType, final Long amount, final String content, final LocalDateTime dateTime) {
        this.accountBook = accountBook;
        this.accountBookCategory = accountBookCategory;
        this.transactionType = transactionType;
        this.amount = amount;
        this.content = content;
        this.dateTime = dateTime;
    }

    public void update(final AccountBook accountBook, final AccountBookCategory accountBookCategory, final TransactionType transactionType, final Long amount, final String content, final LocalDateTime dateTime) {
        this.accountBook = accountBook;
        this.accountBookCategory = accountBookCategory;
        this.transactionType = transactionType;
        this.amount = amount;
        this.content = content;
        this.dateTime = dateTime;
    }

    public String getKoreanDayOfWeek() {
        DayOfWeek dayOfWeek = this.dateTime.getDayOfWeek();
        return dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN);
    }
}