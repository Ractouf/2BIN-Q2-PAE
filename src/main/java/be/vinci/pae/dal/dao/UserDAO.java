package be.vinci.pae.dal.dao;

import be.vinci.pae.business.dto.UserDTO;
import java.util.List;

/**
 * This is the interface of UserDAOImpl.
 */
public interface UserDAO extends GenericDAO<UserDTO> {

  /**
   * Finds a user with the given email.
   *
   * @param email of the user to find
   * @return the user or null if none found
   */
  UserDTO getUserByEmail(String email);

  /**
   * Get all user and sort them by date.
   *
   * @return a list of the users sorted by dates
   */
  List<UserDTO> getAllByDate();

  /**
   * Get all the admins.
   *
   * @return a list of the admins
   */
  List<UserDTO> getAdmins();

  /**
   * Get all the managers.
   *
   * @return a list of the managers
   */
  List<UserDTO> getManagers();

  /**
   * Get the number of users.
   *
   * @return the number of users
   */
  int getNbrUsers();

  /**
   * Get the number of helpers.
   *
   * @return the number of helpers
   */
  int getNbrHelpers();
}
