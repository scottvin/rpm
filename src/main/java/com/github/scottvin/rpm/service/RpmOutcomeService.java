package com.github.scottvin.rpm.service;

import com.github.scottvin.rpm.domain.RpmOutcome;
import com.github.scottvin.rpm.repository.RpmOutcomeRepository;
import com.github.scottvin.rpm.service.dto.RpmOutcomeDTO;
import com.github.scottvin.rpm.service.mapper.RpmOutcomeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RpmOutcome}.
 */
@Service
@Transactional
public class RpmOutcomeService {

    private final Logger log = LoggerFactory.getLogger(RpmOutcomeService.class);

    private final RpmOutcomeRepository rpmOutcomeRepository;

    private final RpmOutcomeMapper rpmOutcomeMapper;

    public RpmOutcomeService(RpmOutcomeRepository rpmOutcomeRepository, RpmOutcomeMapper rpmOutcomeMapper) {
        this.rpmOutcomeRepository = rpmOutcomeRepository;
        this.rpmOutcomeMapper = rpmOutcomeMapper;
    }

    /**
     * Save a rpmOutcome.
     *
     * @param rpmOutcomeDTO the entity to save.
     * @return the persisted entity.
     */
    public RpmOutcomeDTO save(RpmOutcomeDTO rpmOutcomeDTO) {
        log.debug("Request to save RpmOutcome : {}", rpmOutcomeDTO);
        RpmOutcome rpmOutcome = rpmOutcomeMapper.toEntity(rpmOutcomeDTO);
        rpmOutcome = rpmOutcomeRepository.save(rpmOutcome);
        return rpmOutcomeMapper.toDto(rpmOutcome);
    }

    /**
     * Partially update a rpmOutcome.
     *
     * @param rpmOutcomeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RpmOutcomeDTO> partialUpdate(RpmOutcomeDTO rpmOutcomeDTO) {
        log.debug("Request to partially update RpmOutcome : {}", rpmOutcomeDTO);

        return rpmOutcomeRepository
            .findById(rpmOutcomeDTO.getId())
            .map(existingRpmOutcome -> {
                rpmOutcomeMapper.partialUpdate(existingRpmOutcome, rpmOutcomeDTO);

                return existingRpmOutcome;
            })
            .map(rpmOutcomeRepository::save)
            .map(rpmOutcomeMapper::toDto);
    }

    /**
     * Get all the rpmOutcomes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RpmOutcomeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RpmOutcomes");
        return rpmOutcomeRepository.findAll(pageable).map(rpmOutcomeMapper::toDto);
    }

    /**
     * Get one rpmOutcome by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RpmOutcomeDTO> findOne(Long id) {
        log.debug("Request to get RpmOutcome : {}", id);
        return rpmOutcomeRepository.findById(id).map(rpmOutcomeMapper::toDto);
    }

    /**
     * Delete the rpmOutcome by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RpmOutcome : {}", id);
        rpmOutcomeRepository.deleteById(id);
    }
}
