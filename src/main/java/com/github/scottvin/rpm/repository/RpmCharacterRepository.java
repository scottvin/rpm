package com.github.scottvin.rpm.repository;

import com.github.scottvin.rpm.domain.RpmCharacter;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RpmCharacter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RpmCharacterRepository extends JpaRepository<RpmCharacter, Long>, JpaSpecificationExecutor<RpmCharacter> {}
