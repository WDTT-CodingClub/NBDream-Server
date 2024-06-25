package nbdream.farm.service.dto.schedule.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nbdream.farm.domain.Schedule;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ScheduleResDto {
    private Long id;
    private String category;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private String memo;

    public ScheduleResDto updateResponse(Schedule schedule){
        this.id = schedule.getId();
        this.category = schedule.getCategory();
        this.title = schedule.getTitle();
        this.startDate = schedule.getStartDate();
        this.endDate = schedule.getEndDate();
        this.memo = schedule.getMemo();
        return this;
    }
}
