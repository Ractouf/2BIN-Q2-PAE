package be.vinci.pae.business.biz;

import be.vinci.pae.business.dto.AvailabilityDTO;

/**
 * This is the interface of AvailabilityImpl.
 */
public interface Availability extends AvailabilityDTO {

  /**
   * Increment the version of the availability.
   */
  void incrementVersion();
}
