package nbdream.accountBook.service;

import lombok.RequiredArgsConstructor;
import nbdream.accountBook.domain.AccountBook;
import nbdream.accountBook.domain.AccountBookHistory;
import nbdream.accountBook.exception.CategoryNotFoundException;
import nbdream.accountBook.repository.AccountBookRepository;
import nbdream.accountBook.service.dto.GetAccountBookListReqDto;
import nbdream.accountBook.service.dto.GetAccountBookListResDto;
import nbdream.accountBook.service.dto.GetAccountBookResDto;
import nbdream.member.domain.Member;
import nbdream.member.exception.MemberNotFoundException;
import nbdream.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountBookService {

    private final AccountBookHistoryService accountBookHistoryService;
    private final AccountBookRepository accountBookRepository;
    private final MemberRepository memberRepository;

    // 장부 조회에 필요한 리스트 가져오는 메서드
    public GetAccountBookListResDto getMyAccountBookList(GetAccountBookListReqDto reqDto, Long memberId) {
        List<String> categories = getCategoryList();
        AccountBook accountBook = accountBookRepository.findByMemberId(memberId)
                .orElseGet(() -> createNewAccountBook(memberId));
        List<AccountBookHistory> accountBookHistoryList = accountBookHistoryService.getMyAccountBookHistoryList(reqDto, accountBook.getMember().getId());

        Long totalRevenue = accountBook.getTotalRevenue(accountBookHistoryList);
        Long totalExpense = accountBook.getTotalExpense(accountBookHistoryList);
        Long totalCost = totalRevenue + totalExpense;

        List<GetAccountBookResDto> items = accountBookHistoryService.convertToDtoList(accountBookHistoryList);
        return buildAccountBookListResDto(categories, items, totalRevenue, totalExpense, totalCost);
    }

    // 카테고리 목록 조회 메서드
    private List<String> getCategoryList() {
        List<String> categories = accountBookHistoryService.getCategoryList();
        if (categories == null || categories.isEmpty()) {
            throw new CategoryNotFoundException();
        }
        return categories;
    }

    // 장부 생성
    private AccountBook createNewAccountBook(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException());
        AccountBook accountBook = AccountBook.builder()
                .member(member)
                .build();
        return accountBookRepository.save(accountBook);
    }

    // resDto 생성
    private GetAccountBookListResDto buildAccountBookListResDto(List<String> categories, List<GetAccountBookResDto> items,
                                                                Long totalRevenue, Long totalExpense, Long totalCost) {
        return GetAccountBookListResDto.builder()
                .categories(categories)
                .items(items)
                .totalRevenue(totalRevenue)
                .totalExpense(totalExpense)
                .totalCost(totalCost)
                .build();
    }
}
