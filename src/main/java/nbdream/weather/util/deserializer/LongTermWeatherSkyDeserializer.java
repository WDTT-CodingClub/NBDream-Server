package nbdream.weather.util.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import nbdream.weather.domain.Sky;
import nbdream.weather.dto.response.LongTermSkyRes;
import nbdream.weather.util.LongTermWeatherSkyResult;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class LongTermWeatherSkyDeserializer extends JsonDeserializer<LongTermWeatherSkyResult>{

    private final ObjectMapper objectMapper;

    public LongTermWeatherSkyDeserializer()
    {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public LongTermWeatherSkyResult deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException, IOException {

        JsonNode node = p.getCodec().readTree(p);
        JsonNode responseNode = node.findValue("response");

        JsonNode itemNode = responseNode.get("body").get("items").get("item");
        List<LongTermWeatherSkyResult.item> items = Arrays.stream(objectMapper.treeToValue(itemNode, LongTermWeatherSkyResult.item[].class)).collect(Collectors.toList());
        LongTermWeatherSkyResult.item item = items.get(0);

        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        Map<String, LongTermSkyRes> map = new HashMap<>();
        List<String> itemValues = List.of(item.getWf3Pm(), item.getWf4Pm(), item.getWf5Pm(), item.getWf6Pm(),
                item.getWf7Pm(), item.getWf8(), item.getWf9(), item.getWf10());

        for (int i = 3; i <= 10; i++) {
            String date = now.plusDays(i).format(formatter);
            LongTermSkyRes response = new LongTermSkyRes(date);
            response.setSky(Sky.of(itemValues.get(i - 3)));
            map.put(date, response);
        }

        return new LongTermWeatherSkyResult(map);
    }
}