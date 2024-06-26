package nbdream.common.advice;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import nbdream.common.advice.response.ApiResponse;
import nbdream.common.exception.BadRequestException;
import nbdream.common.exception.InvalidFormatException;
import nbdream.common.exception.NotFoundException;
import nbdream.common.exception.UnauthorizedException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.IOException;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class CommonControllerAdvice {

    @ExceptionHandler({
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<ApiResponse> handleBadRequest() {
        return ResponseEntity.badRequest().body(ApiResponse.of(BAD_REQUEST, "입력 형식 또는 타입이 올바르지 않습니다."));
    }

    @ExceptionHandler({BadRequestException.class, InvalidFormatException.class})
    public ResponseEntity<ApiResponse> handleBadRequest(final Exception e) {
        log.debug("HandleBadRequest : {}", e.getMessage());
        return ResponseEntity.badRequest().body(ApiResponse.of(BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler({UnauthorizedException.class, JwtException.class})
    public ResponseEntity<ApiResponse> handleUnauthorized(final Exception e) {
        log.debug("UnauthorizedException : {}", e.getMessage());
        log.debug("Exception Stack Trace : \n{}", e.getStackTrace());
        return ResponseEntity.status(UNAUTHORIZED).body(ApiResponse.of(UNAUTHORIZED, e.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse> handleNotFound(final Exception e) {
        log.debug("NotFoundException : {}", e.getMessage());
        return ResponseEntity.status(NOT_FOUND).body(ApiResponse.of(NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler({RuntimeException.class, IOException.class})
    public ResponseEntity<ApiResponse> handleInternalServerError(RuntimeException e) {
        log.debug("RuntimeException : {}", e.getMessage());
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(ApiResponse.of(INTERNAL_SERVER_ERROR, e.getMessage()));
    }
}
