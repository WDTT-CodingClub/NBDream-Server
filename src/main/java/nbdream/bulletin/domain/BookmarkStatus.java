package nbdream.bulletin.domain;

public enum BookmarkStatus {
    ON(1), OFF(0);

    private final int code;

    BookmarkStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
