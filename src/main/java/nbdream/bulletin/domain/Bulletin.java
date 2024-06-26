package nbdream.bulletin.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbdream.bulletin.exception.UnEditableBulletinException;
import nbdream.comment.domain.Comment;
import nbdream.common.entity.BaseEntity;
import nbdream.common.entity.Status;
import nbdream.member.domain.Member;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLRestriction("status != 'DELETED'")
public class Bulletin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Member author;

    @BatchSize(size = 50)
    @OneToMany(mappedBy = "bulletin", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

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

    public void update(final Long memberId, final String crop, final BulletinCategory category, final String content) {
        if (!isAuthor(memberId)) {
            throw new UnEditableBulletinException();
        }

        this.crop = crop;
        this.bulletinCategory = category;
        this.content = content;
    }

    public void delete(final Long memberId) {
        if (!isAuthor(memberId)) {
            throw new UnEditableBulletinException();
        }

        this.changeStatus(Status.DELETED);
    }

    public void expire(final Long memberId) {
        if (!isAuthor(memberId)) {
            throw new UnEditableBulletinException();
        }

        this.changeStatus(Status.EXPIRED);
    }

    public void plusBookmarkedCount() {
        this.bookmarkedCount += 1;
    }

    public void minusBookmarkedCount() {
        this.bookmarkedCount -= 1;
    }

    public boolean isAuthor(final Long memberId) {
        return memberId.equals(author.getId());
    }
}
