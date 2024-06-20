package nbdream.farm.service.dto.schedule.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class FarmWorkListReqDto {
    private String crop;
    private int month;
}
