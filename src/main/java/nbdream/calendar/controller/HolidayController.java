package nbdream.calendar.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.models.annotations.OpenAPI30;
import lombok.RequiredArgsConstructor;
import nbdream.calendar.dto.HolidayResponse;
import nbdream.calendar.dto.HolidayRequest;
import nbdream.calendar.service.HolidayService;
import nbdream.common.advice.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/calendar")
public class HolidayController {

    private final HolidayService holidayService;

    @Operation(summary = "특일정보 조회", description = "공휴일 정보 리스트를 반환")
    @GetMapping("/holidays")
    public ApiResponse<List<HolidayResponse>> getHolidays(@RequestBody HolidayRequest request) {
        List<HolidayResponse> responses = holidayService.getAllHolidays(request.getSolYear(), request.getSolMonth());
        return ApiResponse.ok(responses);
    }
}
