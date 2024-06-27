package nbdream.farm.service.dto.farmingdiary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbdream.calendar.dto.HolidayResponse;
import nbdream.farm.service.dto.work.WorkDto;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FarmingDiaryResponse {

    private Long farmingDiaryId;

    private String crop;

    private LocalDate date;

    private List<HolidayResponse> holidayResponses;

    private String weatherForecast;

    private List<String> imageUrls;

    private int workingPeopleNumber;

    private int workingTime;

    private int workingArea;

    private List<WorkDto> workDtos;

    private String memo;

}
