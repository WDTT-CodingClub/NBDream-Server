package nbdream.accountBook.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import nbdream.accountBook.service.AccountBookHistoryService;
import nbdream.accountBook.service.AccountBookService;
import nbdream.accountBook.service.dto.*;
import nbdream.auth.config.AuthenticatedMemberId;
import nbdream.common.advice.response.ApiResponse;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/account")
@Tag(name = "AccountBook Controller")
public class AccountBookController {

    private final AccountBookService accountBookService;
    private final AccountBookHistoryService accountBookHistoryService;

    @Operation(summary = "내 장부 조회", description = "요청 날짜 형식 : yyyy-MM-dd")
    @GetMapping
    public ApiResponse<GetAccountBookListResDto> getMyAccountBookList(@RequestParam(required = false) Long lastContentsId,
                                                                      @RequestParam(required = false) String category,
                                                                      @RequestParam(required = false) String sort,
                                                                      @RequestParam(required = false) String start,
                                                                      @RequestParam(required = false) String end,
                                                                      @RequestParam(required = false) String transactionType,
                                                                      @Parameter(hidden = true) @AuthenticatedMemberId Long memberId){
        GetAccountBookListReqDto request = new GetAccountBookListReqDto(lastContentsId, category, sort, start, end, transactionType);
        GetAccountBookListResDto response = accountBookService.getMyAccountBookList(request, memberId);
        return ApiResponse.ok(response);
    }

    @Operation(summary = "장부 작성", description = "요청 날짜 형식 : yyyy-MM-dd HH:mm")
    @PostMapping("/register")
    public ApiResponse<Void> writeAccountBookHistory(@RequestBody PostAccountBookReqDto request,
                                                     @Parameter(hidden = true) @AuthenticatedMemberId Long memberId) {
        return accountBookHistoryService.writeAccountBookHistory(request, memberId);
    }

    @Operation(summary = "장부 내역 수정", description = "요청 날짜 형식 : yyyy-MM-dd HH:mm")
    @PutMapping("/update/{account-book-history-id}")
    public ApiResponse<Void> updatePost(@RequestBody PutAccountBookReqDto request,
                                        @Parameter(hidden = true) @AuthenticatedMemberId Long memberId,
                                        @PathVariable("account-book-history-id") final Long accountBookHistoryId) {
        return accountBookHistoryService.updateAccountBookHistory(request, memberId, accountBookHistoryId);
    }

    @Operation(summary = "장부 상세 조회", description = "")
    @GetMapping("/detail/{account-book-history-id}")
    public ApiResponse<GetAccountBookDetailResDto> getMyAccountBookList(@Parameter(hidden = true) @AuthenticatedMemberId Long memberId,
                                                                        @PathVariable("account-book-history-id") final Long accountBookHistoryId){
        GetAccountBookDetailResDto response = accountBookHistoryService.getAccountBookDetail(memberId, accountBookHistoryId);
        return ApiResponse.ok(response);
    }

    @Operation(summary = "장부 삭제", description = "")
    @DeleteMapping("/delete/{account-book-history-id}")
    public ApiResponse<Void> deleteAccountBookHistory(@Parameter(hidden = true) @AuthenticatedMemberId Long memberId,
                                                      @PathVariable("account-book-history-id") final Long accountBookHistoryId) {
        return accountBookHistoryService.deleteAccountBookHistory(memberId, accountBookHistoryId);
    }
}
