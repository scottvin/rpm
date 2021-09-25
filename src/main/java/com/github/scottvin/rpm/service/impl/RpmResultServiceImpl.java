package com.github.scottvin.rpm.service.impl;

import com.github.scottvin.rpm.domain.RpmResult;
import com.github.scottvin.rpm.repository.RpmResultRepository;
import com.github.scottvin.rpm.service.RpmResultService;
import com.github.scottvin.rpm.service.dto.RpmResultDTO;
import com.github.scottvin.rpm.service.mapper.RpmResultMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RpmResult}.
 */
@Service
@Transactional
public class RpmResultServiceImpl implements RpmResultService {

    private final Logger log = LoggerFactory.getLogger(RpmResultServiceImpl.class);

    private final RpmResultRepository rpmResultRepository;

    private final RpmResultMapper rpmResultMapper;

    public RpmResultServiceImpl(RpmResultRepository rpmResultRepository, RpmResultMapper rpmResultMapper) {
        this.rpmResultRepository = rpmResultRepository;
        this.rpmResultMapper = rpmResultMapper;
    }

    @Override
    public RpmResultDTO save(RpmResultDTO rpmResultDTO) {
        log.debug("Request to save RpmResult : {}", rpmResultDTO);
        RpmResult rpmResult = rpmResultMapper.toEntity(rpmResultDTO);
        rpmResult = rpmResultRepository.save(rpmResult);
        return rpmResultMapper.toDto(rpmResult);
    }

    @Override
    public Optional<RpmResultDTO> partialUpdate(RpmResultDTO rpmResultDTO) {
        log.debug("Request to partially update RpmResult : {}", rpmResultDTO);

        return rpmResultRepository
            .findById(rpmResultDTO.getId())
            .map(existingRpmResult -> {
                rpmResultMapper.partialUpdate(existingRpmResult, rpmResultDTO);

                return existingRpmResult;
            })
            .map(rpmResultRepository::save)
            .map(rpmResultMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RpmResultDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RpmResults");
        return rpmResultRepository.findAll(pageable).map(rpmResultMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RpmResultDTO> findOne(Long id) {
        log.debug("Request to get RpmResult : {}", id);
        return rpmResultRepository.findById(id).map(rpmResultMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RpmResult : {}", id);
        rpmResultRepository.deleteById(id);
    }
}
