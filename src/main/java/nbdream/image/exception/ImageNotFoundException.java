package nbdream.image.exception;

import nbdream.common.exception.NotFoundException;

public class ImageNotFoundException extends NotFoundException {
    public ImageNotFoundException() {
        super("이미지를 찾을 수 없습니다.");
    }
}