package com.cricket.matchdata.entity.stats;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchTeamStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int totalRuns;

    @Column(name = "wickets_lost", nullable = false)
    private int totalWickets;

    @OneToMany(cascade = CascadeType.ALL)
    @OrderColumn(name = "idx")
    private List<PlayerMatchStats> playerStats = new ArrayList<>();

    public void addRuns(int r) {
        totalRuns += r;
    }

    public void incrementWickets() {
        totalWickets++;
    }
}