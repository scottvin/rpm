package com.github.scottvin.rpm.service;

import com.github.scottvin.rpm.service.dto.RpmCharacterDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.github.scottvin.rpm.domain.RpmCharacter}.
 */
public interface RpmCharacterService {
    /**
     * Save a rpmCharacter.
     *
     * @param rpmCharacterDTO the entity to save.
     * @return the persisted entity.
     */
    RpmCharacterDTO save(RpmCharacterDTO rpmCharacterDTO);

    /**
     * Partially updates a rpmCharacter.
     *
     * @param rpmCharacterDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RpmCharacterDTO> partialUpdate(RpmCharacterDTO rpmCharacterDTO);

    /**
     * Get all the rpmCharacters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RpmCharacterDTO> findAll(Pageable pageable);

    /**
     * Get the "id" rpmCharacter.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RpmCharacterDTO> findOne(Long id);

    /**
     * Delete the "id" rpmCharacter.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
