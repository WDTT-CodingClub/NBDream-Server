package nbdream.farm.service;

import lombok.RequiredArgsConstructor;
import nbdream.calendar.domain.Holiday;
import nbdream.calendar.dto.HolidayResponse;
import nbdream.calendar.service.HolidayService;
import nbdream.farm.domain.FarmingDiary;
import nbdream.farm.domain.Work;
import nbdream.farm.service.dto.farmingdiary.*;
import nbdream.farm.service.dto.work.WorkDto;
import nbdream.image.domain.Image;
import nbdream.image.service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FarmingDiaryFacade {

    private final FarmingDiaryService farmingDiaryService;
    private final WorkService workService;
    private final HolidayService holidayService;
    private final ImageService imageService;

    public Long saveFarmingDiaryInfo(final Long memberId, FarmingDiaryRequest request) {
        FarmingDiary farmingDiary = farmingDiaryService.createFarmingDiary(memberId, request);
        workService.createWorks(farmingDiary, request.getWorkDtos());
        holidayService.createHolidays(farmingDiary, request.getHolidayResponses());
        imageService.saveImageUrls(farmingDiary.getId(), request.getImageUrls());
        return farmingDiary.getId();
    }


    public Long editFarmingDiaryInfo(Long farmingDiaryId, FarmingDiaryRequest request) {
        FarmingDiary farmingDiary = farmingDiaryService.updateFarmingDiary(farmingDiaryId, request);
        workService.updateWorks(farmingDiary, request.getWorkDtos());
        holidayService.updateHolidays(farmingDiary, request.getHolidayResponses());
        imageService.updateTargetImages(farmingDiary.getId(), request.getImageUrls());
        return farmingDiary.getId();
    }

    /* 영농일지 상세 조회 */
    public FarmingDiaryResponse fetchFarmingDiaryInfoDetail(Long farmingDiaryId) {
        FarmingDiary farmingDiary = farmingDiaryService.findByFarmingDiaryId(farmingDiaryId);
        List<Holiday> holidays = holidayService.findAllByFarmingDiaryId(farmingDiaryId);
        List<Image> imageUrls = imageService.findAllByTargetId(farmingDiaryId);
        List<Work> works = workService.findAllByFarmingDiaryId(farmingDiaryId);

        List<HolidayResponse> holidayResponses = holidays.stream()
                .map(holiday -> new HolidayResponse(holiday.getLocalDate(),
                        holiday.getDateKind(),
                        holiday.getDateName(),
                        holiday.getIsHoliday()))
                .collect(Collectors.toList());

        List<String> imageUrlList = imageUrls.stream()
                .map(Image::getImageUrl)
                .collect(Collectors.toList());

        List<WorkDto> workDtos = works.stream()
                .map(work -> new WorkDto(work.getWorkCategory().getDescription(),
                        work.getContent()))
                .collect(Collectors.toList());

        FarmingDiaryResponse response = FarmingDiaryResponse.builder()
                .farmingDiaryId(farmingDiaryId)
                .crop(farmingDiary.getCrop())
                .date(farmingDiary.getDate())
                .holidayResponses(holidayResponses)
                .weatherForecast(farmingDiary.getWeatherForecast())
                .imageUrls(imageUrlList)
                .workingPeopleNumber(farmingDiary.getWorkingPeopleNumber())
                .workingTime(farmingDiary.getWorkingTime())
                .workingArea(farmingDiary.getWorkingArea())
                .workDtos(workDtos)
                .memo(farmingDiary.getMemo())
                .build();

        return response;
    }

    public FarmingDiaryListResponse fetchMonthlyFarmingDiary(final FarmingDiaryListRequest request, final Long memberId) {
        List<FarmingDiary> monthlyFarmingDiary = farmingDiaryService.findMonthlyFarmingDiary(request, memberId);

        List<FarmingDiaryResponse> farmingDiaryResponses = monthlyFarmingDiary.stream()
                .map(farmingDiary -> fetchFarmingDiaryInfoDetail(farmingDiary.getId()))
                .collect(Collectors.toList());

        return new FarmingDiaryListResponse(farmingDiaryResponses);
    }

    public FarmingDiaryListResponse searchFarmingDiary(final SearchFarmingDiaryCond cond, final Long memberId) {
        List<FarmingDiary> farmingDiaries = farmingDiaryService.searchFarmingDiary(cond, memberId);

        List<FarmingDiaryResponse> farmingDiaryResponses = farmingDiaries.stream()
                .map(farmingDiary -> fetchFarmingDiaryInfoDetail(farmingDiary.getId()))
                .collect(Collectors.toList());

        return new FarmingDiaryListResponse(farmingDiaryResponses);
    }

    public void deleteFarmingDiaryInfo(Long farmingDiaryId) {
       farmingDiaryService.findByFarmingDiaryId(farmingDiaryId);
       workService.deleteWorks(farmingDiaryId);
       holidayService.deleteHolidays(farmingDiaryId);
       imageService.deleteImageUrls(farmingDiaryId);
       farmingDiaryService.deleteByFarmingDiaryId(farmingDiaryId);
    }
}
