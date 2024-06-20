package nbdream.farm.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import nbdream.common.entity.BaseEntity;
import nbdream.member.domain.Member;

import static nbdream.farm.domain.Location.EMPTY;


@Entity
@Getter
@ToString
public class Farm extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String name;

    @Embedded
    private Location location;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "land_elements_id")
    private LandElements landElements;

    public Farm() {
        this.name = EMPTY;
        this.location = new Location();
        this.landElements = new LandElements();
    }

    public void updateLandElements(final LandElements landElements){
        this.landElements = landElements;
    }
    public void updateLocation(final String address, final double latitude, final double longitude) {
        this.location.update(address, latitude, longitude);
    }


}
