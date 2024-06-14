package nbdream.bulletin.exception;

import nbdream.common.exception.NotFoundException;

public class BulletinNotFoundException extends NotFoundException {
    public BulletinNotFoundException() {
        super("게시글을 찾을 수 없습니다.");
    }
}
