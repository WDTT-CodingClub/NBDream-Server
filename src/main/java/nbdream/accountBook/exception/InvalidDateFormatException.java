package nbdream.accountBook.exception;

public class InvalidDateFormatException extends IllegalArgumentException {
    public InvalidDateFormatException() {
        super("날짜 형식이 올바르지 않습니다.");
    }
}