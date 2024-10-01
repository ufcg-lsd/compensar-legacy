package springboot.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import springboot.dto.output.CustomRestOutput;
import springboot.exception.auth.InvalidTokenException;
import springboot.exception.auth.UserAlreadyExistException;
import springboot.exception.data.NoPendentQuestionException;
import springboot.exception.data.PermissionDeniedException;
import springboot.exception.data.RegisterNotFoundException;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomRestOutput> handleAnyException(Exception ex) {
        return new ResponseEntity<>(new CustomRestOutput(ex), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<CustomRestOutput> handleInvalidTokenException(Exception ex) {
        return new ResponseEntity<>(new CustomRestOutput(ex), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RegisterNotFoundException.class)
    public ResponseEntity<CustomRestOutput> handleRegisterNotFoundException(Exception ex) {
        return new ResponseEntity<>(new CustomRestOutput(ex), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<CustomRestOutput> handleUserAlreadyExistException(Exception ex) {
        return new ResponseEntity<>(new CustomRestOutput(ex), new HttpHeaders(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PermissionDeniedException.class)
    public ResponseEntity<CustomRestOutput> handlePermissionDeniedException(Exception ex) {
        return new ResponseEntity<>(new CustomRestOutput(ex), new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NoPendentQuestionException.class)
    public ResponseEntity<CustomRestOutput> handleNoPendentQuestionException(Exception ex) {
        return new ResponseEntity<>(new CustomRestOutput(ex), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MailException.class)
    public ResponseEntity<CustomRestOutput> handleMailException(Exception ex) {
        return new ResponseEntity<>(new CustomRestOutput(ex), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}