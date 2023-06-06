package be.vinci.pae.ihm;

import be.vinci.pae.business.Factory;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.ucc.UserUCC;
import be.vinci.pae.ihm.filters.Authorize;
import be.vinci.pae.utils.Config;
import be.vinci.pae.utils.ImageManager;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.io.InputStream;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

/**
 * This class contains the login and register routes.
 */
@Singleton
@Path("/auths")
public class AuthRessource {

  private static final String UPLOAD_DIR = "uploads";

  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  private final ObjectMapper jsonMapper = new ObjectMapper();
  @Inject
  private UserUCC myUserUCC;
  @Inject
  private Factory factory;

  /**
   * Gets the email and password. Checks that the fields are not empty and that the email and
   * password exist.
   *
   * @param json contains email and password
   * @return an object containing a token and the user logged in
   */
  @POST
  @Path("login")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode login(JsonNode json) {
    String email = json.get("email").asText();
    String password = json.get("password").asText();

    if (email == null || password == null || email.isBlank() || password.isBlank()) {
      throw new WebApplicationException("Email ou mot de passe manquant.",
          Response.Status.BAD_REQUEST);
    }

    // Try to log in
    UserDTO user = myUserUCC.login(email, password);

    ObjectNode returned = jsonMapper.createObjectNode();
    returned.put("token", createToken(user).get("token").asText());
    returned.putPOJO("user", user);

    return returned;
  }

  /**
   * Gets the email, password, lastname, firstname, photo and phone number. Checks that the fields
   * are not empty and creates a new user.
   *
   * @param file            the photo of the user
   * @param fileDisposition the name of the photo
   * @param email           the email of the user
   * @param password        the password of the user
   * @param lastname        the lastname of the user
   * @param firstname       the firstname of the user
   * @param phone           the phone number of the user
   * @return a token
   */
  @POST
  @Path("register")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode register(@FormDataParam("file") InputStream file,
      @FormDataParam("file") FormDataContentDisposition fileDisposition,
      @FormDataParam("email") String email, @FormDataParam("password") String password,
      @FormDataParam("lastname") String lastname, @FormDataParam("firstname") String firstname,
      @FormDataParam("gsm") String phone) {
    if (email == null || password == null || lastname == null || firstname == null || phone == null
        || email.isBlank() || password.isBlank() || lastname.isBlank() || firstname.isBlank()
        || phone.isBlank()) {
      throw new WebApplicationException(
          "Email, mot de passe, nom, prénom ou numéro de téléphone manquant.",
          Response.Status.BAD_REQUEST);
    }

    if (password.length() < 8) {
      throw new WebApplicationException("Le mot de passe doit contenir au moins 8 caractères.",
          Response.Status.BAD_REQUEST);
    }

    if (password.matches(".*\\s.*")) {
      throw new WebApplicationException("Le mot de passe ne doit pas contenir d'espace.",
          Response.Status.BAD_REQUEST);
    }

    if (!password.matches("(?=.*\\d)(?=.*[^\\w\\s]).+")) {
      throw new WebApplicationException(
          "Le mot de passe doit contenir au moins "
              + "un chiffre et un caractère spécial.",
          Response.Status.BAD_REQUEST);
    }

    if (!phone.matches("0[1-9]\\d{2}\\/\\d{2}\\.\\d{2}\\.\\d{2}")) {
      throw new WebApplicationException("Le numéro de téléphone n'est pas valide (0000/00.00.00).",
          Response.Status.BAD_REQUEST);
    }

    if (email.matches(".*\\s.*")) {
      throw new WebApplicationException("L'email ne doit pas contenir d'espace.",
          Response.Status.BAD_REQUEST);
    }

    if (!email.matches("\\b[\\w.%-]+@[\\w.-]+\\.[A-Za-z]{2,}\\b")) {
      throw new WebApplicationException("L'email n'est pas valide", Response.Status.BAD_REQUEST);
    }

    boolean photoPresent = fileDisposition.getFileName() != null
        && !fileDisposition.getFileName().isBlank();

    String fileName;
    if (photoPresent) {
      String originalFileName = fileDisposition.getFileName();
      String fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.'));
      String uuid = UUID.randomUUID().toString();
      fileName = uuid + fileExtension;
    } else {
      fileName = "default-user.png";
    }

    UserDTO user = factory.getUser();
    user.setEmail(email);
    user.setPassword(password);
    user.setRegisterDate(java.sql.Date.valueOf(LocalDate.now()));
    user.setLastname(lastname);
    user.setFirstname(firstname);
    user.setPhoto(fileName);
    user.setPhoneNumber(phone);

    // Try to register
    user = myUserUCC.register(user);

    if (photoPresent) {
      String filePath = Paths.get(UPLOAD_DIR, fileName).toString();

      ImageManager.uploadImage(file, filePath);
    }

    ObjectNode returned = jsonMapper.createObjectNode();
    returned.put("token", createToken(user).get("token").asText());
    returned.putPOJO("user", user);
    return returned;
  }

  /**
   * Creates a token for a given user.
   *
   * @param user that needs a token
   * @return the token or null if error occurred
   */
  public ObjectNode createToken(UserDTO user) {
    String token;
    try {
      long expirationTime = System.currentTimeMillis() + (60 * 60 * 1000);
      Date expirationDate = new Date(expirationTime);

      token = JWT.create()
          .withIssuer("auth0")
          .withClaim("user", user.getId())
          .withExpiresAt(expirationDate)
          .sign(this.jwtAlgorithm);

      return jsonMapper.createObjectNode()
          .put("token", token)
          .put("id", user.getId());
    } catch (Exception e) {
      throw new WebApplicationException("Erreur lors de la création du token.",
          Status.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Returns the user associated with the JWT token in the Authorization header.
   *
   * @param requestContext the current request context
   * @return the authenticated user
   */
  @GET
  @Path("user")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public UserDTO getUserFromToken(@Context ContainerRequestContext requestContext) {
    return (UserDTO) requestContext.getProperty("user");
  }
}
