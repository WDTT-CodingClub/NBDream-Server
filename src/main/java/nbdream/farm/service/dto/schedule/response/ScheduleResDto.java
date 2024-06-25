package nbdream.farm.service.dto.schedule.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
}
