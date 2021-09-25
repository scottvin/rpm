package com.github.scottvin.rpm.repository;

import com.github.scottvin.rpm.domain.RpmVision;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RpmVision entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RpmVisionRepository extends JpaRepository<RpmVision, Long> {}
