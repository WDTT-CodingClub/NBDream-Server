package nbdream.farm.controller;

import lombok.RequiredArgsConstructor;
import nbdream.farm.service.FarmingDiaryService;
import nbdream.farm.service.dto.farmingdiary.CreateDiaryRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/calendar/diary")
public class FarmingDiaryController {

    private final FarmingDiaryService farmingDiaryService;

    @PostMapping
    public void createFarmingDiary(@RequestBody CreateDiaryRequest request){
        farmingDiaryService.createFarmingDiary(request);
    }

    @PutMapping("/{diary-id}")
    public void updateFarmingDiary(){
    }

    @DeleteMapping("/{diary-id}")
    public void deleteFarmingDiary(){}

    @GetMapping
    public void getFarmingDiaries(){}
}
