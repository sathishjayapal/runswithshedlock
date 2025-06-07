package me.sathish.runswithshedlock.garmin_run;

import me.sathish.runswithshedlock.runner.Runner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GarminRunRepository extends JpaRepository<GarminRun, Long> {

    Page<GarminRun> findAllById(Long id, Pageable pageable);

    GarminRun findFirstByRunner(Runner runner);
}
