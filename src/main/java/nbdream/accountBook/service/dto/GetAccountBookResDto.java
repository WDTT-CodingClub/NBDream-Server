package nbdream.accountBook.service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetAccountBookResDto {

    @NotNull
    private Long id;
    private String title;
    private String category;
    private int year;
    private int month;
    private int day;
    private String dayName;
    private String transactionType;
    private Long amount;
    private String thumbnail;
    private int imageSize;


}
