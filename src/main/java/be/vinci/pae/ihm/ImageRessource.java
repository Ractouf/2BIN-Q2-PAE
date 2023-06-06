package be.vinci.pae.ihm;

import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import java.io.File;

/**
 * This is the implementation of ImageRessource.
 */
@Singleton
@Path("/images")
public class ImageRessource {

  private static final String UPLOADS_DIR = "uploads/";

  /**
   * This method is used to get an image.
   *
   * @param imageName the name of the image
   * @return the image
   */
  @GET
  @Path("/{imageName}")
  @Produces({"image/png", "image/jpeg", "image/gif"})
  public Response getImage(@PathParam("imageName") String imageName) {
    if (imageName == null || imageName.isEmpty()) {
      throw new WebApplicationException("Nom de l'image manquant.", Response.Status.BAD_REQUEST);
    }
    File imageFile = new File(UPLOADS_DIR + imageName);

    if (!imageFile.exists()) {
      throw new WebApplicationException("Image non trouvée.", Response.Status.NOT_FOUND);
    }

    Response.ResponseBuilder responseBuilder = Response.ok(imageFile);
    responseBuilder.header("Content-Disposition", "attachment; filename=\"" + imageName + "\"");
    responseBuilder.header("Content-Type", getContentType(imageName));

    return responseBuilder.build();
  }

  /**
   * This method is used to get the content type of image.
   *
   * @param imageName the name of the image
   * @return the content type of the image
   */
  private String getContentType(String imageName) {
    if (imageName.endsWith(".png")) {
      return "image/png";
    } else if (imageName.endsWith(".jpeg") || imageName.endsWith(".jpg")) {
      return "image/jpeg";
    } else if (imageName.endsWith(".gif")) {
      return "image/gif";
    } else {
      throw new IllegalArgumentException("Type d'image non supporté.");
    }
  }
}
