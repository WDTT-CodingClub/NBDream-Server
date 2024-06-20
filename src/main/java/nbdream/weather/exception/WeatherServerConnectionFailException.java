package nbdream.weather.exception;

import nbdream.common.exception.InternalServerErrorException;

public class WeatherServerConnectionFailException extends InternalServerErrorException {
    public WeatherServerConnectionFailException() {
        super("날씨 서버와 연결에 실패하였습니다.");
    }
}
