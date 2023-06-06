package be.vinci.pae.ihm;

import be.vinci.pae.business.Factory;
import be.vinci.pae.business.dto.AvailabilityDTO;
import be.vinci.pae.business.dto.UserDTO.Role;
import be.vinci.pae.business.ucc.AvailabilityUCC;
import be.vinci.pae.ihm.filters.Authorize;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

/**
 * This class contains the availabilities route.
 */
@Singleton
@Path("/availabilities")
public class AvailabilitiesRessource {

  @Inject
  private AvailabilityUCC myAvailabilityUCC;
  @Inject
  private Factory factory;

  /**
   * Gets all the availabilities.
   *
   * @return a list of all the availabilities
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<AvailabilityDTO> getAvailabilities() {
    return myAvailabilityUCC.getAvailabilities();
  }

  /**
   * Create one availability.
   *
   * @param json the json containing info about the availability
   * @return the just created availability
   */
  @POST
  @Authorize(role = Role.HELPER)
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public AvailabilityDTO createOneAvailability(JsonNode json) {
    String availabilityDate = json.get("availabilityDate").asText();
    String startingHour = json.get("startingHour").asText();
    String endingHour = json.get("endingHour").asText();

    if (availabilityDate == null || availabilityDate.isEmpty()) {
      throw new WebApplicationException("La date de disponibilité est manquante",
          Response.Status.BAD_REQUEST);
    }

    if (startingHour == null || startingHour.isEmpty()) {
      throw new WebApplicationException("L'heure de debut est manquante",
          Response.Status.BAD_REQUEST);
    }

    if (endingHour == null || endingHour.isEmpty()) {
      throw new WebApplicationException("L'heure de fin est manquante",
          Response.Status.BAD_REQUEST);
    }
    AvailabilityDTO availability = factory.getAvailability();

    availability.setAvailabilityDate(Date.valueOf(availabilityDate));
    availability.setStartingHour(Time.valueOf(startingHour));
    availability.setEndingHour(Time.valueOf(endingHour));

    String availabilityDateStr = availability.getAvailabilityDate().toString() + "-"
        + availability.getStartingHour().toString()
        .substring(0, availability.getStartingHour().toString().length() - 3);
    if (myAvailabilityUCC.getAvailabilityFromString(availabilityDateStr) != null) {
      throw new WebApplicationException("La disponibilité existe déjà",
          Status.BAD_REQUEST);
    }

    return myAvailabilityUCC.createOneAvailability(availability);
  }
}
