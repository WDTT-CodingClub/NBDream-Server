package nbdream.member.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import nbdream.auth.application.AuthService;
import nbdream.auth.dto.response.TokenResponse;
import nbdream.auth.infrastructure.JwtTokenProvider;
import nbdream.common.advice.response.ApiResponse;
import nbdream.member.domain.Authority;
import nbdream.member.domain.LoginType;
import nbdream.member.domain.Member;
import nbdream.member.repository.MemberRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
@Tag(name = "Member Controller")
public class MemberController {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "임시 토큰 생성", description = "테스트용 토큰 생성 (memberId는 1)")
    @PostMapping("/token/{memberId}")
    public ApiResponse<String> getToken(@PathVariable("memberId") Long memberId) {
        return ApiResponse.ok(jwtTokenProvider.createAccessToken(memberId));
    }
}
