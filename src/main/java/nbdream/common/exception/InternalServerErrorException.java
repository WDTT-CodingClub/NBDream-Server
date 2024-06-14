package nbdream.common.exception;

public class InternalServerErrorException extends RuntimeException{
    public InternalServerErrorException(final String message) {
        super(message);
    }
}
