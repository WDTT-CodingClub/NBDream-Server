package nbdream.weather.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbdream.weather.dto.response.LongTermSkyRes;
import nbdream.weather.util.deserializer.LongTermWeatherSkyDeserializer;

import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize(using = LongTermWeatherSkyDeserializer.class)
public class LongTermWeatherSkyResult {

    private Map<String, LongTermSkyRes> items;

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class item{
        private String wf3Pm;
        private String wf4Pm;
        private String wf5Pm;
        private String wf6Pm;
        private String wf7Pm;
        private String wf8;
        private String wf9;
        private String wf10;
    }

    @Getter
    @NoArgsConstructor
    public static class temperature{
        private String taMin3;
        private String taMax3;
        private String taMin4;
        private String taMax4;
        private String taMin5;
        private String taMax5;
        private String taMin6;
        private String taMax6;
        private String taMin7;
        private String taMax7;
        private String taMin8;
        private String taMax8;
        private String taMin9;
        private String taMax9;
        private String taMin10;
        private String taMax10;
    }
}
