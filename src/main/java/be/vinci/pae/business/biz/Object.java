package be.vinci.pae.business.biz;

import be.vinci.pae.business.dto.ObjectDTO;

/**
 * This is the interface of ObjectImpl.
 */
public interface Object extends ObjectDTO {

  /**
   * Changes the state of the object.
   *
   * @param status the new state of the object
   */
  void setStatus(Status status);

  /**
   * Increment the version of the object.
   */
  void incrementVersion();
}
