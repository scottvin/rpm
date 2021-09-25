package com.github.scottvin.rpm.service;

import com.github.scottvin.rpm.domain.RpmPurpose;
import com.github.scottvin.rpm.repository.RpmPurposeRepository;
import com.github.scottvin.rpm.service.dto.RpmPurposeDTO;
import com.github.scottvin.rpm.service.mapper.RpmPurposeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RpmPurpose}.
 */
@Service
@Transactional
public class RpmPurposeService {

    private final Logger log = LoggerFactory.getLogger(RpmPurposeService.class);

    private final RpmPurposeRepository rpmPurposeRepository;

    private final RpmPurposeMapper rpmPurposeMapper;

    public RpmPurposeService(RpmPurposeRepository rpmPurposeRepository, RpmPurposeMapper rpmPurposeMapper) {
        this.rpmPurposeRepository = rpmPurposeRepository;
        this.rpmPurposeMapper = rpmPurposeMapper;
    }

    /**
     * Save a rpmPurpose.
     *
     * @param rpmPurposeDTO the entity to save.
     * @return the persisted entity.
     */
    public RpmPurposeDTO save(RpmPurposeDTO rpmPurposeDTO) {
        log.debug("Request to save RpmPurpose : {}", rpmPurposeDTO);
        RpmPurpose rpmPurpose = rpmPurposeMapper.toEntity(rpmPurposeDTO);
        rpmPurpose = rpmPurposeRepository.save(rpmPurpose);
        return rpmPurposeMapper.toDto(rpmPurpose);
    }

    /**
     * Partially update a rpmPurpose.
     *
     * @param rpmPurposeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RpmPurposeDTO> partialUpdate(RpmPurposeDTO rpmPurposeDTO) {
        log.debug("Request to partially update RpmPurpose : {}", rpmPurposeDTO);

        return rpmPurposeRepository
            .findById(rpmPurposeDTO.getId())
            .map(existingRpmPurpose -> {
                rpmPurposeMapper.partialUpdate(existingRpmPurpose, rpmPurposeDTO);

                return existingRpmPurpose;
            })
            .map(rpmPurposeRepository::save)
            .map(rpmPurposeMapper::toDto);
    }

    /**
     * Get all the rpmPurposes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RpmPurposeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RpmPurposes");
        return rpmPurposeRepository.findAll(pageable).map(rpmPurposeMapper::toDto);
    }

    /**
     * Get one rpmPurpose by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RpmPurposeDTO> findOne(Long id) {
        log.debug("Request to get RpmPurpose : {}", id);
        return rpmPurposeRepository.findById(id).map(rpmPurposeMapper::toDto);
    }

    /**
     * Delete the rpmPurpose by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RpmPurpose : {}", id);
        rpmPurposeRepository.deleteById(id);
    }
}
