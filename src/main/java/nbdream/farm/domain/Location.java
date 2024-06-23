package nbdream.farm.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.ToString;
import nbdream.farm.util.GpsTransfer;
import nbdream.farm.util.Grid;

@Getter
@Embeddable
@ToString
public class Location {
    public final static String EMPTY = "";

    private String address;

    private String bjdCode;

    private double latitude;

    private double longitude;

    private int gridX;

    private int gridY;

    public Location() {
        this.address = EMPTY;
        this.bjdCode = EMPTY;
    }

    public void update(final String address, final double latitude, final double longitude) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        Grid grid = GpsTransfer.convertGpsToGrid(latitude, longitude);
        this.gridX = grid.getX();
        this.gridY = grid.getY();
    }
}
