package me.sathish.runswithshedlock.garmin_run;

import jakarta.transaction.Transactional;
import me.sathish.runswithshedlock.runner.Runner;
import me.sathish.runswithshedlock.runner.RunnerRepository;
import me.sathish.runswithshedlock.util.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class GarminRunService {

    private final GarminRunRepository garminRunRepository;
    private final RunnerRepository runnerRepository;

    public GarminRunService(final GarminRunRepository garminRunRepository,
            final RunnerRepository runnerRepository) {
        this.garminRunRepository = garminRunRepository;
        this.runnerRepository = runnerRepository;
    }

    public Page<GarminRunDTO> findAll(final String filter, final Pageable pageable) {
        Page<GarminRun> page;
        if (filter != null) {
            Long longFilter = null;
            try {
                longFilter = Long.parseLong(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = garminRunRepository.findAllById(longFilter, pageable);
        } else {
            page = garminRunRepository.findAll(pageable);
        }
        return new PageImpl<>(page.getContent()
                .stream()
                .map(garminRun -> mapToDTO(garminRun, new GarminRunDTO()))
                .toList(),
                pageable, page.getTotalElements());
    }

    public GarminRunDTO get(final Long id) {
        return garminRunRepository.findById(id)
                .map(garminRun -> mapToDTO(garminRun, new GarminRunDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final GarminRunDTO garminRunDTO) {
        final GarminRun garminRun = new GarminRun();
        mapToEntity(garminRunDTO, garminRun);
        return garminRunRepository.save(garminRun).getId();
    }

    public void update(final Long id, final GarminRunDTO garminRunDTO) {
        final GarminRun garminRun = garminRunRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(garminRunDTO, garminRun);
        garminRunRepository.save(garminRun);
    }

    public void delete(final Long id) {
        garminRunRepository.deleteById(id);
    }

    private GarminRunDTO mapToDTO(final GarminRun garminRun, final GarminRunDTO garminRunDTO) {
        garminRunDTO.setId(garminRun.getId());
        garminRunDTO.setActivityID(garminRun.getActivityID());
        garminRunDTO.setActivityDate(garminRun.getActivityDate());
        garminRunDTO.setActivityType(garminRun.getActivityType());
        garminRunDTO.setActivityName(garminRun.getActivityName());
        garminRunDTO.setActivityDescription(garminRun.getActivityDescription());
        garminRunDTO.setElapsedTime(garminRun.getElapsedTime());
        garminRunDTO.setDistance(garminRun.getDistance());
        garminRunDTO.setMaxHeartRate(garminRun.getMaxHeartRate());
        garminRunDTO.setRunner(garminRun.getRunner() == null ? null : garminRun.getRunner().getId());
        return garminRunDTO;
    }

    private GarminRun mapToEntity(final GarminRunDTO garminRunDTO, final GarminRun garminRun) {
        garminRun.setActivityID(garminRunDTO.getActivityID());
        garminRun.setActivityDate(garminRunDTO.getActivityDate());
        garminRun.setActivityType(garminRunDTO.getActivityType());
        garminRun.setActivityName(garminRunDTO.getActivityName());
        garminRun.setActivityDescription(garminRunDTO.getActivityDescription());
        garminRun.setElapsedTime(garminRunDTO.getElapsedTime());
        garminRun.setDistance(garminRunDTO.getDistance());
        garminRun.setMaxHeartRate(garminRunDTO.getMaxHeartRate());
        final Runner runner = garminRunDTO.getRunner() == null ? null : runnerRepository.findById(garminRunDTO.getRunner())
                .orElseThrow(() -> new NotFoundException("runner not found"));
        garminRun.setRunner(runner);
        return garminRun;
    }

}
