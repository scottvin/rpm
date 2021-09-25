package com.github.scottvin.rpm.repository;

import com.github.scottvin.rpm.domain.RpmComment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RpmComment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RpmCommentRepository extends JpaRepository<RpmComment, Long> {}
