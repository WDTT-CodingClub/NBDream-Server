package nbdream.accountBook.exception;

import nbdream.common.exception.NotFoundException;

public class AccountBookHistoryNotFoundException extends NotFoundException {
    public AccountBookHistoryNotFoundException() {
        super("해당하는 장부 내역을 찾을 수 없습니다.");
    }
}