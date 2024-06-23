package nbdream.farm.service.dto.LandElements.soilData;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;

@Getter
public class Item {
    @JacksonXmlProperty(localName = "PNU_Code")
    private String pnuCode;

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
