package org.backend.project.reportingapp.dao;

import org.backend.project.reportingapp.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReportRepository extends JpaRepository<Report, Long> {
    @Query("SELECT r FROM Report r WHERE " +
            "(:patientName IS NULL OR r.patientName LIKE %:patientName%) AND " +
            "(:patientSurname IS NULL OR r.patientSurname LIKE %:patientSurname%) AND " +
            "(:patientIdentityNumber IS NULL OR r.patientIdentityNumber LIKE %:patientIdentityNumber%) AND " +
            "(:laborantName IS NULL OR r.laborant.firstName LIKE %:laborantName%) AND " +
            "(:laborantSurname IS NULL OR r.laborant.lastName LIKE %:laborantSurname%)")
    Page<Report> findAllByFilters(String patientName, String patientSurname, String patientIdentityNumber, String laborantName, String laborantSurname, Pageable pageable);
}
