package be.vinci.pae.business;

import be.vinci.pae.business.dto.AvailabilityDTO;
import be.vinci.pae.business.dto.NotificationDTO;
import be.vinci.pae.business.dto.NotificationUserDTO;
import be.vinci.pae.business.dto.ObjectDTO;
import be.vinci.pae.business.dto.ObjectTypeDTO;
import be.vinci.pae.business.dto.UserDTO;

/**
 * This is the interface of FactoryImpl.
 */
public interface Factory {

  /**
   * Creates a new UserImpl.
   *
   * @return a new UserImpl
   */
  UserDTO getUser();

  /**
   * Creates a new ObjectImpl.
   *
   * @return a new ObjectImpl
   */
  ObjectDTO getObject();

  /**
   * Creates a new ObjectTypeImpl.
   *
   * @return a new ObjectTypeImpl
   */
  ObjectTypeDTO getObjectType();

  /**
   * Creates a new AvailabilityImpl.
   *
   * @return a new AvailabilityImpl
   */
  AvailabilityDTO getAvailability();

  /**
   * Creates a new NotificationImpl.
   *
   * @return a new NotificationImpl
   */
  NotificationDTO getNotification();

  /**
   * Creates a new NotificationUserImpl.
   *
   * @return a new NotificationUserImpl
   */
  NotificationUserDTO getNotificationUser();
}
