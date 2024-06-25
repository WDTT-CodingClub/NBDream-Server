package nbdream.weather.infrastructure.util;

import nbdream.farm.domain.Location;

public class DefaultLocation {
    public static final String address = "경기 성남시 분당구 백현동 532";
    public static final String bjdCode = "4113511000";
    public static final double longitude = 127.110343648845;
    public static final double latitude = 37.3952612751435;
    public static final int gridX = 62;
    public static final int gridY = 123;

    public static Location get() {
        return Location.builder()
                .address(DefaultLocation.address)
                .bjdCode(DefaultLocation.bjdCode)
                .longitude(DefaultLocation.longitude)
                .latitude(DefaultLocation.latitude)
                .gridX(DefaultLocation.gridX)
                .gridY(DefaultLocation.gridY)
                .build();
    }
}
