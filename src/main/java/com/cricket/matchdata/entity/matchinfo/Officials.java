package com.cricket.matchdata.entity.matchinfo;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
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
    @JsonProperty("match_referees")
    private List<String> matchReferees;

    @ElementCollection
    @JsonProperty("reserve_umpires")
    private List<String> reserveUmpires;

    @ElementCollection
    @JsonProperty("tv_umpires")
    private List<String> tvUmpires;

    @ElementCollection
    @JsonProperty("umpires")
    private List<String> umpires;
}