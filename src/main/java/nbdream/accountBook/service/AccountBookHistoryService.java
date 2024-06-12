package nbdream.accountBook.service;

import lombok.RequiredArgsConstructor;
import nbdream.accountBook.domain.AccountBookCategory;
import nbdream.accountBook.domain.AccountBookHistory;
import nbdream.accountBook.domain.TransactionType;
import nbdream.accountBook.repository.AccountBookHistoryRepository;
import nbdream.accountBook.repository.specifications.AccountBookHistorySpecifications;
import nbdream.accountBook.service.dto.GetAccountBookListReqDto;
import nbdream.accountBook.service.dto.GetAccountBookResDto;
import nbdream.image.domain.Image;
import nbdream.image.repository.ImageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountBookHistoryService {

    private final AccountBookHistoryRepository accountBookHistoryRepository;
    private final ImageRepository imageRepository;
    private static final int PAGE_SIZE = 3;     // 페이지 크기

    // 장부 내역의 카테고리 항목을 리스트로 반환
    public List<String> getCategoryList() {
        return List.of(AccountBookCategory.values())
                .stream()
                .map(AccountBookCategory::getValue)
                .collect(Collectors.toList());
    }

    // 내 장부 내역을 리스트로 반환
    public List<AccountBookHistory> getMyAccountBookHistoryList(GetAccountBookListReqDto reqDto, Long memberId) {
        Pageable pageable = PageRequest.of(reqDto.getPage(), PAGE_SIZE);
        Specification<AccountBookHistory> spec = AccountBookHistorySpecifications.withFilters(reqDto, memberId);
        Page<AccountBookHistory> historyPage = accountBookHistoryRepository.findAll(spec, pageable);
        return historyPage.getContent();
    }

    // 장부 내역을 dtoList로 변환
    public List<GetAccountBookResDto> convertToDtoList(List<AccountBookHistory> historyList) {
        return historyList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 장부 내역을 dto로 변환
    private GetAccountBookResDto convertToDto(AccountBookHistory history) {
        List<Image> imgList = imageRepository.findAllByTargetId(history.getId());

        String imgUrl = imgList.stream()
                .findFirst()
                .map(Image::getStoredPath)
                .orElse(null);

        return GetAccountBookResDto.builder()
                .id(history.getId().toString())                         // 장부 내역의 id
                .title(history.getContent())
                .category(history.getAccountBookCategory().getValue())
                .year(history.getDate().getYear())
                .month(history.getDate().getMonthValue())
                .day(history.getDate().getDayOfMonth())
                .dayName(history.getKoreanDayOfWeek())
                .expense(history.getTransactionType() == TransactionType.EXPENSE ? (long) history.getAmount() : null)
                .revenue(history.getTransactionType() == TransactionType.REVENUE ? (long) history.getAmount() : null)
                .thumbnail(imgUrl)
                .imageSize(imgList.size())
                .build();
    }
}
