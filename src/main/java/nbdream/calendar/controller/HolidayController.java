package nbdream.calendar.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import nbdream.calendar.dto.HolidayResponse;
import nbdream.calendar.service.HolidayService;
import nbdream.common.advice.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/calendar")
@Tag(name = "Holiday Controller")
public class HolidayController {

    private final HolidayService holidayService;

    @Operation(summary = "특일정보 조회", description = "조회 연도 형식: yyyy, 월 형식: MM(1월의 경우 01과 같이 입력해야 함)")
    @GetMapping("/holidays")
    public ApiResponse<List<HolidayResponse>> getHolidays(
            @RequestParam(value = "solYear") String solYear,
            @RequestParam(value = "solMonth") String solMonth) {
        List<HolidayResponse> responses = holidayService.getAllHolidays(solYear, solMonth);
        return ApiResponse.ok(responses);
    }
}
