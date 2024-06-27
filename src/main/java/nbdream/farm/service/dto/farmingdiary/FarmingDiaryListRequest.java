package nbdream.farm.service.dto.farmingdiary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FarmingDiaryListRequest {
    private String crop;
    private int year;
    private int month;
}