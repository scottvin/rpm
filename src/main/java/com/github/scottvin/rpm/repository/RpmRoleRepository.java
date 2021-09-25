package com.github.scottvin.rpm.repository;

import com.github.scottvin.rpm.domain.RpmRole;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RpmRole entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RpmRoleRepository extends JpaRepository<RpmRole, Long> {}
