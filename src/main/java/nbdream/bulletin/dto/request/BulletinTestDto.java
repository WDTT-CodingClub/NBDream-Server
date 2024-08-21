package nbdream.bulletin.dto.request;

import lombok.Data;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class BulletinTestDto {
    private String content;
    private List<MultipartFile> files;
}
