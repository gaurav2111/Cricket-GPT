package com.cricket.matchdata.entity.stats;

import com.cricket.matchdata.entity.Match;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerMatchStats {
    @Id
    @GeneratedValue
    private Long id;

    private String playerName;

    private int runsScored;
    private int ballsFaced;
    private int fours;
    private int sixes;

    private int ballsBowled;
    private int runsConceded;
    private int wickets;

    private int catches;
    private int stumpings;

    private int year;

    private boolean hasBatted = false;


    private boolean dismissed;
    private String dismissalType;


    @ManyToOne
    @JoinColumn(name = "match_id")
    @JsonBackReference
    private Match match;


    public void addRuns(int r,boolean isNonBoundary) {
        runsScored += r;

        if (r == 4 && !isNonBoundary) fours++;
        if (r == 6 && !isNonBoundary) sixes++;
    }

    public void incrementBallsFaced() {
        ballsFaced++;
    }

    public void addRunsConceded(int r) {
        runsConceded += r;
    }

    public void incrementBallsBowled() {
        ballsBowled++;
    }

    public void incrementWickets() {
       this.wickets++;
    }
    public void incrementCatches() {
        this.catches++;
    }

    public void incrementStumpings() {
        this.stumpings++;
    }

}