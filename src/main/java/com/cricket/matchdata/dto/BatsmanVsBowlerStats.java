package com.cricket.matchdata.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BatsmanVsBowlerStats {
    private String batsman;
    private String bowler;

    private int totalRuns;
    private int ballsFaced;
    private int dismissals;

    private int fours;
    private int sixes;

    // Getters and Setters
}