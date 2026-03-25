package com.retail.rewards.exception;

import com.retail.rewards.model.ErrorResponse;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.MethodArgumentTypeMismatchException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Converts application exceptions into consistent REST responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles requests for customers that are not present.
     *
     * @param exception missing customer exception
     * @return 404 error response
     */
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCustomerNotFound(CustomerNotFoundException exception) {
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    /**
     * Handles invalid business validation scenarios.
     *
     * @param exception validation exception
     * @return 400 error response
     */
    @ExceptionHandler({InvalidTransactionException.class, InvalidDateRangeException.class})
    public ResponseEntity<ErrorResponse> handleBadRequest(RuntimeException exception) {
        return buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    /**
     * Handles invalid request parameter type conversions.
     *
     * @param exception type mismatch exception
     * @return 400 error response
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException exception) {
        return buildResponse(HttpStatus.BAD_REQUEST,
                "Invalid value for parameter '" + exception.getName() + "'.");
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(new ErrorResponse(Instant.now(), status.value(), status.getReasonPhrase(), message));
    }
}
