package nbdream.weather.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nbdream.farm.domain.Farm;
import nbdream.weather.domain.SimpleWeather;
import nbdream.weather.domain.Sky;
import nbdream.weather.domain.Weather;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
@Setter
@ToString
public class ShortTermWeatherRes {
    private String date;
    private int precipitationProbability;
    private int precipitationAmount;
    private int humidity;
    private int windSpeed;
    private int highestTemperature;
    private int lowestTemperature;
    private int currentTemperature;
    private String sky;

    public ShortTermWeatherRes(String date) {
        this.date = date;
    }


    public ShortTermWeatherRes(final Weather weather) {
        this.date = weather.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.precipitationProbability = weather.getPrecipitationProbability();
        this.precipitationAmount = weather.getPrecipitationAmount();
        this.humidity = weather.getHumidity();
        this.windSpeed = weather.getWindSpeed();
        this.highestTemperature = weather.getHighestTemperature();
        this.lowestTemperature = weather.getLowestTemperature();
        this.currentTemperature = weather.getCurrentTemperature();
        this.sky = weather.getSky().getValue();
    }

    public Weather toWeatherEntity(final Farm farm) {
        System.out.println("short" + sky);
        return Weather.builder()
                .precipitationProbability(precipitationProbability)
                .precipitationAmount(precipitationAmount)
                .humidity(humidity)
                .windSpeed(windSpeed)
                .highestTemperature(highestTemperature)
                .lowestTemperature(lowestTemperature)
                .currentTemperature(currentTemperature)
                .sky(Sky.of(sky))
                .date(LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .farm(farm)
                .build();
    }

    public SimpleWeather toSimpleWeatherEntity(final Farm farm) {
        return SimpleWeather.builder()
                .date(LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .farm(farm)
                .sky(Sky.of(sky))
                .highestTemperature(highestTemperature)
                .lowestTemperature(lowestTemperature)
                .build();
    }
}
