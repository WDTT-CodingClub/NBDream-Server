package nbdream.auth.infrastructure;

import nbdream.auth.dto.response.OAuthUserProfile;
import nbdream.member.domain.LoginType;

import java.util.Arrays;
import java.util.Map;

public enum OAuthAttributes {

    KAKAO("kakao") {
        @Override
        public OAuthUserProfile of(Map<String, Object> attributes) {
            Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) account.get("profile");
            return OAuthUserProfile.builder()
                    .socialId(attributes.get("id").toString())
                    .nickname((String) profile.get("nickname"))
                    .profileImageUrl((String) profile.get("profile_image_url"))
                    .loginType(LoginType.KAKAO)
                    .build();
        }
    },
    NAVER("naver") {
        @Override
        public OAuthUserProfile of(Map<String, Object> attributes) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            return OAuthUserProfile.builder()
                    .socialId((String) response.get("id"))
                    .nickname((String) response.get("nickname"))
                    .profileImageUrl((String) response.get("profile_image"))
                    .loginType(LoginType.NAVER)
                    .build();
        }
    },
    GOOGLE("google") {
        @Override
        public OAuthUserProfile of(Map<String, Object> attributes) {
            return OAuthUserProfile.builder()
                    .socialId((String) attributes.get("sub"))
                    .nickname((String) attributes.get("name"))
                    .profileImageUrl((String) attributes.get("picture"))
                    .loginType(LoginType.GOOGLE)
                    .build();
        }
    };


    private final String providerName;

    OAuthAttributes(String name) {
        this.providerName = name;
    }

    public static OAuthUserProfile extract(String providerName, Map<String, Object> attributes) {
        return Arrays.stream(values())
                .filter(provider -> providerName.equals(provider.providerName))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .of(attributes);
    }

    public abstract OAuthUserProfile of(Map<String, Object> attributes);
}
