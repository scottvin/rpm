package com.github.scottvin.rpm.repository;

import com.github.scottvin.rpm.domain.RpmOutcome;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RpmOutcome entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RpmOutcomeRepository extends JpaRepository<RpmOutcome, Long> {}
