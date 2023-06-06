package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.UserDTO;
import java.util.List;

/**
 * This is the interface of UserUCCImpl.
 */
public interface UserUCC {

  /**
   * Logs in an User.
   *
   * @param email    of the user to log in
   * @param password of the user to log in
   * @return the logged-in user
   */
  UserDTO login(String email, String password);

  /**
   * Registers an User.
   *
   * @param user user to register
   * @return the created user
   */
  UserDTO register(UserDTO user);

  /**
   * Finds a user with the given id.
   *
   * @param id of the user to find
   * @return the user or null if none found
   */
  UserDTO getUserById(int id);

  /**
   * Get every user.
   *
   * @return a list of all the user
   */
  List<UserDTO> getAll();

  /**
   * Promote a user to helper or manager.
   *
   * @param id the id of the user which role is to be change
   * @return the changed user
   */
  UserDTO promote(int id);

  /**
   * Get all user and sort them by date.
   *
   * @return a list of the users sorted by dates
   */
  List<UserDTO> getAllByDate();

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

  /**
   * Edits a user to the given changes.
   *
   * @param user to edit
   * @return the edited user
   */
  UserDTO editUser(UserDTO user);
}
