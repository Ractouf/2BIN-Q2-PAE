package be.vinci.pae.exceptions;

/**
 * Class representing the exception throw when forbidden in biz.
 */
public class BizExceptionForbidden extends RuntimeException {

  /**
   * Class constructor.
   *
   * @param message error message
   */
  public BizExceptionForbidden(String message) {
    super(message);
  }
}
