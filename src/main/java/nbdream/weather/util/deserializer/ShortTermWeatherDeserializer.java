package nbdream.weather.util.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import nbdream.weather.domain.Sky;
import nbdream.weather.dto.response.ShortTermWeatherRes;
import nbdream.weather.util.ShortTermWeatherResult;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ShortTermWeatherDeserializer extends JsonDeserializer<ShortTermWeatherResult> {

    private final ObjectMapper objectMapper;

    public ShortTermWeatherDeserializer()
    {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public ShortTermWeatherResult deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException, IOException {

        JsonNode node = p.getCodec().readTree(p);
        JsonNode responseNode = node.findValue("response");

        JsonNode itemNode = responseNode.get("body").get("items").get("item");
        List<ShortTermWeatherResult.item> items = Arrays.stream(objectMapper.treeToValue(itemNode, ShortTermWeatherResult.item[].class)).collect(Collectors.toList());
        LocalDate now = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String today = now.format(formatter);
        String tomorrow = now.plusDays(1).format(formatter);
        String theDayAfterTomorrow = now.plusDays(2).format(formatter);

        Map<String, ShortTermWeatherRes> map = new HashMap<>();
        map.put(today, new ShortTermWeatherRes(today));
        map.put(tomorrow, new ShortTermWeatherRes(tomorrow));
        map.put(theDayAfterTomorrow, new ShortTermWeatherRes(theDayAfterTomorrow));

        //TMP(1시간 기온), TMX(최고온도), TMN(최저온도), REH(시간별 습도)
        //POP(강수 확률), PCP(강수량), SKY(하늘상태), WSD(풍속)
        for(ShortTermWeatherResult.item item : items){
            if (map.get(item.getFcstDate()) == null) break;
            ShortTermWeatherRes response = map.get(item.getFcstDate());
            if(item.getCategory().equals("TMP")) {
                response.setCurrentTemperature(Integer.valueOf(item.getFcstValue()));
            } else if (item.getCategory().equals("TMX")) {
                response.setHighestTemperature(Math.round(Float.valueOf(item.getFcstValue())));
            } else if (item.getCategory().equals("TMN")) {
                response.setLowestTemperature(Math.round(Float.valueOf(item.getFcstValue())));
            } else if (item.getCategory().equals("REH")) {
                response.setHumidity(Integer.valueOf(item.getFcstValue()));
            } else if (item.getCategory().equals("POP")) {
                response.setPrecipitationProbability(Integer.valueOf(item.getFcstValue()));
            } else if (item.getCategory().equals("PCP")) {
                response.setPrecipitationAmount((item.getFcstValue().equals("강수없음")) ? 0 : Integer.valueOf(item.getFcstValue()));
            } else if (item.getCategory().equals("WSD")) {
                response.setWindSpeed(Math.round(Float.valueOf(item.getFcstValue())));
            } else if (item.getCategory().equals("SKY")) {
                response.setSky(Sky.of(Integer.valueOf(item.getFcstValue())));
            }
        }
        return new ShortTermWeatherResult(map);
    }
}