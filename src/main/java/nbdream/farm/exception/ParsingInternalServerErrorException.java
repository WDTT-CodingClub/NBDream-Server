package nbdream.farm.exception;

import nbdream.common.exception.InternalServerErrorException;

public class ParsingInternalServerErrorException extends InternalServerErrorException {
    public ParsingInternalServerErrorException() {
        super("오픈 API 데이터 파싱에 실패했습니다.");
    }
}