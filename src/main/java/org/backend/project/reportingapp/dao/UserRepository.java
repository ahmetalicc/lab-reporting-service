package org.backend.project.reportingapp.dao;

import org.backend.project.reportingapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.name) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :filter, '%')) ")
    Page<User> findUsersByFilter(@Param("filter") String filter, Pageable pageable);

    boolean existsByUsername(String username);
}
