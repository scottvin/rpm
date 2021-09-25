package com.github.scottvin.rpm.web.rest;

import com.github.scottvin.rpm.repository.RpmActionRepository;
import com.github.scottvin.rpm.service.RpmActionService;
import com.github.scottvin.rpm.service.dto.RpmActionDTO;
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
 * REST controller for managing {@link com.github.scottvin.rpm.domain.RpmAction}.
 */
@RestController
@RequestMapping("/api")
public class RpmActionResource {

    private final Logger log = LoggerFactory.getLogger(RpmActionResource.class);

    private static final String ENTITY_NAME = "rpmAction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RpmActionService rpmActionService;

    private final RpmActionRepository rpmActionRepository;

    public RpmActionResource(RpmActionService rpmActionService, RpmActionRepository rpmActionRepository) {
        this.rpmActionService = rpmActionService;
        this.rpmActionRepository = rpmActionRepository;
    }

    /**
     * {@code POST  /rpm-actions} : Create a new rpmAction.
     *
     * @param rpmActionDTO the rpmActionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rpmActionDTO, or with status {@code 400 (Bad Request)} if the rpmAction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rpm-actions")
    public ResponseEntity<RpmActionDTO> createRpmAction(@Valid @RequestBody RpmActionDTO rpmActionDTO) throws URISyntaxException {
        log.debug("REST request to save RpmAction : {}", rpmActionDTO);
        if (rpmActionDTO.getId() != null) {
            throw new BadRequestAlertException("A new rpmAction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RpmActionDTO result = rpmActionService.save(rpmActionDTO);
        return ResponseEntity
            .created(new URI("/api/rpm-actions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rpm-actions/:id} : Updates an existing rpmAction.
     *
     * @param id the id of the rpmActionDTO to save.
     * @param rpmActionDTO the rpmActionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rpmActionDTO,
     * or with status {@code 400 (Bad Request)} if the rpmActionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rpmActionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rpm-actions/{id}")
    public ResponseEntity<RpmActionDTO> updateRpmAction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RpmActionDTO rpmActionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RpmAction : {}, {}", id, rpmActionDTO);
        if (rpmActionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rpmActionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rpmActionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RpmActionDTO result = rpmActionService.save(rpmActionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rpmActionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rpm-actions/:id} : Partial updates given fields of an existing rpmAction, field will ignore if it is null
     *
     * @param id the id of the rpmActionDTO to save.
     * @param rpmActionDTO the rpmActionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rpmActionDTO,
     * or with status {@code 400 (Bad Request)} if the rpmActionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rpmActionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rpmActionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rpm-actions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RpmActionDTO> partialUpdateRpmAction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RpmActionDTO rpmActionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RpmAction partially : {}, {}", id, rpmActionDTO);
        if (rpmActionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rpmActionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rpmActionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RpmActionDTO> result = rpmActionService.partialUpdate(rpmActionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rpmActionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rpm-actions} : get all the rpmActions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rpmActions in body.
     */
    @GetMapping("/rpm-actions")
    public ResponseEntity<List<RpmActionDTO>> getAllRpmActions(Pageable pageable) {
        log.debug("REST request to get a page of RpmActions");
        Page<RpmActionDTO> page = rpmActionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rpm-actions/:id} : get the "id" rpmAction.
     *
     * @param id the id of the rpmActionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rpmActionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rpm-actions/{id}")
    public ResponseEntity<RpmActionDTO> getRpmAction(@PathVariable Long id) {
        log.debug("REST request to get RpmAction : {}", id);
        Optional<RpmActionDTO> rpmActionDTO = rpmActionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rpmActionDTO);
    }

    /**
     * {@code DELETE  /rpm-actions/:id} : delete the "id" rpmAction.
     *
     * @param id the id of the rpmActionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rpm-actions/{id}")
    public ResponseEntity<Void> deleteRpmAction(@PathVariable Long id) {
        log.debug("REST request to delete RpmAction : {}", id);
        rpmActionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
