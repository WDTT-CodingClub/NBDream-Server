package nbdream.accountBook.controller;

import lombok.RequiredArgsConstructor;
import nbdream.accountBook.service.AccountBookService;
import nbdream.accountBook.service.dto.GetAccountBookListReqDto;
import nbdream.accountBook.service.dto.GetAccountBookListResDto;
import nbdream.auth.config.AuthenticatedMemberId;
import nbdream.common.advice.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class AccountBookController {

    private final AccountBookService accountBookService;

    //내 장부 조회
    @GetMapping("/auth/account")
    public ApiResponse<GetAccountBookListResDto> getMyAccountBookList( @ModelAttribute GetAccountBookListReqDto reqDto,
                                                                       @AuthenticatedMemberId Long memberId){
        GetAccountBookListResDto resDto = accountBookService.getMyAccountBookList(reqDto, memberId);
        return ApiResponse.ok(resDto);
    }
}