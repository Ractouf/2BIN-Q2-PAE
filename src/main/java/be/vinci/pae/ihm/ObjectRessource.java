package be.vinci.pae.ihm;

import be.vinci.pae.business.Factory;
import be.vinci.pae.business.dto.AvailabilityDTO;
import be.vinci.pae.business.dto.NotificationDTO;
import be.vinci.pae.business.dto.ObjectDTO;
import be.vinci.pae.business.dto.ObjectDTO.Status;
import be.vinci.pae.business.dto.ObjectTypeDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.dto.UserDTO.Role;
import be.vinci.pae.business.ucc.AvailabilityUCC;
import be.vinci.pae.business.ucc.NotificationUCC;
import be.vinci.pae.business.ucc.ObjectTypeUCC;
import be.vinci.pae.business.ucc.ObjectUCC;
import be.vinci.pae.business.ucc.UserUCC;
import be.vinci.pae.ihm.filters.Authorize;
import be.vinci.pae.utils.ImageManager;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.InputStream;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

/**
 * This class contains the object route.
 */
@Singleton
@Path("/objects")
public class ObjectRessource {

  private static final String UPLOAD_DIR = "uploads";

  @Inject
  private ObjectUCC myObjectUCC;
  @Inject
  private ObjectTypeUCC myObjectTypeUCC;
  @Inject
  private UserUCC myUserUCC;
  @Inject
  private AvailabilityUCC myAvailabilityUCC;
  @Inject
  private NotificationUCC myNotificationUCC;
  @Inject
  private Factory factory;

  /**
   * Gets all the objects.
   *
   * @return a list of all the objects
   */
  @GET
  @Authorize(role = Role.HELPER)
  @Produces(MediaType.APPLICATION_JSON)
  public List<ObjectDTO> getObjects() {
    return myObjectUCC.getListObjects();
  }

  /**
   * Gets all the home page objects.
   *
   * @return a list of all the objects
   */
  @GET
  @Path("/home")
  @Produces(MediaType.APPLICATION_JSON)
  public List<ObjectDTO> getHomePageObjects() {
    return myObjectUCC.getHomePageObjects();
  }

  /**
   * Gets an objects with given id.
   *
   * @param id of object to get
   * @return a DTO Object
   */
  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectDTO getById(@PathParam("id") int id) {
    return myObjectUCC.getById(id);
  }

  /**
   * Get proposed objects.
   *
   * @return a list of object
   */
  @GET
  @Path("proposed")
  @Authorize(role = Role.MANAGER)
  @Produces(MediaType.APPLICATION_JSON)
  public List<ObjectDTO> listOfObjectsProposed() {
    return myObjectUCC.listOfObjectsProposed();
  }

  /**
   * Creates an object with the "PROPOSED" status.
   *
   * @param file                   the photo of the objects
   * @param fileDisposition        the name of the photo of the user
   * @param type                   of the object
   * @param description            of the object
   * @param availability           of the object
   * @param offeringMemberId       of the object
   * @param unknownUserPhoneNumber of the object
   * @return the created object
   */
  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectDTO proposeObject(@FormDataParam("file") InputStream file,
      @FormDataParam("file") FormDataContentDisposition fileDisposition,
      @FormDataParam("type") String type, @FormDataParam("description") String description,
      @FormDataParam("availability") String availability,
      @FormDataParam("offering_member") int offeringMemberId,
      @FormDataParam("unknown_user_phone_number") String unknownUserPhoneNumber) {
    if (type == null || description == null || availability == null
        || type.isBlank() || description.isBlank() || availability.isBlank()) {
      throw new WebApplicationException(
          "Type, description ou disponibilité manquante.", Response.Status.BAD_REQUEST
      );
    }

    if (description.length() > 120) {
      throw new WebApplicationException(
          "Description trop longue, maximum 120 caractères", Response.Status.BAD_REQUEST
      );
    }

    if (!availability.matches(
        "^\\d{4}-(0[1-9]|1[0-2])-([0-2][1-9]|[1-3]0|31)-([01]\\d|2[0-3]):[0-5]\\d$")) {
      throw new WebApplicationException(
          "La disponibilité n'est pas dans le bon format (YYYY-MM-DD-HH:MM)",
          Response.Status.BAD_REQUEST);
    }

    if (offeringMemberId == 0
        && !unknownUserPhoneNumber.matches("0[1-9]\\d{2}\\/\\d{2}\\.\\d{2}\\.\\d{2}")) {
      throw new WebApplicationException(
          "Le numéro de téléphone n'est pas valide (0000/00.00.00).",
          Response.Status.BAD_REQUEST);
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
      fileName = "default-object.png";
    }

    ObjectTypeDTO objectType = myObjectTypeUCC.getObjectTypeByName(type);
    AvailabilityDTO availabilityObject = myAvailabilityUCC.getAvailabilityFromString(availability);

    ObjectDTO object = factory.getObject();
    object.setProposalDate(Date.valueOf(LocalDate.now()));
    object.setDescription(description);
    object.setPhoto(fileName);
    object.setFkObjectType(objectType);
    object.setFkAvailability(availabilityObject);

    if (offeringMemberId != 0) {
      UserDTO offeringMember = myUserUCC.getUserById(offeringMemberId);
      object.setFkOfferingMember(offeringMember);
    } else {
      object.setUnknownUserPhoneNumber(unknownUserPhoneNumber);
    }

    object = myObjectUCC.proposeObject(object);

    if (photoPresent) {
      String filePath = Paths.get(UPLOAD_DIR, fileName).toString();
      ImageManager.uploadImage(file, filePath);
    }

    NotificationDTO notification = factory.getNotification();
    notification.setTextNotification(
        "Un nouvel objet " + "\"" + object.getDescription() + "\"" + " a été proposé");
    notification.setFkConcernedObject(object);

    notification = myNotificationUCC.createNotification(notification);

    myNotificationUCC.notifyAdmins(notification);

    return object;
  }

