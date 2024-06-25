package nbdream.farm.service;

import nbdream.calendar.service.HolidayService;
import nbdream.farm.domain.FarmingDiary;
import nbdream.farm.service.dto.farmingdiary.CreateDiaryRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FarmingDiaryFacade {

    FarmingDiaryService farmingDiaryService;
    WorkService workService;
    HolidayService holidayService;

    public void saveFarmingDiaryInfo(CreateDiaryRequest request) {
        FarmingDiary farmingDiaryId = farmingDiaryService.createFarmingDiary(request);
        workService.createWorks(farmingDiaryId, request.getWorkRequests());
        holidayService.createHolidays(farmingDiaryId, request.getHolidayResponses());


    }
}
