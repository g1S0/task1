package org.tbank.hw10.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tbank.hw10.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
