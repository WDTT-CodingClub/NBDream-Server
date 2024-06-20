package nbdream.farm.exception;

import nbdream.common.exception.NotFoundException;

public class CropNotFoundException extends NotFoundException {
    public CropNotFoundException() {
        super("작물을 찾지 못하였습니다.");
    }
}
