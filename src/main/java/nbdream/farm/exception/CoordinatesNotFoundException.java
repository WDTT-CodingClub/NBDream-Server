package nbdream.farm.exception;

import nbdream.common.exception.NotFoundException;

public class CoordinatesNotFoundException extends NotFoundException {
    public CoordinatesNotFoundException() {
        super("유저의 위도, 경도를 찾을 수 없습니다.");
    }
}