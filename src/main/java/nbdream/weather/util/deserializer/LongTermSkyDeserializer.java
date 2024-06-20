package nbdream.weather.util.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import nbdream.weather.dto.response.LongTermSkyRes;
import nbdream.weather.util.LongTermSkyResult;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LongTermSkyDeserializer extends JsonDeserializer<LongTermSkyResult>{

    private final ObjectMapper objectMapper;

    public LongTermSkyDeserializer()
    {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public LongTermSkyResult deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException, IOException {

        JsonNode node = p.getCodec().readTree(p);
        JsonNode responseNode = node.findValue("response");

        JsonNode itemNode = responseNode.get("body").get("items").get("item");
        List<LongTermSkyResult.item> items = Arrays.stream(objectMapper.treeToValue(itemNode, LongTermSkyResult.item[].class)).collect(Collectors.toList());
        LongTermSkyResult.item item = items.get(0);

        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<LongTermSkyRes> responses = new ArrayList<>();
        List<String> itemValues = List.of(item.getWf3Pm(), item.getWf4Pm(), item.getWf5Pm(), item.getWf6Pm(),
                item.getWf7Pm(), item.getWf8(), item.getWf9(), item.getWf10());

        for (int i = 3; i <= 10; i++) {
            String date = now.plusDays(i).format(formatter);
            LongTermSkyRes response = new LongTermSkyRes(date);
            response.setSky(itemValues.get(i - 3));
            responses.add(response);
        }

        return new LongTermSkyResult(responses);
    }
}