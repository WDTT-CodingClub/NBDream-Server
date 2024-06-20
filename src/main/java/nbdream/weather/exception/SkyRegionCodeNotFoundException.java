package nbdream.weather.exception;

import nbdream.common.exception.NotFoundException;

public class SkyRegionCodeNotFoundException extends NotFoundException {
    public SkyRegionCodeNotFoundException() {
        super("날씨 예보 지역 코드를 찾을 수 없습니다.");
    }
}
