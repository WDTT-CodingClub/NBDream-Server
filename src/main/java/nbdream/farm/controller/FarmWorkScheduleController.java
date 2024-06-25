package nbdream.farm.controller;

import lombok.RequiredArgsConstructor;
import nbdream.common.advice.response.ApiResponse;
import nbdream.farm.service.FarmWorkScheduleService;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class FarmWorkScheduleController {

    private final FarmWorkScheduleService farmWorkScheduleService;

    public ApiResponse<Void> saveWorkSchedule(){
        farmWorkScheduleService.saveWorkSchedule();
        return ApiResponse.ok();
    }
}
