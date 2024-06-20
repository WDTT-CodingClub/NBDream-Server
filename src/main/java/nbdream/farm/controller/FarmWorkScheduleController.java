package nbdream.farm.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import nbdream.common.advice.response.ApiResponse;
import nbdream.farm.service.FarmWorkScheduleService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@Tag(name = "FarmWorkSchedule Controller")
public class FarmWorkScheduleController {

    private final FarmWorkScheduleService farmWorkScheduleService;

    @Operation(summary = "농작업 일정 openAPI", description = "openApi 요청해서 DB에 저장")
    @PostMapping("/calendar/farmwork/save")
    public ApiResponse<Void> saveWorkSchedule(){
        farmWorkScheduleService.saveWorkSchedule();
        return ApiResponse.ok();
    }
}
