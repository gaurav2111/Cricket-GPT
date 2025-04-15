package com.cricket.matchdata.repository;

import com.cricket.matchdata.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {
}