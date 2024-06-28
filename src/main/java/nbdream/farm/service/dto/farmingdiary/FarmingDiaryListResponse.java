package nbdream.farm.service.dto.farmingdiary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FarmingDiaryListResponse {
    private List<FarmingDiaryResponse> response;
}
