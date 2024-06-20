package nbdream.farm.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import nbdream.auth.config.AuthenticatedMemberId;
import nbdream.common.advice.response.ApiResponse;
import nbdream.farm.service.ScheduleService;
import nbdream.farm.service.dto.schedule.request.FarmWorkListReqDto;
import nbdream.farm.service.dto.schedule.response.FarmWorkListResDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/calendar")
@Tag(name = "Schedule Controller")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Operation(summary = "농작업 일정 조회", description = "(작물 : 고추, 벼, 감자, 고구마, 사과, 딸기, 마늘, 상추, 배추, 토마토) (월 : 1~12)")
    @GetMapping("/farm-work")
    public ApiResponse<FarmWorkListResDto> getFarmWorkSchedule(@RequestParam(required = true) String crop,
                                                              @RequestParam(required = true) int month,
                                                              @Parameter(hidden = true) @AuthenticatedMemberId final Long memberId){
        FarmWorkListReqDto request = new FarmWorkListReqDto(crop, month);
        return ApiResponse.ok(scheduleService.getFarmWorkSchedule(request, memberId));
    }

}
