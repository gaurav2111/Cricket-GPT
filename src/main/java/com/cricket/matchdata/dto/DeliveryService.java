package com.cricket.matchdata.dto;

import java.util.List;
import java.util.Map;

public interface DeliveryService {
    public PlayerCareerStats getCareerSummary(String playerName);
//    List<PlayerYearStats> getYearlySummary(String playerName);
//    Map<Integer, List<InningsStats>> getYearlyInningWise(String playerName);
}