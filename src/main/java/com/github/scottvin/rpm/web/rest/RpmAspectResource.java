package com.github.scottvin.rpm.web.rest;

import com.github.scottvin.rpm.repository.RpmAspectRepository;
import com.github.scottvin.rpm.service.RpmAspectService;
import com.github.scottvin.rpm.service.dto.RpmAspectDTO;
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
 * REST controller for managing {@link com.github.scottvin.rpm.domain.RpmAspect}.
 */
@RestController
@RequestMapping("/api")
public class RpmAspectResource {

    private final Logger log = LoggerFactory.getLogger(RpmAspectResource.class);

    private static final String ENTITY_NAME = "rpmAspect";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RpmAspectService rpmAspectService;

    private final RpmAspectRepository rpmAspectRepository;

    public RpmAspectResource(RpmAspectService rpmAspectService, RpmAspectRepository rpmAspectRepository) {
        this.rpmAspectService = rpmAspectService;
        this.rpmAspectRepository = rpmAspectRepository;
    }

    /**
     * {@code POST  /rpm-aspects} : Create a new rpmAspect.
     *
     * @param rpmAspectDTO the rpmAspectDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rpmAspectDTO, or with status {@code 400 (Bad Request)} if the rpmAspect has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rpm-aspects")
    public ResponseEntity<RpmAspectDTO> createRpmAspect(@Valid @RequestBody RpmAspectDTO rpmAspectDTO) throws URISyntaxException {
        log.debug("REST request to save RpmAspect : {}", rpmAspectDTO);
        if (rpmAspectDTO.getId() != null) {
            throw new BadRequestAlertException("A new rpmAspect cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RpmAspectDTO result = rpmAspectService.save(rpmAspectDTO);
        return ResponseEntity
            .created(new URI("/api/rpm-aspects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rpm-aspects/:id} : Updates an existing rpmAspect.
     *
     * @param id the id of the rpmAspectDTO to save.
     * @param rpmAspectDTO the rpmAspectDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rpmAspectDTO,
     * or with status {@code 400 (Bad Request)} if the rpmAspectDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rpmAspectDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rpm-aspects/{id}")
    public ResponseEntity<RpmAspectDTO> updateRpmAspect(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RpmAspectDTO rpmAspectDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RpmAspect : {}, {}", id, rpmAspectDTO);
        if (rpmAspectDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rpmAspectDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rpmAspectRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RpmAspectDTO result = rpmAspectService.save(rpmAspectDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rpmAspectDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rpm-aspects/:id} : Partial updates given fields of an existing rpmAspect, field will ignore if it is null
     *
     * @param id the id of the rpmAspectDTO to save.
     * @param rpmAspectDTO the rpmAspectDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rpmAspectDTO,
     * or with status {@code 400 (Bad Request)} if the rpmAspectDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rpmAspectDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rpmAspectDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rpm-aspects/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RpmAspectDTO> partialUpdateRpmAspect(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RpmAspectDTO rpmAspectDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RpmAspect partially : {}, {}", id, rpmAspectDTO);
        if (rpmAspectDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rpmAspectDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rpmAspectRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RpmAspectDTO> result = rpmAspectService.partialUpdate(rpmAspectDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rpmAspectDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rpm-aspects} : get all the rpmAspects.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rpmAspects in body.
     */
    @GetMapping("/rpm-aspects")
    public ResponseEntity<List<RpmAspectDTO>> getAllRpmAspects(Pageable pageable) {
        log.debug("REST request to get a page of RpmAspects");
        Page<RpmAspectDTO> page = rpmAspectService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rpm-aspects/:id} : get the "id" rpmAspect.
     *
     * @param id the id of the rpmAspectDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rpmAspectDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rpm-aspects/{id}")
    public ResponseEntity<RpmAspectDTO> getRpmAspect(@PathVariable Long id) {
        log.debug("REST request to get RpmAspect : {}", id);
        Optional<RpmAspectDTO> rpmAspectDTO = rpmAspectService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rpmAspectDTO);
    }

    /**
     * {@code DELETE  /rpm-aspects/:id} : delete the "id" rpmAspect.
     *
     * @param id the id of the rpmAspectDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rpm-aspects/{id}")
    public ResponseEntity<Void> deleteRpmAspect(@PathVariable Long id) {
        log.debug("REST request to delete RpmAspect : {}", id);
        rpmAspectService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
