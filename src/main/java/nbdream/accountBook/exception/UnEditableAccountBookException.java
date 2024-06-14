package nbdream.accountBook.exception;

import nbdream.common.exception.BadRequestException;
import nbdream.common.exception.NotFoundException;

public class UnEditableAccountBookException extends BadRequestException {
    public UnEditableAccountBookException() {
        super("장부를 수정, 삭제 할 수 없습니다.");
    }
}