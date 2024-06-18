package nbdream.weather.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nbdream.weather.domain.Sky;

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
    private Sky sky;

    public ShortTermWeatherRes(String date) {
        this.date = date;
    }
}
