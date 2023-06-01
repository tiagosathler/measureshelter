package com.agrotechfields.measureshelter.exception.handler;

import com.agrotechfields.measureshelter.exception.EntityAlreadyExistsException;
import com.agrotechfields.measureshelter.exception.EntityNotFoundException;
import com.agrotechfields.measureshelter.exception.NotPermittedException;
import com.agrotechfields.measureshelter.exception.payload.ErrorPayload;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * The Class ExceptionHandlerController.
 */
@ControllerAdvice
public class ExceptionHandlerController {

  /**
   * Handle entity already exists.
   *
   * @param e the e
   * @return the response entity
   */
  @ExceptionHandler(EntityAlreadyExistsException.class)
  public ResponseEntity<ErrorPayload> handleEntityAlreadyExists(EntityAlreadyExistsException e) {
    HttpStatus httpStatus = HttpStatus.CONFLICT;
    String msg = e.getMessage() + " already exists";
    return buildResponse(msg, httpStatus);
  }

  /**
   * Handle entity not found.
   *
   * @param e the e
   * @return the response entity
   */
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorPayload> handleEntityNotFound(EntityNotFoundException e) {
    HttpStatus httpStatus = HttpStatus.NOT_FOUND;
    String msg = e.getMessage() + " not found";
    return buildResponse(msg, httpStatus);
  }

  /**
   * Handle method argument not valid.
   *
   * @param e the e
   * @return the response entity
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorPayload> handleMethodArgumentNotValid(
      MethodArgumentNotValidException e) {
    HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    FieldError fd = e.getFieldErrors().get(0);
    String msg = fd.getField() + ": " + fd.getDefaultMessage();
    return buildResponse(msg, httpStatus);
  }

  /**
   * Handle not permitted.
   *
   * @param e the e
   * @return the response entity
   */
  @ExceptionHandler(NotPermittedException.class)
  public ResponseEntity<ErrorPayload> handleNotPermitted(NotPermittedException e) {
    HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;
    String msg = e.getMessage() + " is not permitted or not working to do this";
    return buildResponse(msg, httpStatus);
  }

  /**
   * Handle generic error.
   *
   * @param e the e
   * @return the response entity
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorPayload> handleGenericError(Exception e) {
    HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String msg = e.getMessage();
    e.printStackTrace();
    return buildResponse(msg, httpStatus);
  }

  /**
   * Builds the response.
   *
   * @param msg the msg
   * @param httpStatus the http status
   * @return the response entity
   */
  private ResponseEntity<ErrorPayload> buildResponse(String msg, HttpStatus httpStatus) {
    ErrorPayload error = new ErrorPayload(msg, httpStatus);
    return ResponseEntity.status(httpStatus).body(error);
  }
}
