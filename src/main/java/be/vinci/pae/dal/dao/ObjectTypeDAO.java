package be.vinci.pae.dal.dao;

import be.vinci.pae.business.dto.ObjectTypeDTO;

/**
 * This is the interface of ObjectTypeDAOImpl.
 */
public interface ObjectTypeDAO extends GenericDAO<ObjectTypeDTO> {

  /**
   * Gets the type related to the given String.
   *
   * @param type name in String format
   * @return the ObjectType
   */
  ObjectTypeDTO getObjectTypeByName(String type);
}
