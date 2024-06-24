package nbdream.accountBook.service;

import lombok.RequiredArgsConstructor;
import nbdream.accountBook.domain.AccountBook;
import nbdream.accountBook.domain.AccountBookCategory;
import nbdream.accountBook.domain.AccountBookHistory;
import nbdream.accountBook.domain.TransactionType;
import nbdream.accountBook.exception.CategoryNotFoundException;
import nbdream.accountBook.repository.AccountBookHistoryRepository;
import nbdream.accountBook.repository.AccountBookRepository;
import nbdream.accountBook.service.dto.GetAccountBookGraphResDto;
import nbdream.accountBook.service.dto.GetAccountBookListReqDto;
import nbdream.accountBook.service.dto.GetAccountBookListResDto;
import nbdream.accountBook.service.dto.GetAccountBookResDto;
import nbdream.image.domain.Image;
import nbdream.image.repository.ImageRepository;
import nbdream.member.domain.Member;
import nbdream.member.exception.MemberNotFoundException;
import nbdream.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountBookService {

    private final AccountBookRepository accountBookRepository;
    private final MemberRepository memberRepository;
    private final AccountBookHistoryRepository accountBookHistoryRepository;
    private final ImageRepository imageRepository;

    private static final int PAGE_SIZE = 10;

    // 장부 조회에 필요한 리스트 가져오는 메서드
    public GetAccountBookListResDto getMyAccountBookList(GetAccountBookListReqDto request, Long memberId) {
        AccountBook accountBook = accountBookRepository.findByMemberId(memberId)
                .orElseGet(() -> createNewAccountBook(memberId));

        //커서 페이징
        Long cursor = request.getLastContentsId() == null ? 0 : request.getLastContentsId();
        List<AccountBookHistory> accountBookHistoryList = accountBookHistoryRepository.findByFilterAndCursor(memberId, cursor, PAGE_SIZE + 1, request);

        boolean hasNext = hasNext(accountBookHistoryList);
        //
        List<AccountBookHistory> allList = accountBookHistoryRepository.findAllByFilter(memberId, request);

        List<String> categories = getCategoryList(allList);
        Long totalRevenue = accountBook.getTotalRevenue(allList);
        Long totalExpense = accountBook.getTotalExpense(allList);
        Long totalCost = totalRevenue + totalExpense;
        List<GetAccountBookGraphResDto> revenuePercent = getPercentsByCategory(allList, totalRevenue, TransactionType.REVENUE);
        List<GetAccountBookGraphResDto> expensePercent = getPercentsByCategory(allList, totalExpense, TransactionType.EXPENSE);

        for (GetAccountBookGraphResDto item : revenuePercent){
            System.out.println("수입 카테고리 퍼센트 : " + item);
        }
        for (GetAccountBookGraphResDto item : expensePercent){
            System.out.println("지출 카테고리 퍼센트 : " + item);
        }

        List<GetAccountBookResDto> items = convertToDtoList(accountBookHistoryList);
        return createAccountBookListResDto(categories, items, totalRevenue, totalExpense, totalCost, hasNext, revenuePercent, expensePercent);
    }

    //카테고리별 금액의 퍼센티지를 반환
    private List<GetAccountBookGraphResDto> getPercentsByCategory(List<AccountBookHistory> allList, Long totalAmount, TransactionType transactionType) {
        List<AccountBookHistory> list = new ArrayList<>();
        for (AccountBookHistory item : allList) {
            if (item.getTransactionType().equals(transactionType)) {
                list.add(item);
            }
        }

        Map<AccountBookCategory, Long> categoryMap = new HashMap<>();
        for (AccountBookHistory item : list) {
            AccountBookCategory category = item.getAccountBookCategory();
            Long amount = item.getAmount();
            categoryMap.put(category, categoryMap.getOrDefault(category, 0L) + amount);
        }

        List<GetAccountBookGraphResDto> result = new ArrayList<>();
        for (Map.Entry<AccountBookCategory, Long> entry : categoryMap.entrySet()) {
            AccountBookCategory category = entry.getKey();
            Long sum = entry.getValue();
            float percent = (float) sum / totalAmount * 100;
            result.add(new GetAccountBookGraphResDto(percent, category.getValue()));
        }

        return result;
    }

    private boolean hasNext(List<AccountBookHistory> list){
        boolean hasNext = list.size() > PAGE_SIZE ? true : false;
        if (hasNext) {
            list.remove(PAGE_SIZE);
        }
        return hasNext;
    }

    // 장부 생성
    private AccountBook createNewAccountBook(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        AccountBook accountBook = AccountBook.builder()
                .member(member)
                .build();
        return accountBookRepository.save(accountBook);
    }

    // 카테고리 목록 조회 메서드
    private List<String> getCategoryList(List<AccountBookHistory> list) {
        Set<String> categorySet = new HashSet<>();
        for (AccountBookHistory item : list) {
            categorySet.add(item.getAccountBookCategory().getValue());
        }
        return new ArrayList<>(categorySet);
    }

    private List<GetAccountBookResDto> convertToDtoList(List<AccountBookHistory> historyList) {
        return historyList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private GetAccountBookResDto convertToDto(AccountBookHistory history) {
        List<Image> imgList = imageRepository.findAllByTargetId(history.getId());
        String imgUrl = imgList.stream()
                .findFirst()
                .map(Image::getImageUrl)
                .orElse(null);

        return GetAccountBookResDto.builder()
                .id(history.getId())
                .title(history.getContent())
                .category(history.getAccountBookCategory().getValue())
                .year(history.getDateTime().getYear())
                .month(history.getDateTime().getMonthValue())
                .day(history.getDateTime().getDayOfMonth())
                .dayName(history.getKoreanDayOfWeek())
                .transactionType(history.getTransactionType().getValue())
                .amount(history.getAmount())
                .thumbnail(imgUrl)
                .imageSize(imgList.size())
                .build();
    }

    private GetAccountBookListResDto createAccountBookListResDto(List<String> categories, List<GetAccountBookResDto> items,
                                                                Long totalRevenue, Long totalExpense, Long totalCost, boolean hasNext,
                                                                 List<GetAccountBookGraphResDto> revenuePercent, List<GetAccountBookGraphResDto> expensePercent) {
        return GetAccountBookListResDto.builder()
                .categories(categories)
                .items(items)
                .totalRevenue(totalRevenue)
                .totalExpense(totalExpense)
                .totalCost(totalCost)
                .revenuePercent(revenuePercent)
                .expensePercent(expensePercent)
                .hasNext(hasNext)
                .build();
    }

}
