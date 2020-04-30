package org.acme.people.service;

import java.time.LocalDate;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import org.acme.people.model.EyeColor;
import org.acme.people.model.Person;

import io.quarkus.vertx.ConsumeEvent;

/**
 * PersonService
 */
@ApplicationScoped
public class PersonService {

    @ConsumeEvent(value ="add-person", blocking = true)
    @Transactional
    public Person addPerson(String name){
        LocalDate birth = LocalDate.now().plusWeeks(Math.round(Math.floor(Math.random()*20*52*-1)));
        EyeColor color = EyeColor.values()[(int)(Math.floor(Math.random()*EyeColor.values().length))];
        Person p = new Person();
        p.setBirth(birth);
        p.setEyes(color);
        p.setName(name);
        Person.persist(p);
        return p;
    }
    
}