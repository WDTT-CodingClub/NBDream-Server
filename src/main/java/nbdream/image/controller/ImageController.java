package nbdream.image.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import nbdream.common.advice.response.ApiResponse;
import nbdream.image.dto.ImageDto;
import nbdream.image.service.ImageService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
@Tag(name = "Image Controller")
public class ImageController {

    private final ImageService imageService;

    @Operation(summary = "이미지 업로드")
    @PostMapping(value = "/upload/{domain}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<String> uploadImage(@PathVariable("domain") String domain, @RequestPart MultipartFile image) throws IOException {
        return ApiResponse.ok(imageService.uploadImage(domain, image));
    }

    @Operation(summary = "이미지 삭제")
    @DeleteMapping
    public ApiResponse<Void> deleteImage(@RequestBody ImageDto request) {
        imageService.deleteImage(request);
        return ApiResponse.ok();
    }
}
