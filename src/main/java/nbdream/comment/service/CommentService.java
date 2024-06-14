package nbdream.comment.service;


import lombok.RequiredArgsConstructor;
import nbdream.bulletin.domain.Bulletin;
import nbdream.bulletin.exception.BulletinNotFoundException;
import nbdream.bulletin.repository.BulletinRepository;
import nbdream.comment.domain.Comment;
import nbdream.comment.domain.request.PostCommentRequest;
import nbdream.comment.repository.CommentRepository;
import nbdream.common.exception.UnauthorizedException;
import nbdream.member.domain.Member;
import nbdream.member.exception.MemberNotFoundException;
import nbdream.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final BulletinRepository bulletinRepository;

    public Long postComment(Long bulletinId, String commentDetail, Long memberId) {
        Member memberEntity = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        Bulletin bulletinEntity = bulletinRepository.findById(bulletinId)
                .orElseThrow(BulletinNotFoundException::new);

        Comment commentEntity = Comment.builder()
                .author(memberEntity)
                .bulletin(bulletinEntity)
                .content(commentDetail)
                .build();

        commentRepository.save(commentEntity);

        return commentEntity.getId();
    }


    @Transactional(readOnly = true)
    public void getCommentsList(Long bulletinId) {
    }


    @Transactional(readOnly = true)
    public void getMyCommentsList() {
    }


    public void editComment(Long commentId, Long memberId, String commentDetail) {
        if (checkAuthorization(commentId, memberId)) {
            Member commentEditor = memberRepository.getReferenceById(memberId);

            Comment commentEntity = Comment.builder()
                    .id(commentId)
                    .author(commentEditor)
                    .content(commentDetail)
                    .build();
            commentRepository.save(commentEntity);
        } else {
            throw new UnauthorizedException("NOT_AUTHORIZED");
        }
    }


    public void deleteComment(Long commentId, Long memberId) {
        if (checkAuthorization(commentId, memberId)) {
            Member commentEditor = memberRepository.getReferenceById(memberId);
            Comment commentEntity = commentRepository.getReferenceById(commentId);
        } else {
            throw new UnauthorizedException("NOT_AUTHORIZED");
        }
    }

    protected boolean checkAuthorization(Long commentId, Long memberId) {
        Member commentWriter = commentRepository.findById(commentId)
                .orElseThrow(MemberNotFoundException::new)
                .getAuthor();

        Member commentEditor = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        return commentWriter == commentEditor;
    }
}



