package nbdream.image.exception;

import nbdream.common.exception.BadRequestException;

public class InvalidDomainException extends BadRequestException {
    public InvalidDomainException() {
        super("domain 이름이 올바르지 않습니다.");
    }
}
