package nbdream.farm.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import nbdream.farm.service.ScheduleService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Schedule Controller")
public class ScheduleController {

    private final ScheduleService scheduleService;

//    @GetMapping("/calendar/farmwork")
//    public void getWorkSchedule(){
//        farmWorkScheduleService.saveWorkSchedule();
//    }

}
