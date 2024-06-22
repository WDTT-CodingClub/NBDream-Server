package nbdream.calendar.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class HolidayResponse {

    private String dateKind;

    private String localDate;

    private String dateName;

    private String isHoliday;

    @Builder
    public HolidayResponse(String dateKind, String localDate, String dateName, String isHoliday) {
        this.dateKind = dateKind;
        this.localDate = localDate;
        this.dateName = dateName;
        this.isHoliday = isHoliday;
    }

}
