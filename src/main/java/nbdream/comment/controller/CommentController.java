package nbdream.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import nbdream.auth.config.AuthenticatedMemberId;
import nbdream.comment.domain.request.CreatePostRequest;
import nbdream.comment.domain.request.UpdateCommentRequest;
import nbdream.comment.service.CommentService;
import nbdream.common.advice.response.ApiResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Comment Controller")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "코멘트 생성")
    @PostMapping("/bulletin/{bulletin-id}/comments")
    public ApiResponse<Long> createComment(@PathVariable(name = "bulletin-id") Long bulletinId,
                                           @RequestBody CreatePostRequest request,
                                           @Parameter(hidden = true) @AuthenticatedMemberId Long memberId
    ) {
        Long commentId = commentService.postComment(bulletinId, request, memberId);
        return ApiResponse.ok(commentId);
    }


//    @GetMapping("/bulletin/{id}/comment")
//    public void getCommentsList(@PathVariable Long bulletinId) {
//    }
//
//
//    @GetMapping("/{nickname}/comment")
//    public void getMyCommentsList(@PathVariable String nickname) {
//    }


    @Operation(summary = "코멘트 수정")
    @PatchMapping("/comments/{comment-id}")
    public ApiResponse<String> updateComment(@PathVariable(name = "comment-id") Long commentId,
                                             @Parameter(hidden = true) @AuthenticatedMemberId Long memberId,
                                             @RequestBody UpdateCommentRequest request) {

        commentService.editComment(commentId, memberId, request);
        return ApiResponse.ok("UPDATED");
    }


    @Operation(summary = "코멘트 삭제")
    @DeleteMapping("/comments/{comment-id}")
    public ApiResponse<String> deleteComment(@PathVariable(name = "comment-id") Long commentId,
                                             @Parameter(hidden = true) @AuthenticatedMemberId Long memberId) {
        commentService.deleteComment(commentId, memberId);
        return ApiResponse.ok("DELETED");
    }


}
