package com.cricket.matchdata.dto;

public interface DeliveryService {
    public PlayerCareerStats getCareerSummary(String playerName);
//    List<PlayerYearStats> getYearlySummary(String playerName);
//    Map<Integer, List<InningsStats>> getYearlyInningWise(String playerName);
}