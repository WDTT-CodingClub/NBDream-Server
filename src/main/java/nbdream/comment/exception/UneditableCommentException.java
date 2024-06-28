package nbdream.comment.exception;

import nbdream.common.exception.BadRequestException;

public class UneditableCommentException extends BadRequestException {
    public UneditableCommentException() { super("댓글 수정 또는 삭제 권한이 없습니다.");}


}
