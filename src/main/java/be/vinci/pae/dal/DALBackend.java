package be.vinci.pae.dal;

import java.sql.PreparedStatement;

/**
 * This interface is used to generate a PrepareStatement based on the given sql in a String format.
 */
public interface DALBackend {

  /**
   * Generate a PrepareStatement based on the given sql in a String format.
   *
   * @param sql the sql in a String format
   * @return PrepareStatement of the String
   */
  PreparedStatement getPrepareStatement(String sql);
}
