package be.vinci.pae.business.dto;

import be.vinci.pae.business.biz.NotificationImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * This is the interface of NotificationImpl.
 */
@JsonDeserialize(as = NotificationImpl.class)
public interface NotificationDTO {

  /**
   * Gets the id of the notification.
   *
   * @return the id of the notification
   */
  int getId();

  /**
   * Sets the id of the notification.
   *
   * @param id the id of the notification
   */
  void setId(int id);

  /**
   * Gets the text of the notification.
   *
   * @return the text of the notification
   */
  String getTextNotification();

  /**
   * Sets the text of the notification.
   *
   * @param textNotification the text of the notification
   */
  void setTextNotification(String textNotification);

  /**
   * Gets the concerned object of the notification.
   *
   * @return the concerned object of the notification
   */
  ObjectDTO getFkConcernedObject();

  /**
   * Sets the concerned object of the notification.
   *
   * @param fkConcernedObject the concerned object of the notification
   */
  void setFkConcernedObject(ObjectDTO fkConcernedObject);

  /**
   * Gets the version of the notification.
   *
   * @return the version of the notification
   */
  int getVersion();

  /**
   * Sets the version of the notification.
   *
   * @param version of the notification
   */
  void setVersion(int version);
}
