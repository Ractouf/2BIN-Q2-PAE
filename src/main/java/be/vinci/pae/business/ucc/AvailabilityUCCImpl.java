package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.AvailabilityDTO;
import be.vinci.pae.dal.DALServices;
import be.vinci.pae.dal.dao.AvailabilityDAO;
import be.vinci.pae.dal.transactions.Transaction;
import jakarta.inject.Inject;
import java.util.List;

/**
 * This is the implementation of AvailabilityUCC.
 */
public class AvailabilityUCCImpl implements AvailabilityUCC {

  @Inject
  private AvailabilityDAO myAvailabilityDAO;

  @Inject
  private DALServices dalServices;

  @Override
  public List<AvailabilityDTO> getAvailabilities() {
    try (Transaction transaction = new Transaction(dalServices)) {
      List<AvailabilityDTO> availabilities = myAvailabilityDAO.getAll();

      transaction.commit();

      return availabilities;
    }
  }

  @Override
  public AvailabilityDTO getAvailabilityFromString(String availability) {
    try (Transaction transaction = new Transaction(dalServices)) {
      AvailabilityDTO availabilities = myAvailabilityDAO.getAvailabilityFromString(availability);

      transaction.commit();

      return availabilities;
    }
  }

  @Override
  public AvailabilityDTO createOneAvailability(AvailabilityDTO availability) {
    try (Transaction transaction = new Transaction(dalServices)) {
      AvailabilityDTO availabilities = myAvailabilityDAO.createOne(availability);

      transaction.commit();

      return availabilities;
    }
  }
}
