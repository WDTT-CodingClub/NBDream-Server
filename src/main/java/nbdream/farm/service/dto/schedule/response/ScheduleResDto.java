package nbdream.farm.service.dto.schedule.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nbdream.farm.domain.Schedule;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private boolean alarmOn;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime alarmDateTime;

    public ScheduleResDto updateResponse(Schedule schedule){
        this.id = schedule.getId();
        this.category = schedule.getCategory();
        this.title = schedule.getTitle();
        this.startDate = schedule.getStartDate();
        this.endDate = schedule.getEndDate();
        this.memo = schedule.getMemo();
        this.alarmOn = schedule.isAlarmOn();
        this.alarmDateTime = schedule.getAlarmDateTime();
        return this;
    }
}
