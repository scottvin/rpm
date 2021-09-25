package com.github.scottvin.rpm.web.rest;

import com.github.scottvin.rpm.repository.RpmPracticeRepository;
import com.github.scottvin.rpm.service.RpmPracticeService;
import com.github.scottvin.rpm.service.dto.RpmPracticeDTO;
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
 * REST controller for managing {@link com.github.scottvin.rpm.domain.RpmPractice}.
 */
@RestController
@RequestMapping("/api")
public class RpmPracticeResource {

    private final Logger log = LoggerFactory.getLogger(RpmPracticeResource.class);

    private static final String ENTITY_NAME = "rpmPractice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RpmPracticeService rpmPracticeService;

    private final RpmPracticeRepository rpmPracticeRepository;

    public RpmPracticeResource(RpmPracticeService rpmPracticeService, RpmPracticeRepository rpmPracticeRepository) {
        this.rpmPracticeService = rpmPracticeService;
        this.rpmPracticeRepository = rpmPracticeRepository;
    }

    /**
     * {@code POST  /rpm-practices} : Create a new rpmPractice.
     *
     * @param rpmPracticeDTO the rpmPracticeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rpmPracticeDTO, or with status {@code 400 (Bad Request)} if the rpmPractice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rpm-practices")
    public ResponseEntity<RpmPracticeDTO> createRpmPractice(@Valid @RequestBody RpmPracticeDTO rpmPracticeDTO) throws URISyntaxException {
        log.debug("REST request to save RpmPractice : {}", rpmPracticeDTO);
        if (rpmPracticeDTO.getId() != null) {
            throw new BadRequestAlertException("A new rpmPractice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RpmPracticeDTO result = rpmPracticeService.save(rpmPracticeDTO);
        return ResponseEntity
            .created(new URI("/api/rpm-practices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rpm-practices/:id} : Updates an existing rpmPractice.
     *
     * @param id the id of the rpmPracticeDTO to save.
     * @param rpmPracticeDTO the rpmPracticeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rpmPracticeDTO,
     * or with status {@code 400 (Bad Request)} if the rpmPracticeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rpmPracticeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rpm-practices/{id}")
    public ResponseEntity<RpmPracticeDTO> updateRpmPractice(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RpmPracticeDTO rpmPracticeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RpmPractice : {}, {}", id, rpmPracticeDTO);
        if (rpmPracticeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rpmPracticeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rpmPracticeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RpmPracticeDTO result = rpmPracticeService.save(rpmPracticeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rpmPracticeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rpm-practices/:id} : Partial updates given fields of an existing rpmPractice, field will ignore if it is null
     *
     * @param id the id of the rpmPracticeDTO to save.
     * @param rpmPracticeDTO the rpmPracticeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rpmPracticeDTO,
     * or with status {@code 400 (Bad Request)} if the rpmPracticeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rpmPracticeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rpmPracticeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rpm-practices/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RpmPracticeDTO> partialUpdateRpmPractice(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RpmPracticeDTO rpmPracticeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RpmPractice partially : {}, {}", id, rpmPracticeDTO);
        if (rpmPracticeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rpmPracticeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rpmPracticeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RpmPracticeDTO> result = rpmPracticeService.partialUpdate(rpmPracticeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rpmPracticeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rpm-practices} : get all the rpmPractices.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rpmPractices in body.
     */
    @GetMapping("/rpm-practices")
    public ResponseEntity<List<RpmPracticeDTO>> getAllRpmPractices(Pageable pageable) {
        log.debug("REST request to get a page of RpmPractices");
        Page<RpmPracticeDTO> page = rpmPracticeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rpm-practices/:id} : get the "id" rpmPractice.
     *
     * @param id the id of the rpmPracticeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rpmPracticeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rpm-practices/{id}")
    public ResponseEntity<RpmPracticeDTO> getRpmPractice(@PathVariable Long id) {
        log.debug("REST request to get RpmPractice : {}", id);
        Optional<RpmPracticeDTO> rpmPracticeDTO = rpmPracticeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rpmPracticeDTO);
    }

    /**
     * {@code DELETE  /rpm-practices/:id} : delete the "id" rpmPractice.
     *
     * @param id the id of the rpmPracticeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rpm-practices/{id}")
    public ResponseEntity<Void> deleteRpmPractice(@PathVariable Long id) {
        log.debug("REST request to delete RpmPractice : {}", id);
        rpmPracticeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
