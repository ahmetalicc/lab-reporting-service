package org.backend.project.reportingapp.dao;

import org.backend.project.reportingapp.entity.Laborant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LaborantRepository extends JpaRepository<Laborant, Long> {
    Optional<Laborant> findByHospitalId(String hospitalId);

    @Query("SELECT l FROM Laborant l WHERE " +
            "LOWER(l.firstName) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(l.lastName) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(l.hospitalId) LIKE LOWER(CONCAT('%', :filter, '%'))")
    Page<Laborant> findLaborantsByFilter(@Param("filter") String filter, Pageable pageable);
}
