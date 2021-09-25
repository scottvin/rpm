package com.github.scottvin.rpm.web.rest;

import com.github.scottvin.rpm.repository.RpmCharacterRepository;
import com.github.scottvin.rpm.service.RpmCharacterQueryService;
import com.github.scottvin.rpm.service.RpmCharacterService;
import com.github.scottvin.rpm.service.criteria.RpmCharacterCriteria;
import com.github.scottvin.rpm.service.dto.RpmCharacterDTO;
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
 * REST controller for managing {@link com.github.scottvin.rpm.domain.RpmCharacter}.
 */
@RestController
@RequestMapping("/api")
public class RpmCharacterResource {

    private final Logger log = LoggerFactory.getLogger(RpmCharacterResource.class);

    private static final String ENTITY_NAME = "rpmCharacter";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RpmCharacterService rpmCharacterService;

    private final RpmCharacterRepository rpmCharacterRepository;

    private final RpmCharacterQueryService rpmCharacterQueryService;

    public RpmCharacterResource(
        RpmCharacterService rpmCharacterService,
        RpmCharacterRepository rpmCharacterRepository,
        RpmCharacterQueryService rpmCharacterQueryService
    ) {
        this.rpmCharacterService = rpmCharacterService;
        this.rpmCharacterRepository = rpmCharacterRepository;
        this.rpmCharacterQueryService = rpmCharacterQueryService;
    }

    /**
     * {@code POST  /rpm-characters} : Create a new rpmCharacter.
     *
     * @param rpmCharacterDTO the rpmCharacterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rpmCharacterDTO, or with status {@code 400 (Bad Request)} if the rpmCharacter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rpm-characters")
    public ResponseEntity<RpmCharacterDTO> createRpmCharacter(@Valid @RequestBody RpmCharacterDTO rpmCharacterDTO)
        throws URISyntaxException {
        log.debug("REST request to save RpmCharacter : {}", rpmCharacterDTO);
        if (rpmCharacterDTO.getId() != null) {
            throw new BadRequestAlertException("A new rpmCharacter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RpmCharacterDTO result = rpmCharacterService.save(rpmCharacterDTO);
        return ResponseEntity
            .created(new URI("/api/rpm-characters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rpm-characters/:id} : Updates an existing rpmCharacter.
     *
     * @param id the id of the rpmCharacterDTO to save.
     * @param rpmCharacterDTO the rpmCharacterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rpmCharacterDTO,
     * or with status {@code 400 (Bad Request)} if the rpmCharacterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rpmCharacterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rpm-characters/{id}")
    public ResponseEntity<RpmCharacterDTO> updateRpmCharacter(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RpmCharacterDTO rpmCharacterDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RpmCharacter : {}, {}", id, rpmCharacterDTO);
        if (rpmCharacterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rpmCharacterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rpmCharacterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RpmCharacterDTO result = rpmCharacterService.save(rpmCharacterDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rpmCharacterDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rpm-characters/:id} : Partial updates given fields of an existing rpmCharacter, field will ignore if it is null
     *
     * @param id the id of the rpmCharacterDTO to save.
     * @param rpmCharacterDTO the rpmCharacterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rpmCharacterDTO,
     * or with status {@code 400 (Bad Request)} if the rpmCharacterDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rpmCharacterDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rpmCharacterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rpm-characters/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RpmCharacterDTO> partialUpdateRpmCharacter(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RpmCharacterDTO rpmCharacterDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RpmCharacter partially : {}, {}", id, rpmCharacterDTO);
        if (rpmCharacterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rpmCharacterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rpmCharacterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RpmCharacterDTO> result = rpmCharacterService.partialUpdate(rpmCharacterDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rpmCharacterDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rpm-characters} : get all the rpmCharacters.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rpmCharacters in body.
     */
    @GetMapping("/rpm-characters")
    public ResponseEntity<List<RpmCharacterDTO>> getAllRpmCharacters(RpmCharacterCriteria criteria, Pageable pageable) {
        log.debug("REST request to get RpmCharacters by criteria: {}", criteria);
        Page<RpmCharacterDTO> page = rpmCharacterQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rpm-characters/count} : count all the rpmCharacters.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/rpm-characters/count")
    public ResponseEntity<Long> countRpmCharacters(RpmCharacterCriteria criteria) {
        log.debug("REST request to count RpmCharacters by criteria: {}", criteria);
        return ResponseEntity.ok().body(rpmCharacterQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /rpm-characters/:id} : get the "id" rpmCharacter.
     *
     * @param id the id of the rpmCharacterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rpmCharacterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rpm-characters/{id}")
    public ResponseEntity<RpmCharacterDTO> getRpmCharacter(@PathVariable Long id) {
        log.debug("REST request to get RpmCharacter : {}", id);
        Optional<RpmCharacterDTO> rpmCharacterDTO = rpmCharacterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rpmCharacterDTO);
    }

    /**
     * {@code DELETE  /rpm-characters/:id} : delete the "id" rpmCharacter.
     *
     * @param id the id of the rpmCharacterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rpm-characters/{id}")
    public ResponseEntity<Void> deleteRpmCharacter(@PathVariable Long id) {
        log.debug("REST request to delete RpmCharacter : {}", id);
        rpmCharacterService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
