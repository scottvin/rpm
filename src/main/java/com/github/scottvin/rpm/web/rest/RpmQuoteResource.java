package com.github.scottvin.rpm.web.rest;

import com.github.scottvin.rpm.repository.RpmQuoteRepository;
import com.github.scottvin.rpm.service.RpmQuoteService;
import com.github.scottvin.rpm.service.dto.RpmQuoteDTO;
import com.github.scottvin.rpm.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.github.scottvin.rpm.domain.RpmQuote}.
 */
@RestController
@RequestMapping("/api")
public class RpmQuoteResource {

    private final Logger log = LoggerFactory.getLogger(RpmQuoteResource.class);

    private static final String ENTITY_NAME = "rpmQuote";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RpmQuoteService rpmQuoteService;

    private final RpmQuoteRepository rpmQuoteRepository;

    public RpmQuoteResource(RpmQuoteService rpmQuoteService, RpmQuoteRepository rpmQuoteRepository) {
        this.rpmQuoteService = rpmQuoteService;
        this.rpmQuoteRepository = rpmQuoteRepository;
    }

    /**
     * {@code POST  /rpm-quotes} : Create a new rpmQuote.
     *
     * @param rpmQuoteDTO the rpmQuoteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rpmQuoteDTO, or with status {@code 400 (Bad Request)} if the rpmQuote has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rpm-quotes")
    public ResponseEntity<RpmQuoteDTO> createRpmQuote(@Valid @RequestBody RpmQuoteDTO rpmQuoteDTO) throws URISyntaxException {
        log.debug("REST request to save RpmQuote : {}", rpmQuoteDTO);
        if (rpmQuoteDTO.getId() != null) {
            throw new BadRequestAlertException("A new rpmQuote cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RpmQuoteDTO result = rpmQuoteService.save(rpmQuoteDTO);
        return ResponseEntity
            .created(new URI("/api/rpm-quotes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rpm-quotes/:id} : Updates an existing rpmQuote.
     *
     * @param id the id of the rpmQuoteDTO to save.
     * @param rpmQuoteDTO the rpmQuoteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rpmQuoteDTO,
     * or with status {@code 400 (Bad Request)} if the rpmQuoteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rpmQuoteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rpm-quotes/{id}")
    public ResponseEntity<RpmQuoteDTO> updateRpmQuote(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RpmQuoteDTO rpmQuoteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RpmQuote : {}, {}", id, rpmQuoteDTO);
        if (rpmQuoteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rpmQuoteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rpmQuoteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RpmQuoteDTO result = rpmQuoteService.save(rpmQuoteDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rpmQuoteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rpm-quotes/:id} : Partial updates given fields of an existing rpmQuote, field will ignore if it is null
     *
     * @param id the id of the rpmQuoteDTO to save.
     * @param rpmQuoteDTO the rpmQuoteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rpmQuoteDTO,
     * or with status {@code 400 (Bad Request)} if the rpmQuoteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rpmQuoteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rpmQuoteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rpm-quotes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RpmQuoteDTO> partialUpdateRpmQuote(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RpmQuoteDTO rpmQuoteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RpmQuote partially : {}, {}", id, rpmQuoteDTO);
        if (rpmQuoteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rpmQuoteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rpmQuoteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RpmQuoteDTO> result = rpmQuoteService.partialUpdate(rpmQuoteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rpmQuoteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rpm-quotes} : get all the rpmQuotes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rpmQuotes in body.
     */
    @GetMapping("/rpm-quotes")
    public ResponseEntity<List<RpmQuoteDTO>> getAllRpmQuotes(Pageable pageable) {
        log.debug("REST request to get a page of RpmQuotes");
        Page<RpmQuoteDTO> page = rpmQuoteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rpm-quotes/:id} : get the "id" rpmQuote.
     *
     * @param id the id of the rpmQuoteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rpmQuoteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rpm-quotes/{id}")
    public ResponseEntity<RpmQuoteDTO> getRpmQuote(@PathVariable Long id) {
        log.debug("REST request to get RpmQuote : {}", id);
        Optional<RpmQuoteDTO> rpmQuoteDTO = rpmQuoteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rpmQuoteDTO);
    }

    /**
     * {@code DELETE  /rpm-quotes/:id} : delete the "id" rpmQuote.
     *
     * @param id the id of the rpmQuoteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rpm-quotes/{id}")
    public ResponseEntity<Void> deleteRpmQuote(@PathVariable Long id) {
        log.debug("REST request to delete RpmQuote : {}", id);
        rpmQuoteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
