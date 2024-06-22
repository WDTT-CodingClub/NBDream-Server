package nbdream.farm.service.dto.LandElements.soilData;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;


@Getter
public class SoilDataResponse {
    public Script script;
    public Header header;
    public Body body;
}

@Getter
class Script {
}

@Getter
class Header {
    @JacksonXmlProperty(localName = "Result_Code")
    public int resultCode;

    @JacksonXmlProperty(localName = "Result_Msg")
    public String resultMsg;
}


