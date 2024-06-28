package nbdream.farm.service.dto.farmingdiary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchFarmingDiaryCond {
    private String crop;
    private String query;
    private LocalDate startDate;
    private LocalDate endDate;
}
