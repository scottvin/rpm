package com.github.scottvin.rpm.service;

import com.github.scottvin.rpm.domain.RpmAction;
import com.github.scottvin.rpm.repository.RpmActionRepository;
import com.github.scottvin.rpm.service.dto.RpmActionDTO;
import com.github.scottvin.rpm.service.mapper.RpmActionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RpmAction}.
 */
@Service
@Transactional
public class RpmActionService {

    private final Logger log = LoggerFactory.getLogger(RpmActionService.class);

    private final RpmActionRepository rpmActionRepository;

    private final RpmActionMapper rpmActionMapper;

    public RpmActionService(RpmActionRepository rpmActionRepository, RpmActionMapper rpmActionMapper) {
        this.rpmActionRepository = rpmActionRepository;
        this.rpmActionMapper = rpmActionMapper;
    }

    /**
     * Save a rpmAction.
     *
     * @param rpmActionDTO the entity to save.
     * @return the persisted entity.
     */
    public RpmActionDTO save(RpmActionDTO rpmActionDTO) {
        log.debug("Request to save RpmAction : {}", rpmActionDTO);
        RpmAction rpmAction = rpmActionMapper.toEntity(rpmActionDTO);
        rpmAction = rpmActionRepository.save(rpmAction);
        return rpmActionMapper.toDto(rpmAction);
    }

    /**
     * Partially update a rpmAction.
     *
     * @param rpmActionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RpmActionDTO> partialUpdate(RpmActionDTO rpmActionDTO) {
        log.debug("Request to partially update RpmAction : {}", rpmActionDTO);

        return rpmActionRepository
            .findById(rpmActionDTO.getId())
            .map(existingRpmAction -> {
                rpmActionMapper.partialUpdate(existingRpmAction, rpmActionDTO);

                return existingRpmAction;
            })
            .map(rpmActionRepository::save)
            .map(rpmActionMapper::toDto);
    }

    /**
     * Get all the rpmActions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RpmActionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RpmActions");
        return rpmActionRepository.findAll(pageable).map(rpmActionMapper::toDto);
    }

    /**
     * Get one rpmAction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RpmActionDTO> findOne(Long id) {
        log.debug("Request to get RpmAction : {}", id);
        return rpmActionRepository.findById(id).map(rpmActionMapper::toDto);
    }

    /**
     * Delete the rpmAction by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RpmAction : {}", id);
        rpmActionRepository.deleteById(id);
    }
}
