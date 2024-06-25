package nbdream.farm.exception;

import nbdream.common.exception.NotFoundException;

public class FarmingDiaryNotFoundException extends NotFoundException {
    public FarmingDiaryNotFoundException() { super("농장 일지를 찾을 수 없습니다.");}
}
