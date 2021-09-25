package com.github.scottvin.rpm.service.impl;

import com.github.scottvin.rpm.domain.RpmProject;
import com.github.scottvin.rpm.repository.RpmProjectRepository;
import com.github.scottvin.rpm.service.RpmProjectService;
import com.github.scottvin.rpm.service.dto.RpmProjectDTO;
import com.github.scottvin.rpm.service.mapper.RpmProjectMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RpmProject}.
 */
@Service
@Transactional
public class RpmProjectServiceImpl implements RpmProjectService {

    private final Logger log = LoggerFactory.getLogger(RpmProjectServiceImpl.class);

    private final RpmProjectRepository rpmProjectRepository;

    private final RpmProjectMapper rpmProjectMapper;

    public RpmProjectServiceImpl(RpmProjectRepository rpmProjectRepository, RpmProjectMapper rpmProjectMapper) {
        this.rpmProjectRepository = rpmProjectRepository;
        this.rpmProjectMapper = rpmProjectMapper;
    }

    @Override
    public RpmProjectDTO save(RpmProjectDTO rpmProjectDTO) {
        log.debug("Request to save RpmProject : {}", rpmProjectDTO);
        RpmProject rpmProject = rpmProjectMapper.toEntity(rpmProjectDTO);
        rpmProject = rpmProjectRepository.save(rpmProject);
        return rpmProjectMapper.toDto(rpmProject);
    }

    @Override
    public Optional<RpmProjectDTO> partialUpdate(RpmProjectDTO rpmProjectDTO) {
        log.debug("Request to partially update RpmProject : {}", rpmProjectDTO);

        return rpmProjectRepository
            .findById(rpmProjectDTO.getId())
            .map(existingRpmProject -> {
                rpmProjectMapper.partialUpdate(existingRpmProject, rpmProjectDTO);

                return existingRpmProject;
            })
            .map(rpmProjectRepository::save)
            .map(rpmProjectMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RpmProjectDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RpmProjects");
        return rpmProjectRepository.findAll(pageable).map(rpmProjectMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RpmProjectDTO> findOne(Long id) {
        log.debug("Request to get RpmProject : {}", id);
        return rpmProjectRepository.findById(id).map(rpmProjectMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RpmProject : {}", id);
        rpmProjectRepository.deleteById(id);
    }
}
