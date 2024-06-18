package nbdream.accountBook.exception;

import nbdream.common.exception.NotFoundException;

public class TransactionTypeNotFoundException extends NotFoundException {
    public TransactionTypeNotFoundException() {
        super("수입, 지출 타입을 찾을 수 없습니다.");
    }
}