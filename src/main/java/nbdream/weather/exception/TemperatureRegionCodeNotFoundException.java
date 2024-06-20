package nbdream.weather.exception;

import nbdream.common.exception.NotFoundException;

public class TemperatureRegionCodeNotFoundException extends NotFoundException {
    public TemperatureRegionCodeNotFoundException() {
        super("기온 예보 지역 코드를 찾을 수 없습니다.");
    }
}
