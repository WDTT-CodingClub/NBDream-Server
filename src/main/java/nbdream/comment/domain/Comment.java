package nbdream.comment.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbdream.bulletin.domain.Bulletin;
import nbdream.bulletin.exception.UnEditableBulletinException;
import nbdream.comment.exception.UneditableCommentException;
import nbdream.common.entity.BaseEntity;
import nbdream.common.entity.Status;
import nbdream.member.domain.Member;
import org.hibernate.annotations.SQLRestriction;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
@SQLRestriction("status != 'DELETED'")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Member author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bulletin_id")
    private Bulletin bulletin;

    private String content;

    public void update(Long memberId, String content) {
        if (!isAuthor(memberId)) {
            throw new UneditableCommentException();
        }

        this.content = content;
    }

    public void delete(Long memberId) {
        if (!isAuthor(memberId) && !isBulletinAuthor(memberId)) {
            throw new UneditableCommentException();
        }

        this.changeStatus(Status.DELETED);
    }

    public void expire(final Long memberId) {
        if (!isAuthor(memberId)) {
            throw new UnEditableBulletinException();
        }

        this.changeStatus(Status.EXPIRED);
    }

    public boolean isAuthor(final Long memberId) {
        return memberId.equals(author.getId());
    }

    public boolean isBulletinAuthor(final Long memberId) {
        return memberId.equals(bulletin.getAuthor().getId());
    }

}
