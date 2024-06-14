package nbdream.comment.controller;

import lombok.RequiredArgsConstructor;
import nbdream.auth.config.AuthenticatedMemberId;
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
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/bulletin/{id}/comment")
    public ApiResponse<Long> postComment(@PathVariable Long bulletinId,
                                         @RequestBody String commentDetail,
                                         @AuthenticatedMemberId Long memberId) {
        Long commentId = commentService.postComment(bulletinId, commentDetail, memberId);
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


    @PatchMapping("/comment/{id}")
    public ApiResponse<String> editComment(@PathVariable Long commentId,
                                           @AuthenticatedMemberId Long memberId,
                                           @RequestBody String commentDetail) {

        commentService.editComment(commentId, memberId, commentDetail);
        return ApiResponse.ok("UPDATED");
    }


    @DeleteMapping("/comment/{id}")
    public ApiResponse<String> deleteComment(@PathVariable Long commentId,
                                             @AuthenticatedMemberId Long memberId) {
        commentService.deleteComment(commentId, memberId);
        return ApiResponse.ok("DELETED");
    }


}
