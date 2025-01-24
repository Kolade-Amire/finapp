package com.finapp.backend.security.repository;

import com.finapp.backend.security.entity.Token;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RedisTokenRepository extends ListCrudRepository<Token, Long> {

    Optional<Token> findByToken(String token);

    Optional<Token> findByUserId(String userId);

}
