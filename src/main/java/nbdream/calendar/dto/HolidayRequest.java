package nbdream.calendar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "특일정보 조회 요청DTO")
public class HolidayRequest {


    private String solYear;
    private String solMonth;
}
