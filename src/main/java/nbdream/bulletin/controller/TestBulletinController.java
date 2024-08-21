package nbdream.bulletin.controller;

import lombok.RequiredArgsConstructor;
import nbdream.bulletin.dto.request.BulletinTestDto;
import nbdream.bulletin.service.TestBulletinService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.awt.*;

@RestController
@RequiredArgsConstructor
public class TestBulletinController {

    private final TestBulletinService service;

    @PostMapping("/test")
    public void test(@RequestPart(required = false) BulletinTestDto dto) {
        service.uploadTestBulletin(dto);
    }
}
