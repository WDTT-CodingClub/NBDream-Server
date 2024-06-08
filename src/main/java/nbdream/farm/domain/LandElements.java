package nbdream.farm.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbdream.common.entity.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LandElements extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private float pH; // 산도

    private float availablePhosphate; // 유효인산

    private float organicMatter; // 유기물

    private float magnesium; // 마그네슘

    private float potassium; // 칼륨

    private float Calcium; // 칼슘

    public LandElements(final float pH, final float availablePhosphate, final float organicMatter, final float magnesium, final float potassium, final float calcium) {
        this.pH = pH;
        this.availablePhosphate = availablePhosphate;
        this.organicMatter = organicMatter;
        this.magnesium = magnesium;
        this.potassium = potassium;
        Calcium = calcium;
    }
}
