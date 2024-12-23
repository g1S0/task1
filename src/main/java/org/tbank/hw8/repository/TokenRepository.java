package org.tbank.hw8.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.tbank.hw8.entity.Token;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    @Query(value = """
            select t from Token t inner join User u\s
            on t.user.id = u.id\s
            where u.id = :id and (t.revoked = false)\s
            """)
    List<Token> findAllValidTokenByUser(@Param("id") Integer id);

    @Query("select t from Token t where t.token = :token")
    Optional<Token> findTokenByValue(@Param("token") String token);
}
