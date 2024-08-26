package nbdream.bulletin.controller;

import lombok.RequiredArgsConstructor;
import nbdream.bulletin.dto.request.BulletinTestDto;
import nbdream.bulletin.dto.request.DeleteTestDto;
import nbdream.bulletin.service.TestBulletinService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.awt.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestBulletinController {

    private final TestBulletinService service;

    @PostMapping("/test")
    public void test(@RequestPart(name = "contents",required = false)String content, @RequestPart(name = "dto", required = false)List<MultipartFile> files) {
        service.uploadTestBulletin(files);
    }

    @DeleteMapping("/test")
    public ResponseEntity<String> deleteTest(@RequestBody DeleteTestDto testDto) {
        return ResponseEntity.ok(testDto.getTest());
    }
}
