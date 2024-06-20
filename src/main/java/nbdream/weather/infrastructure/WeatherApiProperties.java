package nbdream.weather.infrastructure;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class WeatherApiProperties {
    public static final String SHORT_TERM_WEATHER_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?";
    public static final String LONG_TERM_SKY_URL = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst?";
    public static final String LONG_TERM_TEMPERATURE_URL = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa?";

    private final String serviceKey;

    public WeatherApiProperties(@Value("${weather.service-key}") String serviceKey) {
        this.serviceKey = serviceKey;
    }
}
