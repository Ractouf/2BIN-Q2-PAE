package be.vinci.pae.dal;

/**
 * This is the interface of DALServicesImpl.
 */
public interface DALServices {

  /**
   * Start a transaction.
   */
  void initTransaction();

  /**
   * Commit a transaction.
   */
  void commitTransaction();

  /**
   * Roll back a transaction.
   */
  void rollBackTransaction();

  /**
   * Check if a transaction is in progress.
   *
   * @return true if a transaction is in progress, false otherwise
   */
  boolean isTransactionInProgress();
}
