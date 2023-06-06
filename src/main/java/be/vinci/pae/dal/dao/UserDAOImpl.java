package be.vinci.pae.dal.dao;

import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.dal.DALBackend;
import be.vinci.pae.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is the data access object of User.
 */
public class UserDAOImpl extends GenericDAOImpl<UserDTO> implements UserDAO {

  @Inject
  private DALBackend myDALBackend;

  @Override
  public UserDTO getUserByEmail(String email) {
    String sql = "SELECT us.id_user, us.email, us.password, us.register_date, us.lastname, "
        + "us.firstname, us.photo, us.phone_number, us.role, us.version "
        + "FROM projet_pae.users us WHERE us.email = ?";

    try (PreparedStatement getUserByEmail = myDALBackend.getPrepareStatement(sql)) {
      getUserByEmail.setString(1, email);

      try (ResultSet rs = getUserByEmail.executeQuery()) {
        if (!rs.next()) {
          return null;
        }

        return get(rs);
      }
    } catch (SQLException se) {
      throw new FatalException(se.getMessage(), se);
    }
  }

  @Override
  public List<UserDTO> getAllByDate() {
    String sql = "SELECT us.id_user, us.email, us.password, us.register_date, us.lastname, "
        + "us.firstname, us.photo, us.phone_number, us.role, us.version "
        + "FROM projet_pae.users us ORDER BY us.register_date DESC";

    List<UserDTO> reponse = new ArrayList<>();

    try (PreparedStatement getAllByDate = myDALBackend.getPrepareStatement(sql)) {

      try (ResultSet rs = getAllByDate.executeQuery()) {
        while (rs.next()) {
          reponse.add(get(rs));
        }
      }
    } catch (SQLException se) {
      throw new FatalException(se.getMessage(), se);
    }
    return reponse;
  }

  @Override
  public List<UserDTO> getAdmins() {
    String sql = "SELECT us.id_user, us.email, us.password, us.register_date, us.lastname, "
        + "us.firstname, us.photo, us.phone_number, us.role, us.version "
        + "FROM projet_pae.users us "
        + "WHERE us.role = 'H' OR us.role = 'M'";

    List<UserDTO> reponse = new ArrayList<>();

    try (PreparedStatement getAdmins = myDALBackend.getPrepareStatement(sql)) {

      try (ResultSet rs = getAdmins.executeQuery()) {
        while (rs.next()) {
          reponse.add(get(rs));
        }
      }
    } catch (SQLException se) {
      throw new FatalException(se.getMessage(), se);
    }
    return reponse;
  }

  @Override
  public List<UserDTO> getManagers() {
    String sql = "SELECT us.id_user, us.email, us.password, us.register_date, us.lastname, "
        + "us.firstname, us.photo, us.phone_number, us.role, us.version "
        + "FROM projet_pae.users us "
        + "WHERE us.role = 'M'";

    List<UserDTO> reponse = new ArrayList<>();

    try (PreparedStatement getAdmins = myDALBackend.getPrepareStatement(sql)) {

      try (ResultSet rs = getAdmins.executeQuery()) {
        while (rs.next()) {
          reponse.add(get(rs));
        }
      }
    } catch (SQLException se) {
      throw new FatalException(se.getMessage(), se);
    }
    return reponse;
  }

  @Override
  public int getNbrUsers() {
    String sql = "SELECT COUNT(id_user) FROM projet_pae.users";
    int nbrUsers = 0;
    try (PreparedStatement getNbrUsers = myDALBackend.getPrepareStatement(sql)) {
      try (ResultSet rs = getNbrUsers.executeQuery()) {
        if (rs.next()) {
          nbrUsers = rs.getInt(1);
        }
      }
    } catch (SQLException se) {
      throw new FatalException(se.getMessage(), se);
    }
    return nbrUsers;
  }

  @Override
  public int getNbrHelpers() {
    String sql = "SELECT COUNT(id_user) FROM projet_pae.users WHERE role = 'H'";
    int nbrHelpers = 0;
    try (PreparedStatement getNbrHelpers = myDALBackend.getPrepareStatement(sql)) {
      try (ResultSet rs = getNbrHelpers.executeQuery()) {
        if (rs.next()) {
          nbrHelpers = rs.getInt(1);
        }
      }
    } catch (SQLException se) {
      throw new FatalException(se.getMessage(), se);
    }
    return nbrHelpers;
  }
}
