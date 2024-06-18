package nbdream.farm.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import nbdream.auth.config.AuthenticatedMemberId;
import nbdream.common.advice.response.ApiResponse;
import nbdream.farm.service.GeminiService;
import nbdream.farm.service.LandElementsService;
import nbdream.farm.service.dto.LandElements.GetLandElementResDto;
import nbdream.farm.service.dto.gemini.PostAiChatReqDto;
import nbdream.farm.service.dto.gemini.PostAiChatResDto;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Tag(name = "LandElements Controller")
public class LandElementsController {
    private final LandElementsService landElementsService;
    private final GeminiService geminiService;

    @Operation(summary = "토지 성분 분석", description = "")
    @GetMapping("/auth/land-elements")
    public ApiResponse<GetLandElementResDto> getLandElements(@Parameter(hidden = true) @AuthenticatedMemberId Long memberId){
        return ApiResponse.ok(landElementsService.getLandElements(memberId));
    }


    @Operation(summary = "AI 채팅", description = "")
    @PostMapping("/auth/ai")
    public ApiResponse<PostAiChatResDto> gemini(@Parameter(hidden = true) @AuthenticatedMemberId Long memberId,
                                                @RequestBody PostAiChatReqDto reqDto){
        PostAiChatResDto response = new PostAiChatResDto(geminiService.getContents(reqDto, memberId));
        return ApiResponse.ok(response);
//        try {
//            return ResponseEntity.ok().body(geminiService.getContents(reqDto, memberId));
//        } catch (HttpClientErrorException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
    }
}
