package nbdream.calendar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@Schema(description = "특일정보 조회 응답DTO")
public class HolidayResponse {

    @Schema(description = "종류(01:국경일, 02:기념일, 03:24절기, 04:잡절)", example = "01")
    private String dateKind;

    @Schema(description = "날짜", example = "20240622")
    private String localDate;

    @Schema(description = "명칭", example = "삼일절")
    private String dateName;

    @Schema(description = "공공기관 휴일여부", example = "Y")
    private String isHoliday;

    @Builder
    public HolidayResponse(String dateKind, String localDate, String dateName, String isHoliday) {
        this.dateKind = dateKind;
        this.localDate = localDate;
        this.dateName = dateName;
        this.isHoliday = isHoliday;
    }

}
