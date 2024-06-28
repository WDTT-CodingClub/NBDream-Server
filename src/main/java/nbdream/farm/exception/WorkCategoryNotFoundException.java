package nbdream.farm.exception;

import nbdream.common.exception.NotFoundException;

public class WorkCategoryNotFoundException extends NotFoundException {
    public WorkCategoryNotFoundException() {
        super("해당하는 작업 카테고리를 찾을 수 없습니다.");
    }
}
