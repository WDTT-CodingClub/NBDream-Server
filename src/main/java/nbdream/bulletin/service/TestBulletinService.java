package nbdream.bulletin.service;

import lombok.RequiredArgsConstructor;
import nbdream.bulletin.dto.request.BulletinTestDto;
import nbdream.bulletin.repository.BulletinRepository;
import nbdream.image.service.ImageService;
import nbdream.image.service.TestImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestBulletinService {

    private final BulletinService service;
    private final TestImageService testImageService;
    private final BulletinRepository repository;
    private final ImageService imageService;

    public void uploadTestBulletin(List<MultipartFile> files) {
        for (int i = 0; i < files.size(); i++) {
            testImageService.uploadTestImage(files.get(i));

        }
    }


}
