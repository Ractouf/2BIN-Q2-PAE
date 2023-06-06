package be.vinci.pae.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.business.dto.AvailabilityDTO;
import be.vinci.pae.business.dto.ObjectDTO;
import be.vinci.pae.business.dto.ObjectDTO.Status;
import be.vinci.pae.business.dto.ObjectTypeDTO;
import be.vinci.pae.business.ucc.ObjectUCC;
import be.vinci.pae.business.ucc.ObjectUCCImpl;
import be.vinci.pae.dal.dao.ObjectDAO;
import be.vinci.pae.exceptions.BizExceptionNotFound;
import be.vinci.pae.exceptions.FatalException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;

/**
 * Test class for ObjectUCCImpl.
 */
@TestInstance(Lifecycle.PER_CLASS)
class ObjectUCCImplTest {

  private Factory factory;
  private ObjectUCC objectUCC;
  private ObjectDAO objectDAO;

  private List<ObjectDTO> listForTest;
  private ObjectDTO objectStatusProposed;
  private ObjectDTO objectStatusAccepted;
  private ObjectDTO objectStatusRefused;
  private ObjectDTO objectStatusSHOP;
  private ObjectDTO objectStatusSOLD;
  private ObjectDTO objectStatusOnSALE;
  private List<ObjectDTO> listObjectUser;
  private ObjectDTO object1;
  private ObjectDTO object2;
  private ObjectDTO object3;
  private ObjectDTO proposedObject;
  private ObjectTypeDTO proposedType;
  private AvailabilityDTO proposedAvailability;

