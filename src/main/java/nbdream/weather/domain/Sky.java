package nbdream.weather.domain;

import java.util.Arrays;

public enum Sky {
    SUNNY(0, "맑음"), CLOUDY(6, "구름많음"), OVERCAST(9, "흐림");

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
}
