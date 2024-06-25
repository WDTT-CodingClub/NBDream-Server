package nbdream.farm.controller;

import lombok.RequiredArgsConstructor;
import nbdream.common.advice.response.ApiResponse;
import nbdream.farm.service.FarmingDiaryFacade;
import nbdream.farm.service.FarmingDiaryService;
import nbdream.farm.service.dto.farmingdiary.FarmingDiaryRequest;
import nbdream.farm.service.dto.farmingdiary.FarmingDiaryResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/calendar/diary")
public class FarmingDiaryController {

    private final FarmingDiaryFacade farmingDiaryFacade;
    private final FarmingDiaryService farmingDiaryService;

    @PostMapping
    public ApiResponse<Long> createFarmingDiary(@RequestBody FarmingDiaryRequest request){
        Long farmingDiaryId = farmingDiaryFacade.saveFarmingDiaryInfo(request);
        return ApiResponse.ok(farmingDiaryId);
    }

    @PutMapping("/{diary-id}")
    public ApiResponse<Long> updateFarmingDiary(@PathVariable(name = "diary-id") Long farmingDiaryId,
                                   @RequestBody FarmingDiaryRequest request) {
        Long updatedFarmingDiaryId = farmingDiaryFacade.editFarmingDiaryInfo(farmingDiaryId, request);
        return ApiResponse.ok(updatedFarmingDiaryId);
    }

    @DeleteMapping("/{diary-id}")
    public ApiResponse<Void> deleteFarmingDiary(@PathVariable(name = "diary-id") Long farmingDiaryId) {
        farmingDiaryFacade.deleteFarmingDiaryInfo(farmingDiaryId);
        return ApiResponse.ok();
    }

    @GetMapping
    public void getFarmingDiaries(){}


    @GetMapping("/{diary-id}")
    public ApiResponse<FarmingDiaryResponse> getFarmingDiary(@PathVariable(name = "diary-id") Long farmingDiaryId) {
        FarmingDiaryResponse response = farmingDiaryFacade.fetchFarmingDiaryInfoDetail(farmingDiaryId);
        return ApiResponse.ok(response);
    }
}
