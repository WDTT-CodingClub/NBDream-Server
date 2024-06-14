package nbdream.bulletin.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbdream.bulletin.exception.UnEditableBulletinException;
import nbdream.common.entity.BaseEntity;
import nbdream.member.domain.Member;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Bulletin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Member author;

    private String crop;

    @Enumerated(EnumType.STRING)
    private BulletinCategory bulletinCategory;

    private String content;

    private int bookmarkedCount;

    public Bulletin(final Member author, final String crop, final BulletinCategory bulletinCategory, final String content) {
        this.author = author;
        this.crop = crop;
        this.bulletinCategory = bulletinCategory;
        this.content = content;
        this.bookmarkedCount = 0;
    }

    public void update(final Member member, final String crop, final BulletinCategory category, final String content) {

    }

    public void delete(final Member member) {
        if (!isAuthor(member)) {
            throw new UnEditableBulletinException();
        }

        this.expired();
    }

    public boolean isAuthor(final Member member) {
        return member.getId().equals(author.getId());
    }
}
