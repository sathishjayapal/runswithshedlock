package me.sathish.runswithshedlock;

import me.sathish.runswithshedlock.garmin_run.GarminRunResource;
import me.sathish.runswithshedlock.runner.RunnerResource;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HomeResource {

    @GetMapping("/home")
    public RepresentationModel<?> index() {
        return RepresentationModel.of(null)
                .add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(GarminRunResource.class).getAllGarminRuns(null, null)).withRel("garminRuns"))
                .add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RunnerResource.class).getAllRunners()).withRel("runners"));
    }

}
