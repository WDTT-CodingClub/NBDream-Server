package nbdream.farm.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbdream.common.entity.BaseEntity;
import nbdream.member.domain.Member;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Farm extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Embedded
    private Location location;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "land_elements_id")
    private LandElements landElements;

    public Farm(final Member member) {
        this.location = new Location();
        this.landElements = new LandElements();
        this.member = member;
    }

    public void updateLandElements(final LandElements landElements){
        this.landElements = landElements;
    }
    public void updateLocation(final String address, final String bjdCode, final double latitude, final double longitude) {
        this.location.update(address, bjdCode, latitude, longitude);
    }


}
