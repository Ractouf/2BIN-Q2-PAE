package be.vinci.pae.business.dto;

import be.vinci.pae.business.biz.NotificationUserImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * This is the interface of NotificationUserImpl.
 */
@JsonDeserialize(as = NotificationUserImpl.class)
public interface NotificationUserDTO {

  /**
   * Checks if the notification user is read.
   *
   * @return true if notification user is read false if not
   */
  boolean isRead();

  /**
   * Changes the "read" attribute of notification user.
   *
   * @param read the value to set to.
   */
  void setRead(boolean read);

  /**
   * Gets the concerned user of the notification user.
   *
   * @return the concerned user of the notification user
   */
  UserDTO getFkConcernedUser();

  /**
   * Sets the concerned user of the notification user.
   *
   * @param fkConcernedUser the concerned user of the notification user
   */
  void setFkConcernedUser(UserDTO fkConcernedUser);

  /**
   * Gets the notification of the notification user.
   *
   * @return the notification of the notification user
   */
  NotificationDTO getFkNotification();

  /**
   * Sets the notification of the notification user.
   *
   * @param fkNotification the notification of the notification user
   */
  void setFkNotification(NotificationDTO fkNotification);

  /**
   * Gets the version of the notification user.
   *
   * @return the version of the notification user
   */
  int getVersion();

  /**
   * Sets the version of the notification user.
   *
   * @param version of the notification user
   */
  void setVersion(int version);
}
