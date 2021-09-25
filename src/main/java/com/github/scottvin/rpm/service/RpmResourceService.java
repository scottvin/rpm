package com.github.scottvin.rpm.service;

import com.github.scottvin.rpm.domain.RpmResource;
import com.github.scottvin.rpm.repository.RpmResourceRepository;
import com.github.scottvin.rpm.service.dto.RpmResourceDTO;
import com.github.scottvin.rpm.service.mapper.RpmResourceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RpmResource}.
 */
@Service
@Transactional
public class RpmResourceService {

    private final Logger log = LoggerFactory.getLogger(RpmResourceService.class);

    private final RpmResourceRepository rpmResourceRepository;

    private final RpmResourceMapper rpmResourceMapper;

    public RpmResourceService(RpmResourceRepository rpmResourceRepository, RpmResourceMapper rpmResourceMapper) {
        this.rpmResourceRepository = rpmResourceRepository;
        this.rpmResourceMapper = rpmResourceMapper;
    }

    /**
     * Save a rpmResource.
     *
     * @param rpmResourceDTO the entity to save.
     * @return the persisted entity.
     */
    public RpmResourceDTO save(RpmResourceDTO rpmResourceDTO) {
        log.debug("Request to save RpmResource : {}", rpmResourceDTO);
        RpmResource rpmResource = rpmResourceMapper.toEntity(rpmResourceDTO);
        rpmResource = rpmResourceRepository.save(rpmResource);
        return rpmResourceMapper.toDto(rpmResource);
    }

    /**
     * Partially update a rpmResource.
     *
     * @param rpmResourceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RpmResourceDTO> partialUpdate(RpmResourceDTO rpmResourceDTO) {
        log.debug("Request to partially update RpmResource : {}", rpmResourceDTO);

        return rpmResourceRepository
            .findById(rpmResourceDTO.getId())
            .map(existingRpmResource -> {
                rpmResourceMapper.partialUpdate(existingRpmResource, rpmResourceDTO);

                return existingRpmResource;
            })
            .map(rpmResourceRepository::save)
            .map(rpmResourceMapper::toDto);
    }

    /**
     * Get all the rpmResources.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RpmResourceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RpmResources");
        return rpmResourceRepository.findAll(pageable).map(rpmResourceMapper::toDto);
    }

    /**
     * Get one rpmResource by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RpmResourceDTO> findOne(Long id) {
        log.debug("Request to get RpmResource : {}", id);
        return rpmResourceRepository.findById(id).map(rpmResourceMapper::toDto);
    }

    /**
     * Delete the rpmResource by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RpmResource : {}", id);
        rpmResourceRepository.deleteById(id);
    }
}
