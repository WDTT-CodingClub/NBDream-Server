package nbdream.auth.service;

import lombok.RequiredArgsConstructor;
import nbdream.auth.dto.request.TokenRequest;
import nbdream.auth.dto.response.LoginResponse;
import nbdream.auth.dto.response.OAuthUserProfile;
import nbdream.auth.dto.response.TokenResponse;
import nbdream.auth.infrastructure.JwtTokenProvider;
import nbdream.auth.infrastructure.OAuthAttributes;
import nbdream.auth.infrastructure.OAuthProvider;
import nbdream.member.domain.Member;
import nbdream.member.repository.MemberRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider tokenProvider;
    private final OAuthProvider oAuthProvider;
    private final WebClient webClient;

    @Transactional
    public LoginResponse oAuthLogin(String providerName, String oAuthAccessToken) {
        String userInfoUri = oAuthProvider.getUserInfoUri(providerName);

        OAuthUserProfile oAuthUserProfile = getUserProfile(providerName, oAuthAccessToken, userInfoUri);

        LoginResponse loginResponse = saveOrLogin(oAuthUserProfile);

        loginResponse.setTokenResponse(new TokenResponse(tokenProvider.createAccessToken(loginResponse.getMemberId()),
                tokenProvider.createRefreshToken(loginResponse.getMemberId())));

        return loginResponse;
    }

    private LoginResponse saveOrLogin(OAuthUserProfile oAuthUserProfile) {
        Optional<Member> member = memberRepository.findBySocialId(oAuthUserProfile.getSocialId());
        if (member.isEmpty()) {
            return LoginResponse.builder()
                    .alreadyExistMember(false)
                    .memberId(memberRepository.save(oAuthUserProfile.toMember()).getId())
                    .build();
        }
        return LoginResponse.builder()
                .alreadyExistMember(true)
                .memberId(member.get().getId())
                .build();
    }

    private OAuthUserProfile getUserProfile(String providerName, String oAuthAccessToken, String userInfoUri) {
        Map<String, Object> OAuthUserAttributes = getUserAttributes(userInfoUri, oAuthAccessToken);
        return OAuthAttributes.extract(providerName, OAuthUserAttributes);
    }

    private Map<String, Object> getUserAttributes(String userInfoUri, String oAuthAccessToken) {
        return webClient.get()
                .uri(userInfoUri)
                .headers(header -> header.setBearerAuth(oAuthAccessToken))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }

}
