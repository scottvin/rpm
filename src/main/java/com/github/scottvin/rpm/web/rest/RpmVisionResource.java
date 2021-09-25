package com.github.scottvin.rpm.web.rest;

import com.github.scottvin.rpm.repository.RpmVisionRepository;
import com.github.scottvin.rpm.service.RpmVisionService;
import com.github.scottvin.rpm.service.dto.RpmVisionDTO;
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
 * REST controller for managing {@link com.github.scottvin.rpm.domain.RpmVision}.
 */
@RestController
@RequestMapping("/api")
public class RpmVisionResource {

    private final Logger log = LoggerFactory.getLogger(RpmVisionResource.class);

    private static final String ENTITY_NAME = "rpmVision";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RpmVisionService rpmVisionService;

    private final RpmVisionRepository rpmVisionRepository;

    public RpmVisionResource(RpmVisionService rpmVisionService, RpmVisionRepository rpmVisionRepository) {
        this.rpmVisionService = rpmVisionService;
        this.rpmVisionRepository = rpmVisionRepository;
    }

    /**
     * {@code POST  /rpm-visions} : Create a new rpmVision.
     *
     * @param rpmVisionDTO the rpmVisionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rpmVisionDTO, or with status {@code 400 (Bad Request)} if the rpmVision has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rpm-visions")
    public ResponseEntity<RpmVisionDTO> createRpmVision(@Valid @RequestBody RpmVisionDTO rpmVisionDTO) throws URISyntaxException {
        log.debug("REST request to save RpmVision : {}", rpmVisionDTO);
        if (rpmVisionDTO.getId() != null) {
            throw new BadRequestAlertException("A new rpmVision cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RpmVisionDTO result = rpmVisionService.save(rpmVisionDTO);
        return ResponseEntity
            .created(new URI("/api/rpm-visions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rpm-visions/:id} : Updates an existing rpmVision.
     *
     * @param id the id of the rpmVisionDTO to save.
     * @param rpmVisionDTO the rpmVisionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rpmVisionDTO,
     * or with status {@code 400 (Bad Request)} if the rpmVisionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rpmVisionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rpm-visions/{id}")
    public ResponseEntity<RpmVisionDTO> updateRpmVision(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RpmVisionDTO rpmVisionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RpmVision : {}, {}", id, rpmVisionDTO);
        if (rpmVisionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rpmVisionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rpmVisionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RpmVisionDTO result = rpmVisionService.save(rpmVisionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rpmVisionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rpm-visions/:id} : Partial updates given fields of an existing rpmVision, field will ignore if it is null
     *
     * @param id the id of the rpmVisionDTO to save.
     * @param rpmVisionDTO the rpmVisionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rpmVisionDTO,
     * or with status {@code 400 (Bad Request)} if the rpmVisionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rpmVisionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rpmVisionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rpm-visions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RpmVisionDTO> partialUpdateRpmVision(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RpmVisionDTO rpmVisionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RpmVision partially : {}, {}", id, rpmVisionDTO);
        if (rpmVisionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rpmVisionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rpmVisionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RpmVisionDTO> result = rpmVisionService.partialUpdate(rpmVisionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rpmVisionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rpm-visions} : get all the rpmVisions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rpmVisions in body.
     */
    @GetMapping("/rpm-visions")
    public ResponseEntity<List<RpmVisionDTO>> getAllRpmVisions(Pageable pageable) {
        log.debug("REST request to get a page of RpmVisions");
        Page<RpmVisionDTO> page = rpmVisionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rpm-visions/:id} : get the "id" rpmVision.
     *
     * @param id the id of the rpmVisionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rpmVisionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rpm-visions/{id}")
    public ResponseEntity<RpmVisionDTO> getRpmVision(@PathVariable Long id) {
        log.debug("REST request to get RpmVision : {}", id);
        Optional<RpmVisionDTO> rpmVisionDTO = rpmVisionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rpmVisionDTO);
    }

    /**
     * {@code DELETE  /rpm-visions/:id} : delete the "id" rpmVision.
     *
     * @param id the id of the rpmVisionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rpm-visions/{id}")
    public ResponseEntity<Void> deleteRpmVision(@PathVariable Long id) {
        log.debug("REST request to delete RpmVision : {}", id);
        rpmVisionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
