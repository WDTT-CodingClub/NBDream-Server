package nbdream.image.exception;

import nbdream.common.exception.InternalServerErrorException;

public class GcsConnectionException extends InternalServerErrorException {

    public GcsConnectionException() {
        super("이미지 서버 연결에 실패하였습니다.");
    }
}
