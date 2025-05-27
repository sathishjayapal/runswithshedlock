package me.sathish.runswithshedlock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;


@SpringBootApplication
@ConfigurationPropertiesScan
public class RunswithshedlockApplication {

    public static void main(final String[] args) {
        SpringApplication.run(RunswithshedlockApplication.class, args);
    }

}
