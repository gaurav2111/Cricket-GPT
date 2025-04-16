package com.cricket.matchdata.dto;

import com.cricket.matchdata.entity.inningInfo.Delivery;
import com.cricket.matchdata.repository.DeliveryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;

    public DeliveryServiceImpl(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    @Override
    public PlayerCareerStats getCareerSummary(String playerName) {
        List<Delivery> deliveries = deliveryRepository.findByBatter(playerName);

        int totalRuns = 0, totalBalls = 0, fours = 0, sixes = 0;

        for (Delivery d : deliveries) {
            totalRuns += d.getRuns().getBatter();
            totalBalls++;
            if (d.getRuns().getBatter() == 4) fours++;
            if (d.getRuns().getBatter() == 6) sixes++;
        }

        return new PlayerCareerStats(totalRuns, totalBalls, fours, sixes);
    }

//    @Override
//    public List<PlayerYearStats> getYearlySummary(String playerName) {
//        List<Delivery> deliveries = deliveryRepository.findByBatsman(playerName);
//
//        Map<Integer, PlayerYearStats> yearMap = new HashMap<>();
//
//        for (Delivery d : deliveries) {
//            int year = d.getMatch().getDate().getYear();
//            PlayerYearStats stats = yearMap.getOrDefault(year, new PlayerYearStats(year, 0, 0, 0, 0));
//
//            stats.setTotalRuns(stats.getTotalRuns() + d.getBatsmanRuns());
//            stats.setTotalBalls(stats.getTotalBalls() + 1);
//            if (d.getBatsmanRuns() == 4) stats.setFours(stats.getFours() + 1);
//            if (d.getBatsmanRuns() == 6) stats.setSixes(stats.getSixes() + 1);
//
//            yearMap.put(year, stats);
//        }
//
//        return new ArrayList<>(yearMap.values());
//    }

//    @Override
//    public Map<Integer, List<InningsStats>> getYearlyInningWise(String playerName) {
//        List<Delivery> deliveries = deliveryRepository.findByBatsman(playerName);
//
//        // Map<Year, List<InningsStats>>
//        Map<Integer, List<InningsStats>> result = new TreeMap<>();
//
//        // Map<matchId, InningsStats> for temporarily storing innings
//        Map<Long, InningsStats> matchStats = new HashMap<>();
//        Map<Long, Integer> matchYearMap = new HashMap<>();
//
//        for (Delivery d : deliveries) {
//            long matchId = d.getMatch().getId();
//            int year = d.getMatch().getDate().getYear();
//
//            matchYearMap.put(matchId, year);
//
//            InningsStats stat = matchStats.getOrDefault(matchId, new InningsStats(0, 0));
//            stat.setRuns(stat.getRuns() + d.getBatsmanRuns());
//            stat.setBalls(stat.getBalls() + 1);
//            matchStats.put(matchId, stat);
//        }
//
//        for (Map.Entry<Long, InningsStats> entry : matchStats.entrySet()) {
//            long matchId = entry.getKey();
//            InningsStats stats = entry.getValue();
//            int year = matchYearMap.get(matchId);
//
//            result.computeIfAbsent(year, y -> new ArrayList<>()).add(stats);
//        }
//
//        return result;
//    }
}