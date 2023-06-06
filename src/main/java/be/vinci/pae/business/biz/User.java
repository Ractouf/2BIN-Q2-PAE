package be.vinci.pae.business.biz;

import be.vinci.pae.business.dto.UserDTO;

/**
 * This is the interface of UserImpl.
 */
public interface User extends UserDTO {

  /**
   * Checks the password.
   *
   * @param password to check
   * @return true if it worked, false if not
   */
  boolean checkPassword(String password);

  /**
   * Hashes a password.
   *
   * @param password to hash
   * @return the hashed password
   */
  String hashPassword(String password);

  /**
   * Increment the version of the user.
   */
  void incrementVersion();
}
