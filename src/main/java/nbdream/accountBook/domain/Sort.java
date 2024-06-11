package nbdream.accountBook.domain;

public enum Sort {
    EARLIEST("earliest"),
    OLDEST("oldest");

    private final String value;

    Sort(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    // 소문자로 된 string을 enum으로 변환
    public static Sort fromValue(String value) {
        for (Sort sort : Sort.values()) {
            if (sort.value.equalsIgnoreCase(value)) {
                return sort;
            }
        }
        throw new IllegalArgumentException("Unknown Sort value: " + value);
    }
}
