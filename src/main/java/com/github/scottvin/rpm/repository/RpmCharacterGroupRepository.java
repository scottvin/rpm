package com.github.scottvin.rpm.repository;

import com.github.scottvin.rpm.domain.RpmCharacterGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RpmCharacterGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RpmCharacterGroupRepository extends JpaRepository<RpmCharacterGroup, Long> {}
