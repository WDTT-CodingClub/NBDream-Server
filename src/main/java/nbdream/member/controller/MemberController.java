package nbdream.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import nbdream.auth.config.AuthenticatedMemberId;
import nbdream.auth.dto.response.TokenResponse;
import nbdream.auth.infrastructure.JwtTokenProvider;
import nbdream.common.advice.response.ApiResponse;
import nbdream.member.dto.request.UpdateProfileReqDto;
import nbdream.member.dto.response.MyPageResDto;
import nbdream.member.service.MemberService;
import nbdream.member.service.MyProfileService;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
@Tag(name = "Member Controller")
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MyProfileService myProfileService;

    @Operation(summary = "마이 페이지 조회")
    @GetMapping("/my-page")
    public ApiResponse<MyPageResDto> getMyPage(@Parameter(hidden = true) @AuthenticatedMemberId Long memberId) {
        return ApiResponse.ok(myProfileService.getMyPage(memberId));
    }

    @Operation(summary = "프로필 수정")
    @PutMapping("/profile")
    public ApiResponse<Void> updateProfile(@Parameter(hidden = true) @AuthenticatedMemberId Long memberId,
                                           @RequestBody UpdateProfileReqDto request) {
        myProfileService.updateProfile(memberId, request);
        return ApiResponse.ok();
    }

    @Operation(summary = "임시 토큰 생성", description = "테스트용 토큰 생성 (memberId는 1)")
    @PostMapping("/token/{memberId}")
    public ApiResponse<TokenResponse> getToken(@PathVariable("memberId") Long memberId) {
        return ApiResponse.ok(new TokenResponse(jwtTokenProvider.createAccessToken(memberId), jwtTokenProvider.createRefreshToken(memberId)));
    }

    @Operation(summary = "임시 회원 가입", description = "테스트용 아이디 생성")
    @PostMapping("/signup")
    public ApiResponse<TokenResponse> signup(@RequestParam(name = "nickname") String nickname) {
        return ApiResponse.ok(memberService.signup(nickname));
    }

}
