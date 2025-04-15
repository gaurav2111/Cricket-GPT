package com.cricket.matchdata.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InningsStats {
    private int runs;
    private int balls;

    // Optional fields you might want to include:
    // private String opposition;
    // private String matchDate;
    // private String matchId;

    // Getters and Setters
}