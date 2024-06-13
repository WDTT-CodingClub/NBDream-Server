package nbdream.image.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import nbdream.common.advice.response.ApiResponse;
import nbdream.image.service.ImageService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Image Controller")
public class ImageController {

    private final ImageService imageService;

    @Operation(summary = "이미지 업로드")
    @PostMapping(value = "/{domain}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<String> uploadImage(@PathVariable("domain") String domain, @RequestPart MultipartFile image) {
        return ApiResponse.ok(imageService.uploadImage(domain, image));
    }

    @DeleteMapping("/{domain}/{filename}")
    public ApiResponse<Void> deleteImage(@PathVariable String domain, @PathVariable(name = "filename") String fileName) {
        imageService.deleteImage(domain, fileName);
        return ApiResponse.ok();
    }
}
