package nbdream.farm.exception;

import nbdream.common.exception.NotFoundException;

public class CropFromWorkScheduleNotFoundException extends NotFoundException {
    public CropFromWorkScheduleNotFoundException() {
        super("해당 작물에 대한 농작업 일정 정보를 찾을 수 없습니다.");
    }
}
