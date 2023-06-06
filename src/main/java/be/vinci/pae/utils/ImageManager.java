package be.vinci.pae.utils;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;

/**
 * This class deletes and uploads images.
 */
public class ImageManager {

  /**
   * Deletes the given image.
   *
   * @param fileName to be deleted
   */
  public static void deleteImage(String fileName) {
    try {
      java.nio.file.Path path = java.nio.file.Path.of(fileName);
      Files.delete(path);
    } catch (NoSuchFileException e) {
      throw new WebApplicationException("Fichier non trouvé.", Response.Status.NOT_FOUND);
    } catch (IOException e) {
      throw new WebApplicationException("Erreur lors de la suppression du fichier.",
          Response.Status.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Uploads the given file to given path.
   *
   * @param file     to be uploaded
   * @param filePath to upload to
   */
  public static void uploadImage(InputStream file, String filePath) {
    try {
      java.nio.file.Path path = java.nio.file.Path.of(filePath);
      Files.createDirectories(path.getParent());
      Files.copy(file, path);
    } catch (IOException e) {
      throw new WebApplicationException("Erreur lors de l'upload du fichier.",
          Response.Status.INTERNAL_SERVER_ERROR);
    }
  }
}
