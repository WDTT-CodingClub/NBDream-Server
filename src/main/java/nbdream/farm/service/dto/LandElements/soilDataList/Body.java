package nbdream.farm.service.dto.LandElements.soilDataList;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class Body {
    @JacksonXmlElementWrapper(localName = "items")
    @JacksonXmlProperty(localName = "item")
    public List<ItemBjd> items;

    @JacksonXmlProperty(localName = "Rcdcnt")
    public int rcdCnt;

    @JacksonXmlProperty(localName = "Page_No")
    public int pageNo;

    @JacksonXmlProperty(localName = "Total_Count")
    public int totalCount;
}
