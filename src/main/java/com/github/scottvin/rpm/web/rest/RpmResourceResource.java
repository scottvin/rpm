package com.github.scottvin.rpm.web.rest;

import com.github.scottvin.rpm.repository.RpmResourceRepository;
import com.github.scottvin.rpm.service.RpmResourceService;
import com.github.scottvin.rpm.service.dto.RpmResourceDTO;
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
 * REST controller for managing {@link com.github.scottvin.rpm.domain.RpmResource}.
 */
@RestController
@RequestMapping("/api")
public class RpmResourceResource {

    private final Logger log = LoggerFactory.getLogger(RpmResourceResource.class);

    private static final String ENTITY_NAME = "rpmResource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RpmResourceService rpmResourceService;

    private final RpmResourceRepository rpmResourceRepository;

    public RpmResourceResource(RpmResourceService rpmResourceService, RpmResourceRepository rpmResourceRepository) {
        this.rpmResourceService = rpmResourceService;
        this.rpmResourceRepository = rpmResourceRepository;
    }

    /**
     * {@code POST  /rpm-resources} : Create a new rpmResource.
     *
     * @param rpmResourceDTO the rpmResourceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rpmResourceDTO, or with status {@code 400 (Bad Request)} if the rpmResource has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rpm-resources")
    public ResponseEntity<RpmResourceDTO> createRpmResource(@Valid @RequestBody RpmResourceDTO rpmResourceDTO) throws URISyntaxException {
        log.debug("REST request to save RpmResource : {}", rpmResourceDTO);
        if (rpmResourceDTO.getId() != null) {
            throw new BadRequestAlertException("A new rpmResource cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RpmResourceDTO result = rpmResourceService.save(rpmResourceDTO);
        return ResponseEntity
            .created(new URI("/api/rpm-resources/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rpm-resources/:id} : Updates an existing rpmResource.
     *
     * @param id the id of the rpmResourceDTO to save.
     * @param rpmResourceDTO the rpmResourceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rpmResourceDTO,
     * or with status {@code 400 (Bad Request)} if the rpmResourceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rpmResourceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rpm-resources/{id}")
    public ResponseEntity<RpmResourceDTO> updateRpmResource(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RpmResourceDTO rpmResourceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RpmResource : {}, {}", id, rpmResourceDTO);
        if (rpmResourceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rpmResourceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rpmResourceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RpmResourceDTO result = rpmResourceService.save(rpmResourceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rpmResourceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rpm-resources/:id} : Partial updates given fields of an existing rpmResource, field will ignore if it is null
     *
     * @param id the id of the rpmResourceDTO to save.
     * @param rpmResourceDTO the rpmResourceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rpmResourceDTO,
     * or with status {@code 400 (Bad Request)} if the rpmResourceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rpmResourceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rpmResourceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rpm-resources/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RpmResourceDTO> partialUpdateRpmResource(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RpmResourceDTO rpmResourceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RpmResource partially : {}, {}", id, rpmResourceDTO);
        if (rpmResourceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rpmResourceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rpmResourceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RpmResourceDTO> result = rpmResourceService.partialUpdate(rpmResourceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rpmResourceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rpm-resources} : get all the rpmResources.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rpmResources in body.
     */
    @GetMapping("/rpm-resources")
    public ResponseEntity<List<RpmResourceDTO>> getAllRpmResources(Pageable pageable) {
        log.debug("REST request to get a page of RpmResources");
        Page<RpmResourceDTO> page = rpmResourceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rpm-resources/:id} : get the "id" rpmResource.
     *
     * @param id the id of the rpmResourceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rpmResourceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rpm-resources/{id}")
    public ResponseEntity<RpmResourceDTO> getRpmResource(@PathVariable Long id) {
        log.debug("REST request to get RpmResource : {}", id);
        Optional<RpmResourceDTO> rpmResourceDTO = rpmResourceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rpmResourceDTO);
    }

    /**
     * {@code DELETE  /rpm-resources/:id} : delete the "id" rpmResource.
     *
     * @param id the id of the rpmResourceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rpm-resources/{id}")
    public ResponseEntity<Void> deleteRpmResource(@PathVariable Long id) {
        log.debug("REST request to delete RpmResource : {}", id);
        rpmResourceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
