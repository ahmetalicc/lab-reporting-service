package org.backend.project.reportingapp.dao;

import org.backend.project.reportingapp.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Long> {
}
