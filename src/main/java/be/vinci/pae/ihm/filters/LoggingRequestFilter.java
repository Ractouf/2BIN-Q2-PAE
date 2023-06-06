package be.vinci.pae.ihm.filters;

import be.vinci.pae.main.Main;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class logs the requests and responses.
 */
@Provider
public class LoggingRequestFilter implements ContainerResponseFilter, ContainerRequestFilter {

  private static final Logger LOGGER = LogManager.getLogger(Main.class);

  @Override
  public void filter(ContainerRequestContext containerRequestContext) {
    LOGGER.info("Started {} /{}",
        containerRequestContext.getMethod(),
        containerRequestContext.getUriInfo().getPath()
    );
  }

  @Override
  public void filter(ContainerRequestContext containerRequestContext,
      ContainerResponseContext containerResponseContext) {
    LOGGER.info("Finished {} /{} with status {}",
        containerRequestContext.getMethod(),
        containerRequestContext.getUriInfo().getPath(),
        containerResponseContext.getStatus()
    );
  }
}
