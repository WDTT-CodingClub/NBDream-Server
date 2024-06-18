package nbdream.weather.util.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import nbdream.weather.dto.response.LongTermTemperatureRes;
import nbdream.weather.util.LongTermWeatherTemperatureResult;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class LongTermWeatherTemperatureDeserializer extends JsonDeserializer<LongTermWeatherTemperatureResult>{

    private final ObjectMapper objectMapper;

    public LongTermWeatherTemperatureDeserializer()
    {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public LongTermWeatherTemperatureResult deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException, IOException {

        JsonNode node = p.getCodec().readTree(p);
        JsonNode responseNode = node.findValue("response");

        JsonNode itemNode = responseNode.get("body").get("items").get("item");
        List<LongTermWeatherTemperatureResult.item> items = Arrays.stream(objectMapper.treeToValue(itemNode, LongTermWeatherTemperatureResult.item[].class)).collect(Collectors.toList());
        LongTermWeatherTemperatureResult.item item = items.get(0);

        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        Map<String, LongTermTemperatureRes> map = new HashMap<>();
        List<List<Integer>> temperatures = List.of(List.of(item.getTaMin3(), item.getTaMax3()), List.of(item.getTaMin4(), item.getTaMax4()),
                List.of(item.getTaMin5(), item.getTaMax5()), List.of(item.getTaMin6(), item.getTaMax6()),
                List.of(item.getTaMin7(), item.getTaMax7()), List.of(item.getTaMin8(), item.getTaMax8()),
                List.of(item.getTaMin9(), item.getTaMax9()), List.of(item.getTaMin10(), item.getTaMax10()));

        for (int i = 3; i <= 10; i++) {
            String date = now.plusDays(i).format(formatter);
            LongTermTemperatureRes response = new LongTermTemperatureRes(date);
            List<Integer> temperature = temperatures.get(i - 3);
            response.setLowestTemperature(temperature.get(0));
            response.setHighestTemperature(temperature.get(1));
            map.put(date, response);
        }

        return new LongTermWeatherTemperatureResult(map);
    }
}