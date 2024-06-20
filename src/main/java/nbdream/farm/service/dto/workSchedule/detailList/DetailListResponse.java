package nbdream.farm.service.dto.workSchedule.detailList;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class DetailListResponse {
    @JacksonXmlProperty(localName = "header")
    private Header header;

    @JacksonXmlProperty(localName = "body")
    private Body body;
}
@ToString
@Getter
class Header {
    @JacksonXmlProperty(localName = "resultCode")
    private String resultCode;

    @JacksonXmlProperty(localName = "resultMsg")
    private String resultMsg;

    @JacksonXmlProperty(localName = "requestParameter")
    private RequestParameter requestParameter;

}

@ToString
@Getter
class RequestParameter {
    @JacksonXmlProperty(localName = "cntntsNo")
    private String cntntsNo;
}

