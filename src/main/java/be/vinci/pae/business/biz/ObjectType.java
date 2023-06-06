package be.vinci.pae.business.biz;

import be.vinci.pae.business.dto.ObjectTypeDTO;

/**
 * This is the interface of ObjectTypeImpl.
 */
public interface ObjectType extends ObjectTypeDTO {

  /**
   * Increment the version of the object type.
   */
  void incrementVersion();
}
