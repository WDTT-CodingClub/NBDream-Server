package nbdream.accountBook.controller;

import lombok.RequiredArgsConstructor;
import nbdream.accountBook.service.AccountBookHistoryService;
import nbdream.accountBook.service.AccountBookService;
import nbdream.accountBook.service.dto.GetAccountBookListReqDto;
import nbdream.accountBook.service.dto.GetAccountBookListResDto;
import nbdream.accountBook.service.dto.PostAccountBookReqDto;
import nbdream.accountBook.service.dto.PutAccountBookReqDto;
import nbdream.auth.config.AuthenticatedMemberId;
import nbdream.common.advice.response.ApiResponse;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/account")
public class AccountBookController {

    private final AccountBookService accountBookService;
    private final AccountBookHistoryService accountBookHistoryService;

    //내 장부 조회
    @GetMapping
    public ApiResponse<GetAccountBookListResDto> getMyAccountBookList( @RequestBody GetAccountBookListReqDto request,
                                                                       @AuthenticatedMemberId Long memberId){
        GetAccountBookListResDto response = accountBookService.getMyAccountBookList(request, memberId);
        return ApiResponse.ok(response);
    }

    //장부 작성
    @PostMapping("/register")
    public ApiResponse<Void> writeAccountBookHistory(@RequestBody PostAccountBookReqDto request,
                                                     @AuthenticatedMemberId Long memberId) {
        return accountBookHistoryService.writeAccountBookHistory(request, memberId);
    }

    //장부 내역 수정
    @PutMapping("/update/{accountbook-history-id}")
    public ApiResponse<Void> updatePost(@RequestBody PutAccountBookReqDto request,
                                        @AuthenticatedMemberId Long memberId,
                                        @PathVariable("accountbook-history-id") final Long accountBookHistoryId) {
        return accountBookHistoryService.updateAccountBookHistory(request, memberId, accountBookHistoryId);
    }

    //장부 상세 조회

    //장부 삭제
    @DeleteMapping("/delete/{accountbook-history-id}")
    public ApiResponse<Void> deleteAccountBookHistory(@AuthenticatedMemberId Long memberId,
                                                      @PathVariable("accountbook-history-id") final Long accountBookHistoryId) {
        return accountBookHistoryService.deleteAccountBookHistory(memberId, accountBookHistoryId);
    }
}
