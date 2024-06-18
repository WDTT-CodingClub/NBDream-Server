package nbdream.accountBook.exception;

import nbdream.common.exception.BadRequestException;

public class UnAccessableAccountBookException extends BadRequestException {
    public UnAccessableAccountBookException() {
        super("장부 내역에 접근할 수 없습니다.");
    }
}