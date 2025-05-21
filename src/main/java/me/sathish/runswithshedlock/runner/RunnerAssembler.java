package me.sathish.runswithshedlock.runner;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import me.sathish.runswithshedlock.model.SimpleValue;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;


@Component
public class RunnerAssembler implements SimpleRepresentationModelAssembler<RunnerDTO> {

    @Override
    public void addLinks(final EntityModel<RunnerDTO> entityModel) {
        entityModel.add(linkTo(methodOn(RunnerResource.class).getRunner(entityModel.getContent().getId())).withSelfRel());
        entityModel.add(linkTo(methodOn(RunnerResource.class).getAllRunners()).withRel(IanaLinkRelations.COLLECTION));
    }

    @Override
    public void addLinks(final CollectionModel<EntityModel<RunnerDTO>> collectionModel) {
        collectionModel.add(linkTo(methodOn(RunnerResource.class).getAllRunners()).withSelfRel());
    }

    public EntityModel<SimpleValue<Long>> toSimpleModel(final Long id) {
        final EntityModel<SimpleValue<Long>> simpleModel = SimpleValue.entityModelOf(id);
        simpleModel.add(linkTo(methodOn(RunnerResource.class).getRunner(id)).withSelfRel());
        return simpleModel;
    }

}
