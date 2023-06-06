package be.vinci.pae.exceptions;

import be.vinci.pae.main.Main;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Maps unhandled exceptions to HTTP responses.
 */
@Provider
public class WebExceptionMapper implements ExceptionMapper<Throwable> {

  private static final Logger LOGGER = LogManager.getLogger(Main.class);

  @Override
  public Response toResponse(Throwable exception) {
    LOGGER.error(exception.getMessage(), exception);
    ObjectNode mapper = new ObjectMapper().createObjectNode();
    if (exception instanceof WebApplicationException webEx) {
      return Response.status(webEx.getResponse().getStatus())
          .entity(mapper.put("message", webEx.getMessage()).toString())
          .type(MediaType.APPLICATION_JSON)
          .build();
    } else if (exception instanceof BizExceptionConflict bizConEx) {
      return Response.status(Response.Status.CONFLICT)
          .entity(mapper.put("message", bizConEx.getMessage()).toString())
          .type(MediaType.APPLICATION_JSON)
          .build();
    } else if (exception instanceof BizExceptionForbidden bizForEx) {
      return Response.status(Response.Status.FORBIDDEN)
          .entity(mapper.put("message", bizForEx.getMessage()).toString())
          .type(MediaType.APPLICATION_JSON)
          .build();
    } else if (exception instanceof BizExceptionUnauthorized bizUnEx) {
      return Response.status(Response.Status.UNAUTHORIZED)
          .entity(mapper.put("message", bizUnEx.getMessage()).toString())
          .type(MediaType.APPLICATION_JSON)
          .build();
    } else if (exception instanceof BizExceptionNotFound bizNotFoundEx) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity(mapper.put("message", bizNotFoundEx.getMessage()).toString())
          .type(MediaType.APPLICATION_JSON)
          .build();
    } else if (exception instanceof FatalException) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity(mapper.put("message", "Erreur serveur, veuillez contactez l'administrateur")
              .toString())
          .type(MediaType.APPLICATION_JSON)
          .build();
    } else if (exception instanceof OptimisticLockException) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity(mapper.put("message", "Veuillez actualiser la page")
              .toString())
          .type(MediaType.APPLICATION_JSON)
          .build();
    }
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .entity(mapper.put("message", "Erreur serveur, veuillez contactez l'administrateur")
            .toString())
        .type(MediaType.APPLICATION_JSON)
        .build();
  }
}
