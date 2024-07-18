package nbdream.alarm.exception;

import nbdream.common.exception.NotFoundException;

public class FcmTokenNotFoundException extends NotFoundException {
    public FcmTokenNotFoundException() {
        super("FCM 토큰을 찾을 수 없습니다.");
    }
}