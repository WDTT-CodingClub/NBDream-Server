package nbdream.farm.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class Location {
    public final static String EMPTY = "";

    private String address;

    private String pnuCode;

    private double latitude;

    private double longitude;

    private double gridX;

    private double gridY;

    public Location() {
        this.address = EMPTY;
        this.pnuCode = EMPTY;
    }

    public void update(final String address, final double latitude, final double longitude) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
