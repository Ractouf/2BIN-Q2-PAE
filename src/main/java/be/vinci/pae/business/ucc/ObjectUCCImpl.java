package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.ObjectDTO;
import be.vinci.pae.business.dto.ObjectDTO.Status;
import be.vinci.pae.dal.DALServices;
import be.vinci.pae.dal.dao.ObjectDAO;
import be.vinci.pae.dal.transactions.Transaction;
import be.vinci.pae.exceptions.BizExceptionNotFound;
import be.vinci.pae.exceptions.FatalException;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * This is the implementation of ObjectUCC.
 */
public class ObjectUCCImpl implements ObjectUCC {

  @Inject
  private ObjectDAO myObjectDAO;

  @Inject
  private DALServices dalServices;

  @Override
  public List<ObjectDTO> getListObjects() {
    try (Transaction transaction = new Transaction(dalServices)) {
      List<ObjectDTO> objects = myObjectDAO.getAll();

      transaction.commit();

      return objects;
    }
  }

  @Override
  public List<ObjectDTO> getListObjectByUser(int id) {
    try (Transaction transaction = new Transaction(dalServices)) {
      List<ObjectDTO> objects = myObjectDAO.getListObjectByUser(id);

      transaction.commit();

      return objects;
    }
  }

  @Override
  public ObjectDTO getById(int id) {
    try (Transaction transaction = new Transaction(dalServices)) {
      ObjectDTO object = myObjectDAO.getById(id);

      transaction.commit();

      return object;
    }
  }

  @Override
  public ObjectDTO refuseObject(int id, String refusalReason) {
    try (Transaction transaction = new Transaction(dalServices)) {
      ObjectDTO object = myObjectDAO.getById(id);

      if (!object.updateStatus(Status.REFUSED)) {
        throw new FatalException("état précédent incorrect");
      }

      object.setReasonForRefusal(refusalReason);

      ObjectDTO updatedObject = myObjectDAO.updateOne(object);

      transaction.commit();

      return updatedObject;
    }
  }

  @Override
  public ObjectDTO updateObjectStatus(int id, Status newStatus) {
    try (Transaction transaction = new Transaction(dalServices)) {
      ObjectDTO object = myObjectDAO.getById(id);

      if (object == null) {
        throw new BizExceptionNotFound("Objet introuvable.");
      }

      if (!object.updateStatus(newStatus)) {
        throw new FatalException("état précédent incorrect");
      }

      ObjectDTO updatedObject = myObjectDAO.updateOne(object);

      transaction.commit();

      return updatedObject;
    }
  }

  @Override
  public List<ObjectDTO> listOfObjectsProposed() {
    try (Transaction transaction = new Transaction(dalServices)) {
      List<ObjectDTO> objects = myObjectDAO.listOfObjectsProposed();

      transaction.commit();

      return objects;
    }
  }

  @Override
  public ObjectDTO editObject(ObjectDTO object) {
    try (Transaction transaction = new Transaction(dalServices)) {
      ObjectDTO updatedObject = myObjectDAO.updateOne(object);

      transaction.commit();

      return updatedObject;
    }
  }

  @Override
  public ObjectDTO proposeObject(ObjectDTO object) {
    try (Transaction transaction = new Transaction(dalServices)) {
      ObjectDTO createdObject = myObjectDAO.createOne(object);

      transaction.commit();

      return createdObject;
    }
  }

  @Override
  public Map<String, Integer> getNbrObjectsByStatus() {
    try (Transaction transaction = new Transaction(dalServices)) {
      Map<String, Integer> map = myObjectDAO.getNbrObjectsByStatus();

      transaction.commit();

      return map;
    }
  }

  @Override
  public Map<Integer, Map<Integer, Integer>> getNbrObjectsProposedByMonth() {
    try (Transaction transaction = new Transaction(dalServices)) {
      Map<Integer, Map<Integer, Integer>> map = myObjectDAO.getNbrObjectsProposedByMonth();

      transaction.commit();

      return map;
    }
  }

  @Override
  public List<ObjectDTO> getHomePageObjects() {
    try (Transaction transaction = new Transaction(dalServices)) {
      List<ObjectDTO> homePageObjects = myObjectDAO.getHomePageObjects();

      transaction.commit();

      return homePageObjects;
    }
  }
}
