package nbdream.comment.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbdream.bulletin.domain.Bulletin;
import nbdream.common.entity.BaseEntity;
import nbdream.common.entity.Status;
import nbdream.member.domain.Member;
import org.hibernate.annotations.SQLRestriction;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
@SQLRestriction("status = 'NORMAL'")
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

    public Comment(final Member author, final Bulletin bulletin, final String content) {
        this.author = author;
        this.bulletin = bulletin;
        this.content = content;
    }

    public Comment upateComment(String content){
        this.content = content;
        return this;
    }
    public void delete() {
        this.changeStatus(Status.EXPIRED);
    }
}
