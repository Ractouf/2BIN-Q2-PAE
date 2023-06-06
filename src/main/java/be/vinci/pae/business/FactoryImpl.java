package be.vinci.pae.business;

import be.vinci.pae.business.biz.AvailabilityImpl;
import be.vinci.pae.business.biz.NotificationImpl;
import be.vinci.pae.business.biz.NotificationUserImpl;
import be.vinci.pae.business.biz.ObjectImpl;
import be.vinci.pae.business.biz.ObjectTypeImpl;
import be.vinci.pae.business.biz.UserImpl;
import be.vinci.pae.business.dto.AvailabilityDTO;
import be.vinci.pae.business.dto.NotificationDTO;
import be.vinci.pae.business.dto.NotificationUserDTO;
import be.vinci.pae.business.dto.ObjectDTO;
import be.vinci.pae.business.dto.ObjectTypeDTO;
import be.vinci.pae.business.dto.UserDTO;

/**
 * This class is used to obtain the implementation of the interfaces.
 */
public class FactoryImpl implements Factory {

  @Override
  public UserDTO getUser() {
    return new UserImpl();
  }

  @Override
  public ObjectDTO getObject() {
    return new ObjectImpl();
  }

  @Override
  public ObjectTypeDTO getObjectType() {
    return new ObjectTypeImpl();
  }

  @Override
  public AvailabilityDTO getAvailability() {
    return new AvailabilityImpl();
  }

  @Override
  public NotificationDTO getNotification() {
    return new NotificationImpl();
  }

  @Override
  public NotificationUserDTO getNotificationUser() {
    return new NotificationUserImpl();
  }
}
