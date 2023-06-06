package be.vinci.pae.exceptions;

/**
 * Class representing the exception throw when conflicts in biz.
 */
public class BizExceptionConflict extends RuntimeException {

  /**
   * Class constructor.
   *
   * @param message error message
   */
  public BizExceptionConflict(String message) {
    super(message);
  }
}
