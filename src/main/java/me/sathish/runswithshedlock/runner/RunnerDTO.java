package me.sathish.runswithshedlock.runner;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RunnerDTO {

    private Long id;

    @Size(max = 255)
    private String username;

    @Size(max = 255)
    private String email;

}
