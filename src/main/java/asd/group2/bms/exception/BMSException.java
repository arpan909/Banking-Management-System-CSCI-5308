package asd.group2.bms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @description: This is server error
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class BMSException extends RuntimeException {

  public BMSException(String message) {
    super(message);
  }

  public BMSException(String message, Throwable cause) {
    super(message, cause);
  }

}
