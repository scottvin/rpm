package com.github.scottvin.rpm.repository;

import com.github.scottvin.rpm.domain.RpmResource;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RpmResource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RpmResourceRepository extends JpaRepository<RpmResource, Long> {}
