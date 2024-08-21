package nbdream.bulletin.controller;

import lombok.RequiredArgsConstructor;
import nbdream.bulletin.dto.request.BulletinTestDto;
import nbdream.bulletin.service.TestBulletinService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestBulletinController {

    private final TestBulletinService service;

    @PostMapping("/test")
    public void test(@RequestPart BulletinTestDto dto) {
        service.uploadTestBulletin(dto);
    }
}
