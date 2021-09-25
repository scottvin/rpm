package com.github.scottvin.rpm.repository;

import com.github.scottvin.rpm.domain.RpmAction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RpmAction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RpmActionRepository extends JpaRepository<RpmAction, Long> {}
