package me.sathish.runswithshedlock.run_event;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import me.sathish.runswithshedlock.model.SimpleValue;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class RunEventAssembler implements SimpleRepresentationModelAssembler<RunEventDTO> {

    @Override
    public void addLinks(final EntityModel<RunEventDTO> entityModel) {
        entityModel.add(linkTo(methodOn(RunEventResource.class)
                        .getRunEvent(entityModel.getContent().getId()))
                .withSelfRel());
        entityModel.add(linkTo(methodOn(RunEventResource.class).getAllRunEvents(null, null))
                .withRel(IanaLinkRelations.COLLECTION));
    }

    @Override
    public void addLinks(final CollectionModel<EntityModel<RunEventDTO>> collectionModel) {
        collectionModel.add(linkTo(methodOn(RunEventResource.class).getAllRunEvents(null, null))
                .withSelfRel());
    }

    public EntityModel<SimpleValue<Long>> toSimpleModel(final Long id) {
        final EntityModel<SimpleValue<Long>> simpleModel = SimpleValue.entityModelOf(id);
        simpleModel.add(linkTo(methodOn(RunEventResource.class).getRunEvent(id)).withSelfRel());
        return simpleModel;
    }
}
