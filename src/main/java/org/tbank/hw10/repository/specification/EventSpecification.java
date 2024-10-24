package org.tbank.hw10.repository.specification;


import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.tbank.hw10.entity.Event;
import org.tbank.hw10.entity.Place;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EventSpecification {
    public static Specification<Event> findByCriteria(String name, Long placeId, LocalDate fromDate, LocalDate toDate) {
        return (root, query, criteriaBuilder) -> {
            root.fetch("place");

            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            }

            if (placeId != null) {
                Join<Event, Place> placeJoin = root.join("place");
                predicates.add(criteriaBuilder.equal(placeJoin.get("id"), placeId));
            }

            if (fromDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), fromDate));
            }

            if (toDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("date"), toDate));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
