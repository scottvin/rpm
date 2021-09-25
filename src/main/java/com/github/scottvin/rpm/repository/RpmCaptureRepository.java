package com.github.scottvin.rpm.repository;

import com.github.scottvin.rpm.domain.RpmCapture;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RpmCapture entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RpmCaptureRepository extends JpaRepository<RpmCapture, Long> {}
