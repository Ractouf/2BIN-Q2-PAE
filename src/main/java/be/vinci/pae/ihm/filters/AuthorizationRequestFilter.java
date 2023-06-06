package be.vinci.pae.ihm.filters;

import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.dto.UserDTO.Role;
import be.vinci.pae.business.ucc.UserUCC;
import be.vinci.pae.exceptions.BizExceptionUnauthorized;
import be.vinci.pae.utils.Config;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.Provider;
import java.lang.reflect.Method;
import java.util.List;

/**
 * This class is a filter that ensures that only authorized users can access certain resources.
 */
@Singleton
@Provider
@Priority(Priorities.AUTHORIZATION)
@Authorize
public class AuthorizationRequestFilter implements ContainerRequestFilter {

  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  private final JWTVerifier jwtVerifier =
      JWT.require(this.jwtAlgorithm).withIssuer("auth0").build();
  @Inject
  UserUCC myUserUCC;

  @Context
  private ResourceInfo resourceInfo;

  @Override
  public void filter(ContainerRequestContext requestContext) {
    Method resourceMethod = resourceInfo.getResourceMethod();
    Class<?> resourceClass = resourceMethod.getDeclaringClass();
    String token = requestContext.getHeaderString("Authorization");
    if (token == null) {
      requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
          .entity("Un token est demandé pour accéder à cette ressource.").build());
    } else {
      DecodedJWT decodedToken;
      try {
        decodedToken = this.jwtVerifier.verify(token);
      } catch (Exception e) {
        throw new BizExceptionUnauthorized(
            "Token malformé : " + e.getMessage()
        );
      }
      UserDTO authenticatedUser = myUserUCC.getUserById(decodedToken.getClaim("user").asInt());
      if (authenticatedUser == null) {
        requestContext.abortWith(Response.status(Status.FORBIDDEN)
            .entity("Il vous est interdit d'accéder à cette ressource.").build());
      }

      Role expectedRole = getExpectedRoleFromAnnotationParameter(resourceMethod, resourceClass);

      if (authenticatedUser.getRole() != expectedRole
          && authenticatedUser.getRole() != Role.MANAGER
          && expectedRole != Role.USER) {
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
            .entity("Vous n'avez pas le bon role pour effectuer cette requête.").build());
      }

      if (isRightUser(resourceMethod)) {
        MultivaluedMap<String, String> mapParam = requestContext.getUriInfo().getPathParameters();
        if (mapParam.containsKey("idUser")) {
          List<String> listParam = mapParam.get("idUser");
          if (!(Integer.parseInt(listParam.get(0)) == authenticatedUser.getId())) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                .entity("Seul l'utilisateur concerné peut effectuer cette requête.").build());
          }
        }
      }

      requestContext.setProperty("user", authenticatedUser);
    }
  }

  private Role getExpectedRoleFromAnnotationParameter(Method resourceMethod,
      Class<?> resourceClass) {
    if (resourceMethod.isAnnotationPresent(Authorize.class)) {
      Authorize authAnnotation = resourceMethod.getAnnotation(Authorize.class);
      return authAnnotation.role();
    } else if (resourceClass.isAnnotationPresent(Authorize.class)) {
      Authorize authAnnotation = resourceClass.getAnnotation(Authorize.class);
      return authAnnotation.role();
    }

    return null;
  }

  private boolean isRightUser(Method resourceMethod) {
    if (resourceMethod.isAnnotationPresent(Authorize.class)) {
      return resourceMethod.getAnnotation(Authorize.class).isRightUser();
    }

    return false;
  }
}
