package com.cricket.matchdata.entity.stats;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchTeamStats {
    @Id
    @GeneratedValue
    private Long id;

    private int totalRuns;

    @Column(name = "wickets_lost", nullable = false)
    private int totalWickets;

    @OneToMany(cascade = CascadeType.ALL)
    private List<PlayerMatchStats> playerStats = new ArrayList<>();

    public void addRuns(int r) {
        totalRuns += r;
    }

    public void incrementWickets() {
        totalWickets++;
    }
}