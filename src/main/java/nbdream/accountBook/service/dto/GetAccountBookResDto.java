package nbdream.accountBook.service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetAccountBookResDto {

    @NotNull
    private String id;
    private String title;
    private String category;
    private int year;
    private int month;
    private int day;
    private String dayName;
    private Long revenue;
    private Long expense;
    private String thumbnail;
    private int imageSize;


}
