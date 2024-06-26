package nbdream.farm.controller;

import lombok.RequiredArgsConstructor;
import nbdream.common.advice.response.ApiResponse;
import nbdream.farm.service.FarmWorkScheduleService;
import org.springframework.web.bind.annotation.RestController;


//@RequestMapping("/api/farm-work-open-api")
@RestController
@RequiredArgsConstructor
public class FarmWorkScheduleController {

    private final FarmWorkScheduleService farmWorkScheduleService;

//    @Operation(summary = "농작업 일정 Open Api 테스트", description = "")
//    @GetMapping
    public ApiResponse<Void> saveWorkSchedule(){
        farmWorkScheduleService.saveWorkSchedule();
        return ApiResponse.ok();
    }
}
