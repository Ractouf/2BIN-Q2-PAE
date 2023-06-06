package be.vinci.pae.business.dto;

import be.vinci.pae.business.biz.ObjectTypeImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * This is the interface of ObjectTypeImpl.
 */
@JsonDeserialize(as = ObjectTypeImpl.class)
public interface ObjectTypeDTO {

  /**
   * Gets the id of the object type.
   *
   * @return the id of the object type
   */
  int getId();

  /**
   * Sets the id of the object type.
   *
   * @param id the id of the object type
   */
  void setId(int id);

  /**
   * Gets the type name of the object type.
   *
   * @return the type name of the object type
   */
  String getTypeName();

  /**
   * Sets the type name of the object type.
   *
   * @param typeName the type name of the object type
   */
  void setTypeName(String typeName);

  /**
   * Gets the version of the object type.
   *
   * @return the version of the object type
   */
  int getVersion();

  /**
   * Sets the version of the object type.
   *
   * @param version of the object type
   */
  void setVersion(int version);
}
