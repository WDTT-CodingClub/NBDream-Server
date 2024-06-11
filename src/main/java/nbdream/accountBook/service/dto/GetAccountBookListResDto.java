package nbdream.accountBook.service.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetAccountBookListResDto {

    private List<String> categories;
    private List<GetAccountBookResDto> items;
    private long totalRevenue;
    private long totalExpense;
    private long totalCost;
}
