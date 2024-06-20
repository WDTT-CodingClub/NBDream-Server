package nbdream.accountBook.service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetAccountBookDetailResDto {

    @NotNull
    private Long id;
    private String title;
    private String category;
    private String transactionType;
    private Long amount;
    private String registerDateTime;
    private List<String> imageUrls;

}
