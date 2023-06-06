package be.vinci.pae.business.ucc;

import be.vinci.pae.business.Factory;
import be.vinci.pae.business.dto.NotificationDTO;
import be.vinci.pae.business.dto.NotificationUserDTO;
import be.vinci.pae.business.dto.ObjectDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.dal.DALServices;
import be.vinci.pae.dal.dao.NotificationDAO;
import be.vinci.pae.dal.dao.ObjectDAO;
import be.vinci.pae.dal.transactions.Transaction;
import jakarta.inject.Inject;
import java.util.Date;
import java.util.List;

/**
 * This is the implementation of NotificationUCC.
 */
public class NotificationUCCImpl implements NotificationUCC {

  @Inject
  private NotificationDAO myNotificationDAO;
  @Inject
  private ObjectDAO myObjectDAO;
  @Inject
  private Factory factory;

  @Inject
  private DALServices dalServices;

  @Override
  public NotificationDTO getById(int id) {
    try (Transaction transaction = new Transaction(dalServices)) {
      NotificationDTO notification = myNotificationDAO.getById(id);

      transaction.commit();

      return notification;
    }
  }

  @Override
  public NotificationUserDTO getOne(int idUser, int idNotification) {
    try (Transaction transaction = new Transaction(dalServices)) {
      NotificationUserDTO notificationUser = myNotificationDAO.getOne(idUser, idNotification);

      transaction.commit();

      return notificationUser;
    }
  }

  @Override
  public NotificationDTO createNotification(NotificationDTO notification) {
    try (Transaction transaction = new Transaction(dalServices)) {
      NotificationDTO createdNotification = myNotificationDAO.createOne(notification);

      transaction.commit();

      return createdNotification;
    }
  }

  @Override
  public void notifyUser(NotificationDTO notification, UserDTO user) {
    try (Transaction transaction = new Transaction(dalServices)) {
      myNotificationDAO.notifyUser(notification, user);

      transaction.commit();
    }
  }

  @Override
  public void notifyAdmins(NotificationDTO notification) {
    try (Transaction transaction = new Transaction(dalServices)) {
      myNotificationDAO.notifyAdmins(notification);

      transaction.commit();
    }
  }

  @Override
  public void notifyManagers(NotificationDTO notification) {
    try (Transaction transaction = new Transaction(dalServices)) {
      myNotificationDAO.notifyManagers(notification);

      transaction.commit();
    }
  }

  @Override
  public void sendNotificationsForExpiredObjects() {
    List<ObjectDTO> objects;
    try (Transaction transaction = new Transaction(dalServices)) {
      objects = myObjectDAO.getObjectsInStore();

      transaction.commit();
    }

    for (ObjectDTO object : objects) {
      if (object.getStoreDepositDate().getTime() + 42L * 24 * 60 * 60 * 1000
          < new Date().getTime()) {
        NotificationDTO notification = factory.getNotification();
        notification.setTextNotification(
            "L'objet " + "\"" + object.getDescription() + "\"" + " est expiré.");
        notification.setFkConcernedObject(object);

        notification = createNotification(notification);

        notifyManagers(notification);
      }
    }
  }

  @Override
  public List<NotificationUserDTO> getUserNotificationsById(int id) {
    try (Transaction transaction = new Transaction(dalServices)) {
      List<NotificationUserDTO> userNotifications = myNotificationDAO.getUserNotificationsById(id);

      transaction.commit();

      return userNotifications;
    }
  }

  @Override
  public NotificationUserDTO updateOne(NotificationUserDTO notificationUser) {
    try (Transaction transaction = new Transaction(dalServices)) {

      notificationUser = myNotificationDAO.updateOne(notificationUser);
      transaction.commit();

      return notificationUser;
    }
  }
}
