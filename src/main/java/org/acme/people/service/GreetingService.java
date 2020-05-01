package org.acme.people.service;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.metrics.annotation.Counted;

/**
 * GreetingService
 * 服务Bean
 */
@ApplicationScoped
@Counted(
    name = "greeting",
    description = "greeting服务的访问次数"
)
public class GreetingService {
    private String hostname = System.getenv().getOrDefault("HOSTNAME", "unknown");

    public String greeting(String name){
        return "hello " + name + " \n\t from " + hostname;
    }

    
}