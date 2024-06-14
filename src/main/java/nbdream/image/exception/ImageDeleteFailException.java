package nbdream.image.exception;

import nbdream.common.exception.BadRequestException;

public class ImageDeleteFailException extends BadRequestException {
    public ImageDeleteFailException() {
        super("이미지 삭제에 실패하였습니다. 도메인과 파일 이름을 올바르게 입력해주세요.");
    }
}
