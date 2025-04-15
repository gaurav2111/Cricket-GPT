package com.cricket.matchdata.repository;

import com.cricket.matchdata.entity.matchinfo.Info;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InfoRepository extends JpaRepository<Info, Long> {
}