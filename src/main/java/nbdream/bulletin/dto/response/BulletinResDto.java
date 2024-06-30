package nbdream.bulletin.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbdream.bulletin.domain.Bulletin;
import nbdream.comment.dto.CommentResDto;
import nbdream.common.entity.Status;
import nbdream.member.domain.Member;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@NoArgsConstructor
public class BulletinResDto {
    private static final String UNKNOWN = "알 수 없음";
    private static final String EMPTY = "";

    private Long authorId;
    private Long bulletinId;
    private String nickname;
    private String profileImageUrl;
    private String content;
    private String crop;
    private List<String> imageUrls;
    private String bulletinCategory;
    private String createdTime;
    private List<CommentResDto> comments;
    private int bookmarkedCount;
    private boolean isAuthor;
    private boolean isBookmarked;

    @Builder
    public BulletinResDto(final Bulletin bulletin, final Member author, final List<String> imageUrls,
                          final List<CommentResDto> comments, final boolean isAuthor, final boolean isBookmarked) {
        this.authorId = author.getId();
        this.bulletinId = bulletin.getId();
        this.nickname = (bulletin.getStatus().equals(Status.EXPIRED)) ? UNKNOWN : author.getNickname();
        this.profileImageUrl = (bulletin.getStatus().equals(Status.EXPIRED)) ? EMPTY : author.getProfileImageUrl();
        this.content = bulletin.getContent();
        this.crop = bulletin.getCrop();
        this.imageUrls = imageUrls;
        this.bulletinCategory = bulletin.getBulletinCategory().getValue();
        this.createdTime = bulletin.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        this.comments = comments;
        this.bookmarkedCount = bulletin.getBookmarkedCount();
        this.isAuthor = isAuthor;
        this.isBookmarked = isBookmarked;
    }

}

