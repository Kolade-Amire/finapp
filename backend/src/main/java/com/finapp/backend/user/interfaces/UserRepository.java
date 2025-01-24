package com.finapp.backend.user.interfaces;

import com.finapp.backend.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends ListCrudRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Page<User> findByDeletionDateBeforeOrderByFirstname(LocalDateTime time,Pageable pageable);

    void deleteByDeletionDateBefore(LocalDateTime time);
}
