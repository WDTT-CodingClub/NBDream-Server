package nbdream.auth.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import nbdream.auth.application.AuthService;
import nbdream.auth.dto.response.TokenResponse;
import nbdream.common.advice.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Auth Controller")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "OAuth 로그인", description = "provider는 kakao, naver, google 만 입력, token은 각 로그인 API에서 받은 token 값 입력")
    @PostMapping("/login/oauth/{provider}")
    public ApiResponse<TokenResponse> oAuthLogin(@PathVariable(name = "provider") String provider, @RequestParam("token") String token) {
        return ApiResponse.ok(authService.oAuthLogin(provider, token));
    }
}
