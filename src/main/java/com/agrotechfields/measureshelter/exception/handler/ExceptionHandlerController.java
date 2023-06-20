package com.agrotechfields.measureshelter.exception.handler;

import com.agrotechfields.measureshelter.exception.EntityAlreadyExistsException;
import com.agrotechfields.measureshelter.exception.EntityNotFoundException;
import com.agrotechfields.measureshelter.exception.InvalidIdException;
import com.agrotechfields.measureshelter.exception.NotPermittedException;
import com.agrotechfields.measureshelter.exception.payload.ErrorPayload;
import com.auth0.jwt.exceptions.IncorrectClaimException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.ServletException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

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
    HttpStatus httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
    FieldError fd = e.getFieldErrors().get(0);
    String msg = fd.getField() + ": " + fd.getDefaultMessage();
    return buildResponse(msg, httpStatus);
  }

  /**
   * Handle invalid id.
   *
   * @param e the e
   * @return the response entity
   */
  @ExceptionHandler(InvalidIdException.class)
  public ResponseEntity<ErrorPayload> handleInvalidId(InvalidIdException e) {
    HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    String msg = e.getMessage() + " is invalid Id";
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
    HttpStatus httpStatus = HttpStatus.FORBIDDEN;
    String msg = e.getMessage() + " does not have permission to do this";
    return buildResponse(msg, httpStatus);
  }

  /**
   * Handle bad credentials.
   *
   * @param e the e
   * @return the response entity
   */
  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ErrorPayload> handleBadCredentials(BadCredentialsException e) {
    HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
    String msg = e.getMessage();
    return buildResponse(msg, httpStatus);
  }

  /**
   * Handle disabled user.
   *
   * @param e the e
   * @return the response entity
   */
  @ExceptionHandler(DisabledException.class)
  public ResponseEntity<ErrorPayload> handleDisabledUser(DisabledException e) {
    HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
    String msg = e.getMessage();
    return buildResponse(msg, httpStatus);
  }

  /**
   * Handle token exception.
   *
   * @param e the e
   * @return the response entity
   */
  @ExceptionHandler(TokenExpiredException.class)
  public ResponseEntity<ErrorPayload> handleTokenException(Exception e) {
    HttpStatus httpStatus = HttpStatus.FORBIDDEN;
    String msg = e.getMessage();
    return buildResponse(msg, httpStatus);
  }

  /**
   * Handle invalid token.
   *
   * @param e the e
   * @return the response entity
   */
  @ExceptionHandler({SignatureVerificationException.class, JWTDecodeException.class,
      IncorrectClaimException.class})
  public ResponseEntity<ErrorPayload> handleInvalidToken(Exception e) {
    HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    String msg = "Invalid token";
    return buildResponse(msg, httpStatus);
  }

  /**
   * Handle username not found.
   *
   * @param e the e
   * @return the response entity
   */
  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<ErrorPayload> handleUsernameNotFound(UsernameNotFoundException e) {
    HttpStatus httpStatus = HttpStatus.NOT_FOUND;
    String msg = "Username '" + e.getMessage() + "' provided by this token not found";
    return buildResponse(msg, httpStatus);
  }

  /**
   * Handle http request method not supported.
   *
   * @param e the e
   * @return the response entity
   */
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ErrorPayload> handleHttpRequestMethodNotSupported(
      HttpRequestMethodNotSupportedException e) {
    HttpStatus httpStatus = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
    String msg = e.getMessage();
    return buildResponse(msg, httpStatus);
  }

  /**
   * Handle multipart exception.
   *
   * @param e the e
   * @return the response entity
   */
  @ExceptionHandler(MultipartException.class)
  public ResponseEntity<ErrorPayload> handleMultipart(
      MaxUploadSizeExceededException e) {
    HttpStatus httpStatus = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
    String msg = e.getMessage();
    return buildResponse(msg, httpStatus);
  }

  /**
   * Handle servlet request.
   *
   * @param e the e
   * @return the response entity
   */
  @ExceptionHandler(ServletException.class)
  public ResponseEntity<ErrorPayload> handleServlet(ServletException e) {
    HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    String msg = e.getMessage();
    return buildResponse(msg, httpStatus);
  }

  /**
   * Handle max upload size exceeded exception.
   *
   * @param e the e
   * @return the response entity
   */
  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<ErrorPayload> handleMaxUploadSizeExceeded(
      MaxUploadSizeExceededException e) {
    HttpStatus httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
    String msg = e.getMessage();
    return buildResponse(msg, httpStatus);
  }

  /**
   * Handle generic error exception.
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
