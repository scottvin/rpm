package com.github.scottvin.rpm.web.rest;

import com.github.scottvin.rpm.repository.RpmRoleRepository;
import com.github.scottvin.rpm.service.RpmRoleService;
import com.github.scottvin.rpm.service.dto.RpmRoleDTO;
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
 * REST controller for managing {@link com.github.scottvin.rpm.domain.RpmRole}.
 */
@RestController
@RequestMapping("/api")
public class RpmRoleResource {

    private final Logger log = LoggerFactory.getLogger(RpmRoleResource.class);

    private static final String ENTITY_NAME = "rpmRole";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RpmRoleService rpmRoleService;

    private final RpmRoleRepository rpmRoleRepository;

    public RpmRoleResource(RpmRoleService rpmRoleService, RpmRoleRepository rpmRoleRepository) {
        this.rpmRoleService = rpmRoleService;
        this.rpmRoleRepository = rpmRoleRepository;
    }

    /**
     * {@code POST  /rpm-roles} : Create a new rpmRole.
     *
     * @param rpmRoleDTO the rpmRoleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rpmRoleDTO, or with status {@code 400 (Bad Request)} if the rpmRole has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rpm-roles")
    public ResponseEntity<RpmRoleDTO> createRpmRole(@Valid @RequestBody RpmRoleDTO rpmRoleDTO) throws URISyntaxException {
        log.debug("REST request to save RpmRole : {}", rpmRoleDTO);
        if (rpmRoleDTO.getId() != null) {
            throw new BadRequestAlertException("A new rpmRole cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RpmRoleDTO result = rpmRoleService.save(rpmRoleDTO);
        return ResponseEntity
            .created(new URI("/api/rpm-roles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rpm-roles/:id} : Updates an existing rpmRole.
     *
     * @param id the id of the rpmRoleDTO to save.
     * @param rpmRoleDTO the rpmRoleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rpmRoleDTO,
     * or with status {@code 400 (Bad Request)} if the rpmRoleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rpmRoleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rpm-roles/{id}")
    public ResponseEntity<RpmRoleDTO> updateRpmRole(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RpmRoleDTO rpmRoleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RpmRole : {}, {}", id, rpmRoleDTO);
        if (rpmRoleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rpmRoleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rpmRoleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RpmRoleDTO result = rpmRoleService.save(rpmRoleDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rpmRoleDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rpm-roles/:id} : Partial updates given fields of an existing rpmRole, field will ignore if it is null
     *
     * @param id the id of the rpmRoleDTO to save.
     * @param rpmRoleDTO the rpmRoleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rpmRoleDTO,
     * or with status {@code 400 (Bad Request)} if the rpmRoleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rpmRoleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rpmRoleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rpm-roles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RpmRoleDTO> partialUpdateRpmRole(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RpmRoleDTO rpmRoleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RpmRole partially : {}, {}", id, rpmRoleDTO);
        if (rpmRoleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rpmRoleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rpmRoleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RpmRoleDTO> result = rpmRoleService.partialUpdate(rpmRoleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rpmRoleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rpm-roles} : get all the rpmRoles.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rpmRoles in body.
     */
    @GetMapping("/rpm-roles")
    public ResponseEntity<List<RpmRoleDTO>> getAllRpmRoles(Pageable pageable) {
        log.debug("REST request to get a page of RpmRoles");
        Page<RpmRoleDTO> page = rpmRoleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rpm-roles/:id} : get the "id" rpmRole.
     *
     * @param id the id of the rpmRoleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rpmRoleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rpm-roles/{id}")
    public ResponseEntity<RpmRoleDTO> getRpmRole(@PathVariable Long id) {
        log.debug("REST request to get RpmRole : {}", id);
        Optional<RpmRoleDTO> rpmRoleDTO = rpmRoleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rpmRoleDTO);
    }

    /**
     * {@code DELETE  /rpm-roles/:id} : delete the "id" rpmRole.
     *
     * @param id the id of the rpmRoleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rpm-roles/{id}")
    public ResponseEntity<Void> deleteRpmRole(@PathVariable Long id) {
        log.debug("REST request to delete RpmRole : {}", id);
        rpmRoleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
