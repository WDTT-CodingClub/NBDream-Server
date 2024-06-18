package nbdream.farm.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nbdream.common.entity.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class LandElements extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private float acid; // 산도
    private float vldpha; // 유효인산
    private float vldsia; //유효규산
    private float om; // 유기물
    private float posifert_mg; // 마그네슘
    private float posifert_k; // 칼륨
    private float posifert_ca; // 칼슘
    private float selc; // 전기전도도

    public LandElements(float acid, float vldpha, float vldsia, float om, float posifert_mg, float posifert_k, float posifert_ca, float selc) {
        this.acid = acid;
        this.vldpha = vldpha;
        this.vldsia = vldsia;
        this.om = om;
        this.posifert_mg = posifert_mg;
        this.posifert_k = posifert_k;
        this.posifert_ca = posifert_ca;
        this.selc = selc;
    }

    public void update(LandElements landElements) {
        this.acid = landElements.getAcid();
        this.vldpha = landElements.getVldpha();
        this.vldsia = landElements.getVldsia();
        this.om = landElements.getOm();
        this.posifert_mg = landElements.getPosifert_mg();
        this.posifert_k = landElements.getPosifert_k();
        this.posifert_ca = landElements.getPosifert_ca();
        this.selc = landElements.getSelc();
    }

    public String toStringForAIChat() {
        return "LandElements{" +
                ", acid=" + acid +
                ", vldpha=" + vldpha +
                ", vldsia=" + vldsia +
                ", om=" + om +
                ", posifert_mg=" + posifert_mg +
                ", posifert_k=" + posifert_k +
                ", posifert_ca=" + posifert_ca +
                ", selc=" + selc +
                '}';
    }
}
