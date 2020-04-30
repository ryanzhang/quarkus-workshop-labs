package org.acme.people.health;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Liveness;

/**
 * DatabaseConnectionHealthCheck
 */
@ApplicationScoped
@Liveness
public class DatabaseConnectionHealthCheck implements HealthCheck {

    @ConfigProperty(name = "database.up", defaultValue = "false")
    public boolean databaseUp;

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("Database connection health check");

        try {
            simulateDatabaseConnectionVerification();
            responseBuilder.up();
        }catch (IllegalStateException e){
            responseBuilder.down()
                .withData("error", e.getMessage());
        }
        return responseBuilder.build();
    }

    private void simulateDatabaseConnectionVerification() {
        if(!databaseUp){
            throw new IllegalStateException("连接数据库失败");
        }
    }

}