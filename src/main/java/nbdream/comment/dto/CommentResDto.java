package nbdream.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbdream.comment.domain.Comment;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResDto {
    private Long memberId;
    private String nickname;
    private String profileImageUrl;
    private String content;
    private LocalDateTime lastModifiedTime;

    public CommentResDto(final Comment comment) {
        this.memberId = comment.getAuthor().getId();
        this.nickname = comment.getAuthor().getNickname();
        this.profileImageUrl = comment.getAuthor().getProfileImageUrl();
        this.content = comment.getContent();
        this.lastModifiedTime = comment.getLastModifiedDate();
    }
}
