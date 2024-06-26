package nbdream.farm.exception;

import nbdream.common.exception.InvalidFormatException;

public class InvalidDateFormatException extends InvalidFormatException {
    public InvalidDateFormatException() { super("유효하지 않은 날짜 형식입니다.");}
}
