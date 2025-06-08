package me.sathish.runswithshedlock.jobs;

import me.sathish.runswithshedlock.garmin_run.GarminRunService;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ProcessGarminRunBulkUpdate {
    private final GarminRunService garminRunService;

    public ProcessGarminRunBulkUpdate(GarminRunService garminRunService) {
        this.garminRunService = garminRunService;
    }

    @Scheduled(fixedDelay = 50_000, initialDelay = 10_000)
    @SchedulerLock(name = "processGarminRunBulkUpdate")
    public void processRunEvents() {
        LockAssert.assertLocked();
        garminRunService.bulkUpdate();
        // Logic to process Garmin run bulk updates
    }
}
