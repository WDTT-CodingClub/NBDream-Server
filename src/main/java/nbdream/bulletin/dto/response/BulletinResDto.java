package nbdream.bulletin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbdream.bulletin.domain.Bulletin;
import nbdream.comment.domain.Comment;
import nbdream.comment.dto.CommentResDto;
import nbdream.member.domain.Member;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BulletinResDto {
    private Long authorId;
    private Long bulletinId;
    private String nickname;
    private String profileImageUrl;
    private String content;
    private String crop;
    private List<String> imageUrls;
    private String bulletinCategory;
    private LocalDate createdTime;
    private List<CommentResDto> comments;
    private int bookmarkedCount;

    public BulletinResDto(final Bulletin bulletin, final Member author, final List<String> imageUrls, final List<Comment> comments) {
        this.authorId = author.getId();
        this.bulletinId = bulletin.getId();
        this.nickname = author.getNickname();
        this.profileImageUrl = author.getProfileImageUrl();
        this.content = bulletin.getContent();
        this.crop = bulletin.getCrop();
        this.imageUrls = imageUrls;
        this.bulletinCategory = bulletin.getBulletinCategory().getValue();
        this.createdTime = bulletin.getCreatedDate().toLocalDate();
        this.comments = comments.stream().map(comment -> new CommentResDto(comment)).collect(Collectors.toList());
        this.bookmarkedCount = bulletin.getBookmarkedCount();
    }
}

