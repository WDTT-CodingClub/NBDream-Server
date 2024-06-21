package nbdream.bulletin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import nbdream.auth.config.AuthenticatedMemberId;
import nbdream.bulletin.dto.request.SearchBulletinCondDto;
import nbdream.bulletin.dto.response.BulletinResDto;
import nbdream.bulletin.dto.response.BulletinsResDto;
import nbdream.bulletin.service.SearchingBulletinService;
import nbdream.common.advice.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/bulletins")
@Tag(name = "Searching Bulletin Controller")
public class SearchingBulletinController {

    private final SearchingBulletinService searchingBulletinService;

    @Operation(summary = "게시글 목록 조회", description = "처음 조회 시, 페이징에 사용되는 lastBulletinId 는 비어있는 상태로 요청해야 함")
    @GetMapping
    public ApiResponse<BulletinsResDto> getBulletins(@RequestParam(value = "keyword", required = false) final String keyword,
                                                     @RequestParam(value = "bulletinCategory", required = false) final String bulletinCategory,
                                                     @RequestParam(value = "crop", required = false) final String crop,
                                                     @RequestParam(value = "lastBulletinId", required = false) final Long lastBulletinId) {

        return ApiResponse.ok(searchingBulletinService.getBulletins(new SearchBulletinCondDto(keyword, bulletinCategory, crop, lastBulletinId)));
    }

    @Operation(summary = "게시글 상세 조회")
    @GetMapping("/{bulletin-id}")
    public ApiResponse<BulletinResDto> getBulletinDetails(@PathVariable("bulletin-id") final Long bulletinId) {
        return ApiResponse.ok(searchingBulletinService.getBulletinDetails(bulletinId));
    }

    @Operation(summary = "북마크한 게시글 목록 조회")
    @GetMapping("/bookmarks")
    public ApiResponse<BulletinsResDto> getBookmarkBulletins(@Parameter(hidden = true) @AuthenticatedMemberId Long memberId,
                                                             @RequestParam(value = "lastBulletinId", required = false) final Long lastBulletinId) {
        return ApiResponse.ok(searchingBulletinService.getBookmarkBulletins(memberId, (lastBulletinId == null) ? Long.MAX_VALUE : lastBulletinId));
    }

    @Operation(summary = "작성한 게시글 목록 조회")
    @GetMapping("/my-bulletins")
    public ApiResponse<BulletinsResDto> getMyBulletins(@Parameter(hidden = true) @AuthenticatedMemberId Long memberId,
                                                       @RequestParam(value = "lastBulletinId", required = false) final Long lastBulletinId) {
        return ApiResponse.ok(searchingBulletinService.getMyBulletins(memberId, (lastBulletinId == null) ? Long.MAX_VALUE : lastBulletinId));
    }
}

