package nbdream.ledger.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbdream.common.entity.BaseEntity;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LedgerHistory extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ledger_id")
    private Ledger ledger;

    @Enumerated(EnumType.STRING)
    private LedgerCategory ledgerCategory;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    private int amount;

    private String content;

    private LocalDate date;

    public LedgerHistory(final Ledger ledger, final LedgerCategory ledgerCategory, final TransactionType transactionType, final int amount, final String content, final LocalDate date) {
        this.ledger = ledger;
        this.ledgerCategory = ledgerCategory;
        this.transactionType = transactionType;
        this.amount = amount;
        this.content = content;
        this.date = date;
    }
}