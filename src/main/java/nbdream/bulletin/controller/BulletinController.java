package nbdream.bulletin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import nbdream.auth.config.AuthenticatedMemberId;
import nbdream.bulletin.dto.request.BulletinReqDto;
import nbdream.bulletin.service.BulletinService;
import nbdream.common.advice.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/bulletins")
@Tag(name = "Bulletin Controller")
public class BulletinController {

    private final BulletinService bulletinService;

    @Operation(summary = "게시글 작성", description = "작성 후 게시글 ID 반환")
    @PostMapping
    public ApiResponse<Long> createBulletin(@Parameter(hidden = true) @AuthenticatedMemberId final Long memberId,
                                            @RequestBody final BulletinReqDto request) {
        return ApiResponse.ok(bulletinService.createBulletin(memberId, request));
    }

    @Operation(summary = "게시글 수정", description = "게시글 수정 시, 추가한 이미지 url만 반환해야 정상적으로 동작(저장되어 있는 이미지 url은 반환x)")
    @PutMapping("/{bulletin-id}")
    public ApiResponse<Long> updateBulletin(@Parameter(hidden = true) @AuthenticatedMemberId final Long memberId,
                                        @PathVariable("bulletin-id") final Long bulletinId,
                                        @RequestBody final BulletinReqDto request) {
        return ApiResponse.ok(bulletinService.updateBulletin(memberId, bulletinId, request));
    }

    @Operation(summary = "게시글 삭제")
    @DeleteMapping("/{bulletin-id}")
    public ApiResponse<Void> deleteBulletin(@Parameter(hidden = true) @AuthenticatedMemberId final Long memberId,
                                            @PathVariable("bulletin-id") final Long bulletinId) {
        bulletinService.deleteBulletin(memberId, bulletinId);
        return ApiResponse.ok();
    }
}
