package nbdream.farm.exception;

import nbdream.common.exception.InternalServerErrorException;

public class KakaoLocalServiceErrorException extends InternalServerErrorException {
    public KakaoLocalServiceErrorException() {
        super("카카오 위도 경도 변환을 실패했습니다.");
    }
}