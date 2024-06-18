package nbdream.member.exception;

import nbdream.common.exception.BadRequestException;

public class DuplicateNicknameException extends BadRequestException {
    public DuplicateNicknameException() {
        super("중복되는 닉네임이 존재합니다.");
    }
}
