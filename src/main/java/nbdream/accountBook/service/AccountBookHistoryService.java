package nbdream.accountBook.service;

import lombok.RequiredArgsConstructor;
import nbdream.accountBook.domain.AccountBook;
import nbdream.accountBook.domain.AccountBookCategory;
import nbdream.accountBook.domain.AccountBookHistory;
import nbdream.accountBook.domain.TransactionType;
import nbdream.accountBook.exception.AccountBookHistoryNotFoundException;
import nbdream.accountBook.exception.AccountBookNotFoundException;
import nbdream.accountBook.exception.ImageNotFoundException;
import nbdream.accountBook.repository.AccountBookHistoryRepository;
import nbdream.accountBook.repository.AccountBookRepository;
import nbdream.accountBook.service.dto.PostAccountBookReqDto;
import nbdream.accountBook.service.dto.PutAccountBookReqDto;
import nbdream.common.advice.response.ApiResponse;
import nbdream.image.domain.Image;
import nbdream.image.repository.ImageRepository;
import nbdream.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        //이미지를 등록했다면 targetId 업데이트
        if(request.getImageUrls() != null){
            List<String> imageUrlList = request.getImageUrls();
            updateImageTargetId(imageUrlList, accountBookHistory);
        }

        return ApiResponse.ok();
    }

    //장부 내역 수정
    @Transactional
    public ApiResponse<Void> updateAccountBookHistory(PutAccountBookReqDto request, Long accountBookHistoryId) {
        AccountBookHistory accountBookHistory = accountBookHistoryRepository.findById(accountBookHistoryId)
                .orElseThrow(AccountBookHistoryNotFoundException::new);

        //이미지를 등록했다면 targetId 업데이트
        if(request.getImageUrls() != null){
            List<String> imageUrlList = request.getImageUrls();
            updateImageTargetId(imageUrlList, accountBookHistory);
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
        return ApiResponse.ok();
    }

    @Transactional
    public void updateImageTargetId(List<String> imageUrlList, AccountBookHistory accountBookHistory){
        for(String url : imageUrlList){
            Image image = imageRepository.findByImageUrl(url).orElseThrow(ImageNotFoundException::new);
            image.update(accountBookHistory.getId());
            imageRepository.save(image);
        }
    }
}
