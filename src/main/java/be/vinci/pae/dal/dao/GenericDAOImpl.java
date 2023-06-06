package be.vinci.pae.dal.dao;

import be.vinci.pae.business.Factory;
import be.vinci.pae.business.dto.ObjectDTO.Status;
import be.vinci.pae.business.dto.UserDTO.Role;
import be.vinci.pae.dal.DALBackend;
import be.vinci.pae.exceptions.FatalException;
import be.vinci.pae.exceptions.OptimisticLockException;
import jakarta.inject.Inject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is the generic data access object.
 *
 * @param <T> the type of the object
 */
public abstract class GenericDAOImpl<T> implements GenericDAO<T> {

  @Inject
  private DALBackend myDALBackend;

  @Inject
  private Factory myDomainFactory;

  @Override
  public List<T> getAll() {
    String tableName = getTableName();

    String objectName = getObjectName();

    List<T> generics = new LinkedList<>();

    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append("SELECT ");

    T object = getObject();
    Field[] fields = object.getClass().getDeclaredFields();
    getFields(fields, objectName, sqlBuilder, false);

    sqlBuilder.append(" FROM projet_pae.").append(tableName);

    try (PreparedStatement getAll = myDALBackend.getPrepareStatement(String.valueOf(sqlBuilder));
        ResultSet rs = getAll.executeQuery()) {

      while (rs.next()) {
        generics.add(get(rs));
      }

    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }

    return generics;
  }

