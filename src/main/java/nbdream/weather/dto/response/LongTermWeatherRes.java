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
public class LongTermWeatherRes {
    private String date;
    private int highestTemperature;
    private int lowestTemperature;
    private Sky sky;

    public LongTermWeatherRes(String date) {
        this.date = date;
    }
}
