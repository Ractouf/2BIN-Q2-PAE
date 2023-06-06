package be.vinci.pae.exceptions;

/**
 * Class representing the exception throw when optimistic lock failed.
 */
public class OptimisticLockException extends RuntimeException {

  /**
   * Class constructor.
   *
   * @param message error message
   */
  public OptimisticLockException(String message) {
    super(message);
  }
}
