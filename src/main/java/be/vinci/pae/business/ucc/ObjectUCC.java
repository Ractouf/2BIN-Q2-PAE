package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.ObjectDTO;
import be.vinci.pae.business.dto.ObjectDTO.Status;
import java.util.List;
import java.util.Map;

/**
 * This is the interface of ObjectUCCImpl.
 */
public interface ObjectUCC {

  /**
   * Gets all the objects.
   *
   * @return a list of all the objects
   */
  List<ObjectDTO> getListObjects();

  /**
   * Gets all the objects of a given user.
   *
   * @param id of the user
   * @return the list of objects
   */
  List<ObjectDTO> getListObjectByUser(int id);

  /**
   * Gets an objects with given id.
   *
   * @param id of object to get
   * @return a DTO Object
   */
  ObjectDTO getById(int id);

  /**
   * Changes the Object status to the new given status.
   *
   * @param id        of the Object to update
   * @param newStatus new status of the object
   * @return The updated Object
   */
  ObjectDTO updateObjectStatus(int id, Status newStatus);

  /**
   * Refuse proposed object with a refusal reason.
   *
   * @param id            of object to refuse
   * @param refusalReason reason why the object was refused
   * @return The refused object
   */
  ObjectDTO refuseObject(int id, String refusalReason);

  /**
   * Get proposed objects.
   *
   * @return a list of object
   */
  List<ObjectDTO> listOfObjectsProposed();

  /**
   * Edits an object to the given changes.
   *
   * @param object to edit
   * @return the edited object
   */
  ObjectDTO editObject(ObjectDTO object);

  /**
   * Creates a new object.
   *
   * @param object to create
   * @return the created object
   */
  ObjectDTO proposeObject(ObjectDTO object);

  /**
   * Gets the number of objects by status.
   *
   * @return a map of the number of objects by status
   */
  Map<String, Integer> getNbrObjectsByStatus();

  /**
   * Gets the number of objects proposed by month and year.
   *
   * @return a map of the number of objects proposed by month
   */
  Map<Integer, Map<Integer, Integer>> getNbrObjectsProposedByMonth();

  /**
   * Gets 'SH' || 'SA' || 'SO' objects.
   *
   * @return a list of those objects
   */
  List<ObjectDTO> getHomePageObjects();
}
