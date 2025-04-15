package com.cricket.matchdata.repository;

import com.cricket.matchdata.entity.stats.PlayerMatchStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerMatchStatsRepository extends JpaRepository<PlayerMatchStats, Long> {
    List<PlayerMatchStats> findByPlayerName(String playerName);
}
