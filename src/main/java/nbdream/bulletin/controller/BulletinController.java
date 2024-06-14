package nbdream.bulletin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import nbdream.auth.config.AuthenticatedMemberId;
import nbdream.bulletin.dto.BulletinReqDto;
import nbdream.bulletin.service.BulletinService;
import nbdream.common.advice.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/bulletins")
@Tag(name = "Bulletin Controller")
public class BulletinController {

    private final BulletinService bulletinService;

    @Operation(summary = "게시글 작성", description = "")
    @PostMapping
    public ApiResponse<Long> createBulletin(@AuthenticatedMemberId final Long memberId, @RequestBody final BulletinReqDto request) {
        return ApiResponse.ok(bulletinService.createBulletin(memberId, request));
    }

    @Operation(summary = "게시글 수정", description = "")
    @PutMapping("/{bulletin-id}")
    public ApiResponse<Long> updateBulletin(@AuthenticatedMemberId final Long memberId,
                                        @PathVariable("bulletin-id") final Long bulletinId,
                                        @RequestBody final BulletinReqDto request) {
        return ApiResponse.ok(bulletinService.updateBulletin(memberId, bulletinId, request));
    }

    @Operation(summary = "게시글 삭제", description = "")
    @DeleteMapping("/{bulletin-id}")
    public ApiResponse<Void> deleteBulletin(@AuthenticatedMemberId final Long memberId, @PathVariable("bulletin-id") final Long bulletinId) {
        bulletinService.deleteBulletin(memberId, bulletinId);
        return ApiResponse.ok();
    }
}
