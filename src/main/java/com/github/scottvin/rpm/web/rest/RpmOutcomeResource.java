package com.github.scottvin.rpm.web.rest;

import com.github.scottvin.rpm.repository.RpmOutcomeRepository;
import com.github.scottvin.rpm.service.RpmOutcomeService;
import com.github.scottvin.rpm.service.dto.RpmOutcomeDTO;
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
 * REST controller for managing {@link com.github.scottvin.rpm.domain.RpmOutcome}.
 */
@RestController
@RequestMapping("/api")
public class RpmOutcomeResource {

    private final Logger log = LoggerFactory.getLogger(RpmOutcomeResource.class);

    private static final String ENTITY_NAME = "rpmOutcome";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RpmOutcomeService rpmOutcomeService;

    private final RpmOutcomeRepository rpmOutcomeRepository;

    public RpmOutcomeResource(RpmOutcomeService rpmOutcomeService, RpmOutcomeRepository rpmOutcomeRepository) {
        this.rpmOutcomeService = rpmOutcomeService;
        this.rpmOutcomeRepository = rpmOutcomeRepository;
    }

    /**
     * {@code POST  /rpm-outcomes} : Create a new rpmOutcome.
     *
     * @param rpmOutcomeDTO the rpmOutcomeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rpmOutcomeDTO, or with status {@code 400 (Bad Request)} if the rpmOutcome has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rpm-outcomes")
    public ResponseEntity<RpmOutcomeDTO> createRpmOutcome(@Valid @RequestBody RpmOutcomeDTO rpmOutcomeDTO) throws URISyntaxException {
        log.debug("REST request to save RpmOutcome : {}", rpmOutcomeDTO);
        if (rpmOutcomeDTO.getId() != null) {
            throw new BadRequestAlertException("A new rpmOutcome cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RpmOutcomeDTO result = rpmOutcomeService.save(rpmOutcomeDTO);
        return ResponseEntity
            .created(new URI("/api/rpm-outcomes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rpm-outcomes/:id} : Updates an existing rpmOutcome.
     *
     * @param id the id of the rpmOutcomeDTO to save.
     * @param rpmOutcomeDTO the rpmOutcomeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rpmOutcomeDTO,
     * or with status {@code 400 (Bad Request)} if the rpmOutcomeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rpmOutcomeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rpm-outcomes/{id}")
    public ResponseEntity<RpmOutcomeDTO> updateRpmOutcome(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RpmOutcomeDTO rpmOutcomeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RpmOutcome : {}, {}", id, rpmOutcomeDTO);
        if (rpmOutcomeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rpmOutcomeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rpmOutcomeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RpmOutcomeDTO result = rpmOutcomeService.save(rpmOutcomeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rpmOutcomeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rpm-outcomes/:id} : Partial updates given fields of an existing rpmOutcome, field will ignore if it is null
     *
     * @param id the id of the rpmOutcomeDTO to save.
     * @param rpmOutcomeDTO the rpmOutcomeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rpmOutcomeDTO,
     * or with status {@code 400 (Bad Request)} if the rpmOutcomeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rpmOutcomeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rpmOutcomeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rpm-outcomes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RpmOutcomeDTO> partialUpdateRpmOutcome(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RpmOutcomeDTO rpmOutcomeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RpmOutcome partially : {}, {}", id, rpmOutcomeDTO);
        if (rpmOutcomeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rpmOutcomeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rpmOutcomeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RpmOutcomeDTO> result = rpmOutcomeService.partialUpdate(rpmOutcomeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rpmOutcomeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rpm-outcomes} : get all the rpmOutcomes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rpmOutcomes in body.
     */
    @GetMapping("/rpm-outcomes")
    public ResponseEntity<List<RpmOutcomeDTO>> getAllRpmOutcomes(Pageable pageable) {
        log.debug("REST request to get a page of RpmOutcomes");
        Page<RpmOutcomeDTO> page = rpmOutcomeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rpm-outcomes/:id} : get the "id" rpmOutcome.
     *
     * @param id the id of the rpmOutcomeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rpmOutcomeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rpm-outcomes/{id}")
    public ResponseEntity<RpmOutcomeDTO> getRpmOutcome(@PathVariable Long id) {
        log.debug("REST request to get RpmOutcome : {}", id);
        Optional<RpmOutcomeDTO> rpmOutcomeDTO = rpmOutcomeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rpmOutcomeDTO);
    }

    /**
     * {@code DELETE  /rpm-outcomes/:id} : delete the "id" rpmOutcome.
     *
     * @param id the id of the rpmOutcomeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rpm-outcomes/{id}")
    public ResponseEntity<Void> deleteRpmOutcome(@PathVariable Long id) {
        log.debug("REST request to delete RpmOutcome : {}", id);
        rpmOutcomeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
