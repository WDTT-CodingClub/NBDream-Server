package nbdream.farm.service.dto.workSchedule.detailList;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class DetailItem {
    @JacksonXmlProperty(localName = "beginEra")
    private String beginEra;

    @JacksonXmlProperty(localName = "beginMon")
    private String beginMon;

    @JacksonXmlProperty(localName = "cntntsNo")
    private String cntntsNo;

    @JacksonXmlProperty(localName = "endEra")
    private String endEra;

    @JacksonXmlProperty(localName = "endMon")
    private String endMon;

    @JacksonXmlProperty(localName = "farmWorkFlag")
    private String farmWorkFlag;

    @JacksonXmlProperty(localName = "infoSeCode")
    private String infoSeCode;

    @JacksonXmlProperty(localName = "infoSeCodeNm")
    private String infoSeCodeNm;

    @JacksonXmlProperty(localName = "kidofcomdtySeCode")
    private String kidofcomdtySeCode;

    @JacksonXmlProperty(localName = "kidofcomdtySeCodeNm")
    private String kidofcomdtySeCodeNm;

    @JacksonXmlProperty(localName = "opertNm")
    private String opertNm;

    @JacksonXmlProperty(localName = "reqreMonth")
    private String reqreMonth;

    @JacksonXmlProperty(localName = "vodUrl")
    private String vodUrl;
}
