package me.sathish.runswithshedlock.run_event;

import me.sathish.runswithshedlock.util.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class RunEventService {
    private final RunEventPublisher runEventPublisher;
    private final RunEventRepository runEventRepository;

    public RunEventService(RunEventPublisher runEventPublisher, final RunEventRepository runEventRepository) {
        this.runEventPublisher = runEventPublisher;
        this.runEventRepository = runEventRepository;
    }

    public Page<RunEventDTO> findAll(final String filter, final Pageable pageable) {
        Page<RunEvent> page;
        if (filter != null) {
            Long longFilter = null;
            try {
                longFilter = Long.parseLong(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = runEventRepository.findAllById(longFilter, pageable);
        } else {
            page = runEventRepository.findAll(pageable);
        }
        return new PageImpl<>(page.getContent()
                .stream()
                .map(runEvent -> mapToDTO(runEvent, new RunEventDTO()))
                .toList(),
                pageable, page.getTotalElements());
    }

    public RunEventDTO get(final Long id) {
        return runEventRepository.findById(id)
                .map(runEvent -> mapToDTO(runEvent, new RunEventDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final RunEventDTO runEventDTO) {
        final RunEvent runEvent = new RunEvent();
        mapToEntity(runEventDTO, runEvent);
        final RunEvent runEventSaved= runEventRepository.save(runEvent);
        return runEventSaved.getId();
    }

    public void update(final Long id, final RunEventDTO runEventDTO) {
        final RunEvent runEvent = runEventRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(runEventDTO, runEvent);
        runEventRepository.save(runEvent);
    }

    public void delete(final Long id) {
        runEventRepository.deleteById(id);
    }

    private RunEventDTO mapToDTO(final RunEvent runEvent, final RunEventDTO runEventDTO) {
        runEventDTO.setId(runEvent.getId());
        runEventDTO.setRunId(runEvent.getRunId());
        runEventDTO.setRunEventType(runEvent.getRunEventType());
        runEventDTO.setRunInformation(runEvent.getRunInformation());
        return runEventDTO;
    }

    private RunEvent mapToEntity(final RunEventDTO runEventDTO, final RunEvent runEvent) {
        runEvent.setRunId(runEventDTO.getRunId());
        runEvent.setRunEventType(runEventDTO.getRunEventType());
        runEvent.setRunInformation(runEventDTO.getRunInformation());
        return runEvent;
    }

    public boolean runIdExists(final String runId) {
        return runEventRepository.existsByRunId(runId);
    }

}
