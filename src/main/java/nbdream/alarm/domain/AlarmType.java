package nbdream.alarm.domain;

public enum AlarmType {
    COMMENT("comment"),
    SCHEDULE("schedule");

    private final String value;

    AlarmType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
