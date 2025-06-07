package me.sathish.runswithshedlock.runner;

import java.util.List;
import me.sathish.runswithshedlock.garmin_run.GarminRun;
import me.sathish.runswithshedlock.garmin_run.GarminRunRepository;
import me.sathish.runswithshedlock.util.NotFoundException;
import me.sathish.runswithshedlock.util.ReferencedWarning;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class RunnerService {

    private final RunnerRepository runnerRepository;
    private final GarminRunRepository garminRunRepository;

    public RunnerService(final RunnerRepository runnerRepository, final GarminRunRepository garminRunRepository) {
        this.runnerRepository = runnerRepository;
        this.garminRunRepository = garminRunRepository;
    }

    public List<RunnerDTO> findAll() {
        final List<Runner> runners = runnerRepository.findAll(Sort.by("id"));
        return runners.stream().map(runner -> mapToDTO(runner, new RunnerDTO())).toList();
    }

    public RunnerDTO get(final Long id) {
        return runnerRepository
                .findById(id)
                .map(runner -> mapToDTO(runner, new RunnerDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final RunnerDTO runnerDTO) {
        final Runner runner = new Runner();
        mapToEntity(runnerDTO, runner);
        return runnerRepository.save(runner).getId();
    }

    public void update(final Long id, final RunnerDTO runnerDTO) {
        final Runner runner = runnerRepository.findById(id).orElseThrow(NotFoundException::new);
        mapToEntity(runnerDTO, runner);
        runnerRepository.save(runner);
    }

    public void delete(final Long id) {
        runnerRepository.deleteById(id);
    }

    private RunnerDTO mapToDTO(final Runner runner, final RunnerDTO runnerDTO) {
        runnerDTO.setId(runner.getId());
        runnerDTO.setUsername(runner.getUsername());
        runnerDTO.setEmail(runner.getEmail());
        runnerDTO.setHash(runner.getHash());
        return runnerDTO;
    }

    private Runner mapToEntity(final RunnerDTO runnerDTO, final Runner runner) {
        runner.setUsername(runnerDTO.getUsername());
        runner.setEmail(runnerDTO.getEmail());
        runner.setHash(runnerDTO.getHash());
        return runner;
    }

    public boolean usernameExists(final String username) {
        return runnerRepository.existsByUsernameIgnoreCase(username);
    }

    public boolean emailExists(final String email) {
        return runnerRepository.existsByEmailIgnoreCase(email);
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Runner runner = runnerRepository.findById(id).orElseThrow(NotFoundException::new);
        final GarminRun runnerGarminRun = garminRunRepository.findFirstByRunner(runner);
        if (runnerGarminRun != null) {
            referencedWarning.setKey("runner.garminRun.runner.referenced");
            referencedWarning.addParam(runnerGarminRun.getId());
            return referencedWarning;
        }
        return null;
    }
}
