package me.sathish.runswithshedlock.run_event;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import me.sathish.runswithshedlock.model.SimpleValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.SortDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/runEvents", produces = MediaType.APPLICATION_JSON_VALUE)
public class RunEventResource {

    private final RunEventService runEventService;
    private final RunEventAssembler runEventAssembler;
    private final PagedResourcesAssembler<RunEventDTO> pagedResourcesAssembler;

    public RunEventResource(final RunEventService runEventService,
            final RunEventAssembler runEventAssembler,
            final PagedResourcesAssembler<RunEventDTO> pagedResourcesAssembler) {
        this.runEventService = runEventService;
        this.runEventAssembler = runEventAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Operation(
            parameters = {
                    @Parameter(
                            name = "page",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = Integer.class)
                    ),
                    @Parameter(
                            name = "size",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = Integer.class)
                    ),
                    @Parameter(
                            name = "sort",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = String.class)
                    )
            }
    )
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<RunEventDTO>>> getAllRunEvents(
            @RequestParam(name = "filter", required = false) final String filter,
            @Parameter(hidden = true) @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable) {
        final Page<RunEventDTO> runEventDTOs = runEventService.findAll(filter, pageable);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(runEventDTOs, runEventAssembler));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<RunEventDTO>> getRunEvent(
            @PathVariable(name = "id") final Long id) {
        final RunEventDTO runEventDTO = runEventService.get(id);
        return ResponseEntity.ok(runEventAssembler.toModel(runEventDTO));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<EntityModel<SimpleValue<Long>>> createRunEvent(
            @RequestBody @Valid final RunEventDTO runEventDTO) {
        final Long createdId = runEventService.create(runEventDTO);
        return new ResponseEntity<>(runEventAssembler.toSimpleModel(createdId), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<SimpleValue<Long>>> updateRunEvent(
            @PathVariable(name = "id") final Long id,
            @RequestBody @Valid final RunEventDTO runEventDTO) {
        runEventService.update(id, runEventDTO);
        return ResponseEntity.ok(runEventAssembler.toSimpleModel(id));
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteRunEvent(@PathVariable(name = "id") final Long id) {
        runEventService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
