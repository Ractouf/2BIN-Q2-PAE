package be.vinci.pae.dal.dao;

import be.vinci.pae.business.dto.ObjectDTO;
import java.util.List;
import java.util.Map;

/**
 * This is the interface of ObjectDAOImpl.
 */
public interface ObjectDAO extends GenericDAO<ObjectDTO> {

  /**
   * Get proposed objects.
   *
   * @return a list of object
   */
  List<ObjectDTO> listOfObjectsProposed();

  /**
   * Gets all the objects of a given user.
   *
   * @param id of the user
   * @return the list of objects
   */
  List<ObjectDTO> getListObjectByUser(int id);

  /**
   * Gets the number of objects by status.
   *
   * @return a map of the number of objects by status
   */
  Map<String, Integer> getNbrObjectsByStatus();

  /**
   * Gets the number of objects proposed by month and year.
   *
   * @return a map containing the number of objects proposed by month and year
   */
  Map<Integer, Map<Integer, Integer>> getNbrObjectsProposedByMonth();

  /**
   * Gets 'SH' || 'SA' || 'SO' objects.
   *
   * @return a list of those objects
   */
  List<ObjectDTO> getHomePageObjects();

  /**
   * Gets objects on sale.
   *
   * @return a list of objects on sale
   */
  List<ObjectDTO> getObjectsInStore();
}
