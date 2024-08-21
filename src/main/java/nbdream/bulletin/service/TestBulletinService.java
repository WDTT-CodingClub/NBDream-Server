package nbdream.bulletin.service;

import lombok.RequiredArgsConstructor;
import nbdream.bulletin.dto.request.BulletinTestDto;
import nbdream.bulletin.repository.BulletinRepository;
import nbdream.image.service.ImageService;
import nbdream.image.service.TestImageService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestBulletinService {

    private final BulletinService service;
    private final TestImageService testImageService;
    private final BulletinRepository repository;
    private final ImageService imageService;

    public void uploadTestBulletin(BulletinTestDto dto) {
        repository.saveContent(dto.getContent());
        for (int i = 0; i < dto.getFiles().size(); i++) {
            testImageService.uploadTestImage(dto.getFiles().get(i));

        }
    }


}
