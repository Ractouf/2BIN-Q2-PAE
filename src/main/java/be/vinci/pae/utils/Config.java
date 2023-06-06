package be.vinci.pae.utils;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Get the different properties in the dev.properties file.
 */
public class Config {

  private static Properties props;

  /**
   * Read a properties file and put them in a properties object.
   *
   * @param file the file to get properties from
   */
  public static void load(String file) {
    props = new Properties();
    try (InputStream input = new FileInputStream(file)) {
      props.load(input);
    } catch (IOException e) {
      throw new WebApplicationException(
          Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).type("text/plain")
              .build());
    }
  }

  /**
   * Get a property from the properties object.
   *
   * @param key the key of the property
   * @return the value of the property as a String
   */
  public static String getProperty(String key) {
    return props.getProperty(key);
  }

  /**
   * Get a property from the properties object.
   *
   * @param key the key of the property
   * @return the value of the property as an Integer
   */
  public static Integer getIntProperty(String key) {
    return Integer.parseInt(props.getProperty(key));
  }

  /**
   * Get a property from the properties object.
   *
   * @param key the key of the property
   * @return the value of the property as a Boolean
   */
  public static boolean getBoolProperty(String key) {
    return Boolean.parseBoolean(props.getProperty(key));
  }
}
