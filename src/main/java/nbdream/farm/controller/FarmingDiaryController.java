package nbdream.farm.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import nbdream.auth.config.AuthenticatedMemberId;
import nbdream.common.advice.response.ApiResponse;
import nbdream.farm.service.FarmingDiaryFacade;
import nbdream.farm.service.FarmingDiaryService;
import nbdream.farm.service.dto.farmingdiary.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/calendar/diary")
@Tag(name = "FarmingDiary Controller")
public class FarmingDiaryController {

    private final FarmingDiaryFacade farmingDiaryFacade;


    @PostMapping
    @Operation(summary = "영농일지 작성")
    public ApiResponse<Long> createFarmingDiary(@Parameter(hidden = true) @AuthenticatedMemberId Long memberId,
                                                @RequestBody FarmingDiaryRequest request){
        Long farmingDiaryId = farmingDiaryFacade.saveFarmingDiaryInfo(memberId, request);
        return ApiResponse.ok(farmingDiaryId);
    }

    @PutMapping("/{diary-id}")
    @Operation(summary = "영농일지 수정")
    public ApiResponse<Long> updateFarmingDiary(@PathVariable(name = "diary-id") Long farmingDiaryId,
                                   @RequestBody FarmingDiaryRequest request) {
        Long updatedFarmingDiaryId = farmingDiaryFacade.editFarmingDiaryInfo(farmingDiaryId, request);
        return ApiResponse.ok(updatedFarmingDiaryId);
    }


    @DeleteMapping("/{diary-id}")
    @Operation(summary = "영농일지 삭제")
    public ApiResponse<Void> deleteFarmingDiary(@PathVariable(name = "diary-id") Long farmingDiaryId) {
        farmingDiaryFacade.deleteFarmingDiaryInfo(farmingDiaryId);
        return ApiResponse.ok();
    }


    @GetMapping("/{diary-id}")
    @Operation(summary = "영농일지 상세 조회")
    public ApiResponse<FarmingDiaryResponse> getFarmingDiary(@PathVariable(name = "diary-id") Long farmingDiaryId) {
        FarmingDiaryResponse response = farmingDiaryFacade.fetchFarmingDiaryInfoDetail(farmingDiaryId);
        return ApiResponse.ok(response);
    }

    @GetMapping
    @Operation(summary = "영농일지 월간 조회")
    public ApiResponse<FarmingDiaryListResponse> getMonthlyFarmingDiary(@Parameter(hidden = true) @AuthenticatedMemberId Long memberId,
                                                                        @RequestParam("year") int year, @RequestParam("month") int month,
                                                                        @RequestParam("crop") String crop) {
        return ApiResponse.ok(farmingDiaryFacade.fetchMonthlyFarmingDiary(new FarmingDiaryListRequest(crop, year, month), memberId));
    }

    @GetMapping("/search")
    @Operation(summary = "영농일지 검색", description = "날짜 입력 시 yyyy-MM-dd 형태로 입력해야 함, 영농일지 memo나 작업 내용에 qeury 값이 포함되는 영농일지들을 가져오도록 구현")
    public ApiResponse<FarmingDiaryListResponse> searchFarmingDiary(@Parameter(hidden = true) @AuthenticatedMemberId Long memberId,
                                                                    @RequestParam("crop") String crop, @RequestParam("query") String query,
                                                                    @RequestParam("start_date") LocalDate startDate, @RequestParam("end_date") LocalDate endDate) {
        return ApiResponse.ok(farmingDiaryFacade.searchFarmingDiary(new SearchFarmingDiaryCond(crop, query, startDate, endDate), memberId));
    }
}
