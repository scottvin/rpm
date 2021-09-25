package com.github.scottvin.rpm.service;

import com.github.scottvin.rpm.domain.RpmCategory;
import com.github.scottvin.rpm.repository.RpmCategoryRepository;
import com.github.scottvin.rpm.service.dto.RpmCategoryDTO;
import com.github.scottvin.rpm.service.mapper.RpmCategoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RpmCategory}.
 */
@Service
@Transactional
public class RpmCategoryService {

    private final Logger log = LoggerFactory.getLogger(RpmCategoryService.class);

    private final RpmCategoryRepository rpmCategoryRepository;

    private final RpmCategoryMapper rpmCategoryMapper;

    public RpmCategoryService(RpmCategoryRepository rpmCategoryRepository, RpmCategoryMapper rpmCategoryMapper) {
        this.rpmCategoryRepository = rpmCategoryRepository;
        this.rpmCategoryMapper = rpmCategoryMapper;
    }

    /**
     * Save a rpmCategory.
     *
     * @param rpmCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public RpmCategoryDTO save(RpmCategoryDTO rpmCategoryDTO) {
        log.debug("Request to save RpmCategory : {}", rpmCategoryDTO);
        RpmCategory rpmCategory = rpmCategoryMapper.toEntity(rpmCategoryDTO);
        rpmCategory = rpmCategoryRepository.save(rpmCategory);
        return rpmCategoryMapper.toDto(rpmCategory);
    }

    /**
     * Partially update a rpmCategory.
     *
     * @param rpmCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RpmCategoryDTO> partialUpdate(RpmCategoryDTO rpmCategoryDTO) {
        log.debug("Request to partially update RpmCategory : {}", rpmCategoryDTO);

        return rpmCategoryRepository
            .findById(rpmCategoryDTO.getId())
            .map(existingRpmCategory -> {
                rpmCategoryMapper.partialUpdate(existingRpmCategory, rpmCategoryDTO);

                return existingRpmCategory;
            })
            .map(rpmCategoryRepository::save)
            .map(rpmCategoryMapper::toDto);
    }

    /**
     * Get all the rpmCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RpmCategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RpmCategories");
        return rpmCategoryRepository.findAll(pageable).map(rpmCategoryMapper::toDto);
    }

    /**
     * Get one rpmCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RpmCategoryDTO> findOne(Long id) {
        log.debug("Request to get RpmCategory : {}", id);
        return rpmCategoryRepository.findById(id).map(rpmCategoryMapper::toDto);
    }

    /**
     * Delete the rpmCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RpmCategory : {}", id);
        rpmCategoryRepository.deleteById(id);
    }
}
