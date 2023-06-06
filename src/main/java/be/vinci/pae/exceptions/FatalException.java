package be.vinci.pae.exceptions;

/**
 * Class representing the exception throw when fatal in biz.
 */
public class FatalException extends RuntimeException {

  /**
   * Class constructor.
   *
   * @param message error message
   * @param cause   error cause
   */
  public FatalException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Class constructor.
   *
   * @param message error message
   */
  public FatalException(String message) {
    super(message);
  }
}
