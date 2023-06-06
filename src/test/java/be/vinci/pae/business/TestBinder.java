package be.vinci.pae.business;

import be.vinci.pae.business.biz.Availability;
import be.vinci.pae.business.biz.AvailabilityImpl;
import be.vinci.pae.business.biz.Notification;
import be.vinci.pae.business.biz.NotificationImpl;
import be.vinci.pae.business.biz.Object;
import be.vinci.pae.business.biz.ObjectImpl;
import be.vinci.pae.business.biz.ObjectType;
import be.vinci.pae.business.biz.ObjectTypeImpl;
import be.vinci.pae.business.biz.User;
import be.vinci.pae.business.biz.UserImpl;
import be.vinci.pae.business.ucc.AvailabilityUCC;
import be.vinci.pae.business.ucc.AvailabilityUCCImpl;
import be.vinci.pae.business.ucc.NotificationUCC;
import be.vinci.pae.business.ucc.NotificationUCCImpl;
import be.vinci.pae.business.ucc.ObjectTypeUCC;
import be.vinci.pae.business.ucc.ObjectTypeUCCImpl;
import be.vinci.pae.business.ucc.ObjectUCC;
import be.vinci.pae.business.ucc.ObjectUCCImpl;
import be.vinci.pae.business.ucc.UserUCC;
import be.vinci.pae.business.ucc.UserUCCImpl;
import be.vinci.pae.dal.DALServices;
import be.vinci.pae.dal.DALServicesImpl;
import be.vinci.pae.dal.dao.AvailabilityDAO;
import be.vinci.pae.dal.dao.AvailabilityDAOImpl;
import be.vinci.pae.dal.dao.NotificationDAO;
import be.vinci.pae.dal.dao.NotificationDAOImpl;
import be.vinci.pae.dal.dao.ObjectDAO;
import be.vinci.pae.dal.dao.ObjectDAOImpl;
import be.vinci.pae.dal.dao.ObjectTypeDAO;
import be.vinci.pae.dal.dao.ObjectTypeDAOImpl;
import be.vinci.pae.dal.dao.UserDAO;
import be.vinci.pae.dal.dao.UserDAOImpl;
import be.vinci.pae.utils.ApplicationBinder;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.mockito.Mockito;

/**
 * Binding via the redefinition of an ApplicationBinder.
 */
@Provider
public class TestBinder extends ApplicationBinder {

  @Override
  protected void configure() {
    bind(FactoryImpl.class).to(Factory.class).in(Singleton.class);

    bind(Mockito.mock(DALServicesImpl.class)).to(DALServices.class);

    bind(UserUCCImpl.class).to(UserUCC.class).in(Singleton.class);
    bind(Mockito.mock(UserDAOImpl.class)).to(UserDAO.class);
    bind(Mockito.mock(UserImpl.class)).to(User.class);

    bind(ObjectUCCImpl.class).to(ObjectUCC.class).in(Singleton.class);
    bind(Mockito.mock(ObjectDAOImpl.class)).to(ObjectDAO.class);
    bind(Mockito.mock(ObjectImpl.class)).to(Object.class);

    bind(ObjectTypeUCCImpl.class).to(ObjectTypeUCC.class).in(Singleton.class);
    bind(Mockito.mock(ObjectTypeDAOImpl.class)).to(ObjectTypeDAO.class);
    bind(Mockito.mock(ObjectTypeImpl.class)).to(ObjectType.class);

    bind(AvailabilityUCCImpl.class).to(AvailabilityUCC.class).in(Singleton.class);
    bind(Mockito.mock(AvailabilityDAOImpl.class)).to(AvailabilityDAO.class);
    bind(Mockito.mock(AvailabilityImpl.class)).to(Availability.class);

    bind(NotificationUCCImpl.class).to(NotificationUCC.class).in(Singleton.class);
    bind(Mockito.mock(NotificationDAOImpl.class)).to(NotificationDAO.class);
    bind(Mockito.mock(NotificationImpl.class)).to(Notification.class);
  }
}
