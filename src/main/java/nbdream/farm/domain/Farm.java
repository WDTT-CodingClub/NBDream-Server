package nbdream.farm.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nbdream.common.entity.BaseEntity;
import nbdream.member.domain.Member;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Farm extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    private String name;

    @Embedded
    private Location location;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "land_elements_id", referencedColumnName = "id")
    private LandElements landElements;

    public Farm(final String name, final Location location, final LandElements landElements) {
        this.name = name;
        this.location = location;
        this.landElements = landElements;
    }

    public void updateLandElements(final LandElements landElements){
        this.landElements = landElements;
    }
}
