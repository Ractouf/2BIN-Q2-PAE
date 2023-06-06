package be.vinci.pae.business.biz;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.sql.Date;
import java.sql.Time;
import java.util.Objects;

/**
 * This is the implementation of Availability.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AvailabilityImpl implements Availability {

  private int id;
  private Date availabilityDate;
  private Time startingHour;
  private Time endingHour;

  @JsonIgnore
  private int version;

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public Date getAvailabilityDate() {
    return availabilityDate;
  }

  @Override
  public void setAvailabilityDate(Date availabilityDate) {
    this.availabilityDate = availabilityDate;
  }

  @Override
  public Time getStartingHour() {
    return startingHour;
  }

  @Override
  public void setStartingHour(Time startingHour) {
    this.startingHour = startingHour;
  }

  @Override
  public Time getEndingHour() {
    return endingHour;
  }

  @Override
  public void setEndingHour(Time endingHour) {
    this.endingHour = endingHour;
  }

  @Override
  public int getVersion() {
    return version;
  }

  @Override
  public void setVersion(int version) {
    this.version = version;
  }

  @Override
  public void incrementVersion() {
    this.version++;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AvailabilityImpl that = (AvailabilityImpl) o;
    return id == that.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
