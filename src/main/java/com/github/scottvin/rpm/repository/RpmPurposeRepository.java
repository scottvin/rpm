package com.github.scottvin.rpm.repository;

import com.github.scottvin.rpm.domain.RpmPurpose;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RpmPurpose entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RpmPurposeRepository extends JpaRepository<RpmPurpose, Long> {}
