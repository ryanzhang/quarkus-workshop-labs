package org.acme.people.stream;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.jboss.resteasy.annotations.SseElementType;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * NameResource
 */
@Path("/names")
public class NameResource {
        private static final Logger log = LoggerFactory.getLogger(NameResource.class.getName());
    @Inject
    @Channel("my-data-stream")
    Publisher<String> names;

    @GET
    @Path("/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType(MediaType.TEXT_PLAIN)
    public Publisher<String> stream(){
        log.warn(names.toString());
        return names;
    }
    
}