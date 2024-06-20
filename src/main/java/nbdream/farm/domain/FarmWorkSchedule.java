package nbdream.farm.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nbdream.common.entity.BaseEntity;


@Entity
@Getter
@ToString
@NoArgsConstructor
public class FarmWorkSchedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String categoryName;       //품목 카테고리 이름 : 벼, 밭농사, 채소
    private String workName;           //농작업 명         : 감자, 고추, 기계이양재배
    private String startEra;            // 작업 시작 시기     : 상 중 하
    private String endEra;              // 작업 종료 시기     : 상 중 하
    private String workCategoryDetail;   // 상세 농작업 분류 : 생육과정, 병해충방제, 기상재해
    private String workNameDetail;        // 상세 농작업 명  : 객토, 퇴비주기
    private String videoUrl;               // 영상url
    private int beginMonth;                // 시작 월
    private int endMonth;                  // 종료 월
    private int requireTimeCount;         // 소요 분기        : (2월 상 ~ 3월 하) 이면 4

    public FarmWorkSchedule(String categoryName, String workName, String startEra, String endEra, String workCategoryDetail, String workNameDetail, String videoUrl, int beginMonth, int endMonth, int requireTimeCount) {
        this.categoryName = categoryName;
        this.workName = workName;
        this.startEra = startEra;
        this.endEra = endEra;
        this.workCategoryDetail = workCategoryDetail;
        this.workNameDetail = workNameDetail;
        this.videoUrl = videoUrl;
        this.beginMonth = beginMonth;
        this.endMonth = endMonth;
        this.requireTimeCount = requireTimeCount;
    }
}
