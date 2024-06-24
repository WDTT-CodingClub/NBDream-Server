package nbdream.weather.infrastructure.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbdream.weather.dto.response.LongTermSkyRes;
import nbdream.weather.infrastructure.util.deserializer.LongTermSkyDeserializer;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize(using = LongTermSkyDeserializer.class)
public class LongTermSkyResult {

    private List<LongTermSkyRes> items;

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
}
