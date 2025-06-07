package me.sathish.runswithshedlock.run_event;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RunEventDTO {

    private Long id;

    @NotNull @RunEventRunIdUnique
    private String runId;

    @NotNull private String runEventType;

    @NotNull private String runInformation;
}
