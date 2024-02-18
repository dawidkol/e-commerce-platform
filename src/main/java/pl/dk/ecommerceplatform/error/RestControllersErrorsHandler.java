package pl.dk.ecommerceplatform.error;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.util.List;

@RestControllerAdvice
class RestControllersErrorsHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<MethodArgumentNotValidWrapper> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> new MethodArgumentNotValidWrapper(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(SQLException.class)
    public SqlErrorWrapper handleDuplicateKeyViolates(SQLException ex) {
        return new SqlErrorWrapper(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public List<ConstraintViolationWrapper> handleConstraintViolationException(ConstraintViolationException ex) {
        return ex.getConstraintViolations()
                .stream().map(fieldError -> new ConstraintViolationWrapper(fieldError.getInvalidValue().toString(), fieldError.getMessage()))
                .toList();
    }

    record SqlErrorWrapper(String message) {
    }

    record MethodArgumentNotValidWrapper(String field, String message) {
    }

    record ConstraintViolationWrapper(String value, String message) {
    }

}
