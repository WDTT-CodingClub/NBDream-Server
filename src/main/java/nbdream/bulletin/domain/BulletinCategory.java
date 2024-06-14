package nbdream.bulletin.domain;

import java.util.Arrays;

public enum BulletinCategory {
    FREE_TOPIC("free"), QUESTION("qna"), PESTS("bug");

    private final String value;

    BulletinCategory(String value) {
        this.value = value;
    }

    public static BulletinCategory of(String value) {
        return Arrays.stream(BulletinCategory.values())
                .filter(v -> v.equals(value))
                .findFirst()
                .orElseThrow();
    }

    public String getValue() {
        return this.value;
    }

}
