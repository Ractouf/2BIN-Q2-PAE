package be.vinci.pae.business.biz;

import be.vinci.pae.business.dto.ObjectDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * This is the implementation of Notification.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationImpl implements Notification {

  private int id;
  private String textNotification;
  private ObjectDTO fkConcernedObject;

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
  public String getTextNotification() {
    return textNotification;
  }

  @Override
  public void setTextNotification(String textNotification) {
    this.textNotification = textNotification;
  }

  @Override
  public ObjectDTO getFkConcernedObject() {
    return fkConcernedObject;
  }

  @Override
  public void setFkConcernedObject(ObjectDTO fkConcernedObject) {
    this.fkConcernedObject = fkConcernedObject;
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

    NotificationImpl that = (NotificationImpl) o;

    return id == that.id;
  }

  @Override
  public int hashCode() {
    return id;
  }
}
