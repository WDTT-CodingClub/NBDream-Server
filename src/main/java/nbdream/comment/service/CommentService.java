package nbdream.comment.service;


import lombok.RequiredArgsConstructor;
import nbdream.alarm.service.AlarmService;
import nbdream.bulletin.domain.Bulletin;
import nbdream.bulletin.exception.BulletinNotFoundException;
import nbdream.bulletin.repository.BulletinRepository;
import nbdream.comment.domain.Comment;
import nbdream.comment.domain.request.CreatePostRequest;
import nbdream.comment.domain.request.UpdateCommentRequest;
import nbdream.comment.dto.CommentResDto;
import nbdream.comment.exception.NotFoundCommentException;
import nbdream.comment.repository.CommentRepository;
import nbdream.member.domain.Member;
import nbdream.member.exception.MemberNotFoundException;
import nbdream.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final BulletinRepository bulletinRepository;
    private final AlarmService alarmService;

    public Long postComment(Long bulletinId, CreatePostRequest request, Long memberId) {
        Member memberEntity = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        Bulletin bulletinEntity = bulletinRepository.findById(bulletinId)
                .orElseThrow(BulletinNotFoundException::new);

        Comment commentEntity = Comment.builder()
                .author(memberEntity)
                .bulletin(bulletinEntity)
                .content(request.getCommentDetail())
                .build();
        commentEntity = commentRepository.save(commentEntity);

        //게시글 작성자에게 push 알람 전송 (게시글 작성자 id, 댓글작성자 닉네임, 댓글 작성자 id)
        alarmService.sendCommentAlarm(bulletinEntity, memberEntity.getNickname(), memberId);
        return commentEntity.getId();
    }


    @Transactional(readOnly = true)
    public List<CommentResDto> getMyCommentsList(final Long memberId) {
        final Member author = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        final List<Comment> comments = commentRepository.findByAuthor(author);

        return comments.stream()
                .map(comment -> new CommentResDto(comment, memberId))
                .collect(Collectors.toList());
    }


    public void editComment(Long commentId, Long memberId, UpdateCommentRequest request) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundCommentException());
        comment.update(memberId, request.getCommentDetail());
        commentRepository.save(comment);
    }


    public void deleteComment(Long commentId, Long memberId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundCommentException());
        comment.delete(memberId);
    }

}




