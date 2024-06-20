package nbdream.weather.service;

import lombok.RequiredArgsConstructor;
import nbdream.farm.domain.Farm;
import nbdream.member.domain.Member;
import nbdream.member.exception.MemberNotFoundException;
import nbdream.member.repository.MemberRepository;
import nbdream.weather.domain.SimpleWeather;
import nbdream.weather.domain.Weather;
import nbdream.weather.dto.response.LongTermWeatherRes;
import nbdream.weather.dto.response.ShortTermWeatherRes;
import nbdream.weather.dto.response.WeatherRes;
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
    private final MemberRepository memberRepository;
    private final SimpleWeatherRepository simpleWeatherRepository;
    private final LoadLongTermWeatherService loadLongTermWeatherService;
    private final LoadShortTermWeatherService loadShortTermWeatherService;

    public WeatherRes getWeathers(final Long memberId) {
        final Member member = memberRepository.findByIdFetchFarm(memberId).orElseThrow(MemberNotFoundException::new);
        final Farm farm = member.getFarm();

        Optional<Weather> todayWeather = weatherRepository.findByFarmIdAndDate(farm.getId(), LocalDate.now());
        if (todayWeather.isEmpty()) {
            simpleWeatherRepository.deleteAllByFarmId(farm.getId());
            loadLongTermWeatherService.loadLongTermWeather(farm.getId());
            loadShortTermWeatherService.loadShortTermWeather(farm.getId());
            todayWeather = weatherRepository.findByFarmIdAndDate(farm.getId(), LocalDate.now());
        }

        List<SimpleWeather> weeksSimpleWeather = simpleWeatherRepository.findByWeeksSimpleWeather(farm.getId());
        List<LongTermWeatherRes> weeksSimpleWeatherRes = weeksSimpleWeather.stream()
                .map(simpleWeather -> new LongTermWeatherRes(simpleWeather))
                .collect(Collectors.toList());

        return new WeatherRes(new ShortTermWeatherRes(todayWeather.get()), weeksSimpleWeatherRes);
    }
}
