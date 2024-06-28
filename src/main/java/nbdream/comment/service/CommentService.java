package nbdream.comment.service;


import lombok.RequiredArgsConstructor;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final BulletinRepository bulletinRepository;

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

        return commentEntity.getId();
    }


    @Transactional(readOnly = true)
    public List<CommentResDto> getMyCommentsList(final Long memberId) {
        final Member author = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        final List<Comment> comments = commentRepository.findByAuthor(author);

        return comments.stream()
                .map(comment -> new CommentResDto(comment))
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




