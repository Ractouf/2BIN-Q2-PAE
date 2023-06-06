package be.vinci.pae.dal.transactions;

import be.vinci.pae.dal.DALServices;

/**
 * This class is used to manage transactions.
 */
public class Transaction implements AutoCloseable {

  private final DALServices dalServices;

  /**
   * Constructor of Transaction.
   *
   * @param dalServices the DALServices
   */
  public Transaction(DALServices dalServices) {
    this.dalServices = dalServices;
    this.dalServices.initTransaction();
  }

  /**
   * Commit the transaction.
   */
  public void commit() {
    dalServices.commitTransaction();
  }

  @Override
  public void close() {
    if (dalServices.isTransactionInProgress()) {
      dalServices.rollBackTransaction();
    }
  }
}
