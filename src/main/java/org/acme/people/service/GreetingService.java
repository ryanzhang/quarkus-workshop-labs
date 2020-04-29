package org.acme.people.service;

import javax.enterprise.context.ApplicationScoped;

/**
 * GreetingService
 * 服务Bean
 */
@ApplicationScoped
public class GreetingService {
    private String hostname = System.getenv().getOrDefault("HOSTNAME", "unknown");

    public String greeting(String name){
        return "hello " + name + " \n\t from " + hostname;
    }

    
}