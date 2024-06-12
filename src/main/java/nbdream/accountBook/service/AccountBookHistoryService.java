package nbdream.accountBook.service;

import lombok.RequiredArgsConstructor;
import nbdream.accountBook.domain.AccountBook;
import nbdream.accountBook.domain.AccountBookCategory;
import nbdream.accountBook.domain.AccountBookHistory;
import nbdream.accountBook.domain.TransactionType;
import nbdream.accountBook.exception.AccountBookNotFoundException;
import nbdream.accountBook.repository.AccountBookHistoryRepository;
import nbdream.accountBook.repository.AccountBookRepository;
import nbdream.accountBook.service.dto.PostAccountBookReqDto;
import nbdream.common.advice.response.ApiResponse;
import nbdream.image.repository.ImageRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountBookHistoryService {

    private final AccountBookRepository accountBookRepository;
    private final AccountBookHistoryRepository accountBookHistoryRepository;
    private final ImageRepository imageRepository;


    //장부 작성
    public ApiResponse<Void> writeAccountBookHistory(PostAccountBookReqDto reqDto, Long memberId) {
        AccountBook accountBook = accountBookRepository.findByMemberId(memberId)
                .orElseThrow(AccountBookNotFoundException::new);

        AccountBookHistory accountBookHistory = AccountBookHistory.builder()
                .accountBook(accountBook)
                .accountBookCategory(AccountBookCategory.fromValue(reqDto.getCategory()))
                .transactionType(reqDto.getExpense() == null ? TransactionType.REVENUE : TransactionType.EXPENSE)
                .amount(reqDto.getExpense() == null ? reqDto.getRevenue() : reqDto.getExpense())
                .content(reqDto.getTitle())
                .dateTime(reqDto.getParsedRegisterDateTime())
                .build();
        accountBookHistoryRepository.save(accountBookHistory);
        return ApiResponse.ok();
    }
}
