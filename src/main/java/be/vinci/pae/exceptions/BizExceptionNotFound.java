package be.vinci.pae.exceptions;

/**
 * Class representing the exception throw when not found in biz.
 */
public class BizExceptionNotFound extends RuntimeException {

  /**
   * Constructor of BizExceptionNotFound.
   *
   * @param message the message of the exception
   */
  public BizExceptionNotFound(String message) {
    super(message);
  }
}
