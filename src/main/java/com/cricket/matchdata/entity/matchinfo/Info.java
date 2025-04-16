package com.cricket.matchdata.entity.matchinfo;

import com.cricket.matchdata.deserializer.TeamMapDeserializer;
import com.cricket.matchdata.entity.Match;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = "match")
public class Info {

    /*
     * Missing fields :
     * bowl_out
     * event -> look inn event file
     *
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("balls_per_over")
    private Integer ballsPerOver;

    private String city;

    @ElementCollection
    @OrderColumn(name = "idx")
    private List<String> dates;

    @OneToOne(cascade = CascadeType.ALL)
    private Event event;

    private String gender;
    @JsonProperty("match_type")
    private String matchType;

    @OneToOne(cascade = CascadeType.ALL)
    private Officials officials;

    @OneToOne(cascade = CascadeType.ALL)
    private Outcome outcome;

    private Integer overs;

    @ElementCollection
@OrderColumn(name = "idx")
    @JsonProperty("player_of_match")
    private List<String> playerOfMatch;

    @ManyToOne
    @JoinColumn(name = "match_id")
    @JsonBackReference
    private Match match;


    @JsonProperty("players")
    @JsonDeserialize(using = TeamMapDeserializer.class)
    @OneToMany(mappedBy = "info", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Team> teams;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "registry_id")
    private Registry registry;

    private String season;
    @JsonProperty("team_type")
    private String teamType;

    @ElementCollection
@OrderColumn(name = "idx")
    @JsonProperty("teams")
    private List<String> teamsList;

    @OneToOne(cascade = CascadeType.ALL)
    private Toss toss;

    private String venue;
}