package nbdream.farm.exception;

import nbdream.common.exception.NotFoundException;

public class FarmWorkScheduleNotFoundException extends NotFoundException {
    public FarmWorkScheduleNotFoundException() { super ("농작업 일정 정보를 찾을 수 없습니다."); }
}
