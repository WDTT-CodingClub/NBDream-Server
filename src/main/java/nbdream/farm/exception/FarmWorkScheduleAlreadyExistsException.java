package nbdream.farm.exception;

import nbdream.common.exception.BadRequestException;

public class FarmWorkScheduleAlreadyExistsException extends BadRequestException {
    public FarmWorkScheduleAlreadyExistsException () { super ("이미 농작업 일정 데이터가 존재합니다"); }
}
