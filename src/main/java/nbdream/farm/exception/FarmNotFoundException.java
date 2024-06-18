package nbdream.farm.exception;

import nbdream.common.exception.NotFoundException;

public class FarmNotFoundException extends NotFoundException {
    public FarmNotFoundException() {
        super("농장을 찾을 수 없습니다.");
    }
}