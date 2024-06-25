package nbdream.farm.exception;

import nbdream.common.exception.NotFoundException;

public class ScheduleNotFoundException extends NotFoundException {
    public ScheduleNotFoundException() {
        super("해당 일정을 찾을 수 없습니다.");
    }
}