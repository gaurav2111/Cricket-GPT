package com.cricket.matchdata.entity.inningInfo.replacement;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Replacements {

    @JsonProperty("match")
    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatchReplacement> matchReplacements = new ArrayList<>();

    @JsonProperty("role")
    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoleReplacement> roleReplacements = new ArrayList<>();

    // getters and setters
}