  /**
   * Edits an object to the given changes.
   *
   * @param file            the photo of the objects
   * @param fileDisposition the name of the photo of the user
   * @param price           the price of the object
   * @param type            the type of the object
   * @param description     the description of the object
   * @param status          the status of the object
   * @param id              the id of the object
   * @return the modified object
   */
  @PATCH
  @Path("{id}/edit")
  @Authorize(role = Role.HELPER)
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectDTO editObject(@FormDataParam("file") InputStream file,
      @FormDataParam("file") FormDataContentDisposition fileDisposition,
      @FormDataParam("price") double price, @FormDataParam("type") String type,
      @FormDataParam("description") String description, @FormDataParam("status") String status,
      @PathParam("id") int id) {
    if (price < 0) {
      throw new WebApplicationException("Le prix doit être positif.", Response.Status.BAD_REQUEST);
    }

    if (price > 10) {
      throw new WebApplicationException("Le prix doit être inférieur à 10.",
          Response.Status.BAD_REQUEST);
    }

    if (type == null || type.isBlank() || description == null || description.isBlank()) {
      throw new WebApplicationException("Type ou description manquant.",
          Response.Status.BAD_REQUEST);
    }

    ObjectDTO object = myObjectUCC.getById(id);

    if (status != null) {
      if (status.equals("SOLD") && price == 0) {
        throw new WebApplicationException("Veuillez mentionner un prix",
            Response.Status.BAD_REQUEST);
      }
      object = myObjectUCC.updateObjectStatus(id, Status.valueOf(status));
    }

    if (object.getStatus() == Status.SHOP && price != 0) {
      object = myObjectUCC.updateObjectStatus(id, Status.ON_SALE);
    }

    boolean photoPresent = fileDisposition.getFileName() != null
        && !fileDisposition.getFileName().isBlank();

    String fileName;
    if (photoPresent) {
      String originalFileName = fileDisposition.getFileName();
      String fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.'));
      String uuid = UUID.randomUUID().toString();
      fileName = uuid + fileExtension;

      String currentFileName = Paths.get(UPLOAD_DIR, object.getPhoto()).toString();

      ImageManager.deleteImage(currentFileName);

      String filePath = Paths.get(UPLOAD_DIR, fileName).toString();

      ImageManager.uploadImage(file, filePath);

      object.setPhoto(fileName);
    }

    ObjectTypeDTO objectType = myObjectTypeUCC.getObjectTypeByName(type);

    object.setSellingPrice(price);
    object.setFkObjectType(objectType);
    object.setDescription(description);

    return myObjectUCC.editObject(object);
  }

  /**
   * Changes the Object status from "PROPOSED" to "ACCEPTED".
   *
   * @param id the id of the Object
   * @return the Patched Object
   */
  @PATCH
  @Path("{id}/accept")
  @Authorize(role = Role.MANAGER)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectDTO acceptObject(@PathParam("id") int id) {
    ObjectDTO object = myObjectUCC.updateObjectStatus(id, Status.ACCEPTED);

    if (object == null) {
      throw new WebApplicationException("L'objet n'existe pas ou n'est pas proposé.",
          Response.Status.BAD_REQUEST);
    }

    if (object.getFkOfferingMember().getId() != 0) {
      NotificationDTO notification = factory.getNotification();
      notification.setFkConcernedObject(object);
      notification.setTextNotification(
          "Votre objet " + "\"" + object.getDescription() + "\"" + " a été accepté");

      notification = myNotificationUCC.createNotification(notification);

      myNotificationUCC.notifyUser(notification, object.getFkOfferingMember());
    }

    return object;
  }

