package org.acme.people.rest;

import java.security.Principal;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/secured")
@RequestScoped
public class JWTResource {
  /**
   * jwt 
   */
  @Inject
  JsonWebToken jwt;

  /**
   * 要求user role
   * Principle 就是包含jwt的对象, Principle包含jwt，还有一些验证结果信息等
   * SecurityContext 包含了security上下文 如果http 头部带了beaer token，框架就会自动
   * 进行jwt校验 然后把解码的jwt信息插入到ctx中 从而获取principle对象
   * @param ctx
   * @return
   */
  @GET
  @Path("/me")
  // @PermitAll
  @RolesAllowed("user")
  @Produces(MediaType.TEXT_PLAIN)
  public String me(@Context SecurityContext ctx){
    Principal caller = ctx.getUserPrincipal();
    String name = (caller == null)? "anonymous": caller.getName();
    boolean hasJWT = (jwt != null);
    return String.format("hello: %s, isSecure: %s, authScheme: %s, hasJWT: %s\n", 
      name, ctx.isSecure(), ctx.getAuthenticationScheme(), hasJWT);
    
  }

  /**
   * 要求“admin” Role 才能访问
   */
  @GET
  @Path("/admin")
  @RolesAllowed("admin")
  @Produces(MediaType.TEXT_PLAIN)
  public String meJwt(@Context SecurityContext ctx){
    Principal caller = ctx.getUserPrincipal();
    String name = (caller == null)? "anonymous":caller.getName();
    boolean hasJWT = (jwt != null);
    final StringBuilder helloReply = new StringBuilder(String.format(
      "hello %s, isSecure: %s, authScheme: %s, hasJWT: %s\n", name, ctx.isSecure(),
      ctx.getAuthenticationScheme(), hasJWT));
    if(hasJWT && (jwt.getClaimNames()!=null)){
      helloReply.append("Injected issuer: [" + jwt.getIssuer() + "]\n");
      jwt.getClaimNames().forEach(n -> {
        helloReply.append("\nClaim Name: [" + n + "] Claim Value: [" + jwt.getClaim(n) + "]");
      });
    }
    return helloReply.toString();
    
  }
}