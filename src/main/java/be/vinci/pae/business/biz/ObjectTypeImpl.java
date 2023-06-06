package be.vinci.pae.business.biz;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Objects;

/**
 * This class contains the ObjectType implementation.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjectTypeImpl implements ObjectType {

  private int id;
  private String typeName;

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
  public String getTypeName() {
    return typeName;
  }

  @Override
  public void setTypeName(String typeName) {
    this.typeName = typeName;
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
    ObjectTypeImpl that = (ObjectTypeImpl) o;
    return id == that.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
