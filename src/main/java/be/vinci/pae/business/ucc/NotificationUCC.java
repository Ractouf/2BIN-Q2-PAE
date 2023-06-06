package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.NotificationDTO;
import be.vinci.pae.business.dto.NotificationUserDTO;
import be.vinci.pae.business.dto.UserDTO;
import java.util.List;

/**
 * This is the interface of NotificationUCCImpl.
 */
public interface NotificationUCC {

  /**
   * Gets a notification by id.
   *
   * @param id the id of the notification
   * @return the notification
   */
  NotificationDTO getById(int id);

  /**
   * Gets one user notification.
   *
   * @param idUser         of the user notification
   * @param idNotification of the user notification
   * @return the user notification
   */
  NotificationUserDTO getOne(int idUser, int idNotification);

  /**
   * Creates a new notification in database.
   *
   * @param notification to created
   * @return the created notification
   */
  NotificationDTO createNotification(NotificationDTO notification);

  /**
   * Notify one user.
   *
   * @param notification the notification
   * @param user         to be notified
   */
  void notifyUser(NotificationDTO notification, UserDTO user);

  /**
   * Notifies all the admins.
   *
   * @param notification the notification
   */
  void notifyAdmins(NotificationDTO notification);

  /**
   * Notifies all the managers.
   *
   * @param notification the notification
   */
  void notifyManagers(NotificationDTO notification);

  /**
   * Notifies all the managers if an object is expired.
   */
  void sendNotificationsForExpiredObjects();

  /**
   * Gets all the notifications of a user.
   *
   * @param id the id of the user
   * @return A list of all the user notifications
   */
  List<NotificationUserDTO> getUserNotificationsById(int id);

  /**
   * Updates one notification user.
   *
   * @param notificationUser to be updated
   * @return the updated notification user
   */
  NotificationUserDTO updateOne(NotificationUserDTO notificationUser);
}
