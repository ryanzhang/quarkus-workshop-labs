package org.acme.people.rest;

import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.acme.people.service.GreetingService;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/hello")
public class GreetingResource {

    public static final Logger log = LoggerFactory.getLogger(GreetingResource.class);

    @Inject
    GreetingService service;

    @ConfigProperty(name = "greeting.message")
    String message;

    @ConfigProperty(name = "greeting.suffix", defaultValue = "!")
    String suffix;

    @ConfigProperty(name = "greeting.name")
    Optional<String> name;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String defaultHello(){
        return message + " " + name.orElse("world") + suffix;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/greeting/{name}")
    public String hello(@PathParam String name) {
        return service.greeting(name);
    }

    @GET
    @Path("/lastletter/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public String lastLetter(@PathParam("name") String name){
        int len = name.length();
        String lastLetter = name.substring(len-1);
        log.info("最后一个字母:" + lastLetter);
        return lastLetter;
    }
}