package nbdream.accountBook.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbdream.accountBook.domain.AccountBookCategory;
import nbdream.accountBook.domain.Sort;
import nbdream.accountBook.domain.TransactionType;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetAccountBookListReqDto {

    private Integer page = 0;
    private String category;
    private String sort;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String start;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String end;
    private String cost;

    private AccountBookCategory categoryEnum;
    private TransactionType transactionTypeEnum;
    private Sort sortEnum;
}