  @BeforeAll
  void setUp() {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new TestBinder());
    this.factory = locator.getService(Factory.class);
    this.objectUCC = locator.getService(ObjectUCC.class);
    this.objectDAO = locator.getService(ObjectDAO.class);
  }

  @BeforeEach
  void init() {
    Mockito.reset(objectDAO);

    listForTest = new ArrayList<>();

    objectStatusProposed = factory.getObject();
    objectStatusProposed.setStatus(Status.PROPOSED);

    objectStatusAccepted = factory.getObject();
    objectStatusAccepted.setStatus(Status.ACCEPTED);

    objectStatusRefused = factory.getObject();
    objectStatusRefused.setStatus(Status.REFUSED);

    objectStatusSHOP = factory.getObject();
    objectStatusSHOP.setStatus(Status.SHOP);

    objectStatusSOLD = factory.getObject();
    objectStatusSOLD.setStatus(Status.SOLD);

    objectStatusOnSALE = factory.getObject();
    objectStatusOnSALE.setStatus(Status.ON_SALE);

    object1 = factory.getObject();
    object1.setId(4);
    object2 = factory.getObject();
    object2.setId(5);
    object3 = factory.getObject();
    object3.setId(6);

    listObjectUser = new ArrayList<>();
    listObjectUser.add(object1);
    listObjectUser.add(object2);
    listObjectUser.add(object3);

    proposedObject = factory.getObject();
    proposedType = factory.getObjectType();
    proposedAvailability = factory.getAvailability();

    proposedType.setId(1);
    proposedType.setTypeName("Table");

    proposedAvailability.setId(1);
    proposedAvailability.setAvailabilityDate(new Date(2023, 3, 1));
    proposedAvailability.setStartingHour(new Time(14));
    proposedAvailability.setEndingHour(new Time(16));

    proposedObject.setId(1);
    proposedObject.setFkObjectType(proposedType);
    proposedObject.setDescription("Description");
    proposedObject.setFkAvailability(proposedAvailability);
    proposedObject.setUnknownUserPhoneNumber("0495/20.41.90");
    proposedObject.setPhoto("Data");
  }

  /**
   * Test method for {@link ObjectUCCImpl#getListObjects()}.
   */

  @Test
  @DisplayName("should return a list of objects")
  void getListObjects_returns_listOfObject() {
    listForTest.add(object1);
    listForTest.add(object2);
    listForTest.add(object3);

    Mockito.when(objectDAO.getAll()).thenReturn(listForTest);

    assertEquals(listForTest, objectUCC.getListObjects());
  }

  /**
   * Test method for {@link ObjectUCCImpl#getListObjectByUser(int)}.
   */

  @Test
  @DisplayName("should return a list of objects of an user")
  void getListObjectsByUser_returns_listOfUserObjects() {
    Mockito.when(objectDAO.getListObjectByUser(1)).thenReturn(listObjectUser);

    assertEquals(objectUCC.getListObjectByUser(1), listObjectUser);
  }

  /**
   * Test method for {@link ObjectUCCImpl#getById(int)} (int, Status)} ()}.
   */

  @Test
  @DisplayName("should return an object based on it's id")
  void getObjectById() {
    Mockito.when(objectDAO.getById(1)).thenReturn(object1);

    assertEquals(object1, objectUCC.getById(1));
  }

  /**
   * Test method for {@link ObjectUCCImpl#refuseObject(int, String)}.
   */

  @Test
  @DisplayName("should refuse object and return updated object")
  void refuseObject() {
    Mockito.when(objectDAO.getById(1)).thenReturn(objectStatusProposed);
    Mockito.when(objectDAO.updateOne(objectStatusRefused)).thenReturn(objectStatusRefused);

    assertEquals(objectStatusRefused, objectUCC.refuseObject(1, "Pas bon"));
  }

  @Test
  @DisplayName("should throw a fatal exception ")
  void refuseObjectFatalException() {
    Mockito.when(objectDAO.getById(1)).thenReturn(objectStatusAccepted);

    assertThrows(FatalException.class, () -> objectUCC.refuseObject(1, "Pas bon"));
  }

  /**
   * Test method for {@link ObjectUCCImpl#updateObjectStatus(int, Status)} ()}.
   */

  @Test
  @DisplayName("should update object status and return updated object")
  void updateObjectStatus() {
    Mockito.when(objectDAO.getById(1)).thenReturn(objectStatusProposed);
    Mockito.when(objectDAO.updateOne(objectStatusAccepted)).thenReturn(objectStatusAccepted);

    assertEquals(objectStatusAccepted, objectUCC.updateObjectStatus(1, Status.ACCEPTED));
  }

  @Test
  @DisplayName("should throw biz exception not found ")
  void updateObjectStatusBizExceptionNotFound() {
    Mockito.when(objectDAO.getById(1)).thenReturn(null);

    assertThrows(BizExceptionNotFound.class,
        () -> objectUCC.updateObjectStatus(1, Status.ACCEPTED));
  }

  @Test
  @DisplayName("should throw fatal exception ")
  void updateObjectStatusFatalException() {
    Mockito.when(objectDAO.getById(1)).thenReturn(objectStatusRefused);

    assertThrows(FatalException.class,
        () -> objectUCC.updateObjectStatus(1, Status.ACCEPTED));
  }

  /**
   * Test method for {@link ObjectUCCImpl#listOfObjectsProposed()} ()}.
   */

  @Test
  @DisplayName("should return a list of Objects with proposed objects")
  void listOfObjectsProposed() {
    listForTest.add(objectStatusProposed);

    Mockito.when(objectDAO.listOfObjectsProposed()).thenReturn(listForTest);

    assertEquals(listForTest, objectUCC.listOfObjectsProposed());
  }

  /**
   * Test method for {@link ObjectUCCImpl#editObject(ObjectDTO)} .
   */

  @Test
  @DisplayName("should return the edited object")
  void editObject() {
    Mockito.when(objectDAO.updateOne(object1)).thenReturn(object1);

    assertEquals(object1, objectUCC.editObject(object1));
  }

  /**
   * Test method for {@link ObjectUCCImpl#proposeObject(ObjectDTO)} .
   */

  @Test
  @DisplayName("should return the just created object")
  public void proposeObject() {
    Mockito.when(objectDAO.createOne(proposedObject)).thenReturn(proposedObject);

    assertEquals(proposedObject, objectUCC.proposeObject(proposedObject));
  }

  /**
   * Test method for {@link ObjectUCCImpl#getNbrObjectsByStatus()} .
   */

  @Test
  @DisplayName("should return a map of the status with the amount of object with that status")
  public void getNbrObjectsByStatus() {
    Map<String, Integer> expectedMap = new HashMap<>();
    expectedMap.put("refused", 10);
    expectedMap.put("accepted", 5);
    expectedMap.put("sold", 7);

    Mockito.when(objectDAO.getNbrObjectsByStatus()).thenReturn(expectedMap);

    assertEquals(expectedMap, objectUCC.getNbrObjectsByStatus());
  }

  /**
   * Test method for {@link ObjectUCCImpl#getNbrObjectsProposedByMonth()} .
   */

  @Test
  @DisplayName("should return a map of years with the amount of 'proposed' objects each month")
  public void getNbrObjectsProposedByMonth() {
    Map<Integer, Map<Integer, Integer>> expectedMap = new HashMap<>();

    Map<Integer, Integer> subMap1 = new HashMap<>();
    subMap1.put(1, 10);
    subMap1.put(2, 5);
    expectedMap.put(2022, subMap1);

    Map<Integer, Integer> subMap2 = new HashMap<>();
    subMap2.put(3, 8);
    subMap2.put(4, 12);
    expectedMap.put(2023, subMap2);

    Mockito.when(objectDAO.getNbrObjectsProposedByMonth()).thenReturn(expectedMap);

    assertEquals(expectedMap, objectUCC.getNbrObjectsProposedByMonth());
  }

  /**
   * Test method for {@link ObjectUCCImpl#getHomePageObjects()} ()} .
   */

  @Test
  @DisplayName("should return a list of object for the home")
  public void getHomePageObjects() {
    List<ObjectDTO> listObjectUser = new ArrayList<>();
    listObjectUser.add(objectStatusOnSALE);
    listObjectUser.add(objectStatusSHOP);
    listObjectUser.add(objectStatusSOLD);

    Mockito.when(objectDAO.getHomePageObjects()).thenReturn(listObjectUser);

    assertEquals(listObjectUser, objectUCC.getHomePageObjects());

  }
}
