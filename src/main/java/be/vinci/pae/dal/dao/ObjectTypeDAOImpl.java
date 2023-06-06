package be.vinci.pae.dal.dao;

import be.vinci.pae.business.dto.ObjectTypeDTO;
import be.vinci.pae.dal.DALBackend;
import be.vinci.pae.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This is the implementation of ObjectTypeDAO.
 */
public class ObjectTypeDAOImpl extends GenericDAOImpl<ObjectTypeDTO> implements ObjectTypeDAO {

  @Inject
  private DALBackend myDALBackend;

  @Override
  public ObjectTypeDTO getObjectTypeByName(String type) {
    String sql = "SELECT ty.id_type, ty.type_name, ty.version"
        + " FROM projet_pae.types ty WHERE ty.type_name = ?";

    try (PreparedStatement getObjectTypeByName = myDALBackend.getPrepareStatement(sql)) {
      getObjectTypeByName.setString(1, type);
      try (ResultSet rs = getObjectTypeByName.executeQuery()) {
        if (rs.next()) {
          return get(rs);
        }
      }
    } catch (SQLException se) {
      throw new FatalException(se.getMessage(), se);
    }
    return null;
  }
}
