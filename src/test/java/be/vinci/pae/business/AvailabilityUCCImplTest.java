package be.vinci.pae.business;

import static org.junit.jupiter.api.Assertions.assertEquals;

import be.vinci.pae.business.dto.AvailabilityDTO;
import be.vinci.pae.business.ucc.AvailabilityUCC;
import be.vinci.pae.business.ucc.AvailabilityUCCImpl;
import be.vinci.pae.dal.dao.AvailabilityDAO;
import java.util.ArrayList;
import java.util.List;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;

@TestInstance(Lifecycle.PER_CLASS)
class AvailabilityUCCImplTest extends TestBinder {

  private Factory factory;
  private AvailabilityUCC availabilityUCC;
  private AvailabilityDAO availabilityDAO;

  private List<AvailabilityDTO> listForTest;
  private AvailabilityDTO availability;

  @BeforeAll
  void setUp() {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new TestBinder());
    this.factory = locator.getService(Factory.class);
    this.availabilityDAO = locator.getService(AvailabilityDAO.class);
    this.availabilityUCC = locator.getService(AvailabilityUCC.class);
  }

  @BeforeEach
  void init() {
    Mockito.reset(availabilityDAO);

    listForTest = new ArrayList<>();

    availability = factory.getAvailability();
    availability.setId(1);

    listForTest.add(availability);
  }

  /**
   * Test methods for {@link AvailabilityUCCImpl#getAvailabilities()}}.
   */

  @Test
  @DisplayName("should return a list of availabilities")
  void getAvailabilities() {
    Mockito.when(availabilityDAO.getAll()).thenReturn(listForTest);
    assertEquals(listForTest, availabilityUCC.getAvailabilities());
  }

  /**
   * Test methods for {@link AvailabilityUCCImpl#getAvailabilityFromString(String availability)}}.
   */

  @Test
  @DisplayName("should return a availability based on a string")
  void getAvailabilityFromString() {
    Mockito.when(availabilityDAO.getAvailabilityFromString("availability"))
        .thenReturn(availability);
    assertEquals(availability, availabilityUCC.getAvailabilityFromString("availability"));
  }

  /**
   * Test methods for {@link AvailabilityUCCImpl#createOneAvailability(AvailabilityDTO)}}.
   */

  @Test
  @DisplayName("should create a new availability")
  void createOneAvailability() {
    Mockito.when(availabilityDAO.createOne(availability)).thenReturn(availability);
    assertEquals(availability, availabilityUCC.createOneAvailability(availability));
  }
}
