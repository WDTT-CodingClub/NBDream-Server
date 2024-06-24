package nbdream.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import nbdream.auth.dto.request.TokenRequest;
import nbdream.auth.dto.response.LoginResponse;
import nbdream.auth.infrastructure.JwtTokenProvider;
import nbdream.auth.service.AuthService;
import nbdream.auth.dto.response.TokenResponse;
import nbdream.common.advice.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Auth Controller")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "OAuth 로그인", description = "provider는 kakao, naver, google 만 입력, token은 각 로그인 API에서 받은 token 값 입력")
    @PostMapping("/login/oauth/{provider}")
    public ApiResponse<TokenResponse> oAuthLogin(@PathVariable(name = "provider") String provider, @RequestParam("token") String token) {
        final LoginResponse loginResponse = authService.oAuthLogin(provider, token);

        if (!loginResponse.isAlreadyExistMember()) {
            return ApiResponse.ok(HttpStatus.CREATED, loginResponse.getTokenResponse());
        }

        return ApiResponse.ok(loginResponse.getTokenResponse());
    }

    @Operation(summary = "토큰 재발급")
    @PostMapping("/refresh-tokens")
    public ApiResponse<TokenResponse> refreshTokens(@RequestBody TokenRequest request) {
        return ApiResponse.ok(jwtTokenProvider.refreshTokens(request));
    }
}
