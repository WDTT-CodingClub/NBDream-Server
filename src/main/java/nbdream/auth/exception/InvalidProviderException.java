package nbdream.auth.exception;


import nbdream.common.exception.BadRequestException;

public class InvalidProviderException extends BadRequestException {
    public InvalidProviderException() {
        super("provider가 올바르지 않습니다. naver, kakao, google만 입력해주세요ㅕ.");
    }
}
