package be.vinci.pae.ihm;

import be.vinci.pae.business.dto.UserDTO.Role;
import be.vinci.pae.business.ucc.ObjectUCC;
import be.vinci.pae.business.ucc.UserUCC;
import be.vinci.pae.ihm.filters.Authorize;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.util.Map;

/**
 * This class contains the dashboard routes.
 */
@Singleton
@Authorize(role = Role.HELPER)
@Path("/dashboard")
public class DashboardRessource {

  private final ObjectMapper jsonMapper = new JsonMapper();
  @Inject
  private UserUCC userUCC;
  @Inject
  private ObjectUCC objectUCC;

  /**
   * Returns the statistics of the application for the dashboard.
   *
   * @return the statistics of the application
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode getStatistics() {
    int nbrUsers = userUCC.getNbrUsers();
    int nbrHelpers = userUCC.getNbrHelpers();

    Map<String, Integer> nbrObjectsByStatus = objectUCC.getNbrObjectsByStatus();
    Map<Integer, Map<Integer, Integer>> nbrObjectsProposedByMonth =
        objectUCC.getNbrObjectsProposedByMonth();

    if (nbrUsers == -1 || nbrHelpers == -1 || nbrObjectsByStatus == null) {
      throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
          .entity("An error occured while getting the statistics.").build());
    }

    ObjectNode nbrObjectsProposedByMonthJson = jsonMapper.createObjectNode();
    for (Map.Entry<Integer, Map<Integer, Integer>> entry : nbrObjectsProposedByMonth.entrySet()) {
      ObjectNode nbrObjectsProposedByMonthYearJson = jsonMapper.createObjectNode();
      for (Map.Entry<Integer, Integer> entry2 : entry.getValue().entrySet()) {
        nbrObjectsProposedByMonthYearJson.put(entry2.getKey().toString(),
            entry2.getValue());
      }
      nbrObjectsProposedByMonthJson.set(entry.getKey().toString(),
          nbrObjectsProposedByMonthYearJson);
    }

    ObjectNode status = jsonMapper.createObjectNode();
    for (Map.Entry<String, Integer> entry : nbrObjectsByStatus.entrySet()) {
      status.put(entry.getKey(), entry.getValue());
    }

    ObjectNode json = jsonMapper.createObjectNode();
    json.put("nbrUsers", nbrUsers);
    json.put("nbrHelpers", nbrHelpers);
    json.set("status", status);
    json.set("nbrObjectsProposedByMonth", nbrObjectsProposedByMonthJson);

    return json;
  }
}
