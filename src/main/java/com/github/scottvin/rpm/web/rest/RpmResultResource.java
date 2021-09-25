package com.github.scottvin.rpm.web.rest;

import com.github.scottvin.rpm.repository.RpmResultRepository;
import com.github.scottvin.rpm.service.RpmResultQueryService;
import com.github.scottvin.rpm.service.RpmResultService;
import com.github.scottvin.rpm.service.criteria.RpmResultCriteria;
import com.github.scottvin.rpm.service.dto.RpmResultDTO;
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
 * REST controller for managing {@link com.github.scottvin.rpm.domain.RpmResult}.
 */
@RestController
@RequestMapping("/api")
public class RpmResultResource {

    private final Logger log = LoggerFactory.getLogger(RpmResultResource.class);

    private static final String ENTITY_NAME = "rpmResult";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RpmResultService rpmResultService;

    private final RpmResultRepository rpmResultRepository;

    private final RpmResultQueryService rpmResultQueryService;

    public RpmResultResource(
        RpmResultService rpmResultService,
        RpmResultRepository rpmResultRepository,
        RpmResultQueryService rpmResultQueryService
    ) {
        this.rpmResultService = rpmResultService;
        this.rpmResultRepository = rpmResultRepository;
        this.rpmResultQueryService = rpmResultQueryService;
    }

    /**
     * {@code POST  /rpm-results} : Create a new rpmResult.
     *
     * @param rpmResultDTO the rpmResultDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rpmResultDTO, or with status {@code 400 (Bad Request)} if the rpmResult has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rpm-results")
    public ResponseEntity<RpmResultDTO> createRpmResult(@Valid @RequestBody RpmResultDTO rpmResultDTO) throws URISyntaxException {
        log.debug("REST request to save RpmResult : {}", rpmResultDTO);
        if (rpmResultDTO.getId() != null) {
            throw new BadRequestAlertException("A new rpmResult cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RpmResultDTO result = rpmResultService.save(rpmResultDTO);
        return ResponseEntity
            .created(new URI("/api/rpm-results/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rpm-results/:id} : Updates an existing rpmResult.
     *
     * @param id the id of the rpmResultDTO to save.
     * @param rpmResultDTO the rpmResultDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rpmResultDTO,
     * or with status {@code 400 (Bad Request)} if the rpmResultDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rpmResultDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rpm-results/{id}")
    public ResponseEntity<RpmResultDTO> updateRpmResult(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RpmResultDTO rpmResultDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RpmResult : {}, {}", id, rpmResultDTO);
        if (rpmResultDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rpmResultDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rpmResultRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RpmResultDTO result = rpmResultService.save(rpmResultDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rpmResultDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rpm-results/:id} : Partial updates given fields of an existing rpmResult, field will ignore if it is null
     *
     * @param id the id of the rpmResultDTO to save.
     * @param rpmResultDTO the rpmResultDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rpmResultDTO,
     * or with status {@code 400 (Bad Request)} if the rpmResultDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rpmResultDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rpmResultDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rpm-results/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RpmResultDTO> partialUpdateRpmResult(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RpmResultDTO rpmResultDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RpmResult partially : {}, {}", id, rpmResultDTO);
        if (rpmResultDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rpmResultDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rpmResultRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RpmResultDTO> result = rpmResultService.partialUpdate(rpmResultDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rpmResultDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rpm-results} : get all the rpmResults.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rpmResults in body.
     */
    @GetMapping("/rpm-results")
    public ResponseEntity<List<RpmResultDTO>> getAllRpmResults(RpmResultCriteria criteria, Pageable pageable) {
        log.debug("REST request to get RpmResults by criteria: {}", criteria);
        Page<RpmResultDTO> page = rpmResultQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rpm-results/count} : count all the rpmResults.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/rpm-results/count")
    public ResponseEntity<Long> countRpmResults(RpmResultCriteria criteria) {
        log.debug("REST request to count RpmResults by criteria: {}", criteria);
        return ResponseEntity.ok().body(rpmResultQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /rpm-results/:id} : get the "id" rpmResult.
     *
     * @param id the id of the rpmResultDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rpmResultDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rpm-results/{id}")
    public ResponseEntity<RpmResultDTO> getRpmResult(@PathVariable Long id) {
        log.debug("REST request to get RpmResult : {}", id);
        Optional<RpmResultDTO> rpmResultDTO = rpmResultService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rpmResultDTO);
    }

    /**
     * {@code DELETE  /rpm-results/:id} : delete the "id" rpmResult.
     *
     * @param id the id of the rpmResultDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rpm-results/{id}")
    public ResponseEntity<Void> deleteRpmResult(@PathVariable Long id) {
        log.debug("REST request to delete RpmResult : {}", id);
        rpmResultService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
