package com.finapp.backend.exception;

import com.finapp.backend.util.AppConstants;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ProblemDetail> handleBadCredentialsException(BadCredentialsException exception, HttpServletRequest request) {
        LOGGER.error(exception.getMessage());
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, AppConstants.INCORRECT_CREDENTIALS);
        problemDetail.setTitle("Bad Credentials");
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        return new ResponseEntity<>(problemDetail, HttpStatusCode.valueOf(problemDetail.getStatus()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ProblemDetail> handleAccessDeniedException(AccessDeniedException exception, HttpServletRequest request) {
        LOGGER.error(exception.getMessage());

        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, AppConstants.ACCESS_DENIED);
        problemDetail.setTitle("Access Denied");
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        return new ResponseEntity<>(problemDetail, HttpStatusCode.valueOf(problemDetail.getStatus()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleEntityNotFoundException(EntityNotFoundException exception, HttpServletRequest request) {
        LOGGER.error(exception.getMessage());

        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getLocalizedMessage());
        problemDetail.setTitle("Entity Not Found");

        problemDetail.setInstance(URI.create(request.getRequestURI()));

        return new ResponseEntity<>(problemDetail, HttpStatusCode.valueOf(problemDetail.getStatus()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ProblemDetail> handleBadRequestException(BadRequestException exception, HttpServletRequest request) {
        LOGGER.error(exception.getMessage());

        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase());

        problemDetail.setTitle("Invalid Request");
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        return new ResponseEntity<>(problemDetail, HttpStatusCode.valueOf(problemDetail.getStatus()));
    }

    @ExceptionHandler(EntityAlreadyExistException.class)
    public ResponseEntity<ProblemDetail> handleEntityExistsException(EntityAlreadyExistException exception, HttpServletRequest request) {
        LOGGER.error(exception.getMessage());

        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getLocalizedMessage());
        problemDetail.setTitle("Entity Already Exists");
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        return new ResponseEntity<>(problemDetail, HttpStatusCode.valueOf(problemDetail.getStatus()));
    }

    @ExceptionHandler(SaveEntityException.class)
    public ResponseEntity<ProblemDetail> handleSaveEntityException(SaveEntityException exception, HttpServletRequest request) {
        LOGGER.error(exception.getMessage());
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, exception.getLocalizedMessage());
        problemDetail.setTitle("Error Saving Entity");
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        return new ResponseEntity<>(problemDetail, HttpStatusCode.valueOf(problemDetail.getStatus()));
    }

    @ExceptionHandler(PasswordsDoNotMatchException.class)
    public ResponseEntity<ProblemDetail> handlePasswordsDoNotMatchException(PasswordsDoNotMatchException exception, HttpServletRequest request) {
        LOGGER.error(exception.getMessage());

        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, AppConstants.PASSWORDS_MISMATCH);
        problemDetail.setTitle("Passwords Mismatch");
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        return new ResponseEntity<>(problemDetail, HttpStatusCode.valueOf(problemDetail.getStatus()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleValidationException(ConstraintViolationException exception, HttpServletRequest request) {
        LOGGER.error(exception.getMessage());

        List<String> errorMessages = exception.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .toList();


        var problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Validation Failed");
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setDetail("One or more validation errors occurred");
        problemDetail.setProperties(Map.of("errors", errorMessages));

        return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(BindException.class)
    public ResponseEntity<ProblemDetail> handleBindException(BindException exception, HttpServletRequest request) {
        List<String> errorMessages = exception.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Validation Failed");
        problemDetail.setDetail("Binding errors occurred");
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperties(Map.of("errors", errorMessages));

        return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
    }
}
