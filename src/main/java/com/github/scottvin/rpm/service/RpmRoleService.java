package com.github.scottvin.rpm.service;

import com.github.scottvin.rpm.domain.RpmRole;
import com.github.scottvin.rpm.repository.RpmRoleRepository;
import com.github.scottvin.rpm.service.dto.RpmRoleDTO;
import com.github.scottvin.rpm.service.mapper.RpmRoleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RpmRole}.
 */
@Service
@Transactional
public class RpmRoleService {

    private final Logger log = LoggerFactory.getLogger(RpmRoleService.class);

    private final RpmRoleRepository rpmRoleRepository;

    private final RpmRoleMapper rpmRoleMapper;

    public RpmRoleService(RpmRoleRepository rpmRoleRepository, RpmRoleMapper rpmRoleMapper) {
        this.rpmRoleRepository = rpmRoleRepository;
        this.rpmRoleMapper = rpmRoleMapper;
    }

    /**
     * Save a rpmRole.
     *
     * @param rpmRoleDTO the entity to save.
     * @return the persisted entity.
     */
    public RpmRoleDTO save(RpmRoleDTO rpmRoleDTO) {
        log.debug("Request to save RpmRole : {}", rpmRoleDTO);
        RpmRole rpmRole = rpmRoleMapper.toEntity(rpmRoleDTO);
        rpmRole = rpmRoleRepository.save(rpmRole);
        return rpmRoleMapper.toDto(rpmRole);
    }

    /**
     * Partially update a rpmRole.
     *
     * @param rpmRoleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RpmRoleDTO> partialUpdate(RpmRoleDTO rpmRoleDTO) {
        log.debug("Request to partially update RpmRole : {}", rpmRoleDTO);

        return rpmRoleRepository
            .findById(rpmRoleDTO.getId())
            .map(existingRpmRole -> {
                rpmRoleMapper.partialUpdate(existingRpmRole, rpmRoleDTO);

                return existingRpmRole;
            })
            .map(rpmRoleRepository::save)
            .map(rpmRoleMapper::toDto);
    }

    /**
     * Get all the rpmRoles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RpmRoleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RpmRoles");
        return rpmRoleRepository.findAll(pageable).map(rpmRoleMapper::toDto);
    }

    /**
     * Get one rpmRole by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RpmRoleDTO> findOne(Long id) {
        log.debug("Request to get RpmRole : {}", id);
        return rpmRoleRepository.findById(id).map(rpmRoleMapper::toDto);
    }

    /**
     * Delete the rpmRole by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RpmRole : {}", id);
        rpmRoleRepository.deleteById(id);
    }
}
