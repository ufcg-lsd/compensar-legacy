package springboot.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import springboot.dto.output.CustomRestOutput;
import springboot.exception.auth.InvalidTokenException;
import springboot.exception.auth.UserAlreadyExistException;
import springboot.exception.data.RegisterNotFoundException;

import java.util.Date;

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

    /*
    @ExceptionHandler(InvalidTokenExce.class)
    public ResponseEntity<CustomRestError> handleWrongCredentialsException(Exception ex) {
        return new ResponseEntity<>(new CustomRestOutput(ex), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<CustomRestError> handleInvalidDataException(Exception ex) {
        CustomRestError errorMessage = new CustomRestError(new Date(), ex.getMessage().getDescription(false));
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<CustomRestError> handleInvalidTokenException(Exception ex) {
        CustomRestError errorMessage = new CustomRestError(new Date(), ex.getMessage().getDescription(false));
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<CustomRestError> handleNotAuthorizedException(Exception ex) {
        CustomRestError errorMessage = new CustomRestError(new Date(), ex.getMessage().getDescription(false));
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }*/
}