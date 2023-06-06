package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.ObjectTypeDTO;
import java.util.List;

/**
 * This is the interface of ObjectTypeUCCImpl.
 */
public interface ObjectTypeUCC {

  /**
   * Gets all the object types.
   *
   * @return a list of all the object types
   */
  List<ObjectTypeDTO> getListObjectTypes();

  /**
   * Gets the type related to the given String.
   *
   * @param type name in String format
   * @return the ObjectType
   */
  ObjectTypeDTO getObjectTypeByName(String type);
}
