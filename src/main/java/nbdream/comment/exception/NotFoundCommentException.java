package nbdream.comment.exception;

import nbdream.common.exception.NotFoundException;

public class NotFoundCommentException extends NotFoundException {
    public NotFoundCommentException() { super("존재하지 않는 댓글입니다.");}
}
