package com.cricket.matchdata.repository;


import com.cricket.matchdata.entity.matchinfo.Officials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfficialsRepository extends JpaRepository<Officials, Long> {
}
