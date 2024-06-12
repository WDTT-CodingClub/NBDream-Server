package nbdream.member.exception;

import nbdream.common.exception.UnauthorizedException;

public class MemberNotFoundException extends UnauthorizedException {
    public MemberNotFoundException() {
        super("유저 정보를 찾을 수 없습니다.");
    }
}