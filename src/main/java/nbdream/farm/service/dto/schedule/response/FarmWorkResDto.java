package nbdream.farm.service.dto.schedule.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class FarmWorkResDto {
    private Long id;
    private String startEra;        // 작업 시작 시기 : 상 중 하
    private String endEra;          // 작업 종료 시기 : 상 중 하
    private int startMonth;
    private int endMonth;
    private String farmWorkCategory;    // 농작업 분류 : 생육과정(주요농작업), 병해충방제, 기상재해
    private String farmWork;            // 농작업 : 객토, 퇴비 주기
    private String videoUrl;

}
