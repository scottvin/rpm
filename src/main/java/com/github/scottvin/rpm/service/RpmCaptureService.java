package com.github.scottvin.rpm.service;

import com.github.scottvin.rpm.domain.RpmCapture;
import com.github.scottvin.rpm.repository.RpmCaptureRepository;
import com.github.scottvin.rpm.service.dto.RpmCaptureDTO;
import com.github.scottvin.rpm.service.mapper.RpmCaptureMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RpmCapture}.
 */
@Service
@Transactional
public class RpmCaptureService {

    private final Logger log = LoggerFactory.getLogger(RpmCaptureService.class);

    private final RpmCaptureRepository rpmCaptureRepository;

    private final RpmCaptureMapper rpmCaptureMapper;

    public RpmCaptureService(RpmCaptureRepository rpmCaptureRepository, RpmCaptureMapper rpmCaptureMapper) {
        this.rpmCaptureRepository = rpmCaptureRepository;
        this.rpmCaptureMapper = rpmCaptureMapper;
    }

    /**
     * Save a rpmCapture.
     *
     * @param rpmCaptureDTO the entity to save.
     * @return the persisted entity.
     */
    public RpmCaptureDTO save(RpmCaptureDTO rpmCaptureDTO) {
        log.debug("Request to save RpmCapture : {}", rpmCaptureDTO);
        RpmCapture rpmCapture = rpmCaptureMapper.toEntity(rpmCaptureDTO);
        rpmCapture = rpmCaptureRepository.save(rpmCapture);
        return rpmCaptureMapper.toDto(rpmCapture);
    }

    /**
     * Partially update a rpmCapture.
     *
     * @param rpmCaptureDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RpmCaptureDTO> partialUpdate(RpmCaptureDTO rpmCaptureDTO) {
        log.debug("Request to partially update RpmCapture : {}", rpmCaptureDTO);

        return rpmCaptureRepository
            .findById(rpmCaptureDTO.getId())
            .map(existingRpmCapture -> {
                rpmCaptureMapper.partialUpdate(existingRpmCapture, rpmCaptureDTO);

                return existingRpmCapture;
            })
            .map(rpmCaptureRepository::save)
            .map(rpmCaptureMapper::toDto);
    }

    /**
     * Get all the rpmCaptures.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RpmCaptureDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RpmCaptures");
        return rpmCaptureRepository.findAll(pageable).map(rpmCaptureMapper::toDto);
    }

    /**
     * Get one rpmCapture by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RpmCaptureDTO> findOne(Long id) {
        log.debug("Request to get RpmCapture : {}", id);
        return rpmCaptureRepository.findById(id).map(rpmCaptureMapper::toDto);
    }

    /**
     * Delete the rpmCapture by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RpmCapture : {}", id);
        rpmCaptureRepository.deleteById(id);
    }
}
