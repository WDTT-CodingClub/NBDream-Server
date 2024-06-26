package nbdream.weather.infrastructure.util;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nbdream.weather.dto.response.ShortTermWeatherRes;
import nbdream.weather.infrastructure.util.deserializer.ShortTermWeatherDeserializer;

import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize(using = ShortTermWeatherDeserializer.class)
public class ShortTermWeatherResult {

    private Map<String, ShortTermWeatherRes> items;

    @Getter
    @NoArgsConstructor
    @ToString
    public static class item{
        private String baseDate;
        private String baseTime;
        private String category;
        private String fcstDate;
        private String fcstTime;
        private String fcstValue;
        private Long nx;
        private Long ny;
    }
}