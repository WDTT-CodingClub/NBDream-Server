package nbdream.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbdream.bulletin.exception.UnEditableBulletinException;
import nbdream.common.entity.BaseEntity;
import nbdream.common.entity.Status;
import org.hibernate.annotations.SQLRestriction;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLRestriction("status != 'DELETED'")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String socialId;

    @Enumerated(value = EnumType.STRING)
    private LoginType loginType;

    private String nickname;

    private String profileImageUrl;

    @Enumerated(value = EnumType.STRING)
    private Authority authority;

    private boolean privacyConsent;

    @Builder
    public Member(final String socialId, final LoginType loginType, final String nickname, final String profileImageUrl, final Authority authority, final boolean privacyConsent) {
        this.socialId = socialId;
        this.loginType = loginType;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.authority = authority;
        this.privacyConsent = privacyConsent;
    }

    public void update(final String nickname, final String profileImageUrl) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }

    public void delete() {
        this.changeStatus(Status.DELETED);
    }

    public void expire() {
        this.changeStatus(Status.EXPIRED);
    }

}
