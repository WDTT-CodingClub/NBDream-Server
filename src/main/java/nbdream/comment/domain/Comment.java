package nbdream.comment.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbdream.bulletin.domain.Bulletin;
import nbdream.common.entity.BaseEntity;
import nbdream.member.domain.Member;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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
}
