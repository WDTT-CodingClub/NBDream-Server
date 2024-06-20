package nbdream.farm.service.dto.workSchedule.workList;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class WorkItem {
    @JacksonXmlProperty(localName = "cntntsNo")
    private String cntntsNo;

    @JacksonXmlProperty(localName = "fileDownUrlInfo")
    private String fileDownUrlInfo;

    @JacksonXmlProperty(localName = "fileName")
    private String fileName;

    @JacksonXmlProperty(localName = "fileSeCode")
    private String fileSeCode;

    @JacksonXmlProperty(localName = "orginlFileNm")
    private String orginlFileNm;

    @JacksonXmlProperty(localName = "sj")
    private String sj;
}
