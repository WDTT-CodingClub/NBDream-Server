package nbdream.farm.service.dto.LandElements.soilDataList;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ItemBjd {
    @JacksonXmlProperty(localName = "No")
    private int no;

    @JacksonXmlProperty(localName = "BJD_Code")
    private String bjdCode;

    @JacksonXmlProperty(localName = "Any_Year")
    private int anyYear;

    @JacksonXmlProperty(localName = "Exam_Day")
    private String examDay;

    @JacksonXmlProperty(localName = "Exam_Type")
    private int examType;

    @JacksonXmlProperty(localName = "PNU_Nm")
    private String pnuNm;

    @JacksonXmlProperty(localName = "ACID")
    private float acid;

    @JacksonXmlProperty(localName = "VLDPHA")
    private float vldpha;

    @JacksonXmlProperty(localName = "VLDSIA")
    private float vldsia;

    @JacksonXmlProperty(localName = "OM")
    private float om;

    @JacksonXmlProperty(localName = "POSIFERT_MG")
    private float posifertMg;

    @JacksonXmlProperty(localName = "POSIFERT_K")
    private float posifertK;

    @JacksonXmlProperty(localName = "POSIFERT_CA")
    private float posifertCa;

    @JacksonXmlProperty(localName = "SELC")
    private float selc;
}
