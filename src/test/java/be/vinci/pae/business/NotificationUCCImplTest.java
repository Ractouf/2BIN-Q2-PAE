package be.vinci.pae.business;

import static org.junit.jupiter.api.Assertions.assertEquals;

import be.vinci.pae.business.dto.NotificationDTO;
import be.vinci.pae.business.dto.NotificationUserDTO;
import be.vinci.pae.business.dto.ObjectDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.ucc.NotificationUCC;
import be.vinci.pae.business.ucc.NotificationUCCImpl;
import be.vinci.pae.dal.DALServices;
import be.vinci.pae.dal.dao.NotificationDAO;
import be.vinci.pae.dal.dao.ObjectDAO;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;

@TestInstance(Lifecycle.PER_CLASS)
class NotificationUCCImplTest extends TestBinder {

  private Factory factory;
  private NotificationUCC notificationUCC;
  private NotificationDAO notificationDAO;
  private DALServices dalServices;

  private ObjectDAO objectDAO;
  private NotificationDTO notification;
  private NotificationUserDTO notificationUser;

  @BeforeAll
  void setUp() {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new TestBinder());
    this.factory = locator.getService(Factory.class);
    this.notificationUCC = locator.getService(NotificationUCC.class);
    this.notificationDAO = locator.getService(NotificationDAO.class);
    this.objectDAO = locator.getService(ObjectDAO.class);
    this.dalServices = locator.getService(DALServices.class);
  }

  @BeforeEach
  void init() {
    Mockito.reset(notificationDAO);
    Mockito.reset(objectDAO);
    Mockito.reset(dalServices);

    notification = factory.getNotification();
    notification.setId(1);

    notificationUser = factory.getNotificationUser();
  }

  /**
   * Test methods for {@link NotificationUCCImpl#getById(int)}}.
   */

  @Test
  @DisplayName("should return the notification related to given id")
  void getById() {
    Mockito.when(notificationDAO.getById(1)).thenReturn(notification);
    assertEquals(notification, notificationUCC.getById(1));
  }

  /**
   * Test methods for {@link NotificationUCCImpl#getOne(int, int)}}.
   */

  @Test
  @DisplayName("should return the user notification related to given id and notification")
  void getOne() {
    Mockito.when(notificationDAO.getOne(1, 1)).thenReturn(notificationUser);
    assertEquals(notificationUser, notificationUCC.getOne(1, 1));
  }

  /**
   * Test methods for {@link NotificationUCCImpl#createNotification(NotificationDTO)}}.
   */

  @Test
  @DisplayName("should return the just created notification")
  void createNotification() {
    Mockito.when(notificationDAO.createOne(notification)).thenReturn(notification);
    assertEquals(notification, notificationUCC.createNotification(notification));
  }

  /**
   * Test methods for {@link NotificationUCCImpl#notifyUser(NotificationDTO, UserDTO)}.
   */

  @Test
  @DisplayName("should notify one user and commit transaction")
  public void notifyUser() {
    NotificationDTO notification = factory.getNotification();
    UserDTO user = factory.getUser();

    notificationUCC.notifyUser(notification, user);

    Mockito.verify(notificationDAO, Mockito.times(1)).notifyUser(notification, user);
    Mockito.verify(dalServices, Mockito.times(1)).commitTransaction();
  }

  /**
   * Test methods for {@link NotificationUCCImpl#notifyAdmins(NotificationDTO)}
   * (NotificationDTO)}}.
   */

  @Test
  @DisplayName("should notify all admins and commit transaction")
  public void notifyAdmins() {
    NotificationDTO notification = factory.getNotification();

    notificationUCC.notifyAdmins(notification);

    Mockito.verify(notificationDAO, Mockito.times(1)).notifyAdmins(notification);
    Mockito.verify(dalServices, Mockito.times(1)).commitTransaction();
  }

  /**
   * Test methods for {@link NotificationUCCImpl#notifyManagers(NotificationDTO)}.
   */

  @Test
  @DisplayName("should notify all managers and commit transaction")
  public void notifyManagers() {
    NotificationDTO notification = factory.getNotification();

    notificationUCC.notifyManagers(notification);

    Mockito.verify(notificationDAO, Mockito.times(1))
        .notifyManagers(notification);
    Mockito.verify(dalServices, Mockito.times(1)).commitTransaction();
  }

  /**
   * Test methods for {@link NotificationUCCImpl#sendNotificationsForExpiredObjects()}.
   */

  @Test
  @DisplayName("should notify managers once")
  void sendNotificationsForExpiredObjects() {
    ObjectDTO object1 = factory.getObject();
    ObjectDTO object2 = factory.getObject();

    object1.setId(1);
    object1.setStoreDepositDate(Date.valueOf("2020-01-01"));

    LocalDate today = LocalDate.now();
    object2.setId(2);
    object2.setStoreDepositDate(Date.valueOf(today));

    List<ObjectDTO> objects = new ArrayList<>();
    objects.add(object1);
    objects.add(object2);

    Mockito.when(objectDAO.getObjectsInStore()).thenReturn(objects);

    NotificationDTO notification = factory.getNotification();
    notification.setTextNotification("L'objet 1 est expiré");
    notification.setFkConcernedObject(object1);

    Mockito.when(notificationDAO.createOne(notification)).thenReturn(notification);

    notificationUCC.sendNotificationsForExpiredObjects();

    Mockito.verify(notificationDAO, Mockito.times(1)).notifyManagers(notification);
    Mockito.verify(dalServices, Mockito.times(3)).commitTransaction();
  }

  /**
   * Test methods for {@link NotificationUCCImpl#getUserNotificationsById(int)}
   * (NotificationDTO)}}.
   */

  @Test
  @DisplayName("should return the list of notifications for the user of this id")
  void getUserNotificationsById() {
    List<NotificationUserDTO> listOfNotifications = new ArrayList<>();
    listOfNotifications.add(notificationUser);

    Mockito.when(notificationDAO.getUserNotificationsById(1)).thenReturn(listOfNotifications);

    assertEquals(listOfNotifications, notificationUCC.getUserNotificationsById(1));
  }

  /**
   * Test methods for {@link NotificationUCCImpl#updateOne(NotificationUserDTO)}}.
   */

  @Test
  @DisplayName("should update and return a user notification")
  void updateOne() {
    Mockito.when(notificationDAO.updateOne(notificationUser)).thenReturn(notificationUser);
    assertEquals(notificationUser, notificationUCC.updateOne(notificationUser));
  }
}
