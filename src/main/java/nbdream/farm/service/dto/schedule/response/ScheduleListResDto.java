package nbdream.farm.service.dto.schedule.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nbdream.farm.domain.Schedule;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ScheduleListResDto {
    private List<ScheduleResDto> scheduleList;

    public ScheduleListResDto createResponse(List<Schedule> schedules){
        List<ScheduleResDto> dtoList = new ArrayList<ScheduleResDto>();
        for(Schedule schedule : schedules){
            dtoList.add(new ScheduleResDto(
                    schedule.getId(),
                    schedule.getCategory(),
                    schedule.getTitle(),
                    schedule.getStartDate(),
                    schedule.getEndDate(),
                    schedule.getMemo()
            ));
        }
        return new ScheduleListResDto(dtoList);
    }
}
