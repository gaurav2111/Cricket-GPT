package com.cricket.matchdata.entity.inningInfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Runs {
    @Column(name = "runs_batter")
    private int batter;

    @Column(name = "runs_extras")
    private int extras;

    @Column(name = "runs_total")
    private int total;

    @JsonProperty("non_boundary")
    @Column(name="non_boundary")
    private boolean nonBoundary;
}