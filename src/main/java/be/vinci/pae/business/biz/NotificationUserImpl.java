package be.vinci.pae.business.biz;

import be.vinci.pae.business.dto.NotificationDTO;
import be.vinci.pae.business.dto.UserDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * This is the implementation of NotificationUser.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationUserImpl implements NotificationUser {

  private boolean isRead;
  private UserDTO fkConcernedUser;
  private NotificationDTO fkNotification;

  @JsonIgnore
  private int version;

  @Override
  public boolean isRead() {
    return isRead;
  }

  @Override
  public void setRead(boolean read) {
    isRead = read;
  }

  @Override
  public UserDTO getFkConcernedUser() {
    return fkConcernedUser;
  }

  @Override
  public void setFkConcernedUser(UserDTO fkConcernedUser) {
    this.fkConcernedUser = fkConcernedUser;
  }

  @Override
  public NotificationDTO getFkNotification() {
    return fkNotification;
  }

  @Override
  public void setFkNotification(NotificationDTO fkNotification) {
    this.fkNotification = fkNotification;
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

    NotificationUserImpl that = (NotificationUserImpl) o;

    if (!fkConcernedUser.equals(that.fkConcernedUser)) {
      return false;
    }
    return fkNotification.equals(that.fkNotification);
  }

  @Override
  public int hashCode() {
    int result = fkConcernedUser.hashCode();
    result = 31 * result + fkNotification.hashCode();
    return result;
  }
}
