package com.cricket.matchdata.service;

import org.springframework.stereotype.*;

@Service
public class DeliveryService {
//    @Autowired
//    private DeliveryRepository deliveryRepository;
//
//    public BatsmanVsBowlerStats getBatsmanVsBowlerStats(String batter, String bowler) {
//        List<Delivery> deliveries = deliveryRepository.findByBatterAndBowler(batter, bowler);
//
//        int totalRuns = 0;
//        int ballsFaced = 0;
//        int dismissals = 0;
//
//        for (Delivery delivery : deliveries) {
//            totalRuns += delivery.getRuns().getBatter();
//            ballsFaced++;
//
//            if (delivery.getWickets() != null) {
//                for (Wicket w : delivery.getWickets()) {
//                    if (w.getPlayerOut().equals(batter)) {
//                        dismissals++;
//                    }
//                }
//            }
//        }
//
//        return new BatsmanVsBowlerStats(batter, bowler, totalRuns, ballsFaced, dismissals);
//    }
}