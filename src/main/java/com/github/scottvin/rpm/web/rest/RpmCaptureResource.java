package com.github.scottvin.rpm.web.rest;

import com.github.scottvin.rpm.repository.RpmCaptureRepository;
import com.github.scottvin.rpm.service.RpmCaptureService;
import com.github.scottvin.rpm.service.dto.RpmCaptureDTO;
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
 * REST controller for managing {@link com.github.scottvin.rpm.domain.RpmCapture}.
 */
@RestController
@RequestMapping("/api")
public class RpmCaptureResource {

    private final Logger log = LoggerFactory.getLogger(RpmCaptureResource.class);

    private static final String ENTITY_NAME = "rpmCapture";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RpmCaptureService rpmCaptureService;

    private final RpmCaptureRepository rpmCaptureRepository;

    public RpmCaptureResource(RpmCaptureService rpmCaptureService, RpmCaptureRepository rpmCaptureRepository) {
        this.rpmCaptureService = rpmCaptureService;
        this.rpmCaptureRepository = rpmCaptureRepository;
    }

    /**
     * {@code POST  /rpm-captures} : Create a new rpmCapture.
     *
     * @param rpmCaptureDTO the rpmCaptureDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rpmCaptureDTO, or with status {@code 400 (Bad Request)} if the rpmCapture has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rpm-captures")
    public ResponseEntity<RpmCaptureDTO> createRpmCapture(@Valid @RequestBody RpmCaptureDTO rpmCaptureDTO) throws URISyntaxException {
        log.debug("REST request to save RpmCapture : {}", rpmCaptureDTO);
        if (rpmCaptureDTO.getId() != null) {
            throw new BadRequestAlertException("A new rpmCapture cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RpmCaptureDTO result = rpmCaptureService.save(rpmCaptureDTO);
        return ResponseEntity
            .created(new URI("/api/rpm-captures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rpm-captures/:id} : Updates an existing rpmCapture.
     *
     * @param id the id of the rpmCaptureDTO to save.
     * @param rpmCaptureDTO the rpmCaptureDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rpmCaptureDTO,
     * or with status {@code 400 (Bad Request)} if the rpmCaptureDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rpmCaptureDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rpm-captures/{id}")
    public ResponseEntity<RpmCaptureDTO> updateRpmCapture(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RpmCaptureDTO rpmCaptureDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RpmCapture : {}, {}", id, rpmCaptureDTO);
        if (rpmCaptureDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rpmCaptureDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rpmCaptureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RpmCaptureDTO result = rpmCaptureService.save(rpmCaptureDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rpmCaptureDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rpm-captures/:id} : Partial updates given fields of an existing rpmCapture, field will ignore if it is null
     *
     * @param id the id of the rpmCaptureDTO to save.
     * @param rpmCaptureDTO the rpmCaptureDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rpmCaptureDTO,
     * or with status {@code 400 (Bad Request)} if the rpmCaptureDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rpmCaptureDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rpmCaptureDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rpm-captures/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RpmCaptureDTO> partialUpdateRpmCapture(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RpmCaptureDTO rpmCaptureDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RpmCapture partially : {}, {}", id, rpmCaptureDTO);
        if (rpmCaptureDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rpmCaptureDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rpmCaptureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RpmCaptureDTO> result = rpmCaptureService.partialUpdate(rpmCaptureDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rpmCaptureDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rpm-captures} : get all the rpmCaptures.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rpmCaptures in body.
     */
    @GetMapping("/rpm-captures")
    public ResponseEntity<List<RpmCaptureDTO>> getAllRpmCaptures(Pageable pageable) {
        log.debug("REST request to get a page of RpmCaptures");
        Page<RpmCaptureDTO> page = rpmCaptureService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rpm-captures/:id} : get the "id" rpmCapture.
     *
     * @param id the id of the rpmCaptureDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rpmCaptureDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rpm-captures/{id}")
    public ResponseEntity<RpmCaptureDTO> getRpmCapture(@PathVariable Long id) {
        log.debug("REST request to get RpmCapture : {}", id);
        Optional<RpmCaptureDTO> rpmCaptureDTO = rpmCaptureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rpmCaptureDTO);
    }

    /**
     * {@code DELETE  /rpm-captures/:id} : delete the "id" rpmCapture.
     *
     * @param id the id of the rpmCaptureDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rpm-captures/{id}")
    public ResponseEntity<Void> deleteRpmCapture(@PathVariable Long id) {
        log.debug("REST request to delete RpmCapture : {}", id);
        rpmCaptureService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
