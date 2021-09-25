package com.github.scottvin.rpm.service.impl;

import com.github.scottvin.rpm.domain.RpmCharacter;
import com.github.scottvin.rpm.repository.RpmCharacterRepository;
import com.github.scottvin.rpm.service.RpmCharacterService;
import com.github.scottvin.rpm.service.dto.RpmCharacterDTO;
import com.github.scottvin.rpm.service.mapper.RpmCharacterMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RpmCharacter}.
 */
@Service
@Transactional
public class RpmCharacterServiceImpl implements RpmCharacterService {

    private final Logger log = LoggerFactory.getLogger(RpmCharacterServiceImpl.class);

    private final RpmCharacterRepository rpmCharacterRepository;

    private final RpmCharacterMapper rpmCharacterMapper;

    public RpmCharacterServiceImpl(RpmCharacterRepository rpmCharacterRepository, RpmCharacterMapper rpmCharacterMapper) {
        this.rpmCharacterRepository = rpmCharacterRepository;
        this.rpmCharacterMapper = rpmCharacterMapper;
    }

    @Override
    public RpmCharacterDTO save(RpmCharacterDTO rpmCharacterDTO) {
        log.debug("Request to save RpmCharacter : {}", rpmCharacterDTO);
        RpmCharacter rpmCharacter = rpmCharacterMapper.toEntity(rpmCharacterDTO);
        rpmCharacter = rpmCharacterRepository.save(rpmCharacter);
        return rpmCharacterMapper.toDto(rpmCharacter);
    }

    @Override
    public Optional<RpmCharacterDTO> partialUpdate(RpmCharacterDTO rpmCharacterDTO) {
        log.debug("Request to partially update RpmCharacter : {}", rpmCharacterDTO);

        return rpmCharacterRepository
            .findById(rpmCharacterDTO.getId())
            .map(existingRpmCharacter -> {
                rpmCharacterMapper.partialUpdate(existingRpmCharacter, rpmCharacterDTO);

                return existingRpmCharacter;
            })
            .map(rpmCharacterRepository::save)
            .map(rpmCharacterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RpmCharacterDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RpmCharacters");
        return rpmCharacterRepository.findAll(pageable).map(rpmCharacterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RpmCharacterDTO> findOne(Long id) {
        log.debug("Request to get RpmCharacter : {}", id);
        return rpmCharacterRepository.findById(id).map(rpmCharacterMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RpmCharacter : {}", id);
        rpmCharacterRepository.deleteById(id);
    }
}
