package be.vinci.pae.exceptions;

/**
 * Class representing the exception throw when unauthorized in biz.
 */
public class BizExceptionUnauthorized extends RuntimeException {

  /**
   * Class constructor.
   *
   * @param message error message
   */
  public BizExceptionUnauthorized(String message) {
    super(message);
  }
}
