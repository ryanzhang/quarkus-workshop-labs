package org.acme.people.stream;

import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import io.smallrye.reactive.messaging.annotations.Broadcast;

/**
 * NameConverter
 */
@ApplicationScoped
@Counted(
    name = "convertedNames",
    description = "处理了多少个名字"
)
@Timed(
    name = "converter",
    description = "处理一个名字花的时间"
)
public class NameConverter {
    private static final String[] honorifics = {"Mr.", "Mrs.", "Sir", "Madam", "Lord", "Lady", 
        "Dr.", "Professor", "vice-Chancellor", "Regent", "Provost", "Perfect"    
    };

    @Incoming("names")
    @Outgoing("my-data-stream")
    @Broadcast
    public String process(String name) throws InterruptedException {
        String honorific = honorifics[(int)(Math.floor(Math.random()*honorifics.length))];
        // TimeUnit.SECONDS.sleep(1);con
        return honorific + " " + name;
    }
    
}