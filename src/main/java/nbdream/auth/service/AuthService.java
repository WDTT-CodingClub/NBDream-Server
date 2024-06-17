package nbdream.auth.application;

import lombok.RequiredArgsConstructor;
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
    public TokenResponse oAuthLogin(String providerName, String oAuthAccessToken) {
        String userInfoUri = oAuthProvider.getUserInfoUri(providerName);

        OAuthUserProfile oAuthUserProfile = getUserProfile(providerName, oAuthAccessToken, userInfoUri);

        Member member = saveOrLogin(oAuthUserProfile);

        return new TokenResponse(tokenProvider.createAccessToken(member.getId()), tokenProvider.createRefreshToken(member.getId()));
    }

    private Member saveOrLogin(OAuthUserProfile oAuthUserProfile) {
        Optional<Member> member = memberRepository.findBySocialId(oAuthUserProfile.getSocialId());
        if (member.isEmpty()) {
            return memberRepository.save(oAuthUserProfile.toMember());
        }
        return member.get();
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
