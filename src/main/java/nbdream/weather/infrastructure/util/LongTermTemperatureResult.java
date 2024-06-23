package nbdream.weather.infrastructure.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbdream.weather.dto.response.LongTermTemperatureRes;
import nbdream.weather.infrastructure.util.deserializer.LongTermTemperatureDeserializer;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize(using = LongTermTemperatureDeserializer.class)
public class LongTermTemperatureResult {

    private List<LongTermTemperatureRes> items;

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class item{
        private int taMin3;
        private int taMax3;
        private int taMin4;
        private int taMax4;
        private int taMin5;
        private int taMax5;
        private int taMin6;
        private int taMax6;
        private int taMin7;
        private int taMax7;
        private int taMin8;
        private int taMax8;
        private int taMin9;
        private int taMax9;
        private int taMin10;
        private int taMax10;
    }
}
