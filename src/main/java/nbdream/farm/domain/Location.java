package nbdream.farm.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Embeddable
@NoArgsConstructor(access = PROTECTED)
@ToString
public class Location {

    private String address;

    private String pnuCode;

    private Double latitude;

    private Double longitude;

    private Double gridX;

    private Double gridY;

    public Location(final String pnuCode, final double latitude, final double longitude) {
        this.pnuCode = pnuCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
