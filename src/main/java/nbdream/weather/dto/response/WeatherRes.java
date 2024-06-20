package nbdream.weather.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherRes {
    private ShortTermWeatherRes shortTermWeatherRes;
    private List<LongTermWeatherRes> longTermWeatherRes;

}
