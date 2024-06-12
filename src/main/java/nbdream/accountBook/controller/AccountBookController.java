package nbdream.accountBook.controller;

import lombok.RequiredArgsConstructor;
import nbdream.accountBook.service.AccountBookHistoryService;
import nbdream.accountBook.service.AccountBookService;
import nbdream.accountBook.service.dto.GetAccountBookListReqDto;
import nbdream.accountBook.service.dto.GetAccountBookListResDto;
import nbdream.accountBook.service.dto.PostAccountBookReqDto;
import nbdream.auth.config.AuthenticatedMemberId;
import nbdream.common.advice.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class AccountBookController {

    private final AccountBookService accountBookService;
    private final AccountBookHistoryService accountBookHistoryService;

    //내 장부 조회
    @GetMapping("/auth/account")
    public ApiResponse<GetAccountBookListResDto> getMyAccountBookList( @RequestBody GetAccountBookListReqDto reqDto,
                                                                       @AuthenticatedMemberId Long memberId){
        GetAccountBookListResDto resDto = accountBookService.getMyAccountBookList(reqDto, memberId);
        return ApiResponse.ok(resDto);
    }

    //장부 작성
    @PostMapping("/auth/account/register")
    public ApiResponse<Void> writeAccountBookHistory(@RequestBody PostAccountBookReqDto reqDto,
                                                     @AuthenticatedMemberId Long memberId) {
        return accountBookHistoryService.writeAccountBookHistory(reqDto, memberId);
    }
}
