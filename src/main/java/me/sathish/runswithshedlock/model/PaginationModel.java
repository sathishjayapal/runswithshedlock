package me.sathish.runswithshedlock.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginationModel {

    private List<PaginationStep> steps;
    private String elements;
}
