package me.sathish.runswithshedlock.garmin_run;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import java.util.List;
import me.sathish.runswithshedlock.runner.Runner;
import org.hibernate.LockOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;

public interface GarminRunRepository extends JpaRepository<GarminRun, Long> {

    Page<GarminRun> findAllById(Long id, Pageable pageable);

    GarminRun findFirstByRunner(Runner runner);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = LockOptions.SKIP_LOCKED + "")})
    public List<GarminRun> findTop100ByOrderByDateCreatedAsc();
}
