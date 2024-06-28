package nbdream.comment.exception;

import nbdream.common.exception.BadRequestException;

public class UneditableCommentException extends BadRequestException {
    public UneditableCommentException() { super("게시글을 수정/삭제할 권한이 없습니다.");}
}
