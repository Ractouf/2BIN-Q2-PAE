package be.vinci.pae.dal.dao;

import be.vinci.pae.business.Factory;
import be.vinci.pae.business.dto.AvailabilityDTO;
import be.vinci.pae.business.dto.ObjectDTO;
import be.vinci.pae.business.dto.ObjectTypeDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.dal.DALBackend;
import be.vinci.pae.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the implementation of ObjectDAO.
 */
public class ObjectDAOImpl extends GenericDAOImpl<ObjectDTO> implements ObjectDAO {

  @Inject
  private DALBackend myDALBackend;
  @Inject
  private Factory myFactory;

  @Override
  public List<ObjectDTO> getAll() {
    List<ObjectDTO> listObjects = new ArrayList<>();
    String sql =
        "SELECT ob.id_object, ob.proposal_date, ob.interest_confirmation_date, "
            + "ob.store_deposit_date, ob.market_withdrawal_date, ob.selling_date, ob.description, "
            + "ob.photo AS object_photo, "
            + "ob.selling_price, ob.status, ob.reason_for_refusal, ob.version AS object_version, "
            + "ty.id_type, ty.type_name, ty.version AS object_type_version, "
            + "av.id_availability, av.availability_date, av.starting_hour, av.ending_hour, "
            + "av.version AS availability_version, "
            + "us.id_user, us.email, us.password, us.register_date, us.lastname, us.firstname, "
            + "us.photo AS user_photo, us.phone_number, us.role, us.version AS user_version, "
            + "ob.unknown_user_phone_number "
            + "FROM projet_pae.objects ob "
            + "INNER JOIN projet_pae.types ty ON ty.id_type = ob.fk_object_type "
            + "INNER JOIN projet_pae.availabilities av ON av.id_availability = ob.fk_availability "
            + "LEFT OUTER JOIN projet_pae.users us ON us.id_user = ob.fk_offering_member ";

    try (PreparedStatement getAll = myDALBackend.getPrepareStatement(sql)) {
      try (ResultSet rs = getAll.executeQuery()) {
        while (rs.next()) {
          listObjects.add(setObject(rs));
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
    return listObjects;
  }

  @Override
  public List<ObjectDTO> getListObjectByUser(int id) {
    List<ObjectDTO> listObjectsUser = new ArrayList<>();
    String sql =
        "SELECT ob.id_object, ob.proposal_date, ob.interest_confirmation_date, "
            + "ob.store_deposit_date, ob.market_withdrawal_date, ob.selling_date, ob.description, "
            + "ob.photo AS object_photo, "
            + "ob.selling_price, ob.status, ob.reason_for_refusal, ob.version AS object_version, "
            + "ty.id_type, ty.type_name, ty.version AS object_type_version, "
            + "av.id_availability, av.availability_date, av.starting_hour, av.ending_hour, "
            + "av.version AS availability_version, "
            + "us.id_user, us.email, us.password, us.register_date, us.lastname, us.firstname, "
            + "us.photo AS user_photo, us.phone_number, us.role, us.version AS user_version, "
            + "ob.unknown_user_phone_number "
            + "FROM projet_pae.objects ob "
            + "INNER JOIN projet_pae.types ty ON ty.id_type = ob.fk_object_type "
            + "INNER JOIN projet_pae.availabilities av ON av.id_availability = ob.fk_availability "
            + "LEFT OUTER JOIN projet_pae.users us ON us.id_user = ob.fk_offering_member "
            + "WHERE ob.fk_offering_member = ?";

    try (PreparedStatement getListObjectByUser = myDALBackend.getPrepareStatement(sql)) {
      getListObjectByUser.setInt(1, id);
      try (ResultSet rs = getListObjectByUser.executeQuery()) {
        while (rs.next()) {
          listObjectsUser.add(setObject(rs));
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
    return listObjectsUser;
  }

  @Override
  public ObjectDTO getById(int id) {
    String sql =
        "SELECT ob.id_object, ob.proposal_date, ob.interest_confirmation_date, "
            + "ob.store_deposit_date, ob.market_withdrawal_date, ob.selling_date, ob.description, "
            + "ob.photo AS object_photo, "
            + "ob.selling_price, ob.status, ob.reason_for_refusal, ob.version AS object_version, "
            + "ty.id_type, ty.type_name, ty.version AS object_type_version, "
            + "av.id_availability, av.availability_date, av.starting_hour, av.ending_hour, "
            + "av.version AS availability_version, "
            + "us.id_user, us.email, us.password, us.register_date, us.lastname, us.firstname, "
            + "us.photo AS user_photo, us.phone_number, us.role, us.version AS user_version, "
            + "ob.unknown_user_phone_number "
            + "FROM projet_pae.objects ob "
            + "INNER JOIN projet_pae.types ty ON ty.id_type = ob.fk_object_type "
            + "INNER JOIN projet_pae.availabilities av ON av.id_availability = ob.fk_availability "
            + "LEFT OUTER JOIN projet_pae.users us ON us.id_user = ob.fk_offering_member "
            + "WHERE ob.id_object = ?";

    try (PreparedStatement getById = myDALBackend.getPrepareStatement(sql)) {
      getById.setInt(1, id);
      try (ResultSet rs = getById.executeQuery()) {
        if (rs.next()) {
          return setObject(rs);
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }

    return null;
  }

  @Override
  public List<ObjectDTO> listOfObjectsProposed() {
    List<ObjectDTO> listObjectsProposed = new ArrayList<>();

    String sql =
        "SELECT ob.id_object, ob.proposal_date, ob.interest_confirmation_date, "
            + "ob.store_deposit_date, ob.market_withdrawal_date, ob.selling_date, ob.description, "
            + "ob.photo AS object_photo, "
            + "ob.selling_price, ob.status, ob.reason_for_refusal, ob.version AS object_version, "
            + "ty.id_type, ty.type_name, ty.version AS object_type_version, "
            + "av.id_availability, av.availability_date, av.starting_hour, av.ending_hour, "
            + "av.version AS availability_version, "
            + "us.id_user, us.email, us.password, us.register_date, us.lastname, us.firstname, "
            + "us.photo AS user_photo, us.phone_number, us.role, us.version AS user_version, "
            + "ob.unknown_user_phone_number "
            + "FROM projet_pae.objects ob "
            + "INNER JOIN projet_pae.types ty ON ty.id_type = ob.fk_object_type "
            + "INNER JOIN projet_pae.availabilities av ON av.id_availability = ob.fk_availability "
            + "LEFT OUTER JOIN projet_pae.users us ON us.id_user = ob.fk_offering_member "
            + "WHERE ob.status = 'PR'";

    try (PreparedStatement listOfObjectsProposed = myDALBackend.getPrepareStatement(sql)) {
      try (ResultSet rs = listOfObjectsProposed.executeQuery()) {
        while (rs.next()) {
          listObjectsProposed.add(setObject(rs));
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
    return listObjectsProposed;
  }

  @Override
  public Map<String, Integer> getNbrObjectsByStatus() {
    Map<String, Integer> nbrObjectsByStatus = new HashMap<>();
    String sql =
        "SELECT "
            + "CASE "
            + "WHEN all_statuses.status = 'AC' THEN 'Accepté' "
            + "WHEN all_statuses.status = 'RF' THEN 'Refusé' "
            + "WHEN all_statuses.status = 'SO' THEN 'Vendu' "
            + "END AS status_description, "
            + "COUNT(o.id_object) AS status_count "
            + "FROM (VALUES ('AC'), ('RF'), ('SO')) AS all_statuses(status) "
            + "LEFT JOIN projet_pae.objects o ON all_statuses.status = o.status "
            + "GROUP BY all_statuses.status";

    try (PreparedStatement nbrObjectsByStatusSql = myDALBackend.getPrepareStatement(sql)) {
      try (ResultSet rs = nbrObjectsByStatusSql.executeQuery()) {
        while (rs.next()) {
          nbrObjectsByStatus.put(rs.getString("status_description"), rs.getInt("status_count"));
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
    return nbrObjectsByStatus;
  }

  @Override
  public Map<Integer, Map<Integer, Integer>> getNbrObjectsProposedByMonth() {
    Map<Integer, Map<Integer, Integer>> nbrObjectsProposedByMonth = new HashMap<>();
    String sql = "SELECT "
        + "EXTRACT(YEAR FROM dates.date) AS year, "
        + "EXTRACT(MONTH FROM dates.date) AS month, "
        + "COUNT(objects.id_object) AS nbr_objects "
        + "FROM ("
        + "    SELECT "
        + "        generate_series("
        + "            DATE_TRUNC('year', MIN(proposal_date)),"
        + "            DATE_TRUNC('month', MAX(proposal_date)),"
        + "            INTERVAL '1 month'"
        + "        ) AS date"
        + "    FROM projet_pae.objects"
        + ") dates "
        + "LEFT JOIN projet_pae.objects ON "
        + "    EXTRACT(YEAR FROM projet_pae.objects.proposal_date) = "
        + "EXTRACT(YEAR FROM dates.date) AND "
        + "    EXTRACT(MONTH FROM projet_pae.objects.proposal_date) = "
        + "EXTRACT(MONTH FROM dates.date)"
        + "GROUP BY year, month "
        + "ORDER BY year, month";

    try (PreparedStatement nbrObjectsProposedByMonthSql =
        myDALBackend.getPrepareStatement(sql)) {
      try (ResultSet rs = nbrObjectsProposedByMonthSql.executeQuery()) {
        while (rs.next()) {
          int year = rs.getInt("year");
          int month = rs.getInt("month");
          int nbrObjects = rs.getInt("nbr_objects");
          if (!nbrObjectsProposedByMonth.containsKey(year)) {
            nbrObjectsProposedByMonth.put(year, new HashMap<>());
          }
          nbrObjectsProposedByMonth.get(year).put(month, nbrObjects);
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
    return nbrObjectsProposedByMonth;
  }

  @Override
  public List<ObjectDTO> getHomePageObjects() {
    List<ObjectDTO> listHomePageObjects = new ArrayList<>();
    String sql =
        "SELECT ob.id_object, ob.proposal_date, ob.interest_confirmation_date, "
            + "ob.store_deposit_date, ob.market_withdrawal_date, ob.selling_date, ob.description, "
            + "ob.photo AS object_photo, "
            + "ob.selling_price, ob.status, ob.reason_for_refusal, ob.version AS object_version, "
            + "ty.id_type, ty.type_name, ty.version AS object_type_version, "
            + "av.id_availability, av.availability_date, av.starting_hour, av.ending_hour, "
            + "av.version AS availability_version, "
            + "us.id_user, us.email, us.password, us.register_date, us.lastname, us.firstname, "
            + "us.photo AS user_photo, us.phone_number, us.role, us.version AS user_version, "
            + "ob.unknown_user_phone_number "
            + "FROM projet_pae.objects ob "
            + "INNER JOIN projet_pae.types ty ON ty.id_type = ob.fk_object_type "
            + "INNER JOIN projet_pae.availabilities av ON av.id_availability = ob.fk_availability "
            + "LEFT OUTER JOIN projet_pae.users us ON us.id_user = ob.fk_offering_member "
            + "WHERE ob.status = 'SH' OR ob.status = 'SA' OR ob.status = 'SO'";

    try (PreparedStatement psListHomePageObjects = myDALBackend.getPrepareStatement(sql)) {
      try (ResultSet rs = psListHomePageObjects.executeQuery()) {
        while (rs.next()) {
          listHomePageObjects.add(setObject(rs));
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
    return listHomePageObjects;
  }

  @Override
  public List<ObjectDTO> getObjectsInStore() {
    List<ObjectDTO> listObjectsInStore = new ArrayList<>();
    String sql =
        "SELECT ob.id_object, ob.proposal_date, ob.interest_confirmation_date, "
            + "ob.store_deposit_date, ob.market_withdrawal_date, ob.selling_date, ob.description, "
            + "ob.photo AS object_photo, "
            + "ob.selling_price, ob.status, ob.reason_for_refusal, ob.version AS object_version, "
            + "ty.id_type, ty.type_name, ty.version AS object_type_version, "
            + "av.id_availability, av.availability_date, av.starting_hour, av.ending_hour, "
            + "av.version AS availability_version, "
            + "us.id_user, us.email, us.password, us.register_date, us.lastname, us.firstname, "
            + "us.photo AS user_photo, us.phone_number, us.role, us.version AS user_version, "
            + "ob.unknown_user_phone_number "
            + "FROM projet_pae.objects ob "
            + "INNER JOIN projet_pae.types ty ON ty.id_type = ob.fk_object_type "
            + "INNER JOIN projet_pae.availabilities av ON av.id_availability = ob.fk_availability "
            + "LEFT OUTER JOIN projet_pae.users us ON us.id_user = ob.fk_offering_member "
            + "WHERE ob.status = 'SH' OR ob.status = 'SA'";

    try (PreparedStatement psListObjectsInStore = myDALBackend.getPrepareStatement(sql)) {
      try (ResultSet rs = psListObjectsInStore.executeQuery()) {
        while (rs.next()) {
          listObjectsInStore.add(setObject(rs));
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
    return listObjectsInStore;
  }

  private ObjectDTO setObject(ResultSet rs) {
    ObjectDTO object = myFactory.getObject();
    ObjectTypeDTO objectType = myFactory.getObjectType();
    AvailabilityDTO availability = myFactory.getAvailability();
    UserDTO user = myFactory.getUser();

    try {
      object.setId(rs.getInt("id_object"));
      object.setProposalDate(rs.getDate("proposal_date"));
      object.setInterestConfirmationDate(rs.getDate("interest_confirmation_date"));
      object.setStoreDepositDate(rs.getDate("store_deposit_date"));
      object.setMarketWithdrawalDate(rs.getDate("market_withdrawal_date"));
      object.setSellingDate(rs.getDate("selling_date"));
      object.setDescription(rs.getString("description"));
      object.setPhoto(rs.getString("object_photo"));
      object.setSellingPrice(rs.getDouble("selling_price"));

      for (ObjectDTO.Status status : ObjectDTO.Status.values()) {
        if (status.getCode().equals(rs.getString("status"))) {
          object.setStatus(status);
          break;
        }
      }

      object.setReasonForRefusal(rs.getString("reason_for_refusal"));
      object.setVersion(rs.getInt("object_version"));

      objectType.setId(rs.getInt("id_type"));
      objectType.setTypeName(rs.getString("type_name"));
      objectType.setVersion(rs.getInt("object_type_version"));

      availability.setId(rs.getInt("id_availability"));
      availability.setAvailabilityDate(rs.getDate("availability_date"));
      availability.setStartingHour(rs.getTime("starting_hour"));
      availability.setEndingHour(rs.getTime("ending_hour"));
      availability.setVersion(rs.getInt("availability_version"));

      user.setId(rs.getInt("id_user"));
      user.setEmail(rs.getString("email"));
      user.setPassword(rs.getString("password"));
      user.setRegisterDate(rs.getDate("register_date"));
      user.setLastname(rs.getString("lastname"));
      user.setFirstname(rs.getString("firstname"));
      user.setPhoto(rs.getString("user_photo"));
      user.setPhoneNumber(rs.getString("phone_number"));

      for (UserDTO.Role role : UserDTO.Role.values()) {
        if (role.getCode().equals(rs.getString("role"))) {
          user.setRole(role);
          break;
        }
      }

      user.setVersion(rs.getInt("user_version"));

      object.setUnknownUserPhoneNumber(rs.getString("unknown_user_phone_number"));

    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }

    object.setFkObjectType(objectType);
    object.setFkAvailability(availability);
    object.setFkOfferingMember(user);

    return object;
  }
}
