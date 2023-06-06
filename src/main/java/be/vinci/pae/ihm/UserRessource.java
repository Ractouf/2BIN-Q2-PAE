package be.vinci.pae.ihm;

import be.vinci.pae.business.biz.User;
import be.vinci.pae.business.dto.NotificationUserDTO;
import be.vinci.pae.business.dto.ObjectDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.dto.UserDTO.Role;
import be.vinci.pae.business.ucc.NotificationUCC;
import be.vinci.pae.business.ucc.ObjectUCC;
import be.vinci.pae.business.ucc.UserUCC;
import be.vinci.pae.ihm.filters.Authorize;
import be.vinci.pae.utils.ImageManager;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

/**
 * This class contains the user route.
 */
@Singleton
@Path("/users")
public class UserRessource {

  private static final String UPLOAD_DIR = "uploads";
  @Inject
  private UserUCC myUserUCC;
  @Inject
  private NotificationUCC myNotificationUCC;
  @Inject
  private ObjectUCC myObjectUCC;

  /**
   * Gets all the users ordered by date.
   *
   * @return a list of all the users
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(role = Role.MANAGER)
  public List<UserDTO> getAllByDate() {
    return myUserUCC.getAllByDate();
  }

  /**
   * Promotes a user to helper or manager.
   *
   * @param idUser the idUser of te user
   * @return the user which role has been changed
   */
  @PATCH
  @Path("{idUser}/promote")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(role = Role.MANAGER)
  public UserDTO promote(@PathParam("idUser") int idUser) {
    return myUserUCC.promote(idUser);
  }

  /**
   * Gets all the objects of a given user.
   *
   * @param idUser of the user
   * @return the list of objects
   */
  @GET
  @Path("{idUser}/objects")
  @Authorize
  @Produces(MediaType.APPLICATION_JSON)
  public List<ObjectDTO> getListObjectByUser(@PathParam("idUser") int idUser) {
    return myObjectUCC.getListObjectByUser(idUser);
  }

  /**
   * Get a specific user based on its idUser.
   *
   * @param idUser the idUser of the user
   * @return the user with the specific idUser
   */
  @GET
  @Path("{idUser}")
  @Authorize(role = Role.HELPER)
  @Produces(MediaType.APPLICATION_JSON)
  public UserDTO getUserById(@PathParam("idUser") int idUser) {
    return myUserUCC.getUserById(idUser);
  }

  /**
   * Gets all the notifications of a user.
   *
   * @param idUser the idUser of the user
   * @return A list of all the user notifications
   */
  @GET
  @Path("{idUser}/notifications")
  @Authorize(isRightUser = true)
  @Produces(MediaType.APPLICATION_JSON)
  public List<NotificationUserDTO> getUserNotificationsById(@PathParam("idUser") int idUser) {
    return myNotificationUCC.getUserNotificationsById(idUser);
  }

  /**
   * Reads one user notification.
   *
   * @param idUser         of the notification
   * @param idNotification of the notification
   * @return the read notification
   */
  @PATCH
  @Path("{idUser}/notifications/{idNotification}")
  @Authorize(isRightUser = true)
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public NotificationUserDTO readNotification(@PathParam("idUser") int idUser,
      @PathParam("idNotification") int idNotification) {
    NotificationUserDTO notificationUser = myNotificationUCC.getOne(idUser, idNotification);

    notificationUser.setRead(true);

    notificationUser = myNotificationUCC.updateOne(notificationUser);

    return notificationUser;
  }

  /**
   * Edits the authenticated user.
   *
   * @param file            photo of the user
   * @param fileDisposition details of the photo
   * @param lastname        last name of the user
   * @param firstname       first name of the user
   * @param email           email of the user
   * @param phone           phone number of the user
   * @param password        password of the user
   * @param idUser          idUser of the user
   * @return updated user
   */
  @PATCH
  @Path("{idUser}/edit")
  @Authorize
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  public UserDTO editUser(@FormDataParam("file") InputStream file,
      @FormDataParam("file") FormDataContentDisposition fileDisposition,
      @FormDataParam("lastname") String lastname,
      @FormDataParam("firstname") String firstname,
      @FormDataParam("email") String email,
      @FormDataParam("phone") String phone,
      @FormDataParam("password") String password,
      @PathParam("idUser") int idUser) {
    if (lastname == null || lastname.isBlank() || firstname == null || firstname.isBlank()) {
      throw new WebApplicationException("le nom et prénom ne peuvent pas êtres nulls",
          Status.BAD_REQUEST);
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

    UserDTO user = myUserUCC.getUserById(idUser);

    if (password != null && !password.equals("")) {
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
      User userHash = (User) user;
      user.setPassword(userHash.hashPassword(password));
    }

    String fileName;
    if (fileDisposition.getFileName() != null && !fileDisposition.getFileName().isBlank()) {
      String originalFileName = fileDisposition.getFileName();
      String fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.'));
      String uuid = UUID.randomUUID().toString();
      fileName = uuid + fileExtension;

      String currentFileName = Paths.get(UPLOAD_DIR, user.getPhoto()).toString();
      ImageManager.deleteImage(currentFileName);

      String filePath = Paths.get(UPLOAD_DIR, fileName).toString();

      ImageManager.uploadImage(file, filePath);

      user.setPhoto(fileName);
    }

    user.setFirstname(firstname);
    user.setLastname(lastname);
    user.setEmail(email);
    user.setPhoneNumber(phone);

    return myUserUCC.editUser(user);
  }
}
