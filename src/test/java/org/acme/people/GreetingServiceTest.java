package org.acme.people;

import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.inject.Inject;

import org.acme.people.service.GreetingService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.test.junit.QuarkusTest;

/**
 * GreetingServiceTest
 */
@QuarkusTest
public class GreetingServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger("GreetingServiceTest");
    
    @Inject 
    GreetingService service;

    @Test
    void testGreetingService(){
      assertTrue(service.greeting("dear").startsWith("hello dear"));  
    }
    
}