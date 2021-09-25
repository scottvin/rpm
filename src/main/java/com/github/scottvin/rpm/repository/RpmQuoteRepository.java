package com.github.scottvin.rpm.repository;

import com.github.scottvin.rpm.domain.RpmQuote;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RpmQuote entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RpmQuoteRepository extends JpaRepository<RpmQuote, Long> {}
