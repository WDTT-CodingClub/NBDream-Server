package nbdream.farm.service;

import lombok.RequiredArgsConstructor;
import nbdream.farm.domain.Crop;
import nbdream.farm.domain.FarmWorkSchedule;
import nbdream.farm.exception.CropFromWorkScheduleNotFoundException;
import nbdream.farm.exception.CropNotFoundException;
import nbdream.farm.exception.FarmWorkScheduleNotFoundException;
import nbdream.farm.repository.CropRepository;
import nbdream.farm.repository.ScheduleRepository;
import nbdream.farm.repository.FarmWorkScheduleCustomRepository;
import nbdream.farm.service.dto.schedule.request.FarmWorkListReqDto;
import nbdream.farm.service.dto.schedule.response.FarmWorkListResDto;
import nbdream.farm.service.dto.schedule.response.FarmWorkResDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final FarmWorkScheduleCustomRepository farmWorkScheduleCustomRepository;
    private final CropRepository cropRepository;
    private final ScheduleRepository scheduleRepository;


    // (작물, 월) 요청받아서 농작업일정을 반환
    public FarmWorkListResDto getFarmWorkSchedule(FarmWorkListReqDto request, Long memberId) {
        List<String> cropNames = farmWorkScheduleCustomRepository.findAllCropNames();
        if(cropNames == null || cropNames.isEmpty() || !cropNames.contains(request.getCrop())){
            throw new CropFromWorkScheduleNotFoundException();
        }

        List<FarmWorkSchedule> workScheduleList = farmWorkScheduleCustomRepository.findSchedulesByCropAndMonth(request.getCrop(), request.getMonth());
        if(workScheduleList == null || workScheduleList.isEmpty()){
            throw new FarmWorkScheduleNotFoundException();
        }

        return createFarmWorkListResDto(workScheduleList);
    }

    private FarmWorkListResDto createFarmWorkListResDto(List<FarmWorkSchedule> workScheduleList) {
        List<FarmWorkResDto> farmWorkResDtoList = new ArrayList<FarmWorkResDto>();
        for(FarmWorkSchedule workSchedule : workScheduleList){
            farmWorkResDtoList.add(new FarmWorkResDto(
                    workSchedule.getId(),
                    workSchedule.getStartEra(),
                    workSchedule.getEndEra(),
                    workSchedule.getBeginMonth(),
                    workSchedule.getEndMonth(),
                    workSchedule.getWorkCategoryDetail(),
                    workSchedule.getWorkNameDetail(),
                    workSchedule.getVideoUrl()
            ));
        }
        return new FarmWorkListResDto(farmWorkResDtoList);
    }
}
