package me.sathish.runswithshedlock.runner;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import me.sathish.runswithshedlock.model.SimpleValue;
import me.sathish.runswithshedlock.util.ReferencedException;
import me.sathish.runswithshedlock.util.ReferencedWarning;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/runners", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAuthority('ROLE_USER')")
public class RunnerResource {

    private final RunnerService runnerService;
    private final RunnerAssembler runnerAssembler;

    public RunnerResource(final RunnerService runnerService,
                          final RunnerAssembler runnerAssembler) {
        this.runnerService = runnerService;
        this.runnerAssembler = runnerAssembler;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<RunnerDTO>>> getAllRunners() {
        final List<RunnerDTO> runnerDTOs = runnerService.findAll();
        return ResponseEntity.ok(runnerAssembler.toCollectionModel(runnerDTOs));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<RunnerDTO>> getRunner(
            @PathVariable(name = "id") final Long id) {
        final RunnerDTO runnerDTO = runnerService.get(id);
        return ResponseEntity.ok(runnerAssembler.toModel(runnerDTO));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<EntityModel<SimpleValue<Long>>> createRunner(
            @RequestBody @Valid final RunnerDTO runnerDTO) {
        final Long createdId = runnerService.create(runnerDTO);
        return new ResponseEntity<>(runnerAssembler.toSimpleModel(createdId), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<SimpleValue<Long>>> updateRunner(
            @PathVariable(name = "id") final Long id,
            @RequestBody @Valid final RunnerDTO runnerDTO) {
        runnerService.update(id, runnerDTO);
        return ResponseEntity.ok(runnerAssembler.toSimpleModel(id));
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteRunner(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = runnerService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        runnerService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

