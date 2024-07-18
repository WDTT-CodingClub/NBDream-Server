package nbdream.alarm.exception;

import nbdream.common.exception.NotFoundException;

public class AlarmNotFoundException extends NotFoundException {
    public AlarmNotFoundException() {
        super("알람을 찾을 수 없습니다.");
    }
}