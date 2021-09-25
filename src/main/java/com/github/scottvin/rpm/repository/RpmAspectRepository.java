package com.github.scottvin.rpm.repository;

import com.github.scottvin.rpm.domain.RpmAspect;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RpmAspect entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RpmAspectRepository extends JpaRepository<RpmAspect, Long> {}
