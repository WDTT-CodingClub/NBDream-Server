package nbdream.farm.service.dto.LandElements.soilDataList;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nbdream.farm.domain.LandElements;

@Getter
@ToString
@NoArgsConstructor
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

    public ItemBjd(float acid, float vldpha, float vldsia, float om, float posifertMg, float posifertK, float posifertCa, float selc) {
        this.acid = acid;
        this.vldpha = vldpha;
        this.vldsia = vldsia;
        this.om = om;
        this.posifertMg = posifertMg;
        this.posifertK = posifertK;
        this.posifertCa = posifertCa;
        this.selc = selc;
    }

    public static ItemBjd defaultSoilData(){
        return new ItemBjd(6, 75, 200, 25, 2, 0.25F, 7.5F, 0.5F);
    }
}
