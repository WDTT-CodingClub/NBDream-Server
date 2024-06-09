package nbdream.auth.infrastructure;


import nbdream.auth.exception.InvalidProviderException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OAuthProvider {

    private final String kakaoUserInfoUrl;
    private final String naverUserInfoUrl;
    private final String googleUserInfoUrl;

    public OAuthProvider(@Value("${oauth.provider.kakao.user-info-uri}") String kakaoUserInfoUrl, @Value("${oauth.provider.naver.user-info-uri}") String naverUserInfoUrl,
                         @Value("${oauth.provider.google.user-info-uri}")String googleUserInfoUrl) {
        this.kakaoUserInfoUrl = kakaoUserInfoUrl;
        this.naverUserInfoUrl = naverUserInfoUrl;
        this.googleUserInfoUrl = googleUserInfoUrl;
    }

    public String getUserInfoUri(String providerName) {
        if (providerName.equals("kakao")) {
            return kakaoUserInfoUrl;
        }
        if (providerName.equals("naver")) {
            return naverUserInfoUrl;
        }
        if (providerName.equals("google")) {
            return googleUserInfoUrl;
        }
        throw new InvalidProviderException();
    }
}
