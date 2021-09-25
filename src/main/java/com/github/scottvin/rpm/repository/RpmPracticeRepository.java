package com.github.scottvin.rpm.repository;

import com.github.scottvin.rpm.domain.RpmPractice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RpmPractice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RpmPracticeRepository extends JpaRepository<RpmPractice, Long> {}
