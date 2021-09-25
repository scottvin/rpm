package com.github.scottvin.rpm.service;

import com.github.scottvin.rpm.domain.RpmPractice;
import com.github.scottvin.rpm.repository.RpmPracticeRepository;
import com.github.scottvin.rpm.service.dto.RpmPracticeDTO;
import com.github.scottvin.rpm.service.mapper.RpmPracticeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RpmPractice}.
 */
@Service
@Transactional
public class RpmPracticeService {

    private final Logger log = LoggerFactory.getLogger(RpmPracticeService.class);

    private final RpmPracticeRepository rpmPracticeRepository;

    private final RpmPracticeMapper rpmPracticeMapper;

    public RpmPracticeService(RpmPracticeRepository rpmPracticeRepository, RpmPracticeMapper rpmPracticeMapper) {
        this.rpmPracticeRepository = rpmPracticeRepository;
        this.rpmPracticeMapper = rpmPracticeMapper;
    }

    /**
     * Save a rpmPractice.
     *
     * @param rpmPracticeDTO the entity to save.
     * @return the persisted entity.
     */
    public RpmPracticeDTO save(RpmPracticeDTO rpmPracticeDTO) {
        log.debug("Request to save RpmPractice : {}", rpmPracticeDTO);
        RpmPractice rpmPractice = rpmPracticeMapper.toEntity(rpmPracticeDTO);
        rpmPractice = rpmPracticeRepository.save(rpmPractice);
        return rpmPracticeMapper.toDto(rpmPractice);
    }

    /**
     * Partially update a rpmPractice.
     *
     * @param rpmPracticeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RpmPracticeDTO> partialUpdate(RpmPracticeDTO rpmPracticeDTO) {
        log.debug("Request to partially update RpmPractice : {}", rpmPracticeDTO);

        return rpmPracticeRepository
            .findById(rpmPracticeDTO.getId())
            .map(existingRpmPractice -> {
                rpmPracticeMapper.partialUpdate(existingRpmPractice, rpmPracticeDTO);

                return existingRpmPractice;
            })
            .map(rpmPracticeRepository::save)
            .map(rpmPracticeMapper::toDto);
    }

    /**
     * Get all the rpmPractices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RpmPracticeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RpmPractices");
        return rpmPracticeRepository.findAll(pageable).map(rpmPracticeMapper::toDto);
    }

    /**
     * Get one rpmPractice by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RpmPracticeDTO> findOne(Long id) {
        log.debug("Request to get RpmPractice : {}", id);
        return rpmPracticeRepository.findById(id).map(rpmPracticeMapper::toDto);
    }

    /**
     * Delete the rpmPractice by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RpmPractice : {}", id);
        rpmPracticeRepository.deleteById(id);
    }
}
