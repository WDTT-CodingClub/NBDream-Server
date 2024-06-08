package nbdream.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbdream.common.entity.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String socialId;

    @Enumerated(value = EnumType.STRING)
    private LoginType loginType;

    private String email;

    private String nickname;

    private String profileImageUrl;

    @Enumerated(value = EnumType.STRING)
    private Authority authority;

    private boolean privacyConsent;

    public Member(final String socialId, final LoginType loginType, final String email, final String nickname, final String profileImageUrl, final Authority authority, final boolean privacyConsent) {
        this.socialId = socialId;
        this.loginType = loginType;
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.authority = authority;
        this.privacyConsent = privacyConsent;
    }
}
