package be.vinci.pae.business.biz;

import be.vinci.pae.business.dto.NotificationUserDTO;

/**
 * This is the interface of NotificationUserImpl.
 */
public interface NotificationUser extends NotificationUserDTO {

  /**
   * Increment the version of the notification user.
   */
  void incrementVersion();
}
