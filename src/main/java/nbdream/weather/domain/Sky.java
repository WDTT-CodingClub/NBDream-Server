package nbdream.weather.domain;

import java.util.Arrays;

public enum Sky {
    SUNNY(0, "맑음"),

    CLOUDY(6, "구름많음"), CLOUDY_AND_RAIN(-1, "구름많고 비"), CLOUDY_AND_SNOW(-1, "구름많고 눈"),
    CLOUDY_AND_RAIN_AND_SNOW(-1, "구름많고 비/눈"), CLOUDY_AND_SHOWER(-1, "구름많고 소나기"),

    OVERCAST(9, "흐림"), OVERCAST_AND_RAIN(-1, "흐리고 비"), OVERCAST_AND_SNOW(-1, "흐리고 눈"),
    OVERCAST_AND_RAIN_AND_SNOW(-1, "흐리고 비/눈"), OVERCAST_SHOWER(-1, "흐리고 소나기");

    private final int code;
    private final String value;

    Sky(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static Sky of(String value) {
        return Arrays.stream(values())
                .filter(s -> s.value.equals(value))
                .findFirst()
                .orElseThrow();
    }

    public static Sky of(int code) {
        if (code >= OVERCAST.code) return OVERCAST;
        if (code >= CLOUDY.code) return CLOUDY;
        return SUNNY;
    }

    public String getValue() {
        return value;
    }
}
