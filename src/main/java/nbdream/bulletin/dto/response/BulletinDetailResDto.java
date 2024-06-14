package nbdream.bulletin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbdream.bulletin.domain.Bulletin;
import nbdream.member.domain.Member;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BulletinDetailResDto {
    private Long authorId;
    private String nickname;
    private String profileImageUrl;
    private String content;
    private String crop;
    private List<String> imageUrls;
    private String bulletinCategory;
    private LocalDate createdTime;
    private int bookmarkedCount;

    public BulletinDetailResDto(final Bulletin bulletin, final Member author, final List<String> imageUrls) {
        this.authorId = author.getId();
        this.nickname = author.getNickname();
        this.profileImageUrl = author.getProfileImageUrl();
        this.content = bulletin.getContent();
        this.crop = bulletin.getContent();
        this.imageUrls = imageUrls;
        this.bulletinCategory = bulletin.getBulletinCategory().getValue();
        this.createdTime = bulletin.getCreatedDate().toLocalDate();
        this.bookmarkedCount = bulletin.getBookmarkedCount();
    }
}

