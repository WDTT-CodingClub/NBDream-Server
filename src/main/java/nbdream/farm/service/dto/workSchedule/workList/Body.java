package nbdream.farm.service.dto.workSchedule.workList;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class Body {
    @JacksonXmlProperty(localName = "items")
    private Items items;
}
