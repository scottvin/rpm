package com.github.scottvin.rpm.web.rest;

import com.github.scottvin.rpm.repository.RpmPlanRepository;
import com.github.scottvin.rpm.service.RpmPlanService;
import com.github.scottvin.rpm.service.dto.RpmPlanDTO;
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
 * REST controller for managing {@link com.github.scottvin.rpm.domain.RpmPlan}.
 */
@RestController
@RequestMapping("/api")
public class RpmPlanResource {

    private final Logger log = LoggerFactory.getLogger(RpmPlanResource.class);

    private static final String ENTITY_NAME = "rpmPlan";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RpmPlanService rpmPlanService;

    private final RpmPlanRepository rpmPlanRepository;

    public RpmPlanResource(RpmPlanService rpmPlanService, RpmPlanRepository rpmPlanRepository) {
        this.rpmPlanService = rpmPlanService;
        this.rpmPlanRepository = rpmPlanRepository;
    }

    /**
     * {@code POST  /rpm-plans} : Create a new rpmPlan.
     *
     * @param rpmPlanDTO the rpmPlanDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rpmPlanDTO, or with status {@code 400 (Bad Request)} if the rpmPlan has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rpm-plans")
    public ResponseEntity<RpmPlanDTO> createRpmPlan(@Valid @RequestBody RpmPlanDTO rpmPlanDTO) throws URISyntaxException {
        log.debug("REST request to save RpmPlan : {}", rpmPlanDTO);
        if (rpmPlanDTO.getId() != null) {
            throw new BadRequestAlertException("A new rpmPlan cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RpmPlanDTO result = rpmPlanService.save(rpmPlanDTO);
        return ResponseEntity
            .created(new URI("/api/rpm-plans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rpm-plans/:id} : Updates an existing rpmPlan.
     *
     * @param id the id of the rpmPlanDTO to save.
     * @param rpmPlanDTO the rpmPlanDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rpmPlanDTO,
     * or with status {@code 400 (Bad Request)} if the rpmPlanDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rpmPlanDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rpm-plans/{id}")
    public ResponseEntity<RpmPlanDTO> updateRpmPlan(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RpmPlanDTO rpmPlanDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RpmPlan : {}, {}", id, rpmPlanDTO);
        if (rpmPlanDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rpmPlanDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rpmPlanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RpmPlanDTO result = rpmPlanService.save(rpmPlanDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rpmPlanDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rpm-plans/:id} : Partial updates given fields of an existing rpmPlan, field will ignore if it is null
     *
     * @param id the id of the rpmPlanDTO to save.
     * @param rpmPlanDTO the rpmPlanDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rpmPlanDTO,
     * or with status {@code 400 (Bad Request)} if the rpmPlanDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rpmPlanDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rpmPlanDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rpm-plans/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RpmPlanDTO> partialUpdateRpmPlan(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RpmPlanDTO rpmPlanDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RpmPlan partially : {}, {}", id, rpmPlanDTO);
        if (rpmPlanDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rpmPlanDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rpmPlanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RpmPlanDTO> result = rpmPlanService.partialUpdate(rpmPlanDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rpmPlanDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rpm-plans} : get all the rpmPlans.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rpmPlans in body.
     */
    @GetMapping("/rpm-plans")
    public ResponseEntity<List<RpmPlanDTO>> getAllRpmPlans(Pageable pageable) {
        log.debug("REST request to get a page of RpmPlans");
        Page<RpmPlanDTO> page = rpmPlanService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rpm-plans/:id} : get the "id" rpmPlan.
     *
     * @param id the id of the rpmPlanDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rpmPlanDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rpm-plans/{id}")
    public ResponseEntity<RpmPlanDTO> getRpmPlan(@PathVariable Long id) {
        log.debug("REST request to get RpmPlan : {}", id);
        Optional<RpmPlanDTO> rpmPlanDTO = rpmPlanService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rpmPlanDTO);
    }

    /**
     * {@code DELETE  /rpm-plans/:id} : delete the "id" rpmPlan.
     *
     * @param id the id of the rpmPlanDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rpm-plans/{id}")
    public ResponseEntity<Void> deleteRpmPlan(@PathVariable Long id) {
        log.debug("REST request to delete RpmPlan : {}", id);
        rpmPlanService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
