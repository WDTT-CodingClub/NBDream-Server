package nbdream.image.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import nbdream.common.exception.NotFoundException;
import nbdream.image.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/{domain}/images")
    public ResponseEntity<String> uploadImage(@PathVariable("domain") String domain, MultipartFile image) {
        if (domain == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid domain");
        }

        try {
            return ResponseEntity.ok(imageService.saveImage(image, domain));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Image upload failed");
        }
    }

    @GetMapping("/{domain}/{id}")
    public ResponseEntity<byte[]> getImages(@PathVariable String domain, @PathVariable Long targetId) {
        try {
            byte[] imageData = imageService.loadImage(domain, targetId);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageData);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{domain}/{filename}")
    public ResponseEntity<String> deleteImage(@PathVariable String domain, @PathVariable(name = "filename") String fileName) {
        try {
            imageService.deleteImage(domain, fileName);
        } catch (NotFoundException | IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not existed");
        }
        return ResponseEntity.ok("deleted");
    }
}
