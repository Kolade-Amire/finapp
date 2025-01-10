package com.finapp.backend.repository;

import com.finapp.backend.entity.Token;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RedisTokenRepository extends ListCrudRepository<Token, Integer> {

    Optional<Token> findByToken(String token);

    Optional<Token> findByUserId(String userId);

}
