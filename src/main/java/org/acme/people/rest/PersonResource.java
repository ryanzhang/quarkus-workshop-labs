package org.acme.people.rest;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.acme.people.model.DataTable;
import org.acme.people.model.EyeColor;
import org.acme.people.model.Person;
import org.acme.people.model.StarWarsPerson;
import org.acme.people.service.StarWarsService;
import org.acme.people.utils.CuteNameGenerator;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.rest.client.inject.RestClient;
// import org.jboss.resteasy.annotations.jaxrs.QueryParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import io.quarkus.runtime.StartupEvent;
import io.vertx.axle.core.eventbus.EventBus;
import io.vertx.axle.core.eventbus.Message;

/**
 * PersonResource
 */
@Path("/person")
@ApplicationScoped
public class PersonResource {
    public static final Logger log = LoggerFactory.getLogger(GreetingResource.class);

    @Inject EventBus bus;
    
    @Transactional
    void onStart(@Observes StartupEvent ev){
        for (int i = 0; i < 1000; i++) {
            String name = CuteNameGenerator.generate();
            LocalDate birth = LocalDate.now().plusWeeks(Math.round(Math.floor(Math.random()*20*52*-1)));
            EyeColor color = EyeColor.values()[(int)Math.floor(Math.random() * EyeColor.values().length)];
            Person p = new Person();
            p.setBirth(birth);
            p.setEyes(color);
            p.setName(name);
            Person.persist(p);
        }
    }

    @POST
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public CompletionStage<Person> addPerson(@PathParam("name") String name){
        return bus.<Person>send("add-person", name)
                .thenApply(Message::body);
    }

    @GET
    @Path("/name/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Person byName(@PathParam("name") String name){
        return Person.find("name", name).firstResult();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Person> getAll(){
        return Person.listAll();
    }

    @GET
    @Path("/eyes/{color}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Person> findByColor(@PathParam(value="color") EyeColor color){
        return Person.findByColor(color);
    }

    @GET
    @Operation(summary = "查找某一年之前出生的人", description = "根据生日中的年份 过滤某一年之前出生的人")
    @APIResponses( value = {
        @APIResponse(responseCode = "200", description = "某一年之前出生的所有人", 
            content = @Content (
                schema = @Schema(implementation = Person.class)
            )
        ),
        @APIResponse(responseCode = "500", description = "出错了")
    })
   
    @Path("/birth/before/{year}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Person> getBeforeYear(
         @Parameter(description = "查询的年份", required = true, name = "year")
         @PathParam(value="year") int year){
        return Person.getBeforeYear(year);
    }

    @GET
    @Path("/datatable")
    @Produces(MediaType.APPLICATION_JSON)
    public DataTable datatable(
        @QueryParam(value="draw") int draw,
        @QueryParam(value="start") int start,
        @QueryParam(value="length") int length,
        @QueryParam(value="search[value]") String searchVal
    ){
        log.info("{} {}", draw, start);
        //TODO: begin result
        DataTable result = new DataTable();
        result.setDraw(draw);

        //TODO: Filter based on search
        PanacheQuery<Person> filteredPeople;

        if(searchVal != null && !searchVal.isEmpty()){
            filteredPeople = Person.<Person>find("name like :search", 
                Parameters.with("search", "%" + searchVal + "%")
            );
        }else{
            filteredPeople = Person.findAll();
        }

        //Todo page and return
        int page_number = start /length;
        filteredPeople.page(page_number, length);

        result.setRecordsFiltered(filteredPeople.count());
        result.setData(filteredPeople.list());
        result.setRecordsTotal(Person.count());
        return result;
    }

    @Inject
    @RestClient
    StarWarsService swService;

    @GET
    @Path("/swpeople")
    @Produces(MediaType.APPLICATION_JSON)
    public List<StarWarsPerson> getCharacters(){
        return IntStream.range(1,6)
                .mapToObj(swService::getPerson)
                .collect(Collectors.toList());
    }
}