package nbdream.farm.exception;

import nbdream.common.exception.InternalServerErrorException;

public class ClosestSoilDataInternalServerErrorException extends InternalServerErrorException {
    public ClosestSoilDataInternalServerErrorException() {
        super("가장 가까운 토양정보를 매칭하는데 실패하였습니다.");
    }
}