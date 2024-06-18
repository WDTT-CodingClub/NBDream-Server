package nbdream.farm.exception;

import nbdream.common.exception.NotFoundException;

public class LandElementsNotFoundException extends NotFoundException {
    public LandElementsNotFoundException() {
        super("토양검정 정보를 찾을 수 없습니다.");
    }
}