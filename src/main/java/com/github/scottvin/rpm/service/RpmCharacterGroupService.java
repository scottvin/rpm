package com.github.scottvin.rpm.service;

import com.github.scottvin.rpm.domain.RpmCharacterGroup;
import com.github.scottvin.rpm.repository.RpmCharacterGroupRepository;
import com.github.scottvin.rpm.service.dto.RpmCharacterGroupDTO;
import com.github.scottvin.rpm.service.mapper.RpmCharacterGroupMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RpmCharacterGroup}.
 */
@Service
@Transactional
public class RpmCharacterGroupService {

    private final Logger log = LoggerFactory.getLogger(RpmCharacterGroupService.class);

    private final RpmCharacterGroupRepository rpmCharacterGroupRepository;

    private final RpmCharacterGroupMapper rpmCharacterGroupMapper;

    public RpmCharacterGroupService(
        RpmCharacterGroupRepository rpmCharacterGroupRepository,
        RpmCharacterGroupMapper rpmCharacterGroupMapper
    ) {
        this.rpmCharacterGroupRepository = rpmCharacterGroupRepository;
        this.rpmCharacterGroupMapper = rpmCharacterGroupMapper;
    }

    /**
     * Save a rpmCharacterGroup.
     *
     * @param rpmCharacterGroupDTO the entity to save.
     * @return the persisted entity.
     */
    public RpmCharacterGroupDTO save(RpmCharacterGroupDTO rpmCharacterGroupDTO) {
        log.debug("Request to save RpmCharacterGroup : {}", rpmCharacterGroupDTO);
        RpmCharacterGroup rpmCharacterGroup = rpmCharacterGroupMapper.toEntity(rpmCharacterGroupDTO);
        rpmCharacterGroup = rpmCharacterGroupRepository.save(rpmCharacterGroup);
        return rpmCharacterGroupMapper.toDto(rpmCharacterGroup);
    }

    /**
     * Partially update a rpmCharacterGroup.
     *
     * @param rpmCharacterGroupDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RpmCharacterGroupDTO> partialUpdate(RpmCharacterGroupDTO rpmCharacterGroupDTO) {
        log.debug("Request to partially update RpmCharacterGroup : {}", rpmCharacterGroupDTO);

        return rpmCharacterGroupRepository
            .findById(rpmCharacterGroupDTO.getId())
            .map(existingRpmCharacterGroup -> {
                rpmCharacterGroupMapper.partialUpdate(existingRpmCharacterGroup, rpmCharacterGroupDTO);

                return existingRpmCharacterGroup;
            })
            .map(rpmCharacterGroupRepository::save)
            .map(rpmCharacterGroupMapper::toDto);
    }

    /**
     * Get all the rpmCharacterGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RpmCharacterGroupDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RpmCharacterGroups");
        return rpmCharacterGroupRepository.findAll(pageable).map(rpmCharacterGroupMapper::toDto);
    }

    /**
     * Get one rpmCharacterGroup by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RpmCharacterGroupDTO> findOne(Long id) {
        log.debug("Request to get RpmCharacterGroup : {}", id);
        return rpmCharacterGroupRepository.findById(id).map(rpmCharacterGroupMapper::toDto);
    }

    /**
     * Delete the rpmCharacterGroup by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RpmCharacterGroup : {}", id);
        rpmCharacterGroupRepository.deleteById(id);
    }
}
