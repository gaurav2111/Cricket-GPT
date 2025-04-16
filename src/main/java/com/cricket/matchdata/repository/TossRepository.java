package com.cricket.matchdata.repository;

import com.cricket.matchdata.entity.matchinfo.Toss;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TossRepository extends JpaRepository<Toss, Long> {
}
