package nbdream.farm.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class Location {

    private String address;

    private String pnuCode;

    private double latitude;

    private double longitude;

    private double gridX;

    private double gridY;

    public Location(final String pnuCode, final double latitude, final double longitude) {
        this.pnuCode = pnuCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
