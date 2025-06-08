package me.sathish.runswithshedlock.garmin_run;

import com.github.javafaker.Faker;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import me.sathish.runswithshedlock.run_event.RunEventPublisher;
import me.sathish.runswithshedlock.runner.Runner;
import me.sathish.runswithshedlock.runner.RunnerRepository;
import me.sathish.runswithshedlock.util.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class GarminRunService {

    private final GarminRunRepository garminRunRepository;
    private final RunnerRepository runnerRepository;
    private final RunEventPublisher runEventPublisher;
    private final TransactionTemplate transactionTemplate;
    private static final Logger logger = LoggerFactory.getLogger(GarminRunService.class);

    public GarminRunService(
            final GarminRunRepository garminRunRepository,
            final RunnerRepository runnerRepository,
            RunEventPublisher runEventPublisher,
            TransactionTemplate transactionTemplate) {
        this.garminRunRepository = garminRunRepository;
        this.runnerRepository = runnerRepository;
        this.runEventPublisher = runEventPublisher;
        this.transactionTemplate = transactionTemplate;
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
        return new PageImpl<>(
                page.getContent().stream()
                        .map(garminRun -> mapToDTO(garminRun, new GarminRunDTO()))
                        .toList(),
                pageable,
                page.getTotalElements());
    }

    public GarminRunDTO get(final Long id) {
        return garminRunRepository
                .findById(id)
                .map(garminRun -> mapToDTO(garminRun, new GarminRunDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final GarminRunDTO garminRunDTO) {
        final GarminRun garminRun = new GarminRun();
        mapToEntity(garminRunDTO, garminRun);
        final GarminRun savedGarminRun = garminRunRepository.save(garminRun);
        mapToDTO(savedGarminRun, garminRunDTO);
        runEventPublisher.publishGarminRun(garminRunDTO);
        return savedGarminRun.getId();
    }

    public void bulkUpdate() {
        for (int i = 0; i < 10000; i++) {
            Faker faker = new Faker();
            GarminRunDTO garminRunDTOWithLoop = new GarminRunDTO();
            GarminRun fakerGarminRun = new GarminRun();
            garminRunDTOWithLoop.setActivityID(faker.number().numberBetween(1, 1000));
            garminRunDTOWithLoop.setActivityName(faker.lorem().characters(5, 25));
            garminRunDTOWithLoop.setActivityType(faker.lorem().characters(5, 25));
            Date fakerDate = faker.date().between(new Date(2025, 01, 01), new Date(2025, 12, 31));
            garminRunDTOWithLoop.setActivityDate(
                    LocalDate.of(fakerDate.getYear(), fakerDate.getMonth() + 1, fakerDate.getDate())
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            garminRunDTOWithLoop.setActivityDescription(faker.lorem().sentence());
            garminRunDTOWithLoop.setElapsedTime(faker.number().digits(5));
            garminRunDTOWithLoop.setDistance(faker.lorem().characters(5, 25));
            garminRunDTOWithLoop.setMaxHeartRate(faker.lorem().characters(140, 175));
            mapToEntity(garminRunDTOWithLoop, fakerGarminRun);
            transactionTemplate.execute(transactionStatus -> {
                garminRunRepository.save(fakerGarminRun);
                logger.info("Saved Garmin run with ID: {}", fakerGarminRun.getId());
                runEventPublisher.publishGarminRun(garminRunDTOWithLoop);
                return true;
            });
        }
    }

    public void update(final Long id, final GarminRunDTO garminRunDTO) {
        final GarminRun garminRun = garminRunRepository.findById(id).orElseThrow(NotFoundException::new);
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
        garminRunDTO.setRunner(
                garminRun.getRunner() == null ? null : garminRun.getRunner().getId());
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
        final Runner runner = garminRunDTO.getRunner() == null
                ? null
                : runnerRepository
                        .findById(garminRunDTO.getRunner())
                        .orElseThrow(() -> new NotFoundException("runner not found"));
        garminRun.setRunner(runner);
        return garminRun;
    }
}
