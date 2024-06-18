package nbdream.bulletin.dto.request;

import lombok.*;

@NoArgsConstructor
@Builder
@Getter
public class SearchBulletinCondDto {
    private String keyword;
    private String bulletinCategory;
    private String crop;
    private Long lastBulletinId;

    public SearchBulletinCondDto(final String keyword, final String bulletinCategory, final String crop, final Long lastBulletinId) {
        this.keyword = keyword;
        this.bulletinCategory = bulletinCategory;
        this.crop = crop;
        this.lastBulletinId = (lastBulletinId == null) ? Long.MAX_VALUE : lastBulletinId;
    }
}
