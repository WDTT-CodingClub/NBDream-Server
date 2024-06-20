package nbdream.farm.service.dto.workSchedule.codeList;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;


@ToString
@Getter
@NoArgsConstructor
public class CodeListResponse {
    private Header header;

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
    private String requestParameter;

}

