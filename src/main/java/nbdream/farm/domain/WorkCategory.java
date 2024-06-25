package nbdream.farm.domain;

import lombok.Getter;

@Getter
public enum WorkCategory {

    SEED_PREPARATION_AND_DISINFECTION("Seed Preparation and Disinfection"), // 종자예조 및 소독
    SEEDBED_PREPARATION_AND_INSTALLATION("Seedbed Preparation and Installation"), // 묘상준비 및 설치
    SOWING("Sowing"), // 파종
    GRAFTING("Grafting"), // 접목
    TRANSPLANTING_SEEDLINGS("Transplanting Seedlings"), // 가식
    SEEDLING_BED_MANAGEMENT("Seedling Bed Management"), // 묘판관리
    PLOWING_AND_SOIL_PREPARATION("Plowing and Soil Preparation"), // 경운정지
    COMPOST_AND_BASE_FERTILIZER_APPLICATION("Compost and Base Fertilizer Application"), // 퇴비 및 기비살포
    PLANTING("Planting"), // 정식
    SETTING_UP_SUPPORTS_AND_NETS("Setting Up Supports and Nets"), // 지주, 네트 세우기
    ADDITIONAL_FERTILIZER_APPLICATION("Additional Fertilizer Application"), // 추비 살포
    PEST_AND_DISEASE_CONTROL("Pest and Disease Control"), // 병충해 방제
    WEEDING("Weeding"), // 제초
    MULCHING_WITH_PLASTIC_OR_SOIL("Mulching with Plastic or Soil"), // 비닐 및 흙 덮기
    PINCHING_AND_PRUNING("Pinching and Pruning"), // 적심적아
    IRRIGATION_MANAGEMENT("Irrigation Management"), // 물 관리
    GREENHOUSE_INSTALLATION_AND_MANAGEMENT("Greenhouse Installation and Management"), // 하우스 설치 및 관리
    TEMPERATURE_CONTROL("Temperature Control"), // 온도 관리
    HARVESTING("Harvesting"), // 수확
    SORTING_AND_PACKAGING("Sorting and Packaging"), // 선별 및 포장
    TRANSPORTATION_AND_STORAGE("Transportation and Storage"), // 운반 및 저장
    MISCELLANEOUS("Miscellaneous"); // 기타

    private final String description;

    WorkCategory(String description) {
        this.description = description;
    }

}
