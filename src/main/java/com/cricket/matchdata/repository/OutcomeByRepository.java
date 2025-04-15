package com.cricket.matchdata.repository;


import com.cricket.matchdata.entity.matchinfo.OutcomeBy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutcomeByRepository extends JpaRepository<OutcomeBy, Long> {
}
