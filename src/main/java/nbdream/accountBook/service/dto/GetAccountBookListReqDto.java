package nbdream.accountBook.service.dto;

import lombok.*;
import nbdream.accountBook.domain.AccountBookCategory;
import nbdream.accountBook.domain.Sort;
import nbdream.accountBook.domain.TransactionType;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class GetAccountBookListReqDto {

    private Long lastContentsId;
    private String category;
    private String sort;
    private String start;
    private String end;
    private String transactionType;



}
