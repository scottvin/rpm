package com.github.scottvin.rpm.service;

import com.github.scottvin.rpm.service.dto.RpmResultDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.github.scottvin.rpm.domain.RpmResult}.
 */
public interface RpmResultService {
    /**
     * Save a rpmResult.
     *
     * @param rpmResultDTO the entity to save.
     * @return the persisted entity.
     */
    RpmResultDTO save(RpmResultDTO rpmResultDTO);

    /**
     * Partially updates a rpmResult.
     *
     * @param rpmResultDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RpmResultDTO> partialUpdate(RpmResultDTO rpmResultDTO);

    /**
     * Get all the rpmResults.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RpmResultDTO> findAll(Pageable pageable);

    /**
     * Get the "id" rpmResult.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RpmResultDTO> findOne(Long id);

    /**
     * Delete the "id" rpmResult.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
