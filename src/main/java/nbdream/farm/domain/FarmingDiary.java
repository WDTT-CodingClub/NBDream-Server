package nbdream.farm.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbdream.common.entity.BaseEntity;
import nbdream.farm.service.dto.farmingdiary.FarmingDiaryRequest;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farm_id")
    private Farm farm;

    @Builder
    public FarmingDiary(final LocalDate date, final String crop, final String weatherForecast, final int workingPeopleNumber,
                        final int workingTime, final int workingArea, final String memo, final Farm farm) {
        this.date = date;
        this.crop = crop;
        this.weatherForecast = weatherForecast;
        this.workingPeopleNumber = workingPeopleNumber;
        this.workingTime = workingTime;
        this.workingArea = workingArea;
        this.memo = memo;
        this.farm = farm;
    }

    public void update(final FarmingDiaryRequest request) {
        this.date = request.getDate();
        this.crop = request.getCrop();
        this.weatherForecast = request.getWeatherForecast();
        this.workingPeopleNumber = request.getWorkingPeopleNumber();
        this.workingTime = request.getWorkingTime();
        this.workingArea = request.getWorkingArea();
        this.memo = request.getMemo();
    }
}
