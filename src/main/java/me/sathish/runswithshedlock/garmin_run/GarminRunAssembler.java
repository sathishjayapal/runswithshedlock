package me.sathish.runswithshedlock.garmin_run;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import me.sathish.runswithshedlock.model.SimpleValue;
import me.sathish.runswithshedlock.runner.RunnerResource;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;


@Component
public class GarminRunAssembler implements SimpleRepresentationModelAssembler<GarminRunDTO> {

    @Override
    public void addLinks(final EntityModel<GarminRunDTO> entityModel) {
        entityModel.add(linkTo(methodOn(GarminRunResource.class).getGarminRun(entityModel.getContent().getId())).withSelfRel());
        entityModel.add(linkTo(methodOn(GarminRunResource.class).getAllGarminRuns(null, null)).withRel(IanaLinkRelations.COLLECTION));
        if (entityModel.getContent().getRunner() != null) {
            entityModel.add(linkTo(methodOn(RunnerResource.class).getRunner(entityModel.getContent().getRunner())).withRel("runner"));
        }
    }

    @Override
    public void addLinks(final CollectionModel<EntityModel<GarminRunDTO>> collectionModel) {
        collectionModel.add(linkTo(methodOn(GarminRunResource.class).getAllGarminRuns(null, null)).withSelfRel());
    }

    public EntityModel<SimpleValue<Long>> toSimpleModel(final Long id) {
        final EntityModel<SimpleValue<Long>> simpleModel = SimpleValue.entityModelOf(id);
        simpleModel.add(linkTo(methodOn(GarminRunResource.class).getGarminRun(id)).withSelfRel());
        return simpleModel;
    }

}
