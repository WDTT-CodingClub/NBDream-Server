package nbdream.farm.exception;

import nbdream.common.exception.BadRequestException;

public class UnEditableScheduleException extends BadRequestException {
    public UnEditableScheduleException() {
        super("일정을 수정/삭제 할 권한이 없습니다.");
    }
}
