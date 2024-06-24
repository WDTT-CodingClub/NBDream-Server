package nbdream.farm.service.dto.farmingdiary;

import lombok.Getter;
import nbdream.calendar.dto.HolidayResponse;
import nbdream.farm.service.dto.work.CreateWorkRequest;

import java.time.LocalDate;
import java.util.List;

@Getter
public class CreateDiaryRequest {

    private String crop;

    private LocalDate date;

    private List<HolidayResponse> holidayResponses;

    private int workingPeopleNumber;

    private int workingTime;

    private int workingArea;

    private List<CreateWorkRequest> workRequests;

    private String memo;


}
