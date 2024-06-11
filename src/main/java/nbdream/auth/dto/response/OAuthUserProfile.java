package nbdream.auth.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nbdream.member.domain.Authority;
import nbdream.member.domain.LoginType;
import nbdream.member.domain.Member;

@Getter
@NoArgsConstructor
@ToString
public class OAuthUserProfile {
    private String socialId;
    private String nickname;
    private String profileImageUrl;
    private LoginType loginType;

    @Builder
    public OAuthUserProfile(final String socialId, final String nickname, final String profileImageUrl, final LoginType loginType) {
        this.socialId = socialId;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.loginType = loginType;
    }

    public Member toMember() {
        return Member.builder()
                .socialId(socialId)
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .loginType(loginType)
                .authority(Authority.USER)
                .privacyConsent(true)
                .build();
    }
}
