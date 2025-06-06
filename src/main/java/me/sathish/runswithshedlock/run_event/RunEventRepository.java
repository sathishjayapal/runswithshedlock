package me.sathish.runswithshedlock.run_event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RunEventRepository extends JpaRepository<RunEvent, Long> {

    Page<RunEvent> findAllById(Long id, Pageable pageable);

    boolean existsByRunId(String runId);

}
