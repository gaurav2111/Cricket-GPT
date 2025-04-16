package com.cricket.matchdata.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlayerCareerStats {
    private int totalRuns;
    private int totalBalls;
    private int fours;
    private int sixes;

    // Optionally add average if you have innings played and dismissals
    // private double average;

    // Getters and Setters
}