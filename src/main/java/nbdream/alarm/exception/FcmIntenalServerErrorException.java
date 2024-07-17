package nbdream.alarm.exception;

import nbdream.common.exception.InternalServerErrorException;
import nbdream.common.exception.NotFoundException;

public class FcmIntenalServerErrorException extends InternalServerErrorException {
    public FcmIntenalServerErrorException() {
        super("FCM 요청에 실패했습니다.");
    }
}