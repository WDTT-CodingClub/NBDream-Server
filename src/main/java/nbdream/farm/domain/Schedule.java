package nbdream.farm.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.type.DateTime;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nbdream.common.entity.BaseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
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

    private String category;

    private boolean isAlarmOn;

    private LocalDateTime alarmDateTime;

    public Schedule(final Farm farm, final String title, final LocalDate startDate, final LocalDate endDate, final String memo, final String category, final boolean isAlarmOn, final LocalDateTime alarmDateTime) {
        this.farm = farm;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.memo = memo;
        this.category = category;
        this.isAlarmOn = isAlarmOn;
        this.alarmDateTime = alarmDateTime;
    }

    public Schedule update(final String title, final LocalDate startDate, final LocalDate endDate, final String memo, final String category, final boolean isAlarmOn, final LocalDateTime alarmDateTime) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.memo = memo;
        this.category = category;
        this.isAlarmOn = isAlarmOn;
        this.alarmDateTime = alarmDateTime;
        return this;
    }
}
