package nbdream.bulletin.exception;

import nbdream.common.exception.BadRequestException;

public class UnEditableBulletinException extends BadRequestException {
    public UnEditableBulletinException() {
        super("게시글을 수정/삭제할 권한이 없습니다.");
    }
}
