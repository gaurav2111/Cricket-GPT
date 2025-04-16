package com.cricket.matchdata.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlayerYearStats {
    private int year;
    private int totalRuns;
    private int totalBalls;
    private int fours;
    private int sixes;

    // Getters and Setters
}