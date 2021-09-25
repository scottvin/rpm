package com.github.scottvin.rpm.service;

import com.github.scottvin.rpm.service.dto.RpmProjectDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.github.scottvin.rpm.domain.RpmProject}.
 */
public interface RpmProjectService {
    /**
     * Save a rpmProject.
     *
     * @param rpmProjectDTO the entity to save.
     * @return the persisted entity.
     */
    RpmProjectDTO save(RpmProjectDTO rpmProjectDTO);

    /**
     * Partially updates a rpmProject.
     *
     * @param rpmProjectDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RpmProjectDTO> partialUpdate(RpmProjectDTO rpmProjectDTO);

    /**
     * Get all the rpmProjects.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RpmProjectDTO> findAll(Pageable pageable);

    /**
     * Get the "id" rpmProject.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RpmProjectDTO> findOne(Long id);

    /**
     * Delete the "id" rpmProject.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
