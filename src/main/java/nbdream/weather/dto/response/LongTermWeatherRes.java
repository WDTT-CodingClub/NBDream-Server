package nbdream.weather.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nbdream.farm.domain.Farm;
import nbdream.weather.domain.SimpleWeather;
import nbdream.weather.domain.Sky;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
@Setter
@ToString
public class LongTermWeatherRes {
    private String date;
    private String sky;
    private int highestTemperature;
    private int lowestTemperature;

    public LongTermWeatherRes(final LongTermSkyRes sky, final LongTermTemperatureRes temperature) {
        this.date = sky.getDate();
        this.sky = sky.getSky();
        this.highestTemperature = temperature.getHighestTemperature();
        this.lowestTemperature = temperature.getLowestTemperature();
    }

    public LongTermWeatherRes(final SimpleWeather simpleWeather) {
        this.date = simpleWeather.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.sky = simpleWeather.getSky().getValue();
        this.highestTemperature = simpleWeather.getHighestTemperature();
        this.lowestTemperature = simpleWeather.getLowestTemperature();
    }

    public SimpleWeather toSimpleWeatherEntity(final Farm farm) {
        System.out.println(sky);
        return SimpleWeather.builder()
                .date(LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .farm(farm)
                .sky(Sky.of(sky))
                .highestTemperature(highestTemperature)
                .lowestTemperature(lowestTemperature)
                .build();
    }

}
