package nbdream.accountBook.service;

import lombok.RequiredArgsConstructor;
import nbdream.accountBook.domain.AccountBook;
import nbdream.accountBook.domain.AccountBookCategory;
import nbdream.accountBook.domain.AccountBookHistory;
import nbdream.accountBook.domain.TransactionType;
import nbdream.accountBook.exception.*;
import nbdream.accountBook.repository.AccountBookHistoryRepository;
import nbdream.accountBook.repository.AccountBookRepository;
import nbdream.accountBook.service.dto.GetAccountBookDetailResDto;
import nbdream.accountBook.service.dto.PostAccountBookReqDto;
import nbdream.accountBook.service.dto.PutAccountBookReqDto;
import nbdream.common.advice.response.ApiResponse;
import nbdream.image.domain.Image;
import nbdream.image.repository.ImageRepository;
import nbdream.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountBookHistoryService {

    private final AccountBookRepository accountBookRepository;
    private final AccountBookHistoryRepository accountBookHistoryRepository;
    private final MemberRepository memberRepository;
    private final ImageRepository imageRepository;

    //장부 내역 작성
    @Transactional
    public ApiResponse<Void> writeAccountBookHistory(PostAccountBookReqDto request, Long memberId) {
        AccountBook accountBook = accountBookRepository.findByMemberId(memberId)
                .orElseThrow(AccountBookNotFoundException::new);

        AccountBookHistory accountBookHistory = AccountBookHistory.builder()
                .accountBook(accountBook)
                .accountBookCategory(AccountBookCategory.fromValue(request.getCategory()))
                .transactionType(TransactionType.fromValue(request.getTransactionType()))
                .amount(request.getAmount())
                .content(request.getTitle())
                .dateTime(request.getParsedRegisterDateTime())
                .build();
        accountBookHistoryRepository.save(accountBookHistory);

        request.getImageUrls()
                .stream()
                .forEach(url -> imageRepository.save(new Image(accountBookHistory.getId(), url)));
        return ApiResponse.ok();
    }

    //장부 내역 수정
    @Transactional
    public ApiResponse<Void> updateAccountBookHistory(PutAccountBookReqDto request, Long memberId, Long accountBookHistoryId) {
        AccountBookHistory accountBookHistory = accountBookHistoryRepository.findById(accountBookHistoryId)
                .orElseThrow(AccountBookHistoryNotFoundException::new);

        if(!accountBookHistory.isWriter(memberId, accountBookHistory)){
            throw new UnEditableAccountBookException();
        }
        accountBookHistory.update(
            accountBookHistory.getAccountBook(),
            AccountBookCategory.fromValue(request.getCategory()),
            TransactionType.fromValue(request.getTransactionType()),
            request.getAmount(),
            request.getTitle(),
            request.getParsedRegisterDateTime()
        );
        accountBookHistoryRepository.save(accountBookHistory);

        request.getImageUrls()
                .stream()
                .forEach(url -> imageRepository.save(new Image(accountBookHistory.getId(), url)));
        return ApiResponse.ok();
    }


    //장부 내역 삭제
    @Transactional
    public ApiResponse<Void> deleteAccountBookHistory(Long memberId, Long accountBookHistoryId) {
        AccountBookHistory accountBookHistory = accountBookHistoryRepository.findById(accountBookHistoryId)
                        .orElseThrow(AccountBookHistoryNotFoundException::new);

        if(!accountBookHistory.isWriter(memberId, accountBookHistory)){
            throw new UnEditableAccountBookException();
        }
        //이미지 삭제 (추후 이미지Service에서 처리할 것임)
        /*List<Image> imageList = imageRepository.findAllByTargetId(accountBookHistory.getId());
        imageRepository.findAllByTargetId(accountBookHistory.getId())
                .stream()
                .forEach(image -> imageRepository.delete(image));*/

        accountBookHistoryRepository.delete(accountBookHistory);
        return ApiResponse.ok();
    }

    //장부 상세 조회
    public GetAccountBookDetailResDto getAccountBookDetail(Long memberId, Long accountBookHistoryId) {
        AccountBookHistory accountBookHistory = accountBookHistoryRepository.findById(accountBookHistoryId)
                .orElseThrow(AccountBookHistoryNotFoundException::new);
        if(!accountBookHistory.isWriter(memberId, accountBookHistory)){
            throw new UnAccessableAccountBookException();
        }

        List<String> imageUrls = imageRepository.findAllByTargetId(accountBookHistoryId)
                .stream()
                .map(Image::getImageUrl)
                .collect(Collectors.toList());

        return new GetAccountBookDetailResDto(
                accountBookHistory.getId().toString(),
                accountBookHistory.getContent(),
                accountBookHistory.getAccountBookCategory().getValue(),
                accountBookHistory.getTransactionType().getValue(),
                accountBookHistory.getAmount(),
                accountBookHistory.getDateTimeToString(),
                imageUrls);
    }
}
