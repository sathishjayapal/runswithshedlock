package me.sathish.runswithshedlock;

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ConfigurationPropertiesScan
@EnableSchedulerLock(defaultLockAtMostFor = "10m")
public class RunswithshedlockApplication {
    public static void main(final String[] args) {
        SpringApplication.run(RunswithshedlockApplication.class, args);
    }
}
