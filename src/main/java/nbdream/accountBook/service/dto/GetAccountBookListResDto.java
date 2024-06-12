package nbdream.accountBook.service.dto;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetAccountBookListResDto {

    private List<String> categories;
    private List<GetAccountBookResDto> items;
    private long totalRevenue;
    private long totalExpense;
    private long totalCost;
}
