package com.github.scottvin.rpm.web.rest;

import com.github.scottvin.rpm.repository.RpmCategoryRepository;
import com.github.scottvin.rpm.service.RpmCategoryService;
import com.github.scottvin.rpm.service.dto.RpmCategoryDTO;
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
 * REST controller for managing {@link com.github.scottvin.rpm.domain.RpmCategory}.
 */
@RestController
@RequestMapping("/api")
public class RpmCategoryResource {

    private final Logger log = LoggerFactory.getLogger(RpmCategoryResource.class);

    private static final String ENTITY_NAME = "rpmCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RpmCategoryService rpmCategoryService;

    private final RpmCategoryRepository rpmCategoryRepository;

    public RpmCategoryResource(RpmCategoryService rpmCategoryService, RpmCategoryRepository rpmCategoryRepository) {
        this.rpmCategoryService = rpmCategoryService;
        this.rpmCategoryRepository = rpmCategoryRepository;
    }

    /**
     * {@code POST  /rpm-categories} : Create a new rpmCategory.
     *
     * @param rpmCategoryDTO the rpmCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rpmCategoryDTO, or with status {@code 400 (Bad Request)} if the rpmCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rpm-categories")
    public ResponseEntity<RpmCategoryDTO> createRpmCategory(@Valid @RequestBody RpmCategoryDTO rpmCategoryDTO) throws URISyntaxException {
        log.debug("REST request to save RpmCategory : {}", rpmCategoryDTO);
        if (rpmCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new rpmCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RpmCategoryDTO result = rpmCategoryService.save(rpmCategoryDTO);
        return ResponseEntity
            .created(new URI("/api/rpm-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rpm-categories/:id} : Updates an existing rpmCategory.
     *
     * @param id the id of the rpmCategoryDTO to save.
     * @param rpmCategoryDTO the rpmCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rpmCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the rpmCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rpmCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rpm-categories/{id}")
    public ResponseEntity<RpmCategoryDTO> updateRpmCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RpmCategoryDTO rpmCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RpmCategory : {}, {}", id, rpmCategoryDTO);
        if (rpmCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rpmCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rpmCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RpmCategoryDTO result = rpmCategoryService.save(rpmCategoryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rpmCategoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rpm-categories/:id} : Partial updates given fields of an existing rpmCategory, field will ignore if it is null
     *
     * @param id the id of the rpmCategoryDTO to save.
     * @param rpmCategoryDTO the rpmCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rpmCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the rpmCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rpmCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rpmCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rpm-categories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RpmCategoryDTO> partialUpdateRpmCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RpmCategoryDTO rpmCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RpmCategory partially : {}, {}", id, rpmCategoryDTO);
        if (rpmCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rpmCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rpmCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RpmCategoryDTO> result = rpmCategoryService.partialUpdate(rpmCategoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rpmCategoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rpm-categories} : get all the rpmCategories.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rpmCategories in body.
     */
    @GetMapping("/rpm-categories")
    public ResponseEntity<List<RpmCategoryDTO>> getAllRpmCategories(Pageable pageable) {
        log.debug("REST request to get a page of RpmCategories");
        Page<RpmCategoryDTO> page = rpmCategoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rpm-categories/:id} : get the "id" rpmCategory.
     *
     * @param id the id of the rpmCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rpmCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rpm-categories/{id}")
    public ResponseEntity<RpmCategoryDTO> getRpmCategory(@PathVariable Long id) {
        log.debug("REST request to get RpmCategory : {}", id);
        Optional<RpmCategoryDTO> rpmCategoryDTO = rpmCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rpmCategoryDTO);
    }

    /**
     * {@code DELETE  /rpm-categories/:id} : delete the "id" rpmCategory.
     *
     * @param id the id of the rpmCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rpm-categories/{id}")
    public ResponseEntity<Void> deleteRpmCategory(@PathVariable Long id) {
        log.debug("REST request to delete RpmCategory : {}", id);
        rpmCategoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
