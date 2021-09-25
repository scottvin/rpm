package com.github.scottvin.rpm.web.rest;

import com.github.scottvin.rpm.repository.RpmProjectRepository;
import com.github.scottvin.rpm.service.RpmProjectQueryService;
import com.github.scottvin.rpm.service.RpmProjectService;
import com.github.scottvin.rpm.service.criteria.RpmProjectCriteria;
import com.github.scottvin.rpm.service.dto.RpmProjectDTO;
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
 * REST controller for managing {@link com.github.scottvin.rpm.domain.RpmProject}.
 */
@RestController
@RequestMapping("/api")
public class RpmProjectResource {

    private final Logger log = LoggerFactory.getLogger(RpmProjectResource.class);

    private static final String ENTITY_NAME = "rpmProject";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RpmProjectService rpmProjectService;

    private final RpmProjectRepository rpmProjectRepository;

    private final RpmProjectQueryService rpmProjectQueryService;

    public RpmProjectResource(
        RpmProjectService rpmProjectService,
        RpmProjectRepository rpmProjectRepository,
        RpmProjectQueryService rpmProjectQueryService
    ) {
        this.rpmProjectService = rpmProjectService;
        this.rpmProjectRepository = rpmProjectRepository;
        this.rpmProjectQueryService = rpmProjectQueryService;
    }

    /**
     * {@code POST  /rpm-projects} : Create a new rpmProject.
     *
     * @param rpmProjectDTO the rpmProjectDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rpmProjectDTO, or with status {@code 400 (Bad Request)} if the rpmProject has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rpm-projects")
    public ResponseEntity<RpmProjectDTO> createRpmProject(@Valid @RequestBody RpmProjectDTO rpmProjectDTO) throws URISyntaxException {
        log.debug("REST request to save RpmProject : {}", rpmProjectDTO);
        if (rpmProjectDTO.getId() != null) {
            throw new BadRequestAlertException("A new rpmProject cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RpmProjectDTO result = rpmProjectService.save(rpmProjectDTO);
        return ResponseEntity
            .created(new URI("/api/rpm-projects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rpm-projects/:id} : Updates an existing rpmProject.
     *
     * @param id the id of the rpmProjectDTO to save.
     * @param rpmProjectDTO the rpmProjectDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rpmProjectDTO,
     * or with status {@code 400 (Bad Request)} if the rpmProjectDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rpmProjectDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rpm-projects/{id}")
    public ResponseEntity<RpmProjectDTO> updateRpmProject(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RpmProjectDTO rpmProjectDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RpmProject : {}, {}", id, rpmProjectDTO);
        if (rpmProjectDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rpmProjectDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rpmProjectRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RpmProjectDTO result = rpmProjectService.save(rpmProjectDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rpmProjectDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rpm-projects/:id} : Partial updates given fields of an existing rpmProject, field will ignore if it is null
     *
     * @param id the id of the rpmProjectDTO to save.
     * @param rpmProjectDTO the rpmProjectDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rpmProjectDTO,
     * or with status {@code 400 (Bad Request)} if the rpmProjectDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rpmProjectDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rpmProjectDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rpm-projects/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RpmProjectDTO> partialUpdateRpmProject(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RpmProjectDTO rpmProjectDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RpmProject partially : {}, {}", id, rpmProjectDTO);
        if (rpmProjectDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rpmProjectDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rpmProjectRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RpmProjectDTO> result = rpmProjectService.partialUpdate(rpmProjectDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rpmProjectDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rpm-projects} : get all the rpmProjects.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rpmProjects in body.
     */
    @GetMapping("/rpm-projects")
    public ResponseEntity<List<RpmProjectDTO>> getAllRpmProjects(RpmProjectCriteria criteria, Pageable pageable) {
        log.debug("REST request to get RpmProjects by criteria: {}", criteria);
        Page<RpmProjectDTO> page = rpmProjectQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rpm-projects/count} : count all the rpmProjects.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/rpm-projects/count")
    public ResponseEntity<Long> countRpmProjects(RpmProjectCriteria criteria) {
        log.debug("REST request to count RpmProjects by criteria: {}", criteria);
        return ResponseEntity.ok().body(rpmProjectQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /rpm-projects/:id} : get the "id" rpmProject.
     *
     * @param id the id of the rpmProjectDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rpmProjectDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rpm-projects/{id}")
    public ResponseEntity<RpmProjectDTO> getRpmProject(@PathVariable Long id) {
        log.debug("REST request to get RpmProject : {}", id);
        Optional<RpmProjectDTO> rpmProjectDTO = rpmProjectService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rpmProjectDTO);
    }

    /**
     * {@code DELETE  /rpm-projects/:id} : delete the "id" rpmProject.
     *
     * @param id the id of the rpmProjectDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rpm-projects/{id}")
    public ResponseEntity<Void> deleteRpmProject(@PathVariable Long id) {
        log.debug("REST request to delete RpmProject : {}", id);
        rpmProjectService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
