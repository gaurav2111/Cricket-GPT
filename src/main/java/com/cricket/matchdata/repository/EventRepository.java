package com.cricket.matchdata.repository;

import com.cricket.matchdata.entity.matchinfo.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}