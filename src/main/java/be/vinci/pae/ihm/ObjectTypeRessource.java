package be.vinci.pae.ihm;

import be.vinci.pae.business.dto.ObjectTypeDTO;
import be.vinci.pae.business.ucc.ObjectTypeUCC;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

/**
 * This class contains the object type route.
 */
@Singleton
@Path("/objectTypes")
public class ObjectTypeRessource {

  @Inject
  private ObjectTypeUCC myObjectTypeUCC;

  /**
   * Gets all the object types.
   *
   * @return a list of all the object types
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<ObjectTypeDTO> getObjectTypes() {
    return myObjectTypeUCC.getListObjectTypes();
  }
}
