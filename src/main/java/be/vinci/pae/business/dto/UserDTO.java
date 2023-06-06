package be.vinci.pae.business.dto;

import be.vinci.pae.business.biz.UserImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.sql.Date;

/**
 * This is the interface of UserImpl.
 */
@JsonDeserialize(as = UserImpl.class)
public interface UserDTO {

  /**
   * Gets the id of the user.
   *
   * @return the id of the user
   */
  int getId();

  /**
   * Sets the id of the user.
   *
   * @param id the id of the user
   */
  void setId(int id);

  /**
   * Gets the email of the user.
   *
   * @return the email of the user
   */
  String getEmail();

  /**
   * Sets the email of the user.
   *
   * @param email the email of the user
   */
  void setEmail(String email);

  /**
   * Gets the password of the user.
   *
   * @return the password of the user
   */
  String getPassword();

  /**
   * Sets the password of the user.
   *
   * @param password the password of the user
   */
  void setPassword(String password);

  /**
   * Gets the register date of the user.
   *
   * @return the register date of the user
   */
  Date getRegisterDate();

  /**
   * Sets the register date of the user.
   *
   * @param registerDate the register date of the user
   */
  void setRegisterDate(Date registerDate);

  /**
   * Gets the last name of the user.
   *
   * @return the last name of the user
   */
  String getLastname();

  /**
   * Sets the last name of the user.
   *
   * @param lastname the last name of the user
   */
  void setLastname(String lastname);

  /**
   * Gets the first name of the user.
   *
   * @return the first name of the user
   */
  String getFirstname();

  /**
   * Sets the first name of the user.
   *
   * @param firstname the first name of the user
   */
  void setFirstname(String firstname);

  /**
   * Gets the photo of the user.
   *
   * @return the photo of the user
   */
  String getPhoto();

  /**
   * Sets the photo of the user.
   *
   * @param photo the photo of the user
   */
  void setPhoto(String photo);

  /**
   * Gets the phone number of the user.
   *
   * @return the phone number of the user
   */
  String getPhoneNumber();

  /**
   * Sets the phone number of the user.
   *
   * @param phoneNumber the phone number of the user
   */
  void setPhoneNumber(String phoneNumber);

  /**
   * Gets the role of the user.
   *
   * @return the role of the user
   */
  Role getRole();

  /**
   * Sets the role of the user.
   *
   * @param role the role of the user
   */
  void setRole(Role role);

  /**
   * Gets the version of the user.
   *
   * @return the version of the user
   */
  int getVersion();

  /**
   * Sets the version of the user.
   *
   * @param version of the user
   */
  void setVersion(int version);

  /**
   * Enum of the roles.
   */
  enum Role {

    /**
     * The user role.
     */
    USER("U"),
    /**
     * The helper role.
     */
    HELPER("H"),
    /**
     * The manager role.
     */
    MANAGER("M");

    private final String code;

    Role(String code) {
      this.code = code;
    }

    /**
     * Gets the code of the role.
     *
     * @return the code of the role
     */
    public String getCode() {
      return code;
    }
  }
}
