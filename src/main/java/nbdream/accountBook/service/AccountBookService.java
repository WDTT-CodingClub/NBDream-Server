package nbdream.accountBook.service;

import lombok.RequiredArgsConstructor;
import nbdream.accountBook.exception.CategoryNotFoundException;
import nbdream.accountBook.repository.AccountBookHistoryRepository;
import nbdream.accountBook.repository.AccountBookRepository;
import nbdream.accountBook.service.dto.GetAccountBookListReqDto;
import nbdream.accountBook.service.dto.GetAccountBookListResDto;
import nbdream.accountBook.service.dto.GetAccountBookResDto;
import org.springframework.stereotype.Service;

import java.util.List;

import static nbdream.accountBook.domain.AccountBook.getTotalExpense;
import static nbdream.accountBook.domain.AccountBook.getTotalRevenue;

@Service
@RequiredArgsConstructor
public class AccountBookService {

    private final AccountBookRepository accountBookRepository;
    private final AccountBookHistoryRepository accountBookHistoryRepository;
    private final AccountBookHistoryService accountBookHistoryService;

    //장부 조회에 필요한 리스트 가져오는 메서드
    public GetAccountBookListResDto getMyAccountBookList(GetAccountBookListReqDto reqDto, Long memberId) {
        //카테고리 목록
        List<String> categories = accountBookHistoryService.getCategoryList();
        if (categories == null || categories.isEmpty()) {
            throw new CategoryNotFoundException();
        }

        //장부 내역 리스트
        List<GetAccountBookResDto> items = accountBookHistoryService.getMyAccountBookHistoryList(reqDto, memberId);

        //금액 로직
        Long totalRevenue = getTotalRevenue(items);
        Long totalExpense = getTotalExpense(items);
        Long totalCost = totalRevenue + totalExpense;

        GetAccountBookListResDto resDto = GetAccountBookListResDto.builder()
                .categories(categories)
                .items(items)
                .totalRevenue(totalRevenue)
                .totalExpense(totalExpense)
                .totalCost(totalCost)
                .build();
        return resDto;
    }


}
