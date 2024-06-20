package nbdream.farm.exception;

import nbdream.common.exception.InternalServerErrorException;

public class CreateURLInternalServerErrorException extends InternalServerErrorException {
    public CreateURLInternalServerErrorException() {
        super("API 요청 URL 생성에 실패했습니다.");
    }
}