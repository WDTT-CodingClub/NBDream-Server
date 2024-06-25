package nbdream.weather.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nbdream.farm.domain.Farm;
import nbdream.weather.domain.SimpleWeather;
import nbdream.weather.domain.Sky;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

@Getter
@NoArgsConstructor
@Setter
public class LongTermWeatherRes {
    private String date;
    private String sky;
    private int highestTemperature;
    private int lowestTemperature;
    private String dayOfTheWeek;

    public LongTermWeatherRes(final LongTermSkyRes sky, final LongTermTemperatureRes temperature) {
        this.date = sky.getDate();
        this.sky = sky.getSky();
        this.highestTemperature = temperature.getHighestTemperature();
        this.lowestTemperature = temperature.getLowestTemperature();
        this.dayOfTheWeek = getDayOfTheWeek();
    }

    public LongTermWeatherRes(final SimpleWeather simpleWeather) {
        this.date = simpleWeather.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.sky = simpleWeather.getSky().getValue();
        this.highestTemperature = simpleWeather.getHighestTemperature();
        this.lowestTemperature = simpleWeather.getLowestTemperature();
        this.dayOfTheWeek = getDayOfTheWeek();
    }

    public SimpleWeather toSimpleWeatherEntity(final Farm farm) {
        return SimpleWeather.builder()
                .date(LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .farm(farm)
                .sky(Sky.of(sky))
                .highestTemperature(highestTemperature)
                .lowestTemperature(lowestTemperature)
                .dayOfTheWeek(getDayOfTheWeek())
                .build();
    }

    public String getDayOfTheWeek() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(this.date, formatter);

        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN);
    }

}
