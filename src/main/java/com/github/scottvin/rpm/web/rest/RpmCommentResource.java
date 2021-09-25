package com.github.scottvin.rpm.web.rest;

import com.github.scottvin.rpm.repository.RpmCommentRepository;
import com.github.scottvin.rpm.service.RpmCommentService;
import com.github.scottvin.rpm.service.dto.RpmCommentDTO;
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
 * REST controller for managing {@link com.github.scottvin.rpm.domain.RpmComment}.
 */
@RestController
@RequestMapping("/api")
public class RpmCommentResource {

    private final Logger log = LoggerFactory.getLogger(RpmCommentResource.class);

    private static final String ENTITY_NAME = "rpmComment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RpmCommentService rpmCommentService;

    private final RpmCommentRepository rpmCommentRepository;

    public RpmCommentResource(RpmCommentService rpmCommentService, RpmCommentRepository rpmCommentRepository) {
        this.rpmCommentService = rpmCommentService;
        this.rpmCommentRepository = rpmCommentRepository;
    }

    /**
     * {@code POST  /rpm-comments} : Create a new rpmComment.
     *
     * @param rpmCommentDTO the rpmCommentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rpmCommentDTO, or with status {@code 400 (Bad Request)} if the rpmComment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rpm-comments")
    public ResponseEntity<RpmCommentDTO> createRpmComment(@Valid @RequestBody RpmCommentDTO rpmCommentDTO) throws URISyntaxException {
        log.debug("REST request to save RpmComment : {}", rpmCommentDTO);
        if (rpmCommentDTO.getId() != null) {
            throw new BadRequestAlertException("A new rpmComment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RpmCommentDTO result = rpmCommentService.save(rpmCommentDTO);
        return ResponseEntity
            .created(new URI("/api/rpm-comments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rpm-comments/:id} : Updates an existing rpmComment.
     *
     * @param id the id of the rpmCommentDTO to save.
     * @param rpmCommentDTO the rpmCommentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rpmCommentDTO,
     * or with status {@code 400 (Bad Request)} if the rpmCommentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rpmCommentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rpm-comments/{id}")
    public ResponseEntity<RpmCommentDTO> updateRpmComment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RpmCommentDTO rpmCommentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RpmComment : {}, {}", id, rpmCommentDTO);
        if (rpmCommentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rpmCommentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rpmCommentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RpmCommentDTO result = rpmCommentService.save(rpmCommentDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rpmCommentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rpm-comments/:id} : Partial updates given fields of an existing rpmComment, field will ignore if it is null
     *
     * @param id the id of the rpmCommentDTO to save.
     * @param rpmCommentDTO the rpmCommentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rpmCommentDTO,
     * or with status {@code 400 (Bad Request)} if the rpmCommentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rpmCommentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rpmCommentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rpm-comments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RpmCommentDTO> partialUpdateRpmComment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RpmCommentDTO rpmCommentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RpmComment partially : {}, {}", id, rpmCommentDTO);
        if (rpmCommentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rpmCommentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rpmCommentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RpmCommentDTO> result = rpmCommentService.partialUpdate(rpmCommentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rpmCommentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rpm-comments} : get all the rpmComments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rpmComments in body.
     */
    @GetMapping("/rpm-comments")
    public ResponseEntity<List<RpmCommentDTO>> getAllRpmComments(Pageable pageable) {
        log.debug("REST request to get a page of RpmComments");
        Page<RpmCommentDTO> page = rpmCommentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rpm-comments/:id} : get the "id" rpmComment.
     *
     * @param id the id of the rpmCommentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rpmCommentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rpm-comments/{id}")
    public ResponseEntity<RpmCommentDTO> getRpmComment(@PathVariable Long id) {
        log.debug("REST request to get RpmComment : {}", id);
        Optional<RpmCommentDTO> rpmCommentDTO = rpmCommentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rpmCommentDTO);
    }

    /**
     * {@code DELETE  /rpm-comments/:id} : delete the "id" rpmComment.
     *
     * @param id the id of the rpmCommentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rpm-comments/{id}")
    public ResponseEntity<Void> deleteRpmComment(@PathVariable Long id) {
        log.debug("REST request to delete RpmComment : {}", id);
        rpmCommentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
