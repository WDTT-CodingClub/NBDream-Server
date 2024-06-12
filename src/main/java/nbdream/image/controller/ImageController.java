package nbdream.image.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import nbdream.common.advice.response.ApiResponse;
import nbdream.common.exception.NotFoundException;
import nbdream.image.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Image Controller")
public class ImageController {

    private final ImageService imageService;

    @Operation(summary = "이미지 업로드")
    @PostMapping(value = "/{domain}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<String> uploadImage(@PathVariable("domain") String domain, MultipartFile image, @RequestPart HttpServletRequest request) {
        if (domain == null) {
            return ApiResponse.ok();
        }
        try {
            return ApiResponse.ok(imageService.saveImage(image, domain));
        } catch (IOException e) {
            return ApiResponse.ok();
        }
    }

    @Operation(summary = "이미지 가져오기")
    @GetMapping("/{domain}/{id}")
    public ResponseEntity<byte[]> getImages(@PathVariable String domain, @PathVariable Long targetId) {
        try {
            byte[] imageData = imageService.loadImage(domain, targetId);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageData);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "이미지 삭제")
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
