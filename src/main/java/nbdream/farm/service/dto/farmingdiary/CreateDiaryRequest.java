package nbdream.farm.service.dto.farmingdiary;

import lombok.Getter;

@Getter
public class CreateDiaryRequest {

    private String crop;

    private int workingPeopleNumber;

    private int workingTime;

    private int workingArea;

    private String memo;



}
