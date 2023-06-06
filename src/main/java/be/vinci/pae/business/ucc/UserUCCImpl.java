package be.vinci.pae.business.ucc;

import be.vinci.pae.business.biz.User;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.dto.UserDTO.Role;
import be.vinci.pae.dal.DALServices;
import be.vinci.pae.dal.dao.UserDAO;
import be.vinci.pae.dal.transactions.Transaction;
import be.vinci.pae.exceptions.BizExceptionConflict;
import be.vinci.pae.exceptions.BizExceptionForbidden;
import be.vinci.pae.exceptions.BizExceptionNotFound;
import be.vinci.pae.exceptions.BizExceptionUnauthorized;
import jakarta.inject.Inject;
import java.util.List;

/**
 * This class contains the use cases of a User.
 */
public class UserUCCImpl implements UserUCC {

  @Inject
  private UserDAO myUserDAO;

  @Inject
  private DALServices dalServices;

  @Override
  public UserDTO login(String email, String password) {
    try (Transaction transaction = new Transaction(dalServices)) {
      User user = (User) myUserDAO.getUserByEmail(email);

      if (user == null) {
        throw new BizExceptionNotFound("Utilisateur non trouvé.");
      }
      if (!user.checkPassword(password)) {
        throw new BizExceptionUnauthorized("Mauvais mot de passe ou email.");
      }

      transaction.commit();

      return user;
    }
  }

  @Override
  public UserDTO register(UserDTO user) {
    try (Transaction transaction = new Transaction(dalServices)) {
      User userInDb = (User) myUserDAO.getUserByEmail(user.getEmail());

      if (userInDb != null) {
        throw new BizExceptionConflict("Cet email est déjà utilisé.");
      }

      user.setPassword(((User) user).hashPassword(user.getPassword()));

      UserDTO createdUser = myUserDAO.createOne(user);

      transaction.commit();

      return createdUser;
    }
  }

  @Override
  public UserDTO getUserById(int id) {
    try (Transaction transaction = new Transaction(dalServices)) {
      UserDTO user = myUserDAO.getById(id);

      transaction.commit();

      return user;
    }
  }

  @Override
  public List<UserDTO> getAll() {
    try (Transaction transaction = new Transaction(dalServices)) {
      List<UserDTO> users = myUserDAO.getAll();

      transaction.commit();

      return users;
    }
  }

  @Override
  public UserDTO promote(int id) {
    try (Transaction transaction = new Transaction(dalServices)) {
      User userInDb = (User) myUserDAO.getById(id);

      if (userInDb == null) {
        throw new BizExceptionNotFound("Utilisateur non trouvé.");
      }

      if (userInDb.getRole() == Role.MANAGER) {
        throw new BizExceptionForbidden("Vous ne pouvez pas promouvoir cet utilisateur.");
      }

      if (userInDb.getRole() == Role.HELPER) {
        userInDb.setRole(Role.MANAGER);
      }

      if (userInDb.getRole() == Role.USER) {
        userInDb.setRole(Role.HELPER);
      }

      UserDTO user = myUserDAO.updateOne(userInDb);

      transaction.commit();

      return user;
    }
  }

  @Override
  public List<UserDTO> getAllByDate() {
    try (Transaction transaction = new Transaction(dalServices)) {
      List<UserDTO> users = myUserDAO.getAllByDate();

      transaction.commit();

      return users;
    }
  }

  @Override
  public int getNbrUsers() {
    try (Transaction transaction = new Transaction(dalServices)) {
      int nbrUsers = myUserDAO.getNbrUsers();

      transaction.commit();

      return nbrUsers;
    }
  }

  @Override
  public int getNbrHelpers() {
    try (Transaction transaction = new Transaction(dalServices)) {
      int nbrHelpers = myUserDAO.getNbrHelpers();

      transaction.commit();

      return nbrHelpers;
    }
  }

  @Override
  public UserDTO editUser(UserDTO user) {
    try (Transaction transaction = new Transaction(dalServices)) {
      UserDTO userUpdated = myUserDAO.updateOne(user);

      transaction.commit();

      return userUpdated;
    }
  }
}
