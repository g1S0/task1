package org.tbank.hw8.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tbank.hw8.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}