  @Override
  public T getById(int id) {
    String tableName = getTableName();

    String objectName = getObjectName();

    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append("SELECT ");

    T object = getObject();
    Field[] fields = object.getClass().getDeclaredFields();
    getFields(fields, objectName, sqlBuilder, false);

    sqlBuilder.append(" FROM projet_pae.").append(tableName).append(" WHERE id_").append(objectName)
        .append(" = ?");

    try (PreparedStatement getById = myDALBackend.getPrepareStatement(String.valueOf(sqlBuilder))) {
      getById.setInt(1, id);
      try (ResultSet rs = getById.executeQuery()) {
        if (rs.next()) {
          return get(rs);
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }

    return null;
  }

  @Override
  public T createOne(T object) {
    String tableName = getTableName();

    StringBuilder sqlBuilder = new StringBuilder("INSERT INTO projet_pae." + tableName + " (");
    Field[] fields = object.getClass().getDeclaredFields();
    getFields(fields, null, sqlBuilder, false);

    sqlBuilder.append(") VALUES (");

    for (Field field : fields) {
      if (field.getName().equals("id")) {
        continue;
      }
      sqlBuilder.append("?, ");
    }

    String objectName = getObjectName();
    sqlBuilder.delete(sqlBuilder.length() - 2, sqlBuilder.length());
    sqlBuilder.append(") RETURNING id_").append(objectName);

    try (PreparedStatement createOne = myDALBackend.getPrepareStatement(
        String.valueOf(sqlBuilder))) {
      setObject(createOne, fields, object, false);

      try (ResultSet rs = createOne.executeQuery()) {
        if (rs.next()) {
          return getById(rs.getInt(1));
        }
      }

    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }

    return object;
  }

  @Override
  public T updateOne(T object) {
    String tableName = getTableName();

    String objectName = getObjectName();

    StringBuilder sqlBuilder = new StringBuilder("UPDATE projet_pae." + tableName + " SET ");
    Field[] fields = object.getClass().getDeclaredFields();

    getFields(fields, null, sqlBuilder, true);

    sqlBuilder.append(" WHERE id_").append(objectName).append(" = ? AND version = ?");

    try (PreparedStatement updateOne = myDALBackend.getPrepareStatement(
        String.valueOf(sqlBuilder))) {
      setObject(updateOne, fields, object, true);

      Method getVersionMethod = object.getClass().getMethod("getVersion");
      int version = (int) getVersionMethod.invoke(object);
      updateOne.setInt(fields.length + 1, version);

      Method incrementVersionMethod = object.getClass().getMethod("incrementVersion");
      incrementVersionMethod.invoke(object);

      version = (int) getVersionMethod.invoke(object);
      updateOne.setInt(fields.length - 1, version);

      int updatedRows = updateOne.executeUpdate();

      if (updatedRows == 0) {
        throw new OptimisticLockException("Object was updated or deleted by another transaction");
      }
    } catch (SQLException | InvocationTargetException | NoSuchMethodException
             | IllegalAccessException e) {
      throw new FatalException(e.getMessage(), e);
    }

    return object;
  }

  @Override
  public void deleteOne(T object) {
    String tableName = getTableName();

    String objectName = getObjectName();

    String sql =
        "DELETE FROM projet_pae." + tableName + " WHERE id_" + objectName + " = ?";

    try (PreparedStatement deleteOne = myDALBackend.getPrepareStatement(sql)) {
      deleteOne.setInt(1, (Integer) object.getClass().getDeclaredField("id").get(object));
      deleteOne.executeUpdate();
    } catch (SQLException | IllegalAccessException | NoSuchFieldException e) {
      throw new FatalException(e.getMessage(), e);
    }
  }

  @Override
  public T get(ResultSet rs) {
    String objectName = getObjectName();

    T object = getObject();
    Field[] fields = object.getClass().getDeclaredFields();

    for (Field field : fields) {
      try {
        field.setAccessible(true);

        if (field.getName().equals("id")) {
          field.set(object, rs.getObject("id_" + objectName));
          continue;
        }

        if (field.getName().equals("role")) {
          for (Role role : Role.values()) {
            if (role.getCode().equals(rs.getString("role"))) {
              field.set(object, role);
              break;
            }
          }
          continue;
        }

        String columnName = hasUpperCaseLetter(field.getName())
            ? toSnakeCase(field.getName())
            : field.getName();

        Object value = rs.getObject(columnName);
        if (value != null) {
          field.set(object, value);
        }
      } catch (IllegalAccessException | SQLException e) {
        throw new FatalException(e.getMessage(), e);
      }
    }

    return object;
  }

  private void setObject(PreparedStatement ps, Field[] fields, Object object, boolean isUpdate) {
    int i = 1;
    for (Field field : fields) {
      field.setAccessible(true);

      try {
        if (field.getName().equals("id")) {
          if (isUpdate) {
            ps.setObject(fields.length, field.get(object));
          }
          continue;
        } else if (field.getName().equals("version")) {
          if (!isUpdate) {
            ps.setInt(i, 1);
          }
          continue;
        } else if (field.getName().equals("registerDate") && !isUpdate) {
          ps.setDate(i, Date.valueOf(LocalDate.now()));
        } else if (field.getName().equals("role")) {
          Role role = (Role) field.get(object);
          String value = (role != null) ? role.getCode() : Role.USER.getCode();
          ps.setString(i, value);
        } else if (field.getName().equals("status")) {
          Status status = (Status) field.get(object);
          String value = (status != null) ? status.getCode() : Status.PROPOSED.getCode();
          ps.setString(i, value);
        } else if (field.getName().startsWith("fk")) {
          T referredObject = (T) field.get(object);

          if (referredObject != null) {
            Class<?> clazz = referredObject.getClass();
            Method method = clazz.getMethod("getId");
            int id = (int) method.invoke(referredObject);

            if (id != 0) {
              ps.setInt(i, id);
            } else {
              ps.setObject(i, null);
            }
          } else {
            ps.setObject(i, null);
          }
        } else {
          ps.setObject(i, field.get(object));
        }
      } catch (SQLException | IllegalAccessException | InvocationTargetException
               | NoSuchMethodException e) {
        throw new FatalException(e.getMessage(), e);
      }

      i++;
    }
  }

  private T getObject() {
    T object;

    try {
      Method method = myDomainFactory.getClass().getMethod(
          "get" + this.getClass().getSimpleName().replace("DAOImpl", ""));

      object = (T) method.invoke(myDomainFactory);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      throw new FatalException(e.getMessage(), e);
    }

    return object;
  }

  private void getFields(Field[] fields, String objectName, StringBuilder sqlBuilder,
      boolean isUpdate) {
    for (Field field : fields) {
      if (objectName == null && field.getName().equals("id")) {
        continue;
      }

      if (field.getName().equals("id")) {
        sqlBuilder.append("id_").append(objectName);
      } else if (hasUpperCaseLetter(field.getName())) {
        sqlBuilder.append(toSnakeCase(field.getName()));
      } else {
        sqlBuilder.append(field.getName());
      }

      if (isUpdate) {
        sqlBuilder.append(" = ?");
      }

      sqlBuilder.append(", ");
    }

    sqlBuilder.delete(sqlBuilder.length() - 2, sqlBuilder.length());
  }

  private String getTableName() {
    String tableName = this.getClass().getSimpleName().toLowerCase()
        .replace("daoimpl", "s");

    if (tableName.equals("availabilitys")) {
      return tableName.replace("ys", "ies");
    }

    if (tableName.equals("objecttypes")) {
      return "types";
    }

    return tableName;
  }

  private String getObjectName() {
    String objectName = this.getClass().getSimpleName().toLowerCase()
        .replace("daoimpl", "");

    if (objectName.equals("objecttype")) {
      return "type";
    }

    return objectName;
  }

  private boolean hasUpperCaseLetter(String str) {
    return !str.equals(str.toLowerCase());
  }

  private String toSnakeCase(String str) {
    return str.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
  }
}
