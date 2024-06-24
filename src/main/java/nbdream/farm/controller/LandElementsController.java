package nbdream.farm.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import nbdream.auth.config.AuthenticatedMemberId;
import nbdream.common.advice.response.ApiResponse;
import nbdream.farm.domain.Farm;
import nbdream.farm.repository.FarmRepository;
import nbdream.farm.service.GeminiService;
import nbdream.farm.service.LandElementsService;
import nbdream.farm.service.dto.LandElements.soilData.GetLandElementResDto;
import nbdream.farm.service.dto.gemini.PostAiChatReqDto;
import nbdream.farm.service.dto.gemini.PostAiChatResDto;
import org.springframework.web.bind.annotation.*;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "LandElements Controller")
public class LandElementsController {
    private final LandElementsService landElementsService;
    private final GeminiService geminiService;


    @Operation(summary = "토지 성분 분석 (법정동코드)", description = "테스트 전 주소 등록되어 있어야 함(프로필수정API 이용)")
    @GetMapping("/land-elements")
    public ApiResponse<GetLandElementResDto> getLandElementsByBjd(@Parameter(hidden = true) @AuthenticatedMemberId Long memberId) {
        return ApiResponse.ok(landElementsService.getLandElements(memberId));
    }

    @Operation(summary = "AI 채팅", description = "테스트 전 주소 등록되어 있어야 함(프로필수정API 이용), question : (1) 또는 (작물명)")
    @PostMapping("/ai")
    public ApiResponse<PostAiChatResDto> gemini(@Parameter(hidden = true) @AuthenticatedMemberId Long memberId,
                                                @RequestBody PostAiChatReqDto reqDto){
        PostAiChatResDto response = new PostAiChatResDto(geminiService.getContents(reqDto, memberId));
        return ApiResponse.ok(response);
    }


}

//    @Operation(summary = "토지 성분 분석", description = "테스트 시 농장에 주소가 등록되어 있어야 함. PNU 코드 이용")
//    @GetMapping("/land-elements")
//    public ApiResponse<GetLandElementResDto> getLandElements(@Parameter(hidden = true) @AuthenticatedMemberId Long memberId){
//        return ApiResponse.ok(landElementsService.getLandElements(memberId));
//    }

