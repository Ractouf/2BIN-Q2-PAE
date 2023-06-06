package be.vinci.pae.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.business.biz.User;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.dto.UserDTO.Role;
import be.vinci.pae.business.ucc.UserUCC;
import be.vinci.pae.business.ucc.UserUCCImpl;
import be.vinci.pae.dal.dao.UserDAO;
import be.vinci.pae.exceptions.BizExceptionConflict;
import be.vinci.pae.exceptions.BizExceptionForbidden;
import be.vinci.pae.exceptions.BizExceptionNotFound;
import be.vinci.pae.exceptions.BizExceptionUnauthorized;
import java.sql.Date;
import java.time.LocalDateTime;
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

/**
 * Test class for UserUCCImpl.
 */
@TestInstance(Lifecycle.PER_CLASS)
class UserUCCImplTest {

  private Factory myFactory;
  private UserUCC userUCC;
  private UserDAO userDAO;

  private List<UserDTO> list;
  private UserDTO user1;
  private UserDTO user2;
  private UserDTO userToRegister;
  private User userToHashPassword;

  @BeforeAll
  void setUp() {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new TestBinder());
    this.myFactory = locator.getService(Factory.class);
    this.userUCC = locator.getService(UserUCC.class);
    this.userDAO = locator.getService(UserDAO.class);
  }

  @BeforeEach
  void init() {
    Mockito.reset(userDAO);

    userToHashPassword = (User) myFactory.getUser();

    user1 = myFactory.getUser();
    user1.setId(1);
    user1.setEmail("user1@gmail.com");
    String hash1 = "password1";
    user1.setPassword(userToHashPassword.hashPassword(hash1));

    user2 = myFactory.getUser();
    user2.setId(2);
    user2.setEmail("user2@gmail.com");
    String hash2 = "password2";
    user2.setPassword(userToHashPassword.hashPassword(hash2));

    userToRegister = myFactory.getUser();
    userToRegister.setId(3);
    userToRegister.setEmail("userToRegister1@gmail.com");
    String hashForRegister1 = "passwordForRegister";
    userToRegister.setPassword(userToHashPassword.hashPassword(hashForRegister1));
    userToRegister.setFirstname("corantin");
    userToRegister.setLastname("NomOfCorantin");
    userToRegister.setPhoneNumber("0456985253");
    userToRegister.setPhoto("default-user.png");
    userToRegister.setRole(Role.USER);
    userToRegister.setRegisterDate(Date.valueOf(LocalDateTime.now().toLocalDate()));

    list = new ArrayList<>();
    list.add(user1);
    list.add(user2);
  }

  /**
   * Test methods for {@link UserUCCImpl#login(String, String)}.
   */

  @Test
  @DisplayName("should return the user when the login is successful")
  void login() {
    Mockito.when(userDAO.getUserByEmail("user1@gmail.com")).thenReturn(user1);

    assertEquals(user1, userUCC.login("user1@gmail.com", "password1"));
  }

  @Test
  @DisplayName("should throw  Biz exception not found")
  void loginBizExceptionNotFound() {
    Mockito.when(userDAO.getUserByEmail("user1@gmail.com")).thenReturn(null);

    assertThrows(BizExceptionNotFound.class, () -> userUCC.login("user1@gmail.com", "password1"));
  }

  @Test
  @DisplayName("should throw  Biz exception unauthorized")
  void loginBizExceptionUnauthorized() {
    Mockito.when(userDAO.getUserByEmail("user1@gmail.com")).thenReturn(user1);

    assertThrows(BizExceptionUnauthorized.class,
        () -> userUCC.login("user1@gmail.com", "password2"));
  }

  /**
   * Test methods for {@link UserUCCImpl#register(UserDTO)}.
   */

  @Test
  @DisplayName("should return the user when the register is successful")
  void register() {
    Mockito.when(userDAO.createOne(userToRegister)).thenReturn(userToRegister);

    assertEquals(userToRegister, userUCC.register(userToRegister));
  }

  @Test
  @DisplayName("should throw biz exception conflict ")
  void registerBizExceptionConflict() {
    Mockito.when(userDAO.getUserByEmail("user1@gmail.com")).thenReturn(user1);

    assertThrows(BizExceptionConflict.class, () -> userUCC.register(user1));
  }

  /**
   * Test methods for {@link UserUCCImpl#getUserById(int)}.
   */

  @Test
  @DisplayName("should return an user based on it's id")
  void getUserById() {
    Mockito.when(userDAO.getById(1)).thenReturn(user1);

    assertEquals(user1, userUCC.getUserById(1));
  }

  /**
   * Test methods for {@link UserUCCImpl#getAll()} (int)}.
   */

  @Test
  @DisplayName("should return a list of users")
  void getAll() {
    Mockito.when(userDAO.getAll()).thenReturn(list);

    assertEquals(userUCC.getAll(), list);
  }

  /**
   * Test methods for {@link UserUCCImpl#promote(int)} (int)}.
   */

  @Test
  @DisplayName("should return the updated user")
  void promoteToHelper() {
    UserDTO mockUser = myFactory.getUser();
    mockUser.setEmail("test@mail.be");
    mockUser.setRole(Role.USER);

    Mockito.when(userDAO.getById(3)).thenReturn(mockUser);

    UserDTO mockHelper = myFactory.getUser();
    mockHelper.setEmail("test@mail.be");
    mockHelper.setRole(Role.HELPER);

    Mockito.when(userDAO.updateOne(mockHelper)).thenReturn(mockHelper);

    assertEquals(userUCC.promote(3).getRole(), Role.HELPER);
  }

  @Test
  @DisplayName("should return the updated user")
  void promoteToManager() {
    UserDTO mockUser = myFactory.getUser();
    mockUser.setEmail("test@mail.be");
    mockUser.setRole(Role.HELPER);

    Mockito.when(userDAO.getById(3)).thenReturn(mockUser);

    UserDTO mockHelper = myFactory.getUser();
    mockHelper.setEmail("test@mail.be");
    mockHelper.setRole(Role.MANAGER);

    Mockito.when(userDAO.updateOne(mockHelper)).thenReturn(mockHelper);

    assertEquals(userUCC.promote(3).getRole(), Role.MANAGER);
  }

  @Test
  @DisplayName("should throw biz exception not found")
  void promoteBizExceptionNotFound() {

    Mockito.when(userDAO.getById(1)).thenReturn(null);

    assertThrows(BizExceptionNotFound.class, () -> userUCC.promote(1));
  }

  @Test
  @DisplayName("should throw biz exception forbidden")
  void promoteBizExceptionForbidden() {
    UserDTO mockUser = myFactory.getUser();
    mockUser.setEmail("test@mail.be");
    mockUser.setRole(Role.MANAGER);

    Mockito.when(userDAO.getById(3)).thenReturn(mockUser);

    assertThrows(BizExceptionForbidden.class, () -> userUCC.promote(3));
  }

  /**
   * Test methods for {@link UserUCCImpl#getAllByDate()}.
   */

  @Test
  @DisplayName("should return the list of users")
  void getAllByDate() {
    Mockito.when(userDAO.getAllByDate()).thenReturn(list);

    assertEquals(userUCC.getAllByDate(), list);
  }

  /**
   * Test methods for {@link UserUCCImpl#getNbrUsers()} ()}.
   */

  @Test
  @DisplayName("should return the number of users")
  void getNbrUsers() {
    Mockito.when(userDAO.getNbrUsers()).thenReturn(1);

    assertEquals(userUCC.getNbrUsers(), 1);
  }

  /**
   * Test methods for {@link UserUCCImpl#getNbrHelpers()} ()}.
   */

  @Test
  @DisplayName("should return the number of helpers")
  void getNbrHelpers() {
    Mockito.when(userDAO.getNbrHelpers()).thenReturn(1);

    assertEquals(userUCC.getNbrHelpers(), 1);
  }

  /**
   * Test methods for {@link UserUCCImpl#editUser(UserDTO)}.
   */

  @Test
  @DisplayName("should return the edited user")
  void editUser() {
    Mockito.when(userDAO.updateOne(user1)).thenReturn(user1);

    assertEquals(user1, userUCC.editUser(user1));
  }
}
