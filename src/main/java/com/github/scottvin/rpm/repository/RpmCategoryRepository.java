package com.github.scottvin.rpm.repository;

import com.github.scottvin.rpm.domain.RpmCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RpmCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RpmCategoryRepository extends JpaRepository<RpmCategory, Long> {}
