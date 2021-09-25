package com.github.scottvin.rpm.repository;

import com.github.scottvin.rpm.domain.RpmPlan;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RpmPlan entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RpmPlanRepository extends JpaRepository<RpmPlan, Long> {}
