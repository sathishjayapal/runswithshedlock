package me.sathish.runswithshedlock.jobs;

import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ProcessRunEvents {
    private static final Logger logger = LoggerFactory.getLogger(ProcessRunEvents.class);

    @Scheduled(fixedRateString = "${runswithshedlock.processRunEvents.fixedRate:10000}")
    public void processRunEvents() {
        logger.info("Processing run events at {}", Instant.now());
        // Add your event processing logic here
    }
}
