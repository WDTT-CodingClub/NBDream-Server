package nbdream.farm.domain;

import lombok.Getter;
import nbdream.farm.exception.WorkCategoryNotFoundException;

import java.util.Arrays;

@Getter
public enum WorkCategory {

    SEED_PREPARATION_AND_DISINFECTION("종자 예조 및 소독"),
    SEEDBED_PREPARATION_AND_INSTALLATION("묘상 준비 및 설치"),
    SOWING("파종"),
    GRAFTING("접목"),
    TRANSPLANTING_SEEDLINGS("가식"),
    SEEDLING_BED_MANAGEMENT("묘판관리"),
    PLOWING_AND_SOIL_PREPARATION("경운정지"),
    COMPOST_AND_BASE_FERTILIZER_APPLICATION("퇴비 및 기비살포"),
    PLANTING("정식"),
    SETTING_UP_SUPPORTS_AND_NETS("지주, 네트 세우기"),
    ADDITIONAL_FERTILIZER_APPLICATION("추비 살포"),
    PEST_AND_DISEASE_CONTROL("병충해 방제"),
    WEEDING("제초"),
    MULCHING_WITH_PLASTIC_OR_SOIL("비닐 및 흙 덮기"),
    PINCHING_AND_PRUNING("적심적아"),
    IRRIGATION_MANAGEMENT("물 관리"),
    GREENHOUSE_INSTALLATION_AND_MANAGEMENT("하우스 설치 및 관리"),
    TEMPERATURE_CONTROL("온도 관리"),
    HARVESTING("수확"),
    SORTING_AND_PACKAGING("선별 및 포장"),
    TRANSPORTATION_AND_STORAGE("운반 및 저장"),
    MISCELLANEOUS("기타");

    private final String description;

    WorkCategory(String description) {
        this.description = description;
    }

    public static WorkCategory of(String value) {
        return Arrays.stream(values())
                .filter(workCategory -> workCategory.description.equals(value))
                .findFirst()
                .orElseThrow(WorkCategoryNotFoundException::new);
    }

}
