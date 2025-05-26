package me.sathish.runswithshedlock.runner;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RunnerDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    @RunnerUsernameUnique
    private String username;

    @NotNull
    @Size(max = 255)
    @RunnerEmailUnique
    private String email;

    @NotNull
    @Size(max = 255)
    private String hash;

}
