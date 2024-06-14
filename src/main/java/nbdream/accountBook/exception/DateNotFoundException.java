package nbdream.accountBook.exception;

import nbdream.common.exception.NotFoundException;

public class DateNotFoundException extends NotFoundException {
    public DateNotFoundException() {
        super("날짜 정보를 읽어올 수 없습니다.");
    }
}