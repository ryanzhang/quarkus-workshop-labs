package org.acme.people.service;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.acme.people.model.StarWarsPerson;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@ApplicationScoped
@RegisterRestClient
@Path("/api")
public interface StarWarsService {
  @GET
  @Path("/people/{id}/")
  @Produces(MediaType.APPLICATION_JSON)
  @ClientHeaderParam(name="User-Agent", value="QuarkusLab")
  StarWarsPerson getPerson(@PathParam("id") int id);
}