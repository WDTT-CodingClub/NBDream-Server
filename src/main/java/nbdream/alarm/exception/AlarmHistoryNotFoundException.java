package nbdream.alarm.exception;

import nbdream.common.exception.NotFoundException;

public class AlarmHistoryNotFoundException extends NotFoundException {
    public AlarmHistoryNotFoundException() {
        super("알람 내역을 찾을 수 없습니다.");
    }
}