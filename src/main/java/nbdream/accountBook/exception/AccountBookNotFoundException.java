package nbdream.accountBook.exception;

import nbdream.common.exception.NotFoundException;

public class AccountBookNotFoundException extends NotFoundException {
    public AccountBookNotFoundException() {
        super("장부를 찾을 수 없습니다.");
    }
}