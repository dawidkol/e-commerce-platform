package pl.dk.ecommerceplatform.error;

import lombok.Builder;
import org.hibernate.annotations.processing.SQL;
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
    public List<ConstraintViolationError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> new ConstraintViolationError(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();
    }

//    @ResponseStatus(HttpStatus.CONFLICT)
//    @ExceptionHandler(SQLException.class)
//    public SqlError handleDuplicateKeyViolates(SQLException ex) {
//        return SqlError.builder()
//                .error(ex.getMessage())
//                .message("The user has already rated the product.")
//                .build();
//    }

}
