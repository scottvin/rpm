package com.github.scottvin.rpm.web.rest;

import com.github.scottvin.rpm.repository.RpmPurposeRepository;
import com.github.scottvin.rpm.service.RpmPurposeService;
import com.github.scottvin.rpm.service.dto.RpmPurposeDTO;
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
 * REST controller for managing {@link com.github.scottvin.rpm.domain.RpmPurpose}.
 */
@RestController
@RequestMapping("/api")
public class RpmPurposeResource {

    private final Logger log = LoggerFactory.getLogger(RpmPurposeResource.class);

    private static final String ENTITY_NAME = "rpmPurpose";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RpmPurposeService rpmPurposeService;

    private final RpmPurposeRepository rpmPurposeRepository;

    public RpmPurposeResource(RpmPurposeService rpmPurposeService, RpmPurposeRepository rpmPurposeRepository) {
        this.rpmPurposeService = rpmPurposeService;
        this.rpmPurposeRepository = rpmPurposeRepository;
    }

    /**
     * {@code POST  /rpm-purposes} : Create a new rpmPurpose.
     *
     * @param rpmPurposeDTO the rpmPurposeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rpmPurposeDTO, or with status {@code 400 (Bad Request)} if the rpmPurpose has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rpm-purposes")
    public ResponseEntity<RpmPurposeDTO> createRpmPurpose(@Valid @RequestBody RpmPurposeDTO rpmPurposeDTO) throws URISyntaxException {
        log.debug("REST request to save RpmPurpose : {}", rpmPurposeDTO);
        if (rpmPurposeDTO.getId() != null) {
            throw new BadRequestAlertException("A new rpmPurpose cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RpmPurposeDTO result = rpmPurposeService.save(rpmPurposeDTO);
        return ResponseEntity
            .created(new URI("/api/rpm-purposes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rpm-purposes/:id} : Updates an existing rpmPurpose.
     *
     * @param id the id of the rpmPurposeDTO to save.
     * @param rpmPurposeDTO the rpmPurposeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rpmPurposeDTO,
     * or with status {@code 400 (Bad Request)} if the rpmPurposeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rpmPurposeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rpm-purposes/{id}")
    public ResponseEntity<RpmPurposeDTO> updateRpmPurpose(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RpmPurposeDTO rpmPurposeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RpmPurpose : {}, {}", id, rpmPurposeDTO);
        if (rpmPurposeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rpmPurposeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rpmPurposeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RpmPurposeDTO result = rpmPurposeService.save(rpmPurposeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rpmPurposeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rpm-purposes/:id} : Partial updates given fields of an existing rpmPurpose, field will ignore if it is null
     *
     * @param id the id of the rpmPurposeDTO to save.
     * @param rpmPurposeDTO the rpmPurposeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rpmPurposeDTO,
     * or with status {@code 400 (Bad Request)} if the rpmPurposeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rpmPurposeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rpmPurposeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rpm-purposes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RpmPurposeDTO> partialUpdateRpmPurpose(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RpmPurposeDTO rpmPurposeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RpmPurpose partially : {}, {}", id, rpmPurposeDTO);
        if (rpmPurposeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rpmPurposeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rpmPurposeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RpmPurposeDTO> result = rpmPurposeService.partialUpdate(rpmPurposeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rpmPurposeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rpm-purposes} : get all the rpmPurposes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rpmPurposes in body.
     */
    @GetMapping("/rpm-purposes")
    public ResponseEntity<List<RpmPurposeDTO>> getAllRpmPurposes(Pageable pageable) {
        log.debug("REST request to get a page of RpmPurposes");
        Page<RpmPurposeDTO> page = rpmPurposeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rpm-purposes/:id} : get the "id" rpmPurpose.
     *
     * @param id the id of the rpmPurposeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rpmPurposeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rpm-purposes/{id}")
    public ResponseEntity<RpmPurposeDTO> getRpmPurpose(@PathVariable Long id) {
        log.debug("REST request to get RpmPurpose : {}", id);
        Optional<RpmPurposeDTO> rpmPurposeDTO = rpmPurposeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rpmPurposeDTO);
    }

    /**
     * {@code DELETE  /rpm-purposes/:id} : delete the "id" rpmPurpose.
     *
     * @param id the id of the rpmPurposeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rpm-purposes/{id}")
    public ResponseEntity<Void> deleteRpmPurpose(@PathVariable Long id) {
        log.debug("REST request to delete RpmPurpose : {}", id);
        rpmPurposeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
