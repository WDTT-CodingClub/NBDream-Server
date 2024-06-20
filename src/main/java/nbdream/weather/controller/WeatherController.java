package nbdream.weather.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import nbdream.auth.config.AuthenticatedMemberId;
import nbdream.common.advice.response.ApiResponse;
import nbdream.weather.dto.response.WeatherRes;
import nbdream.weather.service.WeatherService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/weather")
@Tag(name = "Weather Controller")
public class WeatherController {

    private final WeatherService weatherService;

    @Operation(summary = "주간 날씨 정보 조회", description = "날씨는 맑음, 구름많음, 구름많고 비, 구름많고 눈, 구름많고 비/눈, 구름많고 소나기, 흐림, 흐리고 비, 흐리고 눈, 흐리고 비/눈, 흐리고 소나기 로 이루어져 있고, 날씨 기준은 오전 11시")
    @GetMapping
    public ApiResponse<WeatherRes> getWeathers(@Parameter(hidden = true) @AuthenticatedMemberId final Long memberId) {
        return ApiResponse.ok(weatherService.getWeathers(memberId));
    }
}
