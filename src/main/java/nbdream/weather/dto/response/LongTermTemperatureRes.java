package nbdream.weather.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@NoArgsConstructor
@Setter
@ToString
public class LongTermTemperatureRes {
    private String date;
    private int highestTemperature;
    private int lowestTemperature;

    public LongTermTemperatureRes(String date) {
        this.date = date;
    }
}
