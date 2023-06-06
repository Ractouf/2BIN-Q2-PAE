package be.vinci.pae.dal.dao;

import java.sql.ResultSet;
import java.util.List;

/**
 * This is the interface of GenericDAOImpl.
 *
 * @param <T> the type of the object
 */
public interface GenericDAO<T> {

  /**
   * Gets all the objects.
   *
   * @return a List of all the objects
   */
  List<T> getAll();

  /**
   * Finds an object with the given id.
   *
   * @param id of the object to find
   * @return the object or null if none found
   */
  T getById(int id);

  /**
   * Creates an object.
   *
   * @param object to create
   * @return the created object
   */
  T createOne(T object);

  /**
   * Updates an object.
   *
   * @param object to update
   * @return the updated object
   */
  T updateOne(T object);

  /**
   * Deletes an object.
   *
   * @param object to delete
   */
  void deleteOne(T object);

  /**
   * Gets an object from a ResultSet.
   *
   * @param rs the ResultSet
   * @return the object
   */
  T get(ResultSet rs);
}
