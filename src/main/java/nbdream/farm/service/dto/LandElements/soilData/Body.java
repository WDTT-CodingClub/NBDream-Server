package nbdream.farm.service.dto.LandElements.soilData;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class Body {
    @JacksonXmlElementWrapper(localName = "items")
    @JacksonXmlProperty(localName = "item")
    public List<Item> items;
}
