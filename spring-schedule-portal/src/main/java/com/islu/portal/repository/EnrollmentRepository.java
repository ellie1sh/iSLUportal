// EnrollmentRepository.java - Data access layer for enrollments
package com.islu.portal.repository;

import com.islu.portal.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
}