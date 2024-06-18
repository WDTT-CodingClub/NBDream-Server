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
public class SimpleWeather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int highestTemperature;

    private int lowestTemperature;

    @Enumerated(EnumType.STRING)
    private Sky sky;

    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farm_id")
    private Farm farm;

    public SimpleWeather(final int highestTemperature, final int lowestTemperature, final Sky sky, final LocalDate date, final Farm farm) {
        this.highestTemperature = highestTemperature;
        this.lowestTemperature = lowestTemperature;
        this.sky = sky;
        this.date = date;
        this.farm = farm;
    }
}
