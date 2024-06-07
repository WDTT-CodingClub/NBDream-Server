package nbdream.member.domain;

import java.util.Arrays;

public enum LoginType {
    KAKAO("kakao"), NAVER("naver"), GOOGLE("google");

    private final String value;

    LoginType(String value) {
        this.value = value;
    }

    public static LoginType of(String value) {
        return Arrays.stream(values()).filter(v -> value.equals(v))
                .findFirst()
                .orElseThrow();
    }
}
