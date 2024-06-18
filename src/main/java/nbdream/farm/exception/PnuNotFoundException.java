package nbdream.farm.exception;

import nbdream.common.exception.NotFoundException;

public class PnuNotFoundException extends NotFoundException {
    public PnuNotFoundException() {
        super("PNU코드를 찾을 수 없습니다.");
    }
}