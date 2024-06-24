package nbdream.weather.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import nbdream.farm.domain.Farm;
import nbdream.farm.domain.Location;
import nbdream.farm.exception.FarmNotFoundException;
import nbdream.farm.repository.FarmRepository;
import nbdream.weather.dto.response.ShortTermWeatherRes;
import nbdream.weather.exception.WeatherServerConnectionFailException;
import nbdream.weather.infrastructure.WeatherApiProperties;
import nbdream.weather.repository.SimpleWeatherRepository;
import nbdream.weather.repository.WeatherRepository;
import nbdream.weather.infrastructure.util.ShortTermWeatherResult;
import nbdream.weather.infrastructure.util.url_bulider.ShortTermWeatherApiUriBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static nbdream.weather.infrastructure.WeatherApiProperties.SHORT_TERM_WEATHER_URL;

@Service
@RequiredArgsConstructor
@Transactional
public class LoadShortTermWeatherService {

    private final FarmRepository farmRepository;
    private final WeatherRepository weatherRepository;
    private final SimpleWeatherRepository simpleWeatherRepository;
    private final WeatherApiProperties weatherApiProperties;
    private final ObjectMapper objectMapper;
    private RestTemplate restTemplate = new RestTemplate();

    public void loadShortTermWeather(final Long farmId){
        final Farm farm = farmRepository.findById(farmId).orElseThrow(FarmNotFoundException::new);
        final Location location = farm.getLocation();

        final ShortTermWeatherResult shortTermWeatherResult = loadShortTermResult(location, LocalDate.now());
        final Map<String, ShortTermWeatherRes> weatherResMap = shortTermWeatherResult.getItems();

        for (String date : weatherResMap.keySet()) {
            ShortTermWeatherRes weatherRes = weatherResMap.get(date);
            weatherRes.setDate(date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8)); // yyyyMMdd 형식의 String을 yyyy-MM-dd 로 변환
            weatherRepository.save(weatherRes.toWeatherEntity(farm));
            simpleWeatherRepository.save(weatherRes.toSimpleWeatherEntity(farm));
        }
    }

    public ShortTermWeatherResult loadShortTermResult(final Location location, final LocalDate date) {
        try{
            final URI uri = ShortTermWeatherApiUriBuilder.builder()
                    .baseUrl(SHORT_TERM_WEATHER_URL)
                    .serviceKey(weatherApiProperties.getServiceKey())
                    .baseDate(getBaseDate(date))
                    .nx(location.getGridX())
                    .ny(location.getGridY())
                    .build().get();

            ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), String.class);
            return objectMapper.readValue(result.getBody(), ShortTermWeatherResult.class);
        }catch (IOException e){
            throw new WeatherServerConnectionFailException();
        }
    }

    public String getBaseDate(final LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return date.format(formatter);
    }
}
