package nbdream.farm.service.dto.workSchedule.codeList;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class CodeItem {
    @JacksonXmlProperty(localName = "codeNm")
    private String codeNm;

    @JacksonXmlProperty(localName = "kidofcomdtySeCode")
    private String kidofcomdtySeCode;

    @JacksonXmlProperty(localName = "sort")
    private int sort;
}