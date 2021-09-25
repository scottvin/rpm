package com.github.scottvin.rpm.service;

import com.github.scottvin.rpm.domain.RpmPlan;
import com.github.scottvin.rpm.repository.RpmPlanRepository;
import com.github.scottvin.rpm.service.dto.RpmPlanDTO;
import com.github.scottvin.rpm.service.mapper.RpmPlanMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RpmPlan}.
 */
@Service
@Transactional
public class RpmPlanService {

    private final Logger log = LoggerFactory.getLogger(RpmPlanService.class);

    private final RpmPlanRepository rpmPlanRepository;

    private final RpmPlanMapper rpmPlanMapper;

    public RpmPlanService(RpmPlanRepository rpmPlanRepository, RpmPlanMapper rpmPlanMapper) {
        this.rpmPlanRepository = rpmPlanRepository;
        this.rpmPlanMapper = rpmPlanMapper;
    }

    /**
     * Save a rpmPlan.
     *
     * @param rpmPlanDTO the entity to save.
     * @return the persisted entity.
     */
    public RpmPlanDTO save(RpmPlanDTO rpmPlanDTO) {
        log.debug("Request to save RpmPlan : {}", rpmPlanDTO);
        RpmPlan rpmPlan = rpmPlanMapper.toEntity(rpmPlanDTO);
        rpmPlan = rpmPlanRepository.save(rpmPlan);
        return rpmPlanMapper.toDto(rpmPlan);
    }

    /**
     * Partially update a rpmPlan.
     *
     * @param rpmPlanDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RpmPlanDTO> partialUpdate(RpmPlanDTO rpmPlanDTO) {
        log.debug("Request to partially update RpmPlan : {}", rpmPlanDTO);

        return rpmPlanRepository
            .findById(rpmPlanDTO.getId())
            .map(existingRpmPlan -> {
                rpmPlanMapper.partialUpdate(existingRpmPlan, rpmPlanDTO);

                return existingRpmPlan;
            })
            .map(rpmPlanRepository::save)
            .map(rpmPlanMapper::toDto);
    }

    /**
     * Get all the rpmPlans.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RpmPlanDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RpmPlans");
        return rpmPlanRepository.findAll(pageable).map(rpmPlanMapper::toDto);
    }

    /**
     * Get one rpmPlan by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RpmPlanDTO> findOne(Long id) {
        log.debug("Request to get RpmPlan : {}", id);
        return rpmPlanRepository.findById(id).map(rpmPlanMapper::toDto);
    }

    /**
     * Delete the rpmPlan by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RpmPlan : {}", id);
        rpmPlanRepository.deleteById(id);
    }
}
