package com.github.scottvin.rpm.service;

import com.github.scottvin.rpm.domain.RpmQuote;
import com.github.scottvin.rpm.repository.RpmQuoteRepository;
import com.github.scottvin.rpm.service.dto.RpmQuoteDTO;
import com.github.scottvin.rpm.service.mapper.RpmQuoteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RpmQuote}.
 */
@Service
@Transactional
public class RpmQuoteService {

    private final Logger log = LoggerFactory.getLogger(RpmQuoteService.class);

    private final RpmQuoteRepository rpmQuoteRepository;

    private final RpmQuoteMapper rpmQuoteMapper;

    public RpmQuoteService(RpmQuoteRepository rpmQuoteRepository, RpmQuoteMapper rpmQuoteMapper) {
        this.rpmQuoteRepository = rpmQuoteRepository;
        this.rpmQuoteMapper = rpmQuoteMapper;
    }

    /**
     * Save a rpmQuote.
     *
     * @param rpmQuoteDTO the entity to save.
     * @return the persisted entity.
     */
    public RpmQuoteDTO save(RpmQuoteDTO rpmQuoteDTO) {
        log.debug("Request to save RpmQuote : {}", rpmQuoteDTO);
        RpmQuote rpmQuote = rpmQuoteMapper.toEntity(rpmQuoteDTO);
        rpmQuote = rpmQuoteRepository.save(rpmQuote);
        return rpmQuoteMapper.toDto(rpmQuote);
    }

    /**
     * Partially update a rpmQuote.
     *
     * @param rpmQuoteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RpmQuoteDTO> partialUpdate(RpmQuoteDTO rpmQuoteDTO) {
        log.debug("Request to partially update RpmQuote : {}", rpmQuoteDTO);

        return rpmQuoteRepository
            .findById(rpmQuoteDTO.getId())
            .map(existingRpmQuote -> {
                rpmQuoteMapper.partialUpdate(existingRpmQuote, rpmQuoteDTO);

                return existingRpmQuote;
            })
            .map(rpmQuoteRepository::save)
            .map(rpmQuoteMapper::toDto);
    }

    /**
     * Get all the rpmQuotes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RpmQuoteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RpmQuotes");
        return rpmQuoteRepository.findAll(pageable).map(rpmQuoteMapper::toDto);
    }

    /**
     * Get one rpmQuote by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RpmQuoteDTO> findOne(Long id) {
        log.debug("Request to get RpmQuote : {}", id);
        return rpmQuoteRepository.findById(id).map(rpmQuoteMapper::toDto);
    }

    /**
     * Delete the rpmQuote by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RpmQuote : {}", id);
        rpmQuoteRepository.deleteById(id);
    }
}
