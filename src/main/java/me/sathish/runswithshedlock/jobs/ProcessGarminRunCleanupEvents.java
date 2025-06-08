package me.sathish.runswithshedlock.jobs;

import static javax.management.timer.Timer.ONE_MINUTE;

import java.util.List;
import me.sathish.runswithshedlock.garmin_run.GarminRun;
import me.sathish.runswithshedlock.garmin_run.GarminRunRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

@Component
public class ProcessGarminRunCleanupEvents {
    private static final Logger logger = LoggerFactory.getLogger(ProcessGarminRunCleanupEvents.class);
    private final TransactionTemplate transactionTemplate;
    private final GarminRunRepository garminRunRepository;

    public ProcessGarminRunCleanupEvents(
            TransactionTemplate transactionTemplate, GarminRunRepository garminRunRepository) {
        this.transactionTemplate = transactionTemplate;
        this.garminRunRepository = garminRunRepository;
    }

    @Scheduled(fixedDelay = ONE_MINUTE, initialDelay = 10_000)
    public void processRunEvents() {
        boolean pendingRuns = true; // This should be replaced with actual logic to check for pending runs
        while (pendingRuns) {
            // No more runs to process
            pendingRuns = transactionTemplate.execute(transactionStatus -> {
                List<GarminRun> garminRunList = garminRunRepository.findTop100ByOrderByDateCreatedAsc();
                if (garminRunList.isEmpty()) {
                    logger.info("No more Garmin runs to process.");
                    return false; // No more runs to process
                }
                garminRunList.forEach(garminRun -> {
                    garminRunRepository.delete(garminRun);
                    logger.info("Deleted Garmin run with ID: {}", garminRun.getId());
                });
                return true;
            });
        }
    }
}
