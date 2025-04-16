package com.cricket.matchdata.entity;

import com.cricket.matchdata.entity.inningInfo.Inning;
import com.cricket.matchdata.entity.matchinfo.Info;
import com.cricket.matchdata.entity.metadata.MetaData;
import com.cricket.matchdata.entity.stats.PlayerMatchStats;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "match_data")
@EqualsAndHashCode(exclude = {"meta", "info", "innings"})

public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("meta")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "meta_id")
    private MetaData meta;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonManagedReference
    private Info info;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Inning> innings = new ArrayList<>();

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<PlayerMatchStats> playerStats;
}