package be.vinci.pae.utils;

import be.vinci.pae.business.Factory;
import be.vinci.pae.business.FactoryImpl;
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
import be.vinci.pae.dal.DALBackend;
import be.vinci.pae.dal.DALServices;
import be.vinci.pae.dal.DALServicesImpl;
import be.vinci.pae.dal.dao.AvailabilityDAO;
import be.vinci.pae.dal.dao.AvailabilityDAOImpl;
import be.vinci.pae.dal.dao.GenericDAO;
import be.vinci.pae.dal.dao.GenericDAOImpl;
import be.vinci.pae.dal.dao.NotificationDAO;
import be.vinci.pae.dal.dao.NotificationDAOImpl;
import be.vinci.pae.dal.dao.ObjectDAO;
import be.vinci.pae.dal.dao.ObjectDAOImpl;
import be.vinci.pae.dal.dao.ObjectTypeDAO;
import be.vinci.pae.dal.dao.ObjectTypeDAOImpl;
import be.vinci.pae.dal.dao.UserDAO;
import be.vinci.pae.dal.dao.UserDAOImpl;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

/**
 * Binding via the redefinition of an AbstractBinder.
 */
@Provider
public class ApplicationBinder extends AbstractBinder {

  @Override
  protected void configure() {
    bind(FactoryImpl.class).to(Factory.class).in(Singleton.class);

    bind(AvailabilityUCCImpl.class).to(AvailabilityUCC.class).in(Singleton.class);
    bind(NotificationUCCImpl.class).to(NotificationUCC.class).in(Singleton.class);
    bind(ObjectTypeUCCImpl.class).to(ObjectTypeUCC.class).in(Singleton.class);
    bind(ObjectUCCImpl.class).to(ObjectUCC.class).in(Singleton.class);
    bind(UserUCCImpl.class).to(UserUCC.class).in(Singleton.class);

    bind(AvailabilityDAOImpl.class).to(AvailabilityDAO.class).in(Singleton.class);
    bind(GenericDAOImpl.class).to(GenericDAO.class).in(Singleton.class);
    bind(NotificationDAOImpl.class).to(NotificationDAO.class).in(Singleton.class);
    bind(ObjectDAOImpl.class).to(ObjectDAO.class).in(Singleton.class);
    bind(ObjectTypeDAOImpl.class).to(ObjectTypeDAO.class).in(Singleton.class);
    bind(UserDAOImpl.class).to(UserDAO.class).in(Singleton.class);

    bind(DALServicesImpl.class).to(DALServices.class).to(DALBackend.class).in(Singleton.class);
  }
}
