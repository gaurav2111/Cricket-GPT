package com.cricket.matchdata.entity.inningInfo;

import com.cricket.matchdata.entity.Match;
import com.cricket.matchdata.entity.stats.MatchTeamStats;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Data
@EqualsAndHashCode(exclude = {"target"})
public class Inning {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String team;
    private boolean super_over;
    @ElementCollection
@OrderColumn(name = "idx")
    private List<String> absent_hurt;

    @ManyToOne
    @JoinColumn(name = "match_id")
    @JsonBackReference
    private Match match;

    @OneToMany(mappedBy = "inning", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OverDetail> overs = new ArrayList<>();

    @Transient
    private List<Powerplay> powerplays;

    @JsonProperty("miscounted_overs")
    @Transient
    private Map<Integer, MiscountedOver> miscountedOvers;
    @OneToOne(mappedBy = "inning", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Target target;

    @OneToOne(cascade = CascadeType.ALL)
    private MatchTeamStats matchTeamStats;

}
