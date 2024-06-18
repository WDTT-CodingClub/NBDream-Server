package nbdream.farm.service.dto.LandElements;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbdream.farm.domain.LandElements;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetLandElementResDto {

    private float acid; // 산도
    private float vldpha; // 유효인산
    private float vldsia; //유효규산
    private float om; // 유기물
    private float posifert_mg; // 마그네슘
    private float posifert_k; // 칼륨
    private float posifert_ca; // 칼슘
    private float selc; // 전기전도도


    public void updateResDto(LandElements landElements){
        this.acid = landElements.getAcid();
        this.vldpha = landElements.getVldpha();
        this.vldsia = landElements.getVldsia();
        this.om = landElements.getOm();
        this.posifert_mg = landElements.getPosifert_mg();
        this.posifert_k = landElements.getPosifert_k();
        this.posifert_ca = landElements.getPosifert_ca();
        this.selc = landElements.getSelc();
    }
}
