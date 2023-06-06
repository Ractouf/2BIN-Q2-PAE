package be.vinci.pae.dal.dao;

import be.vinci.pae.business.dto.AvailabilityDTO;

/**
 * This is the interface of AvailabilityDAOImpl.
 */
public interface AvailabilityDAO extends GenericDAO<AvailabilityDTO> {

  /**
   * Gets the Availability associated with the given String.
   *
   * @param availability to get
   * @return the Availability
   */
  AvailabilityDTO getAvailabilityFromString(String availability);
}
