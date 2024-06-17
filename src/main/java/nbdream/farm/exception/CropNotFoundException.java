package nbdream.farm.exception;

import nbdream.common.exception.NotFoundException;

public class CropNotFoundException extends NotFoundException {
    public CropNotFoundException() {
        super("존재하지 않는 작물입니다.");
    }
}
