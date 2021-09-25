package com.github.scottvin.rpm.repository;

import com.github.scottvin.rpm.domain.RpmReason;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RpmReason entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RpmReasonRepository extends JpaRepository<RpmReason, Long> {}
