package me.sathish.runswithshedlock.garmin_run;

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
@RequestMapping(value = "/api/garminRuns", produces = MediaType.APPLICATION_JSON_VALUE)
public class GarminRunResource {

    private final GarminRunService garminRunService;
    private final GarminRunAssembler garminRunAssembler;
    private final PagedResourcesAssembler<GarminRunDTO> pagedResourcesAssembler;

    public GarminRunResource(final GarminRunService garminRunService,
            final GarminRunAssembler garminRunAssembler,
            final PagedResourcesAssembler<GarminRunDTO> pagedResourcesAssembler) {
        this.garminRunService = garminRunService;
        this.garminRunAssembler = garminRunAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<GarminRunDTO>>> getAllGarminRuns(
            @RequestParam(name = "filter", required = false) final String filter,
            @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable) {
        final Page<GarminRunDTO> garminRunDTOs = garminRunService.findAll(filter, pageable);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(garminRunDTOs, garminRunAssembler));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<GarminRunDTO>> getGarminRun(
            @PathVariable(name = "id") final Long id) {
        final GarminRunDTO garminRunDTO = garminRunService.get(id);
        return ResponseEntity.ok(garminRunAssembler.toModel(garminRunDTO));
    }

    @PostMapping
    public ResponseEntity<EntityModel<SimpleValue<Long>>> createGarminRun(
            @RequestBody @Valid final GarminRunDTO garminRunDTO) {
        final Long createdId = garminRunService.create(garminRunDTO);
        return new ResponseEntity<>(garminRunAssembler.toSimpleModel(createdId), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<SimpleValue<Long>>> updateGarminRun(
            @PathVariable(name = "id") final Long id,
            @RequestBody @Valid final GarminRunDTO garminRunDTO) {
        garminRunService.update(id, garminRunDTO);
        return ResponseEntity.ok(garminRunAssembler.toSimpleModel(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGarminRun(@PathVariable(name = "id") final Long id) {
        garminRunService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
