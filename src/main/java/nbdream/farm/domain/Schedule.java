package nbdream.farm.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbdream.common.domain.BaseEntity;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farm_id")
    private Farm farm;

    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

    private String memo;

    public Schedule(final Farm farm, final String title, final LocalDate startDate, final LocalDate endDate, final String memo) {
        this.farm = farm;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.memo = memo;
    }
}
