package com.github.scottvin.rpm.repository;

import com.github.scottvin.rpm.domain.RpmResult;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RpmResult entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RpmResultRepository extends JpaRepository<RpmResult, Long>, JpaSpecificationExecutor<RpmResult> {}
