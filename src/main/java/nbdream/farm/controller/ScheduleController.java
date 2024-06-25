package nbdream.farm.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import nbdream.auth.config.AuthenticatedMemberId;
import nbdream.common.advice.response.ApiResponse;
import nbdream.farm.service.ScheduleService;
import nbdream.farm.service.dto.schedule.request.*;
import nbdream.farm.service.dto.schedule.response.FarmWorkListResDto;
import nbdream.farm.service.dto.schedule.response.ScheduleListResDto;
import nbdream.farm.service.dto.schedule.response.ScheduleResDto;
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

    @Operation(summary = "일정 등록", description = "날짜 : yyyy-MM-dd, 카테고리 : 작물명 또는 '전체'")
    @PostMapping("/schedule/register")
    public ApiResponse<Void> registerSchedule(@RequestBody PostScheduleReqDto request,
                                         @Parameter(hidden = true) @AuthenticatedMemberId final Long memberId){

        return scheduleService.registerSchedule(request, memberId);
    }

    @Operation(summary = "일정 수정", description = "날짜 : yyyy-MM-dd, 카테고리 : 작물명 또는 '전체'")
    @PutMapping("/schedule/update/{schedule-id}")
    public ApiResponse<Void> updateSchedule(@RequestBody PutScheduleReqDto request,
                                            @PathVariable("schedule-id") final Long scheduleId,
                                              @Parameter(hidden = true) @AuthenticatedMemberId final Long memberId){
        return scheduleService.updateSchedule(request, scheduleId, memberId);
    }

    @Operation(summary = "일정 삭제", description = "")
    @DeleteMapping("/schedule/delete/{schedule-id}")
    public ApiResponse<Void> deleteSchedule(@PathVariable("schedule-id") final Long scheduleId,
                                            @Parameter(hidden = true) @AuthenticatedMemberId final Long memberId){
        return scheduleService.deleteSchedule(scheduleId, memberId);
    }

    @Operation(summary = "일정 상세 조회", description = "")
    @GetMapping("/schedule/detail/{schedule-id}")
    public ApiResponse<ScheduleResDto> getScheduleDetail(@PathVariable("schedule-id") final Long scheduleId,
                                                         @Parameter(hidden = true) @AuthenticatedMemberId final Long memberId){

        return ApiResponse.ok(scheduleService.getScheduleDetail(scheduleId, memberId));
    }

    @Operation(summary = "주간 일정 조회", description = "날짜 형식 : yyyy-MM-dd, 카테고리 : 작물명 또는 '전체'")
    @GetMapping("/schedule/week")
    public ApiResponse<ScheduleListResDto> getWeeklySchedule(@RequestParam("category") String category,
                                                             @RequestParam("startDate") String startDate,
                                                             @Parameter(hidden = true) @AuthenticatedMemberId final Long memberId){
        return ApiResponse.ok(scheduleService.getWeeklySchedule(new WeekScheduleListReqDto(category, startDate), memberId));
    }

    @Operation(summary = "월간 일정 조회", description = "날짜 : (year=2024, month 6 또는 06)   카테고리 : 작물명 또는 '전체'")
    @GetMapping("/schedule/month")
    public ApiResponse<ScheduleListResDto> getMonthlySchedule(@RequestParam("category") String category,
                                                              @RequestParam("year") int year,
                                                              @RequestParam("month") int month,
                                                             @Parameter(hidden = true) @AuthenticatedMemberId final Long memberId){
        return ApiResponse.ok(scheduleService.getMonthlySchedule(new ScheduleListReqDto(category, year, month), memberId));
    }

}
