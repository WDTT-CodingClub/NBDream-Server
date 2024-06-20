package nbdream.weather.util.url_bulider;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.net.URI;

@Getter
@NoArgsConstructor
public class LongTermWeatherApiUriBuilder {
    private static String AND = "&";

    private String baseUrl;
    private String serviceKey;
    private String pageNo;
    private String numOfRows;
    private String dataType;
    private String regId;
    private String tmFc;

    @Builder
    public LongTermWeatherApiUriBuilder(final String baseUrl, final String serviceKey, final String regId, final String tmFc) {
        this.baseUrl = baseUrl;
        this.serviceKey = "serviceKey=" + serviceKey;
        this.pageNo = "pageNo=1";
        this.numOfRows = "numOfRows=1000";
        this.dataType = "dataType=JSON";
        this.regId = "regId=" + regId;
        this.tmFc = "tmFc=" + tmFc;
    }

    public URI get() {
        return URI.create(baseUrl + serviceKey + AND + pageNo + AND + numOfRows + AND + dataType + AND + regId + AND + tmFc);
    }
}
