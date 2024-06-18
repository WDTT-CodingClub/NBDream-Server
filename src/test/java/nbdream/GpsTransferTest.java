package nbdream;


import nbdream.common.util.GpsTransfer;
import nbdream.common.util.Grid;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class GpsTransferTest {

    @DisplayName("위도 경도 정보를 격자 정보로 변환할 수 있다")
    @ParameterizedTest
    @MethodSource("gpsAndGrid")
    void convertGpsToGrid(double latitude, double longitude, Grid expected) {
        // when
        Grid actual = GpsTransfer.convertGpsToGrid(latitude, longitude);

        // then
        Assertions.assertThat(actual).isEqualTo(expected);

    }

    static Stream<Arguments> gpsAndGrid() {
        return Stream.of(
                Arguments.arguments(37.579871128849334, 126.98935225645432, new Grid(60, 127)),
                Arguments.arguments(35.101148844565955, 129.02478725562108, new Grid(97, 74)),
                Arguments.arguments(33.500946412305076, 126.54663058817043, new Grid(53, 38)),
                Arguments.arguments(36.531948553218223, 127.01234567891234, new Grid(61, 104)),
                Arguments.arguments(39.987654321654987, 128.98765432198765, new Grid(92, 180))
        );
    }
}
