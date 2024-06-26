package nbdream.farm.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbdream.common.entity.BaseEntity;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FarmingDiary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private String crop;

    private String weatherForecast;

    private int workingPeopleNumber;

    private int workingTime;

    private int workingArea;

    private String memo;

    @Builder
    public FarmingDiary(final LocalDate date, final String crop, final String weatherForecast, final int workingPeopleNumber, final int workingTime, final int workingArea, final String memo) {
        this.date = date;
        this.crop = crop;
        this.weatherForecast = weatherForecast;
        this.workingPeopleNumber = workingPeopleNumber;
        this.workingTime = workingTime;
        this.workingArea = workingArea;
        this.memo = memo;
    }
}
