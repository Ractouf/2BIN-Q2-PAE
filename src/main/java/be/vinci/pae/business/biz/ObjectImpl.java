package be.vinci.pae.business.biz;

import be.vinci.pae.business.dto.AvailabilityDTO;
import be.vinci.pae.business.dto.ObjectDTO;
import be.vinci.pae.business.dto.ObjectTypeDTO;
import be.vinci.pae.business.dto.UserDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;

/**
 * This class contains the Object implementation.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjectImpl implements Object, ObjectDTO {

  private int id;
  private Date proposalDate;
  private Date interestConfirmationDate;
  private Date storeDepositDate;
  private Date marketWithdrawalDate;
  private Date sellingDate;
  private String description;
  private String photo;
  private Double sellingPrice;
  private Status status;
  private String reasonForRefusal;
  private ObjectTypeDTO fkObjectType;
  private AvailabilityDTO fkAvailability;
  private UserDTO fkOfferingMember;
  private String unknownUserPhoneNumber;

  @JsonIgnore
  private int version;

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public Date getProposalDate() {
    return proposalDate;
  }

  @Override
  public void setProposalDate(Date proposalDate) {
    this.proposalDate = proposalDate;
  }

  @Override
  public Date getInterestConfirmationDate() {
    return interestConfirmationDate;
  }

  @Override
  public void setInterestConfirmationDate(Date interestConfirmationDate) {
    this.interestConfirmationDate = interestConfirmationDate;
  }

  @Override
  public Date getStoreDepositDate() {
    return storeDepositDate;
  }

  @Override
  public void setStoreDepositDate(Date storeDepositDate) {
    this.storeDepositDate = storeDepositDate;
  }

  @Override
  public Date getMarketWithdrawalDate() {
    return marketWithdrawalDate;
  }

  @Override
  public void setMarketWithdrawalDate(Date marketWithdrawalDate) {
    this.marketWithdrawalDate = marketWithdrawalDate;
  }

  @Override
  public Date getSellingDate() {
    return sellingDate;
  }

  @Override
  public void setSellingDate(Date sellingDate) {
    this.sellingDate = sellingDate;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String getPhoto() {
    return photo;
  }

  @Override
  public void setPhoto(String photo) {
    this.photo = photo;
  }

  @Override
  public Double getSellingPrice() {
    return sellingPrice;
  }

  @Override
  public void setSellingPrice(Double sellingPrice) {
    this.sellingPrice = sellingPrice;
  }

  @Override
  public Status getStatus() {
    return status;
  }

  @Override
  public void setStatus(Status status) {
    this.status = status;
  }

  @Override
  public boolean updateStatus(Status newStatus) {
    if (this.status.canTransitionTo(newStatus)) {
      this.status = newStatus;

      switch (newStatus) {
        case ACCEPTED, REFUSED -> this.interestConfirmationDate = Date.valueOf(LocalDate.now());
        case SHOP -> this.storeDepositDate = Date.valueOf(LocalDate.now());
        case SOLD -> this.sellingDate = Date.valueOf(LocalDate.now());
        case REMOVED -> this.marketWithdrawalDate = Date.valueOf(LocalDate.now());
        default -> {
        }
      }
      return true;
    }
    return false;
  }

  @Override
  public String getReasonForRefusal() {
    return reasonForRefusal;
  }

  @Override
  public void setReasonForRefusal(String reasonForRefusal) {
    this.reasonForRefusal = reasonForRefusal;
  }

  @Override
  public ObjectTypeDTO getFkObjectType() {
    return fkObjectType;
  }

  @Override
  public void setFkObjectType(ObjectTypeDTO fkObjectType) {
    this.fkObjectType = fkObjectType;
  }

  @Override
  public AvailabilityDTO getFkAvailability() {
    return fkAvailability;
  }

  @Override
  public void setFkAvailability(AvailabilityDTO fkAvailability) {
    this.fkAvailability = fkAvailability;
  }

  @Override
  public UserDTO getFkOfferingMember() {
    return fkOfferingMember;
  }

  @Override
  public void setFkOfferingMember(UserDTO fkOfferingMember) {
    this.fkOfferingMember = fkOfferingMember;
  }

  @Override
  public String getUnknownUserPhoneNumber() {
    return unknownUserPhoneNumber;
  }

  @Override
  public void setUnknownUserPhoneNumber(String unknownUserPhoneNumber) {
    this.unknownUserPhoneNumber = unknownUserPhoneNumber;
  }

  @Override
  public int getVersion() {
    return version;
  }

  @Override
  public void setVersion(int version) {
    this.version = version;
  }

  @Override
  public void incrementVersion() {
    this.version++;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ObjectImpl object = (ObjectImpl) o;
    return id == object.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
