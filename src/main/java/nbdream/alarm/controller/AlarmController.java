package nbdream.alarm.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import nbdream.alarm.dto.*;
import nbdream.alarm.service.AlarmService;
import nbdream.alarm.service.FcmService;
import nbdream.auth.config.AuthenticatedMemberId;
import nbdream.common.advice.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/alarm")
@Tag(name = "Alarm Controller")
public class AlarmController {
    private final AlarmService alarmService;

    @Operation(summary = "알림 설정 조회", description = "")
    @GetMapping("/status")
    public ApiResponse<AlarmStatusDto> getAlarmStatus(@Parameter(hidden = true) @AuthenticatedMemberId Long memberId){
        AlarmStatusDto response = alarmService.getAlarmStatus(memberId);
        return ApiResponse.ok(response);
    }

    @Operation(summary = "알림 설정 변경", description = "초기값은 false")
    @PutMapping("/update")
    public ApiResponse<AlarmStatusDto> updateAlarmStatus(@Parameter(hidden = true) @AuthenticatedMemberId Long memberId,
                                               @RequestBody AlarmStatusDto request){
        return ApiResponse.ok(alarmService.updateAlarmStatus(memberId, request));
    }

    @Operation(summary = "FCM 토큰 저장", description = "로그인 시 요청")
    @PostMapping("/token/save")
    public ApiResponse<Void> saveFcmToken(@Parameter(hidden = true) @AuthenticatedMemberId Long memberId,
                                               @RequestBody FcmTokenDto request){
        alarmService.saveFcmToken(memberId, request);
        return ApiResponse.ok();
    }

    @Operation(summary = "FCM 토큰 만료", description = "로그아웃 시 요청")
    @DeleteMapping("/token/expire")
    public ApiResponse<Void> deleteFcmToken(@Parameter(hidden = true) @AuthenticatedMemberId Long memberId){
        alarmService.deleteFcmToken(memberId);
        return ApiResponse.ok();
    }

    @Operation(summary = "알림 내역 조회", description = "")
    @GetMapping("/history")
    public ApiResponse<AlarmHistoryListResDto> getAlarmHistory(@Parameter(hidden = true) @AuthenticatedMemberId Long memberId){

        return ApiResponse.ok(alarmService.getAlarmHistory(memberId));
    }

    @Operation(summary = "알림 내역 확인", description = "")
    @PutMapping("/history/check")
    public ApiResponse<Void> checkAlarmHistory(@Parameter(hidden = true) @AuthenticatedMemberId Long memberId,
                                               @RequestBody AlarmHistoryCheckReqDto request){
        alarmService.checkAlarmHistory(request);
        return ApiResponse.ok();
    }

    @Operation(summary = "알림 내역 삭제", description = "")
    @PutMapping("/history/delete")
    public ApiResponse<Void> deleteAlarmHistory(@Parameter(hidden = true) @AuthenticatedMemberId Long memberId,
                                               @RequestBody AlarmHistoryDeleteReqDto request){
        alarmService.deleteAlarmHistory(request);
        return ApiResponse.ok();
    }
}
