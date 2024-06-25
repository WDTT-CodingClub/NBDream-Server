package nbdream.weather.service;

import lombok.RequiredArgsConstructor;
import nbdream.farm.domain.Farm;
import nbdream.farm.domain.Location;
import nbdream.farm.exception.FarmNotFoundException;
import nbdream.farm.repository.FarmRepository;
import nbdream.weather.domain.SimpleWeather;
import nbdream.weather.domain.Weather;
import nbdream.weather.dto.response.LongTermWeatherRes;
import nbdream.weather.dto.response.ShortTermWeatherRes;
import nbdream.weather.dto.response.WeatherRes;
import nbdream.weather.infrastructure.util.DefaultLocation;
import nbdream.weather.repository.SimpleWeatherRepository;
import nbdream.weather.repository.WeatherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class WeatherService {

    private final WeatherRepository weatherRepository;
    private final FarmRepository farmRepository;
    private final SimpleWeatherRepository simpleWeatherRepository;
    private final LoadLongTermWeatherService loadLongTermWeatherService;
    private final LoadShortTermWeatherService loadShortTermWeatherService;

    public WeatherRes getWeathers(final Long memberId) {
        final Farm farm = farmRepository.findByMemberId(memberId).orElseThrow(FarmNotFoundException::new);
        final Location location = farm.getLocation();

        if (location.getAddress().isEmpty()) {
            return getDefaultWeathers();
        }
        return getFarmWeathers(farm);
    }

    public WeatherRes getFarmWeathers(final Farm farm) {
        Optional<Weather> todayWeather = weatherRepository.findByFarmIdAndDate(farm.getId(), LocalDate.now());
        if (todayWeather.isEmpty()) {
            simpleWeatherRepository.deleteAllByFarmId(farm.getId());
            loadLongTermWeatherService.loadLongTermWeather(farm.getId());
            loadShortTermWeatherService.loadShortTermWeather(farm.getId());
            todayWeather = weatherRepository.findByFarmIdAndDate(farm.getId(), LocalDate.now());
        }

        List<SimpleWeather> weeksSimpleWeather = simpleWeatherRepository.findWeeksSimpleWeatherByFarmId(farm.getId());
        List<LongTermWeatherRes> weeksSimpleWeatherRes = weeksSimpleWeather.stream()
                .map(simpleWeather -> new LongTermWeatherRes(simpleWeather))
                .collect(Collectors.toList());

        return new WeatherRes(new ShortTermWeatherRes(todayWeather.get()), weeksSimpleWeatherRes);
    }

    public WeatherRes getDefaultWeathers() {
        Optional<Weather> todayWeather = weatherRepository.findDefaultWeatherByDate(LocalDate.now());
        final Location location = DefaultLocation.get();

        if (todayWeather.isEmpty()) {
            simpleWeatherRepository.deleteAllDefaultWeather();
            weatherRepository.deleteAllDefaultWeather();
            loadLongTermWeatherService.loadDefaultLongTermWeather(location);
            loadShortTermWeatherService.loadDefaultShortTermWeather(location);
            todayWeather = weatherRepository.findDefaultWeatherByDate(LocalDate.now());
        }

        List<SimpleWeather> weeksSimpleWeather = simpleWeatherRepository.findWeeksDefaultSimpleWeather();
        List<LongTermWeatherRes> weeksSimpleWeatherRes = weeksSimpleWeather.stream()
                .map(simpleWeather -> new LongTermWeatherRes(simpleWeather))
                .collect(Collectors.toList());

        return new WeatherRes(new ShortTermWeatherRes(todayWeather.get()), weeksSimpleWeatherRes);
    }
}
