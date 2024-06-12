package nbdream.member.exception;

import nbdream.common.exception.NotFoundException;

public class MemberNotFoundException extends NotFoundException {
    public MemberNotFoundException() {
        super("유저 정보를 찾을 수 없습니다.");
    }
}
