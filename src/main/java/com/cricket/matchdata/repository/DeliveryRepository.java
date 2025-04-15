package com.cricket.matchdata.repository;

import com.cricket.matchdata.entity.inningInfo.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    List<Delivery> findByBatterAndBowler(String batter, String bowler);
    List<Delivery> findByBatter(String batsman);


    @Query("SELECT d FROM Delivery d WHERE d.batter = :batter AND d.bowler = :bowler")
    List<Delivery> getHeadToHeadAggregate(@Param("batter") String batter, @Param("bowler") String bowler);
}