package nbdream.accountBook.exception;

import nbdream.common.exception.NotFoundException;

public class CategoryNotFoundException extends NotFoundException {
    public CategoryNotFoundException() {
        super("카테고리 목록을 가져올 수 없습니다.");
    }
}