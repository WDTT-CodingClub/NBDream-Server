package nbdream.farm.service.dto.schedule.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nbdream.farm.domain.FarmWorkSchedule;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class FarmWorkListResDto {
    private List<FarmWorkResDto> farmWorkResDtoList;

    public FarmWorkListResDto createFarmWorkListResDto(List<FarmWorkSchedule> workScheduleList) {
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
