package com.cricket.matchdata.repository;


import com.cricket.matchdata.entity.matchinfo.Outcome;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutcomeRepository extends JpaRepository<Outcome, Long> {
}
