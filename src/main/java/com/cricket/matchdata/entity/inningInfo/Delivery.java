package com.cricket.matchdata.entity.inningInfo;

import com.cricket.matchdata.entity.inningInfo.replacement.MatchReplacement;
import com.cricket.matchdata.entity.inningInfo.replacement.RoleReplacement;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.*;
import java.util.Map;

@Entity
@Data
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String batter;
    private String bowler;

    @JsonProperty("non_striker")
    private String nonStriker;

    @Embedded
    private Runs runs;

    @Embedded
    private Extras extras;

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Wicket> wickets = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "over_id")
    @JsonBackReference
    private OverDetail overDetail;

    @OneToOne(mappedBy = "delivery", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Review review;

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatchReplacement> matchReplacements = new ArrayList<>();

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoleReplacement> roleReplacements = new ArrayList<>();

    @JsonProperty("replacements")
    public void unpackReplacements(Map<String, List<Map<String, Object>>> replacementsMap) {
        ObjectMapper mapper = new ObjectMapper();

        if (replacementsMap.containsKey("match")) {
            for (Map<String, Object> rep : replacementsMap.get("match")) {
                MatchReplacement mr = mapper.convertValue(rep, MatchReplacement.class);
                mr.setDelivery(this);
                this.matchReplacements.add(mr);
            }
        }

        if (replacementsMap.containsKey("role")) {
            for (Map<String, Object> rep : replacementsMap.get("role")) {
                RoleReplacement rr = mapper.convertValue(rep, RoleReplacement.class);
                rr.setDelivery(this);
                this.roleReplacements.add(rr);
            }
        }
    }

}