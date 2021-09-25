package com.github.scottvin.rpm.service;

import com.github.scottvin.rpm.domain.RpmVision;
import com.github.scottvin.rpm.repository.RpmVisionRepository;
import com.github.scottvin.rpm.service.dto.RpmVisionDTO;
import com.github.scottvin.rpm.service.mapper.RpmVisionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RpmVision}.
 */
@Service
@Transactional
public class RpmVisionService {

    private final Logger log = LoggerFactory.getLogger(RpmVisionService.class);

    private final RpmVisionRepository rpmVisionRepository;

    private final RpmVisionMapper rpmVisionMapper;

    public RpmVisionService(RpmVisionRepository rpmVisionRepository, RpmVisionMapper rpmVisionMapper) {
        this.rpmVisionRepository = rpmVisionRepository;
        this.rpmVisionMapper = rpmVisionMapper;
    }

    /**
     * Save a rpmVision.
     *
     * @param rpmVisionDTO the entity to save.
     * @return the persisted entity.
     */
    public RpmVisionDTO save(RpmVisionDTO rpmVisionDTO) {
        log.debug("Request to save RpmVision : {}", rpmVisionDTO);
        RpmVision rpmVision = rpmVisionMapper.toEntity(rpmVisionDTO);
        rpmVision = rpmVisionRepository.save(rpmVision);
        return rpmVisionMapper.toDto(rpmVision);
    }

    /**
     * Partially update a rpmVision.
     *
     * @param rpmVisionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RpmVisionDTO> partialUpdate(RpmVisionDTO rpmVisionDTO) {
        log.debug("Request to partially update RpmVision : {}", rpmVisionDTO);

        return rpmVisionRepository
            .findById(rpmVisionDTO.getId())
            .map(existingRpmVision -> {
                rpmVisionMapper.partialUpdate(existingRpmVision, rpmVisionDTO);

                return existingRpmVision;
            })
            .map(rpmVisionRepository::save)
            .map(rpmVisionMapper::toDto);
    }

    /**
     * Get all the rpmVisions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RpmVisionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RpmVisions");
        return rpmVisionRepository.findAll(pageable).map(rpmVisionMapper::toDto);
    }

    /**
     * Get one rpmVision by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RpmVisionDTO> findOne(Long id) {
        log.debug("Request to get RpmVision : {}", id);
        return rpmVisionRepository.findById(id).map(rpmVisionMapper::toDto);
    }

    /**
     * Delete the rpmVision by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RpmVision : {}", id);
        rpmVisionRepository.deleteById(id);
    }
}
