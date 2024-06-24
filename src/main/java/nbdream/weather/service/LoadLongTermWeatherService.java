package nbdream.weather.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import nbdream.farm.domain.Farm;
import nbdream.farm.domain.Location;
import nbdream.farm.exception.FarmNotFoundException;
import nbdream.farm.repository.FarmRepository;
import nbdream.weather.domain.code.SkyRegionCode;
import nbdream.weather.dto.response.LongTermSkyRes;
import nbdream.weather.dto.response.LongTermTemperatureRes;
import nbdream.weather.dto.response.LongTermWeatherRes;
import nbdream.weather.exception.SkyRegionCodeNotFoundException;
import nbdream.weather.exception.TemperatureRegionCodeNotFoundException;
import nbdream.weather.exception.WeatherServerConnectionFailException;
import nbdream.weather.infrastructure.WeatherApiProperties;
import nbdream.weather.repository.SimpleWeatherRepository;
import nbdream.weather.repository.SkyRegionCodeRepository;
import nbdream.weather.repository.TemperatureRegionCodeRepository;
import nbdream.weather.infrastructure.util.LongTermSkyResult;
import nbdream.weather.infrastructure.util.LongTermTemperatureResult;
import nbdream.weather.infrastructure.util.url_bulider.LongTermWeatherApiUriBuilder;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static nbdream.weather.infrastructure.WeatherApiProperties.*;

@Service
@RequiredArgsConstructor
@Transactional
public class LoadLongTermWeatherService {
    private final TemperatureRegionCodeRepository temperatureRegionCodeRepository;
    private final SkyRegionCodeRepository skyRegionCodeRepository;
    private final FarmRepository farmRepository;
    private final SimpleWeatherRepository simpleWeatherRepository;
    private final WeatherApiProperties weatherApiProperties;
    private final ObjectMapper objectMapper;
    private RestTemplate restTemplate = new RestTemplate();

    public void loadLongTermWeather(final Long farmId){
        final Farm farm = farmRepository.findById(farmId).orElseThrow(FarmNotFoundException::new);
        final Location location = farm.getLocation();

        final String skyRegionCode = getSkyRegionCode(location.getAddress());
        final String temperatureRegionCode = getTemperatureRegionCode(location.getAddress());
        final LocalDate now = LocalDate.now();

        final LongTermSkyResult longTermSkyResult = loadLongTermSkyResult(skyRegionCode, now);
        final LongTermTemperatureResult longTermTemperatureResult = loadLongTermTemperatureResult(temperatureRegionCode, now);

        final List<LongTermSkyRes> sky = longTermSkyResult.getItems();
        final List<LongTermTemperatureRes> temperature = longTermTemperatureResult.getItems();
        final List<LongTermWeatherRes> weathersRes = combineSkyAndTemperatureToWeather(sky, temperature);

        weathersRes.stream()
                .forEach(weatherRes -> simpleWeatherRepository.save(weatherRes.toSimpleWeatherEntity(farm)));
    }

    public LongTermSkyResult loadLongTermSkyResult(final String skyRegionCode, final LocalDate date) {
        try{
            final URI uri = LongTermWeatherApiUriBuilder.builder()
                    .baseUrl(LONG_TERM_SKY_URL)
                    .serviceKey(weatherApiProperties.getServiceKey())
                    .regId(skyRegionCode)
                    .tmFc(getTmFc(date))
                    .build().get();
            ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), String.class);
            return objectMapper.readValue(result.getBody(), LongTermSkyResult.class);
        }catch (IOException e){
            throw new WeatherServerConnectionFailException();
        }
    }

    public LongTermTemperatureResult loadLongTermTemperatureResult(final String temperatureRegionCode, final LocalDate date) {
        try{
            final URI uri = LongTermWeatherApiUriBuilder.builder()
                    .baseUrl(LONG_TERM_TEMPERATURE_URL)
                    .serviceKey(weatherApiProperties.getServiceKey())
                    .regId(temperatureRegionCode)
                    .tmFc(getTmFc(date))
                    .build().get();

            ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), String.class);
            return objectMapper.readValue(result.getBody(), LongTermTemperatureResult.class);
        }catch (IOException e){
            throw new WeatherServerConnectionFailException();
        }
    }

    public String getSkyRegionCode(final String fullAddress) {
        String[] splitAddress = fullAddress.split(" ");
        final String siOrDo = splitAddress[0] + splitAddress[1];

        Optional<SkyRegionCode> findAddress = skyRegionCodeRepository.findByAddress(siOrDo);

        if (!findAddress.isEmpty()) {
            return findAddress.get().getCode();
        }

        return skyRegionCodeRepository.findByAddress(fullAddress)
                .orElseThrow(SkyRegionCodeNotFoundException::new).getCode();
    }

    public String getTemperatureRegionCode(final String address) {
        return temperatureRegionCodeRepository.findByAddress(address)
                .orElseThrow(TemperatureRegionCodeNotFoundException::new).getCode();
    }

    private String getTmFc(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return date.format(formatter) + "0600";
    }

    private List<LongTermWeatherRes> combineSkyAndTemperatureToWeather(List<LongTermSkyRes> sky, List<LongTermTemperatureRes> temperature) {
        List<LongTermWeatherRes> result = new ArrayList<>();
        for (int i = 0; i < sky.size(); i++) {
            result.add(new LongTermWeatherRes(sky.get(i), temperature.get(i)));
        }
        return result;
    }

}
