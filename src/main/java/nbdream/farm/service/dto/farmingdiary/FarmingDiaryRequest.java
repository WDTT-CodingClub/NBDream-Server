package nbdream.farm.service.dto.farmingdiary;

import lombok.Getter;
import nbdream.calendar.dto.HolidayResponse;
import nbdream.farm.service.dto.work.WorkDto;

import java.time.LocalDate;
import java.util.List;

@Getter
public class FarmingDiaryRequest {

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
