package be.vinci.pae.business.dto;

import be.vinci.pae.business.biz.AvailabilityImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.sql.Date;
import java.sql.Time;

/**
 * This is the interface of AvailabilityImpl.
 */
@JsonDeserialize(as = AvailabilityImpl.class)
public interface AvailabilityDTO {

  /**
   * Gets the id of the availability.
   *
   * @return the id of the availability
   */
  int getId();

  /**
   * Sets the id of the availability.
   *
   * @param id the id of the availability
   */
  void setId(int id);

  /**
   * Gets the date of the availability.
   *
   * @return the date of the availability
   */
  Date getAvailabilityDate();

  /**
   * Sets the date of the availability.
   *
   * @param availabilityDate the date of the availability
   */
  void setAvailabilityDate(Date availabilityDate);

  /**
   * Gets the starting hour of the availability.
   *
   * @return the starting hour of the availability
   */
  Time getStartingHour();

  /**
   * Sets the starting hour of the availability.
   *
   * @param startingHour the starting hour of the availability
   */
  void setStartingHour(Time startingHour);

  /**
   * Gets the ending hour of the availability.
   *
   * @return the ending hour of the availability
   */
  Time getEndingHour();

  /**
   * Sets the ending hour of the availability.
   *
   * @param endingHour the ending hour of the availability
   */
  void setEndingHour(Time endingHour);

  /**
   * Gets the version of the availability.
   *
   * @return the version of the availability
   */
  int getVersion();

  /**
   * Sets the version of the availability.
   *
   * @param version of the availability
   */
  void setVersion(int version);
}
