package nbdream.bulletin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class BulletinReqDto {
    private String content;
    private String dreamCrop;
    private String bulletinCategory;
    private List<String> imageUrls;

    public BulletinReqDto(final String content, final String dreamCrop, final String bulletinCategory, final List<String> imageUrls) {
        this.content = content;
        this.dreamCrop = dreamCrop;
        this.bulletinCategory = bulletinCategory;
        this.imageUrls = imageUrls;
    }
}