  /**
   * Changes the Object status from "PROPOSED" to "REFUSED" and sets the reason for refusal.
   *
   * @param id   the id of the Object
   * @param json the reason why the object was refused
   * @return the Patched Object
   */
  @PATCH
  @Path("{id}/refuse")
  @Authorize(role = Role.MANAGER)
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectDTO refuseObject(@PathParam("id") int id,
      JsonNode json) {
    String refusalReason = json.get("refusalReason").asText();

    if (refusalReason == null || refusalReason.isEmpty()) {
      throw new WebApplicationException("La raison du refus est manquante.",
          Response.Status.BAD_REQUEST);
    }

    ObjectDTO object = myObjectUCC.refuseObject(id, refusalReason);

    if (object == null) {
      throw new WebApplicationException("L'objet n'existe pas ou n'est pas proposé.",
          Response.Status.BAD_REQUEST);
    }

    if (object.getFkOfferingMember() != null) {
      NotificationDTO notification = factory.getNotification();
      notification.setFkConcernedObject(object);
      notification.setTextNotification(
          "Votre objet " + "\"" + object.getDescription() + "\"" + " a été refusé");

      notification = myNotificationUCC.createNotification(notification);

      myNotificationUCC.notifyUser(notification, object.getFkOfferingMember());
    }

    return object;
  }

  /**
   * Changes the Object status from "ACCEPTED" to "WORKSHOP".
   *
   * @param id the id of the Object
   * @return the Patched Object
   */
  @PATCH
  @Path("{id}/workshop")
  @Authorize(role = Role.MANAGER)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectDTO setObjectInWorkshop(@PathParam("id") int id) {
    ObjectDTO object = myObjectUCC.updateObjectStatus(id, Status.WORKSHOP);

    if (object == null) {
      throw new WebApplicationException("L'objet n'existe pas ou n'est pas accepté.",
          Response.Status.BAD_REQUEST);
    }

    return object;
  }

  /**
   * Changes the Object status from "ACCEPTED" || "WORKSHOP" to "SHOP".
   *
   * @param id the id of the Object
   * @return the Patched Object
   */
  @PATCH
  @Path("{id}/shop")
  @Authorize(role = Role.MANAGER)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectDTO setObjectInShop(@PathParam("id") int id) {
    ObjectDTO object = myObjectUCC.updateObjectStatus(id, Status.SHOP);

    if (object == null) {
      throw new WebApplicationException(
          "L'objet n'existe pas ou n'est pas accepté ou en atelier.",
          Response.Status.BAD_REQUEST);
    }

    return object;
  }

  /**
   * Changes the Object status from "SHOP" to "ON_SALE".
   *
   * @param id the id of the Object
   * @return the Patched Object
   */
  @PATCH
  @Path("{id}/onsale")
  @Authorize(role = Role.MANAGER)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectDTO setObjectOnSale(@PathParam("id") int id) {
    ObjectDTO object = myObjectUCC.updateObjectStatus(id, Status.ON_SALE);

    if (object == null) {
      throw new WebApplicationException(
          "L'objet n'existe pas ou n'est pas dans le magasin.",
          Response.Status.BAD_REQUEST);
    }

    return object;
  }

  /**
   * Changes the Object status from "ON_SALE" to "SOLD".
   *
   * @param id the id of the Object
   * @return the Patched Object
   */
  @PATCH
  @Path("{id}/sell")
  @Authorize(role = Role.MANAGER)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectDTO sellObject(@PathParam("id") int id) {
    ObjectDTO object = myObjectUCC.updateObjectStatus(id, Status.SOLD);

    if (object == null) {
      throw new WebApplicationException("L'objet n'existe pas ou n'est pas en vente.",
          Response.Status.BAD_REQUEST);
    }

    return object;
  }

  /**
   * Changes the object status from "ON_SALE" to "REMOVED".
   *
   * @param id the id of the Object
   * @return the Patched Object
   */
  @PATCH
  @Path("{id}/remove")
  @Authorize(role = Role.MANAGER)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectDTO removeObject(@PathParam("id") int id) {
    ObjectDTO object = myObjectUCC.updateObjectStatus(id, Status.REMOVED);

    if (object == null) {
      throw new WebApplicationException("L'objet n'existe pas ou n'est pas en vente.",
          Response.Status.BAD_REQUEST);
    }

    return object;
  }
}
