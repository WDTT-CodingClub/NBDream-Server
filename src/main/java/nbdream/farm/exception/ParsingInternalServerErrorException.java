package nbdream.farm.exception;

import nbdream.common.exception.InternalServerErrorException;

public class ParsingInternalServerErrorException extends InternalServerErrorException {
    public ParsingInternalServerErrorException() {
        super("토양검정 데이터 파싱에 실패했습니다.");
    }
}