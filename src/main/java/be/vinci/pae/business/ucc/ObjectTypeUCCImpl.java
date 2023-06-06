package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.ObjectTypeDTO;
import be.vinci.pae.dal.DALServices;
import be.vinci.pae.dal.dao.ObjectTypeDAO;
import be.vinci.pae.dal.transactions.Transaction;
import jakarta.inject.Inject;
import java.util.List;

/**
 * This is the implementation of ObjectTypeUCC.
 */
public class ObjectTypeUCCImpl implements ObjectTypeUCC {

  @Inject
  private ObjectTypeDAO myObjectTypeDAO;

  @Inject
  private DALServices dalServices;

  @Override
  public List<ObjectTypeDTO> getListObjectTypes() {
    try (Transaction transaction = new Transaction(dalServices)) {
      List<ObjectTypeDTO> objects = myObjectTypeDAO.getAll();

      transaction.commit();

      return objects;
    }
  }

  @Override
  public ObjectTypeDTO getObjectTypeByName(String type) {
    try (Transaction transaction = new Transaction(dalServices)) {
      ObjectTypeDTO objectType = myObjectTypeDAO.getObjectTypeByName(type);

      transaction.commit();

      return objectType;
    }
  }
}
