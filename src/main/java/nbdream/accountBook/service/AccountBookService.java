package nbdream.accountBook.service;

import lombok.RequiredArgsConstructor;
import nbdream.accountBook.domain.AccountBook;
import nbdream.accountBook.domain.AccountBookCategory;
import nbdream.accountBook.domain.AccountBookHistory;
import nbdream.accountBook.domain.TransactionType;
import nbdream.accountBook.exception.CategoryNotFoundException;
import nbdream.accountBook.repository.AccountBookHistoryRepository;
import nbdream.accountBook.repository.AccountBookRepository;
import nbdream.accountBook.repository.specifications.AccountBookHistorySpecifications;
import nbdream.accountBook.service.dto.GetAccountBookListReqDto;
import nbdream.accountBook.service.dto.GetAccountBookListResDto;
import nbdream.accountBook.service.dto.GetAccountBookResDto;
import nbdream.image.domain.Image;
import nbdream.image.repository.ImageRepository;
import nbdream.member.domain.Member;
import nbdream.member.exception.MemberNotFoundException;
import nbdream.member.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountBookService {

    private final AccountBookRepository accountBookRepository;
    private final MemberRepository memberRepository;
    private final AccountBookHistoryRepository accountBookHistoryRepository;
    private final ImageRepository imageRepository;

    private static final int PAGE_SIZE = 3;

    // 장부 조회에 필요한 리스트 가져오는 메서드
    public GetAccountBookListResDto getMyAccountBookList(GetAccountBookListReqDto reqDto, Long memberId) {
        AccountBook accountBook = accountBookRepository.findByMemberId(memberId)
                .orElseGet(() -> createNewAccountBook(memberId));
        List<String> categories = getCategoryList();

        Pageable pageable = PageRequest.of(reqDto.getPage(), PAGE_SIZE);
        Specification<AccountBookHistory> spec = AccountBookHistorySpecifications.withFilters(reqDto, memberId);
        Page<AccountBookHistory> accountBookHistoryPage = accountBookHistoryRepository.findAll(spec, pageable);

        List<AccountBookHistory> accountBookHistoryList = accountBookHistoryPage.getContent();

        Long totalRevenue = accountBook.getTotalRevenue(accountBookHistoryList);
        Long totalExpense = accountBook.getTotalExpense(accountBookHistoryList);
        Long totalCost = totalRevenue + totalExpense;

        List<GetAccountBookResDto> items = convertToDtoList(accountBookHistoryList);
        return createAccountBookListResDto(categories, items, totalRevenue, totalExpense, totalCost);
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
    private List<String> getCategoryList() {
        List<String> categories = List.of(AccountBookCategory.values())
                .stream()
                .map(AccountBookCategory::getValue)
                .collect(Collectors.toList());

        if (categories == null || categories.isEmpty()) {
            throw new CategoryNotFoundException();
        }
        return categories;
    }

    // 내역 리스트를 DTO로 변환
    private List<GetAccountBookResDto> convertToDtoList(List<AccountBookHistory> historyList) {
        return historyList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 내역을 DTO로 변환
    private GetAccountBookResDto convertToDto(AccountBookHistory history) {
//        List<Image> imgList = imageRepository.findAllByTargetId(history.getId());
//
//        String imgUrl = imgList.stream()
//                .findFirst()
//                .map(Image::getStoredPath)
//                .orElse(null);

        return GetAccountBookResDto.builder()
                .id(history.getId().toString())
                .title(history.getContent())
                .category(history.getAccountBookCategory().getValue())
                .year(history.getDateTime().getYear())
                .month(history.getDateTime().getMonthValue())
                .day(history.getDateTime().getDayOfMonth())
                .dayName(history.getKoreanDayOfWeek())
                .expense(history.getTransactionType() == TransactionType.EXPENSE ? (long) history.getAmount() : null)
                .revenue(history.getTransactionType() == TransactionType.REVENUE ? (long) history.getAmount() : null)
//                .thumbnail(imgUrl)
//                .imageSize(imgList.size())
                .build();
    }

    // resDto 생성
    private GetAccountBookListResDto createAccountBookListResDto(List<String> categories, List<GetAccountBookResDto> items,
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
