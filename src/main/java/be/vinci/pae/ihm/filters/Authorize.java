package be.vinci.pae.ihm.filters;

import be.vinci.pae.business.dto.UserDTO.Role;
import jakarta.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * An annotation that indicates that a JAX-RS resource or method requires authorization.
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface Authorize {

  /**
   * Determine the expected role which is allowed to access whatever is protected by the
   * annotation.
   *
   * @return a char of the role
   */
  Role role() default Role.USER;

  /**
   * Determine if the user wich has launch the request have to be the same as the user concerned by
   * the request.
   *
   * @return true or false based on if the user have to be verified or not
   */
  boolean isRightUser() default false;
}
