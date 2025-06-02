package me.sathish.runswithshedlock.jobs;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ProcessRunEvents {
    @Scheduled(fixedDelay = 1000)
    public void processRunEvents() {
        System.out.println("Processing run events...");
    }
}
