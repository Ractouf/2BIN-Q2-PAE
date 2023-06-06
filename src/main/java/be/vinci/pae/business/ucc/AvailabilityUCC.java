package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.AvailabilityDTO;
import java.util.List;

/**
 * This is the interface of AvailabilityUCCImpl.
 */
public interface AvailabilityUCC {

  /**
   * Gets all the availabilities.
   *
   * @return list of all availabilities
   */
  List<AvailabilityDTO> getAvailabilities();

  /**
   * Gets the Availability associated with the given String.
   *
   * @param availability to get
   * @return the Availability
   */
  AvailabilityDTO getAvailabilityFromString(String availability);

  /**
   * Create one availability.
   *
   * @param availability the availability to be created
   * @return the just created availability
   */
  AvailabilityDTO createOneAvailability(AvailabilityDTO availability);
}
