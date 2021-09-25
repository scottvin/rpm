package com.github.scottvin.rpm.web.rest;

import com.github.scottvin.rpm.repository.RpmCharacterGroupRepository;
import com.github.scottvin.rpm.service.RpmCharacterGroupService;
import com.github.scottvin.rpm.service.dto.RpmCharacterGroupDTO;
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
 * REST controller for managing {@link com.github.scottvin.rpm.domain.RpmCharacterGroup}.
 */
@RestController
@RequestMapping("/api")
public class RpmCharacterGroupResource {

    private final Logger log = LoggerFactory.getLogger(RpmCharacterGroupResource.class);

    private static final String ENTITY_NAME = "rpmCharacterGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RpmCharacterGroupService rpmCharacterGroupService;

    private final RpmCharacterGroupRepository rpmCharacterGroupRepository;

    public RpmCharacterGroupResource(
        RpmCharacterGroupService rpmCharacterGroupService,
        RpmCharacterGroupRepository rpmCharacterGroupRepository
    ) {
        this.rpmCharacterGroupService = rpmCharacterGroupService;
        this.rpmCharacterGroupRepository = rpmCharacterGroupRepository;
    }

    /**
     * {@code POST  /rpm-character-groups} : Create a new rpmCharacterGroup.
     *
     * @param rpmCharacterGroupDTO the rpmCharacterGroupDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rpmCharacterGroupDTO, or with status {@code 400 (Bad Request)} if the rpmCharacterGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rpm-character-groups")
    public ResponseEntity<RpmCharacterGroupDTO> createRpmCharacterGroup(@Valid @RequestBody RpmCharacterGroupDTO rpmCharacterGroupDTO)
        throws URISyntaxException {
        log.debug("REST request to save RpmCharacterGroup : {}", rpmCharacterGroupDTO);
        if (rpmCharacterGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new rpmCharacterGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RpmCharacterGroupDTO result = rpmCharacterGroupService.save(rpmCharacterGroupDTO);
        return ResponseEntity
            .created(new URI("/api/rpm-character-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rpm-character-groups/:id} : Updates an existing rpmCharacterGroup.
     *
     * @param id the id of the rpmCharacterGroupDTO to save.
     * @param rpmCharacterGroupDTO the rpmCharacterGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rpmCharacterGroupDTO,
     * or with status {@code 400 (Bad Request)} if the rpmCharacterGroupDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rpmCharacterGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rpm-character-groups/{id}")
    public ResponseEntity<RpmCharacterGroupDTO> updateRpmCharacterGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RpmCharacterGroupDTO rpmCharacterGroupDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RpmCharacterGroup : {}, {}", id, rpmCharacterGroupDTO);
        if (rpmCharacterGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rpmCharacterGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rpmCharacterGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RpmCharacterGroupDTO result = rpmCharacterGroupService.save(rpmCharacterGroupDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rpmCharacterGroupDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rpm-character-groups/:id} : Partial updates given fields of an existing rpmCharacterGroup, field will ignore if it is null
     *
     * @param id the id of the rpmCharacterGroupDTO to save.
     * @param rpmCharacterGroupDTO the rpmCharacterGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rpmCharacterGroupDTO,
     * or with status {@code 400 (Bad Request)} if the rpmCharacterGroupDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rpmCharacterGroupDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rpmCharacterGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rpm-character-groups/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RpmCharacterGroupDTO> partialUpdateRpmCharacterGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RpmCharacterGroupDTO rpmCharacterGroupDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RpmCharacterGroup partially : {}, {}", id, rpmCharacterGroupDTO);
        if (rpmCharacterGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rpmCharacterGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rpmCharacterGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RpmCharacterGroupDTO> result = rpmCharacterGroupService.partialUpdate(rpmCharacterGroupDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rpmCharacterGroupDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rpm-character-groups} : get all the rpmCharacterGroups.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rpmCharacterGroups in body.
     */
    @GetMapping("/rpm-character-groups")
    public ResponseEntity<List<RpmCharacterGroupDTO>> getAllRpmCharacterGroups(Pageable pageable) {
        log.debug("REST request to get a page of RpmCharacterGroups");
        Page<RpmCharacterGroupDTO> page = rpmCharacterGroupService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rpm-character-groups/:id} : get the "id" rpmCharacterGroup.
     *
     * @param id the id of the rpmCharacterGroupDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rpmCharacterGroupDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rpm-character-groups/{id}")
    public ResponseEntity<RpmCharacterGroupDTO> getRpmCharacterGroup(@PathVariable Long id) {
        log.debug("REST request to get RpmCharacterGroup : {}", id);
        Optional<RpmCharacterGroupDTO> rpmCharacterGroupDTO = rpmCharacterGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rpmCharacterGroupDTO);
    }

    /**
     * {@code DELETE  /rpm-character-groups/:id} : delete the "id" rpmCharacterGroup.
     *
     * @param id the id of the rpmCharacterGroupDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rpm-character-groups/{id}")
    public ResponseEntity<Void> deleteRpmCharacterGroup(@PathVariable Long id) {
        log.debug("REST request to delete RpmCharacterGroup : {}", id);
        rpmCharacterGroupService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
