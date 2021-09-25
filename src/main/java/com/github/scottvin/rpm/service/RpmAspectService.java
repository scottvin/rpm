package com.github.scottvin.rpm.service;

import com.github.scottvin.rpm.domain.RpmAspect;
import com.github.scottvin.rpm.repository.RpmAspectRepository;
import com.github.scottvin.rpm.service.dto.RpmAspectDTO;
import com.github.scottvin.rpm.service.mapper.RpmAspectMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RpmAspect}.
 */
@Service
@Transactional
public class RpmAspectService {

    private final Logger log = LoggerFactory.getLogger(RpmAspectService.class);

    private final RpmAspectRepository rpmAspectRepository;

    private final RpmAspectMapper rpmAspectMapper;

    public RpmAspectService(RpmAspectRepository rpmAspectRepository, RpmAspectMapper rpmAspectMapper) {
        this.rpmAspectRepository = rpmAspectRepository;
        this.rpmAspectMapper = rpmAspectMapper;
    }

    /**
     * Save a rpmAspect.
     *
     * @param rpmAspectDTO the entity to save.
     * @return the persisted entity.
     */
    public RpmAspectDTO save(RpmAspectDTO rpmAspectDTO) {
        log.debug("Request to save RpmAspect : {}", rpmAspectDTO);
        RpmAspect rpmAspect = rpmAspectMapper.toEntity(rpmAspectDTO);
        rpmAspect = rpmAspectRepository.save(rpmAspect);
        return rpmAspectMapper.toDto(rpmAspect);
    }

    /**
     * Partially update a rpmAspect.
     *
     * @param rpmAspectDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RpmAspectDTO> partialUpdate(RpmAspectDTO rpmAspectDTO) {
        log.debug("Request to partially update RpmAspect : {}", rpmAspectDTO);

        return rpmAspectRepository
            .findById(rpmAspectDTO.getId())
            .map(existingRpmAspect -> {
                rpmAspectMapper.partialUpdate(existingRpmAspect, rpmAspectDTO);

                return existingRpmAspect;
            })
            .map(rpmAspectRepository::save)
            .map(rpmAspectMapper::toDto);
    }

    /**
     * Get all the rpmAspects.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RpmAspectDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RpmAspects");
        return rpmAspectRepository.findAll(pageable).map(rpmAspectMapper::toDto);
    }

    /**
     * Get one rpmAspect by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RpmAspectDTO> findOne(Long id) {
        log.debug("Request to get RpmAspect : {}", id);
        return rpmAspectRepository.findById(id).map(rpmAspectMapper::toDto);
    }

    /**
     * Delete the rpmAspect by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RpmAspect : {}", id);
        rpmAspectRepository.deleteById(id);
    }
}
