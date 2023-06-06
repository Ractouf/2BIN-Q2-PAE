package be.vinci.pae.business;

import static org.junit.jupiter.api.Assertions.assertEquals;

import be.vinci.pae.business.dto.ObjectTypeDTO;
import be.vinci.pae.business.ucc.ObjectTypeUCC;
import be.vinci.pae.business.ucc.ObjectTypeUCCImpl;
import be.vinci.pae.dal.dao.ObjectTypeDAO;
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
class ObjectTypeUCCImplTest extends TestBinder {

  private Factory factory;
  private ObjectTypeUCC objectTypeUCC;
  private ObjectTypeDAO objectTypeDAO;

  private List<ObjectTypeDTO> listForTest;
  private ObjectTypeDTO objectType;

  @BeforeAll
  void setUp() {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new TestBinder());
    this.factory = locator.getService(Factory.class);
    this.objectTypeUCC = locator.getService(ObjectTypeUCC.class);
    this.objectTypeDAO = locator.getService(ObjectTypeDAO.class);
  }

  @BeforeEach
  void init() {
    Mockito.reset(objectTypeDAO);

    listForTest = new ArrayList<>();

    objectType = factory.getObjectType();
    objectType.setId(1);
    objectType.setTypeName("typeTest");

    listForTest.add(objectType);
    Mockito.when(objectTypeDAO.getAll()).thenReturn(listForTest);
  }

  /**
   * Test method for {@link ObjectTypeUCCImpl#getListObjectTypes()} ()}.
   */

  @Test
  @DisplayName("should return a list of object types")
  void getListObjectTypes() {
    assertEquals(listForTest, objectTypeUCC.getListObjectTypes());
  }

  /**
   * Test method for {@link ObjectTypeUCCImpl#getObjectTypeByName(String)} ()} ()}.
   */

  @Test
  @DisplayName("should return ObjectTypeDTO when ObjectType exists")
  void getObjectTypeByName() {
    Mockito.when(objectTypeDAO.getObjectTypeByName(objectType.getTypeName()))
        .thenReturn(objectType);

    assertEquals(objectType, objectTypeUCC.getObjectTypeByName(objectType.getTypeName()));
  }
}
