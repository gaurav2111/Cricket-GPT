package com.cricket.matchdata.entity.matchinfo;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Officials {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
@OrderColumn(name = "idx")
    @JsonProperty("match_referees")
    private List<String> matchReferees;

    @ElementCollection
@OrderColumn(name = "idx")
    @JsonProperty("reserve_umpires")
    private List<String> reserveUmpires;

    @ElementCollection
@OrderColumn(name = "idx")
    @JsonProperty("tv_umpires")
    private List<String> tvUmpires;

    @ElementCollection
@OrderColumn(name = "idx")
    @JsonProperty("umpires")
    private List<String> umpires;
}