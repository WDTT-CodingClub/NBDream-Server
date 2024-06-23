package nbdream.weather.infrastructure.util.url_bulider;

import lombok.Builder;

import java.net.URI;

public class ShortTermWeatherApiUriBuilder {
    private static String AND = "&";

    private String baseUrl;
    private String serviceKey;
    private String pageNo;
    private String numOfRows;
    private String dataType;
    private String baseDate;
    private String baseTime;
    private String nx;
    private String ny;

    @Builder
    public ShortTermWeatherApiUriBuilder(final String baseUrl, final String serviceKey, final String baseDate, final int nx, final int ny) {
        this.baseUrl = baseUrl;
        this.serviceKey = "serviceKey=" + serviceKey;
        this.pageNo = "pageNo=1";
        this.numOfRows = "numOfRows=1000";
        this.dataType = "dataType=JSON";
        this.baseDate = "base_date=" + baseDate;
        this.baseTime = "base_time=0200";
        this.nx = "nx=" + nx;
        this.ny = "ny=" + ny;
    }

    public URI get() {
        return URI.create(baseUrl + serviceKey + AND + pageNo + AND + numOfRows + AND + dataType + AND +
                baseDate + AND + baseTime + AND + nx + AND + ny);
    }
}
