package be.vinci.pae.dal.dao;

import be.vinci.pae.business.dto.AvailabilityDTO;
import be.vinci.pae.dal.DALBackend;
import be.vinci.pae.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;

/**
 * This is the implementation of AvailabilityDAO.
 */
public class AvailabilityDAOImpl extends GenericDAOImpl<AvailabilityDTO>
    implements AvailabilityDAO {

  @Inject
  private DALBackend myDALBackend;

  @Override
  public AvailabilityDTO getAvailabilityFromString(String availability) {
    int availabilityYear = Integer.parseInt(availability.substring(0, 4));
    int availabilityMonth = Integer.parseInt(availability.substring(5, 7));
    int availabilityDay = Integer.parseInt(availability.substring(8, 10));

    String time = availability.substring(availability.length() - 5) + ":00";
    Time startingHour = Time.valueOf(time);

    Date availabilityDate = Date.valueOf(
        LocalDate.of(availabilityYear, availabilityMonth, availabilityDay));

    String sql =
        "SELECT av.id_availability, av.availability_date, "
            + "av.starting_hour, av.ending_hour, av.version "
            + "FROM projet_pae.availabilities av "
            + "WHERE av.availability_date = ? AND av.starting_hour = ?";

    try (PreparedStatement ps = myDALBackend.getPrepareStatement(sql)) {
      ps.setDate(1, availabilityDate);
      ps.setTime(2, startingHour);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return get(rs);
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }

    return null;
  }
}