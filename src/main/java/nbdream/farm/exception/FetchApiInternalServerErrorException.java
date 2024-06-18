package nbdream.farm.exception;

import nbdream.common.exception.InternalServerErrorException;

public class FetchApiInternalServerErrorException extends InternalServerErrorException {
    public FetchApiInternalServerErrorException() {
        super("오픈API 데이터를 받아오는데 실패했습니다.");
    }
}