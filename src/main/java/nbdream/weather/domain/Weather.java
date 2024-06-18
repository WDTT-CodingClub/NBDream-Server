package nbdream.weather.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbdream.farm.domain.Farm;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int precipitationProbability;

    private int precipitationAmount;

    private int humidity;

    private int windSpeed;

    private int highestTemperature;

    private int lowestTemperature;

    private int currentTemperature;

    @Enumerated(EnumType.STRING)
    private Sky sky;

    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farm_id")
    private Farm farm;

    public Weather(final int precipitationProbability, final int precipitationAmount, final int humidity, final int windSpeed, final int highestTemperature, final int lowestTemperature, final int currentTemperature, final Sky sky, final LocalDate date, final Farm farm) {
        this.precipitationProbability = precipitationProbability;
        this.precipitationAmount = precipitationAmount;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.highestTemperature = highestTemperature;
        this.lowestTemperature = lowestTemperature;
        this.currentTemperature = currentTemperature;
        this.sky = sky;
        this.date = date;
        this.farm = farm;
    }
}
