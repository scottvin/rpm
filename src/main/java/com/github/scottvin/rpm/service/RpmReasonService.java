package com.github.scottvin.rpm.service;

import com.github.scottvin.rpm.domain.RpmReason;
import com.github.scottvin.rpm.repository.RpmReasonRepository;
import com.github.scottvin.rpm.service.dto.RpmReasonDTO;
import com.github.scottvin.rpm.service.mapper.RpmReasonMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RpmReason}.
 */
@Service
@Transactional
public class RpmReasonService {

    private final Logger log = LoggerFactory.getLogger(RpmReasonService.class);

    private final RpmReasonRepository rpmReasonRepository;

    private final RpmReasonMapper rpmReasonMapper;

    public RpmReasonService(RpmReasonRepository rpmReasonRepository, RpmReasonMapper rpmReasonMapper) {
        this.rpmReasonRepository = rpmReasonRepository;
        this.rpmReasonMapper = rpmReasonMapper;
    }

    /**
     * Save a rpmReason.
     *
     * @param rpmReasonDTO the entity to save.
     * @return the persisted entity.
     */
    public RpmReasonDTO save(RpmReasonDTO rpmReasonDTO) {
        log.debug("Request to save RpmReason : {}", rpmReasonDTO);
        RpmReason rpmReason = rpmReasonMapper.toEntity(rpmReasonDTO);
        rpmReason = rpmReasonRepository.save(rpmReason);
        return rpmReasonMapper.toDto(rpmReason);
    }

    /**
     * Partially update a rpmReason.
     *
     * @param rpmReasonDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RpmReasonDTO> partialUpdate(RpmReasonDTO rpmReasonDTO) {
        log.debug("Request to partially update RpmReason : {}", rpmReasonDTO);

        return rpmReasonRepository
            .findById(rpmReasonDTO.getId())
            .map(existingRpmReason -> {
                rpmReasonMapper.partialUpdate(existingRpmReason, rpmReasonDTO);

                return existingRpmReason;
            })
            .map(rpmReasonRepository::save)
            .map(rpmReasonMapper::toDto);
    }

    /**
     * Get all the rpmReasons.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RpmReasonDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RpmReasons");
        return rpmReasonRepository.findAll(pageable).map(rpmReasonMapper::toDto);
    }

    /**
     * Get one rpmReason by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RpmReasonDTO> findOne(Long id) {
        log.debug("Request to get RpmReason : {}", id);
        return rpmReasonRepository.findById(id).map(rpmReasonMapper::toDto);
    }

    /**
     * Delete the rpmReason by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RpmReason : {}", id);
        rpmReasonRepository.deleteById(id);
    }
}
