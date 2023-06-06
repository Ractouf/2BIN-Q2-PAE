package be.vinci.pae.dal.dao;

import be.vinci.pae.business.Factory;
import be.vinci.pae.business.biz.NotificationUser;
import be.vinci.pae.business.dto.NotificationDTO;
import be.vinci.pae.business.dto.NotificationUserDTO;
import be.vinci.pae.business.dto.ObjectDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.dal.DALBackend;
import be.vinci.pae.exceptions.FatalException;
import be.vinci.pae.exceptions.OptimisticLockException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the implementation of NotificationDAO.
 */
public class NotificationDAOImpl extends GenericDAOImpl<NotificationDTO> implements
    NotificationDAO {

  @Inject
  private UserDAO myUserDAO;
  @Inject
  private ObjectDAO myObjectDAO;
  @Inject
  private DALBackend myDALBackend;
  @Inject
  private Factory myFactory;

  @Override
  public NotificationDTO getById(int id) {
    String sql = "SELECT no.id_notification, no.text_notification, no.fk_concerned_object, "
        + "no.version "
        + "FROM projet_pae.notifications no "
        + "WHERE no.id_notification = ?";

    try (PreparedStatement getById = myDALBackend.getPrepareStatement(sql)) {
      getById.setInt(1, id);

      try (ResultSet rs = getById.executeQuery()) {
        if (rs.next()) {
          return setNotification(rs);
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }

    return null;
  }

  @Override
  public NotificationUserDTO getOne(int idUser, int idNotification) {
    String sql = "SELECT no.id_notification, no.text_notification, no.fk_concerned_object, "
        + "no.version AS notification_version, "
        + "nu.is_read, nu.fk_concerned_user, nu.fk_notification, "
        + "nu.version AS notification_user_version "
        + "FROM projet_pae.notifications no, projet_pae.notifications_users nu "
        + "WHERE no.id_notification = nu.fk_notification AND nu.fk_concerned_user = ? "
        + "AND nu.fk_notification = ?";

    try (PreparedStatement psGetOne = myDALBackend.getPrepareStatement(sql)) {
      psGetOne.setInt(1, idUser);
      psGetOne.setInt(2, idNotification);

      try (ResultSet rs = psGetOne.executeQuery()) {
        if (rs.next()) {
          return setNotificationUser(rs);
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }

    return null;
  }

  @Override
  public NotificationDTO createOne(NotificationDTO notification) {
    String sql = "INSERT INTO projet_pae.notifications "
        + "(text_notification, fk_concerned_object, version) VALUES "
        + "(?, ?, 1) RETURNING id_notification";

    try (PreparedStatement createOne = myDALBackend.getPrepareStatement(sql)) {
      createOne.setString(1, notification.getTextNotification());
      createOne.setInt(2, notification.getFkConcernedObject().getId());

      try (ResultSet rs = createOne.executeQuery()) {
        if (rs.next()) {
          return getById(rs.getInt(1));
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }

    return null;
  }

  @Override
  public void notifyUser(NotificationDTO notification, UserDTO user) {
    String sql = "INSERT INTO projet_pae.notifications_users "
        + "(is_read, fk_concerned_user, fk_notification, version) VALUES "
        + "(false, ?, ?, 1)";

    try (PreparedStatement notifyUser = myDALBackend.getPrepareStatement(sql)) {
      notifyUser.setInt(1, user.getId());
      notifyUser.setInt(2, notification.getId());

      notifyUser.execute();
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
  }

  @Override
  public void notifyAdmins(NotificationDTO notification) {
    String sql = "INSERT INTO projet_pae.notifications_users "
        + "(is_read, fk_concerned_user, fk_notification, version) VALUES "
        + "(false, ?, ?, 1)";

    List<UserDTO> admins = myUserDAO.getAdmins();

    for (UserDTO admin : admins) {
      try (PreparedStatement notifyAdmins = myDALBackend.getPrepareStatement(sql)) {
        notifyAdmins.setInt(1, admin.getId());
        notifyAdmins.setInt(2, notification.getId());

        notifyAdmins.execute();
      } catch (SQLException e) {
        throw new FatalException(e.getMessage(), e);
      }
    }
  }

  @Override
  public void notifyManagers(NotificationDTO notification) {
    String sql = "INSERT INTO projet_pae.notifications_users "
        + "(is_read, fk_concerned_user, fk_notification, version) VALUES "
        + "(false, ?, ?, 1)";

    List<UserDTO> managers = myUserDAO.getManagers();

    for (UserDTO manager : managers) {
      try (PreparedStatement notifyManagers = myDALBackend.getPrepareStatement(sql)) {
        notifyManagers.setInt(1, manager.getId());
        notifyManagers.setInt(2, notification.getId());

        notifyManagers.execute();
      } catch (SQLException e) {
        throw new FatalException(e.getMessage(), e);
      }
    }
  }

  @Override
  public List<NotificationUserDTO> getUserNotificationsById(int id) {
    List<NotificationUserDTO> userNotifications = new ArrayList<>();
    String sql = "SELECT no.id_notification, no.text_notification, no.fk_concerned_object, "
        + "no.version AS notification_version, "
        + "nu.is_read, nu.fk_concerned_user, nu.fk_notification, "
        + "nu.version AS notification_user_version "
        + "FROM projet_pae.notifications no, projet_pae.notifications_users nu "
        + "WHERE nu.fk_notification = no.id_notification AND nu.fk_concerned_user = ?";

    try (PreparedStatement psUserNotifications = myDALBackend.getPrepareStatement(sql)) {
      psUserNotifications.setInt(1, id);
      try (ResultSet rs = psUserNotifications.executeQuery()) {
        while (rs.next()) {
          userNotifications.add(setNotificationUser(rs));
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
    return userNotifications;
  }

  @Override
  public NotificationUserDTO updateOne(NotificationUserDTO notificationUser) {
    NotificationUser notificationUserCast = (NotificationUser) notificationUser;

    String sql = "UPDATE projet_pae.notifications_users SET is_read = ?, version = ? "
        + "WHERE version = ? AND fk_concerned_user = ? AND fk_notification = ?";

    try (PreparedStatement psUpdateOne = myDALBackend.getPrepareStatement(sql)) {
      psUpdateOne.setBoolean(1, notificationUserCast.isRead());
      psUpdateOne.setInt(3, notificationUserCast.getVersion());
      notificationUserCast.incrementVersion();
      psUpdateOne.setInt(2, notificationUserCast.getVersion());
      psUpdateOne.setInt(4, notificationUserCast.getFkConcernedUser().getId());
      psUpdateOne.setInt(5, notificationUserCast.getFkNotification().getId());

      int updatedRows = psUpdateOne.executeUpdate();

      if (updatedRows == 0) {
        throw new OptimisticLockException("Object was updated or deleted by another transaction");
      }
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
    return notificationUser;
  }

  private NotificationDTO setNotification(ResultSet rs) {
    NotificationDTO notification = myFactory.getNotification();

    try {
      ObjectDTO object = myObjectDAO.getById(rs.getInt("fk_concerned_object"));

      notification.setId(rs.getInt("id_notification"));
      notification.setTextNotification(rs.getString("text_notification"));
      notification.setFkConcernedObject(object);
      notification.setVersion(rs.getInt("version"));

      return notification;
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
  }

  private NotificationUserDTO setNotificationUser(ResultSet rs) {
    NotificationDTO notification = myFactory.getNotification();
    NotificationUserDTO notificationUser = myFactory.getNotificationUser();

    try {
      ObjectDTO object = myObjectDAO.getById(rs.getInt("fk_concerned_object"));

      notification.setId(rs.getInt("id_notification"));
      notification.setTextNotification(rs.getString("text_notification"));
      notification.setFkConcernedObject(object);
      notification.setVersion(rs.getInt("notification_version"));

      UserDTO user = myUserDAO.getById(rs.getInt("fk_concerned_user"));

      notificationUser.setRead(rs.getBoolean("is_read"));
      notificationUser.setFkConcernedUser(user);
      notificationUser.setFkNotification(notification);
      notificationUser.setVersion(rs.getInt("notification_user_version"));

      return notificationUser;
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
  }
}
