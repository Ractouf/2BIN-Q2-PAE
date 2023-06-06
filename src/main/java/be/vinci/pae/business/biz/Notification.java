package be.vinci.pae.business.biz;

import be.vinci.pae.business.dto.NotificationDTO;

/**
 * This is the interface of NotificationImpl.
 */
public interface Notification extends NotificationDTO {

  /**
   * Increment the version of the notification.
   */
  void incrementVersion();
}
