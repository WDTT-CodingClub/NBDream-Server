package nbdream.accountBook.exception;

import nbdream.common.exception.NotFoundException;

public class SortTypeNotFoundException extends NotFoundException {
    public SortTypeNotFoundException() {
        super("정렬 타입을 찾을 수 없습니다.");
    }
}