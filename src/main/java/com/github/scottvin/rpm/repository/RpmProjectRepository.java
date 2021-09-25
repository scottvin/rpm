package com.github.scottvin.rpm.repository;

import com.github.scottvin.rpm.domain.RpmProject;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RpmProject entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RpmProjectRepository extends JpaRepository<RpmProject, Long>, JpaSpecificationExecutor<RpmProject> {}
