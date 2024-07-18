package nbdream.alarm.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbdream.alarm.dto.FcmSendDto;
import nbdream.alarm.service.FcmService;
import nbdream.common.advice.response.ApiResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fcm")
@Tag(name = "FCM Controller")
public class FcmController {
    private final FcmService fcmService;

    @Operation(summary = "알람 전송", description = "테스트용도")
    @PostMapping("/send")
    public ApiResponse<Void> pushMessage(@RequestBody @Validated FcmSendDto fcmSendDto) throws IOException {
        int result = fcmService.sendMessageTo(fcmSendDto);
        return ApiResponse.ok();
    }
}
