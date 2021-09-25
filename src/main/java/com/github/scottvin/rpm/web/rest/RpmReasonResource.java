package com.github.scottvin.rpm.web.rest;

import com.github.scottvin.rpm.repository.RpmReasonRepository;
import com.github.scottvin.rpm.service.RpmReasonService;
import com.github.scottvin.rpm.service.dto.RpmReasonDTO;
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
 * REST controller for managing {@link com.github.scottvin.rpm.domain.RpmReason}.
 */
@RestController
@RequestMapping("/api")
public class RpmReasonResource {

    private final Logger log = LoggerFactory.getLogger(RpmReasonResource.class);

    private static final String ENTITY_NAME = "rpmReason";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RpmReasonService rpmReasonService;

    private final RpmReasonRepository rpmReasonRepository;

    public RpmReasonResource(RpmReasonService rpmReasonService, RpmReasonRepository rpmReasonRepository) {
        this.rpmReasonService = rpmReasonService;
        this.rpmReasonRepository = rpmReasonRepository;
    }

    /**
     * {@code POST  /rpm-reasons} : Create a new rpmReason.
     *
     * @param rpmReasonDTO the rpmReasonDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rpmReasonDTO, or with status {@code 400 (Bad Request)} if the rpmReason has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rpm-reasons")
    public ResponseEntity<RpmReasonDTO> createRpmReason(@Valid @RequestBody RpmReasonDTO rpmReasonDTO) throws URISyntaxException {
        log.debug("REST request to save RpmReason : {}", rpmReasonDTO);
        if (rpmReasonDTO.getId() != null) {
            throw new BadRequestAlertException("A new rpmReason cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RpmReasonDTO result = rpmReasonService.save(rpmReasonDTO);
        return ResponseEntity
            .created(new URI("/api/rpm-reasons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rpm-reasons/:id} : Updates an existing rpmReason.
     *
     * @param id the id of the rpmReasonDTO to save.
     * @param rpmReasonDTO the rpmReasonDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rpmReasonDTO,
     * or with status {@code 400 (Bad Request)} if the rpmReasonDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rpmReasonDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rpm-reasons/{id}")
    public ResponseEntity<RpmReasonDTO> updateRpmReason(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RpmReasonDTO rpmReasonDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RpmReason : {}, {}", id, rpmReasonDTO);
        if (rpmReasonDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rpmReasonDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rpmReasonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RpmReasonDTO result = rpmReasonService.save(rpmReasonDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rpmReasonDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rpm-reasons/:id} : Partial updates given fields of an existing rpmReason, field will ignore if it is null
     *
     * @param id the id of the rpmReasonDTO to save.
     * @param rpmReasonDTO the rpmReasonDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rpmReasonDTO,
     * or with status {@code 400 (Bad Request)} if the rpmReasonDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rpmReasonDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rpmReasonDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rpm-reasons/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RpmReasonDTO> partialUpdateRpmReason(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RpmReasonDTO rpmReasonDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RpmReason partially : {}, {}", id, rpmReasonDTO);
        if (rpmReasonDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rpmReasonDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rpmReasonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RpmReasonDTO> result = rpmReasonService.partialUpdate(rpmReasonDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rpmReasonDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rpm-reasons} : get all the rpmReasons.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rpmReasons in body.
     */
    @GetMapping("/rpm-reasons")
    public ResponseEntity<List<RpmReasonDTO>> getAllRpmReasons(Pageable pageable) {
        log.debug("REST request to get a page of RpmReasons");
        Page<RpmReasonDTO> page = rpmReasonService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rpm-reasons/:id} : get the "id" rpmReason.
     *
     * @param id the id of the rpmReasonDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rpmReasonDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rpm-reasons/{id}")
    public ResponseEntity<RpmReasonDTO> getRpmReason(@PathVariable Long id) {
        log.debug("REST request to get RpmReason : {}", id);
        Optional<RpmReasonDTO> rpmReasonDTO = rpmReasonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rpmReasonDTO);
    }

    /**
     * {@code DELETE  /rpm-reasons/:id} : delete the "id" rpmReason.
     *
     * @param id the id of the rpmReasonDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rpm-reasons/{id}")
    public ResponseEntity<Void> deleteRpmReason(@PathVariable Long id) {
        log.debug("REST request to delete RpmReason : {}", id);
        rpmReasonService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
