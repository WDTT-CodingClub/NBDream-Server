package nbdream.farm.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbdream.common.entity.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Farm extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Location location;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "land_elements_id")
    private LandElements landElements;

    public Farm(final String name, final Location location, final LandElements landElements) {
        this.name = name;
        this.location = location;
        this.landElements = landElements;
    }
}
