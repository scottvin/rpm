package com.github.scottvin.rpm.service;

import com.github.scottvin.rpm.domain.RpmComment;
import com.github.scottvin.rpm.repository.RpmCommentRepository;
import com.github.scottvin.rpm.service.dto.RpmCommentDTO;
import com.github.scottvin.rpm.service.mapper.RpmCommentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RpmComment}.
 */
@Service
@Transactional
public class RpmCommentService {

    private final Logger log = LoggerFactory.getLogger(RpmCommentService.class);

    private final RpmCommentRepository rpmCommentRepository;

    private final RpmCommentMapper rpmCommentMapper;

    public RpmCommentService(RpmCommentRepository rpmCommentRepository, RpmCommentMapper rpmCommentMapper) {
        this.rpmCommentRepository = rpmCommentRepository;
        this.rpmCommentMapper = rpmCommentMapper;
    }

    /**
     * Save a rpmComment.
     *
     * @param rpmCommentDTO the entity to save.
     * @return the persisted entity.
     */
    public RpmCommentDTO save(RpmCommentDTO rpmCommentDTO) {
        log.debug("Request to save RpmComment : {}", rpmCommentDTO);
        RpmComment rpmComment = rpmCommentMapper.toEntity(rpmCommentDTO);
        rpmComment = rpmCommentRepository.save(rpmComment);
        return rpmCommentMapper.toDto(rpmComment);
    }

    /**
     * Partially update a rpmComment.
     *
     * @param rpmCommentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RpmCommentDTO> partialUpdate(RpmCommentDTO rpmCommentDTO) {
        log.debug("Request to partially update RpmComment : {}", rpmCommentDTO);

        return rpmCommentRepository
            .findById(rpmCommentDTO.getId())
            .map(existingRpmComment -> {
                rpmCommentMapper.partialUpdate(existingRpmComment, rpmCommentDTO);

                return existingRpmComment;
            })
            .map(rpmCommentRepository::save)
            .map(rpmCommentMapper::toDto);
    }

    /**
     * Get all the rpmComments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RpmCommentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RpmComments");
        return rpmCommentRepository.findAll(pageable).map(rpmCommentMapper::toDto);
    }

    /**
     * Get one rpmComment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RpmCommentDTO> findOne(Long id) {
        log.debug("Request to get RpmComment : {}", id);
        return rpmCommentRepository.findById(id).map(rpmCommentMapper::toDto);
    }

    /**
     * Delete the rpmComment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RpmComment : {}", id);
        rpmCommentRepository.deleteById(id);
    }
}
