package org.tbank.hw10.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tbank.hw10.entity.Place;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
}
