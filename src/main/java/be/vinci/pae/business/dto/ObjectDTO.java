package be.vinci.pae.business.dto;

import be.vinci.pae.business.biz.ObjectImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.sql.Date;

/**
 * This is the interface of ObjectImpl.
 */
@JsonDeserialize(as = ObjectImpl.class)
public interface ObjectDTO {

  /**
   * Gets the id of the object.
   *
   * @return the id of the object
   */
  int getId();

  /**
   * Sets the id of the object.
   *
   * @param id the id of the object
   */
  void setId(int id);

  /**
   * Gets the proposal date of the object.
   *
   * @return the proposal date of the object
   */
  Date getProposalDate();

  /**
   * Sets the proposal date of the object.
   *
   * @param proposalDate the proposal date of the object
   */
  void setProposalDate(Date proposalDate);

  /**
   * Gets the date of the interest confirmation.
   *
   * @return the date of the interest confirmation
   */
  Date getInterestConfirmationDate();

  /**
   * Sets the date of the interest confirmation.
   *
   * @param interestConfirmationDate the date of the interest confirmation
   */
  void setInterestConfirmationDate(Date interestConfirmationDate);

  /**
   * Gets the date of the store deposit.
   *
   * @return the date of the store deposit
   */
  Date getStoreDepositDate();

  /**
   * Sets the date of the store deposit.
   *
   * @param storeDepositDate the date of the store deposit
   */
  void setStoreDepositDate(Date storeDepositDate);

  /**
   * Gets the date of the market withdrawal.
   *
   * @return the date of the market withdrawal
   */
  Date getMarketWithdrawalDate();

  /**
   * Sets the date of the market withdrawal.
   *
   * @param marketWithdrawalDate the date of the market withdrawal
   */
  void setMarketWithdrawalDate(Date marketWithdrawalDate);

  /**
   * Gets the date of the selling.
   *
   * @return the date of the selling
   */
  Date getSellingDate();

  /**
   * Sets the date of the selling.
   *
   * @param sellingDate the date of the selling
   */
  void setSellingDate(Date sellingDate);

  /**
   * Gets the description of the object.
   *
   * @return the description of the object
   */
  String getDescription();

  /**
   * Sets the description of the object.
   *
   * @param description the description of the object
   */
  void setDescription(String description);

  /**
   * Gets the photo of the object.
   *
   * @return the photo of the object
   */
  String getPhoto();

  /**
   * Sets the photo of the object.
   *
   * @param photo the photo of the object
   */
  void setPhoto(String photo);

  /**
   * Gets the price of the object.
   *
   * @return the price of the object
   */
  Double getSellingPrice();

  /**
   * Sets the price of the object.
   *
   * @param sellingPrice the price of the object
   */
  void setSellingPrice(Double sellingPrice);

  /**
   * Gets the status of the object.
   *
   * @return the status of the object
   */
  Status getStatus();

  /**
   * Sets the status of the object.
   *
   * @param status the status of the object
   */
  void setStatus(Status status);

  /**
   * Updates the status.
   *
   * @param newStatus status to update to
   * @return true if previous status is right
   */
  boolean updateStatus(Status newStatus);

  /**
   * Gets the reason for refusal of the object.
   *
   * @return the reason for refusal of the object
   */
  String getReasonForRefusal();

  /**
   * Sets the reason for refusal of the object.
   *
   * @param reasonForRefusal the reason for refusal of the object
   */
  void setReasonForRefusal(String reasonForRefusal);

  /**
   * Gets the type of the object.
   *
   * @return the type of the object.
   */
  ObjectTypeDTO getFkObjectType();

  /**
   * Sets the type of the object.
   *
   * @param fkObjectType the type of the object.
   */
  void setFkObjectType(ObjectTypeDTO fkObjectType);

  /**
   * Gets the availability for the object.
   *
   * @return the availability for the object.
   */
  AvailabilityDTO getFkAvailability();

  /**
   * Sets the availability for the object.
   *
   * @param fkAvailability the availability for the object.
   */
  void setFkAvailability(AvailabilityDTO fkAvailability);

  /**
   * Gets the user who is offering the object.
   *
   * @return the user who is offering the object.
   */
  UserDTO getFkOfferingMember();

  /**
   * Sets the user who is offering the object.
   *
   * @param fkOfferingMember the user who is offering the object.
   */
  void setFkOfferingMember(UserDTO fkOfferingMember);

  /**
   * Gets the number of the unknown user.
   *
   * @return the number of the unknown user.
   */
  String getUnknownUserPhoneNumber();

  /**
   * Sets the number of the unknown user.
   *
   * @param unknownUserPhoneNumber the number of the unknown user.
   */
  void setUnknownUserPhoneNumber(String unknownUserPhoneNumber);

  /**
   * Gets the version of the object.
   *
   * @return the version of the object
   */
  int getVersion();

  /**
   * Sets the version of the object.
   *
   * @param version of the object
   */
  void setVersion(int version);

  /**
   * This is the enum for the status of the object.
   */
  enum Status {

    /**
     * Status proposed.
     */
    PROPOSED("PR") {
      @Override
      public boolean canTransitionTo(Status newStatus) {
        return newStatus == ACCEPTED || newStatus == REFUSED;
      }
    },

    /**
     * Status accepted.
     */
    ACCEPTED("AC") {
      @Override
      public boolean canTransitionTo(Status newStatus) {
        return newStatus == WORKSHOP || newStatus == SHOP;
      }
    },

    /**
     * Status refused.
     */
    REFUSED("RF") {
      @Override
      public boolean canTransitionTo(Status newStatus) {
        return false;
      }
    },

    /**
     * Status in workshop.
     */
    WORKSHOP("WK") {
      @Override
      public boolean canTransitionTo(Status newStatus) {
        return newStatus == SHOP;
      }
    },

    /**
     * Status in shop.
     */
    SHOP("SH") {
      @Override
      public boolean canTransitionTo(Status newStatus) {
        return newStatus == ON_SALE || newStatus == SOLD;
      }
    },

    /**
     * Status on sale.
     */
    ON_SALE("SA") {
      @Override
      public boolean canTransitionTo(Status newStatus) {
        return newStatus == SOLD || newStatus == REMOVED;
      }
    },

    /**
     * Status sold.
     */
    SOLD("SO") {
      @Override
      public boolean canTransitionTo(Status newStatus) {
        return false;
      }
    },

    /**
     * Status removed.
     */
    REMOVED("RM") {
      @Override
      public boolean canTransitionTo(Status newStatus) {
        return false;
      }
    };

    final String code;

    Status(String code) {
      this.code = code;
    }

    /**
     * This method verifies if the status can transition to the new one.
     *
     * @param newStatus status to transition to
     * @return true if it can transition
     */
    public abstract boolean canTransitionTo(Status newStatus);

    /**
     * Gets the code of the status.
     *
     * @return the code of the status.
     */
    public String getCode() {
      return code;
    }
  }
}
