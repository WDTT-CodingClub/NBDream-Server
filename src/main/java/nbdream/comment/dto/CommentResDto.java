package nbdream.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbdream.comment.domain.Comment;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResDto {
    private Long memberId;
    private Long commentId;
    private Long bulletinId;
    private String bulletinAuthor;
    private String nickname;
    private String profileImageUrl;
    private String content;
    private Boolean isAuthor;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;


    public CommentResDto(final Comment comment, long authorId) {
        this.memberId = comment.getAuthor().getId();
        this.commentId = comment.getId();
        this.bulletinId = comment.getBulletin().getId();
        this.bulletinAuthor = comment.getBulletin().getAuthor().getNickname();
        this.nickname = comment.getAuthor().getNickname();
        this.profileImageUrl = comment.getAuthor().getProfileImageUrl();
        this.content = comment.getContent();
        this.isAuthor = comment.getAuthor().getId() == authorId;
        this.createdDate = comment.getCreatedDate();
        this.lastModifiedDate = comment.getLastModifiedDate();
    }


}
