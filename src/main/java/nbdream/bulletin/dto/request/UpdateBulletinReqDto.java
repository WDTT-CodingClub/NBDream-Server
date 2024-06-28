package nbdream.bulletin.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class UpdateBulletinReqDto {
    private String content;
    private String dreamCrop;
    private String bulletinCategory;
    private List<String> addImageUrls;
    private List<String> deleteImageUrls;

    public UpdateBulletinReqDto(final String content, final String dreamCrop, final String bulletinCategory, final List<String> addImageUrls, final List<String> deleteImageUrls) {
        this.content = content;
        this.dreamCrop = dreamCrop;
        this.bulletinCategory = bulletinCategory;
        this.addImageUrls = addImageUrls;
        this.deleteImageUrls = deleteImageUrls;
    }
}