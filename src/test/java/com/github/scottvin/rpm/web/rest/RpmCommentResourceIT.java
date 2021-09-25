package com.github.scottvin.rpm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.github.scottvin.rpm.IntegrationTest;
import com.github.scottvin.rpm.domain.RpmAction;
import com.github.scottvin.rpm.domain.RpmCharacter;
import com.github.scottvin.rpm.domain.RpmComment;
import com.github.scottvin.rpm.domain.RpmProject;
import com.github.scottvin.rpm.domain.RpmResult;
import com.github.scottvin.rpm.repository.RpmCommentRepository;
import com.github.scottvin.rpm.service.dto.RpmCommentDTO;
import com.github.scottvin.rpm.service.mapper.RpmCommentMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RpmCommentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RpmCommentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/rpm-comments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RpmCommentRepository rpmCommentRepository;

    @Autowired
    private RpmCommentMapper rpmCommentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRpmCommentMockMvc;

    private RpmComment rpmComment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RpmComment createEntity(EntityManager em) {
        RpmComment rpmComment = new RpmComment().name(DEFAULT_NAME);
        // Add required entity
        RpmResult rpmResult;
        if (TestUtil.findAll(em, RpmResult.class).isEmpty()) {
            rpmResult = RpmResultResourceIT.createEntity(em);
            em.persist(rpmResult);
            em.flush();
        } else {
            rpmResult = TestUtil.findAll(em, RpmResult.class).get(0);
        }
        rpmComment.setResult(rpmResult);
        // Add required entity
        RpmProject rpmProject;
        if (TestUtil.findAll(em, RpmProject.class).isEmpty()) {
            rpmProject = RpmProjectResourceIT.createEntity(em);
            em.persist(rpmProject);
            em.flush();
        } else {
            rpmProject = TestUtil.findAll(em, RpmProject.class).get(0);
        }
        rpmComment.setProject(rpmProject);
        // Add required entity
        RpmAction rpmAction;
        if (TestUtil.findAll(em, RpmAction.class).isEmpty()) {
            rpmAction = RpmActionResourceIT.createEntity(em);
            em.persist(rpmAction);
            em.flush();
        } else {
            rpmAction = TestUtil.findAll(em, RpmAction.class).get(0);
        }
        rpmComment.setAction(rpmAction);
        // Add required entity
        RpmCharacter rpmCharacter;
        if (TestUtil.findAll(em, RpmCharacter.class).isEmpty()) {
            rpmCharacter = RpmCharacterResourceIT.createEntity(em);
            em.persist(rpmCharacter);
            em.flush();
        } else {
            rpmCharacter = TestUtil.findAll(em, RpmCharacter.class).get(0);
        }
        rpmComment.setCharacter(rpmCharacter);
        return rpmComment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RpmComment createUpdatedEntity(EntityManager em) {
        RpmComment rpmComment = new RpmComment().name(UPDATED_NAME);
        // Add required entity
        RpmResult rpmResult;
        if (TestUtil.findAll(em, RpmResult.class).isEmpty()) {
            rpmResult = RpmResultResourceIT.createUpdatedEntity(em);
            em.persist(rpmResult);
            em.flush();
        } else {
            rpmResult = TestUtil.findAll(em, RpmResult.class).get(0);
        }
        rpmComment.setResult(rpmResult);
        // Add required entity
        RpmProject rpmProject;
        if (TestUtil.findAll(em, RpmProject.class).isEmpty()) {
            rpmProject = RpmProjectResourceIT.createUpdatedEntity(em);
            em.persist(rpmProject);
            em.flush();
        } else {
            rpmProject = TestUtil.findAll(em, RpmProject.class).get(0);
        }
        rpmComment.setProject(rpmProject);
        // Add required entity
        RpmAction rpmAction;
        if (TestUtil.findAll(em, RpmAction.class).isEmpty()) {
            rpmAction = RpmActionResourceIT.createUpdatedEntity(em);
            em.persist(rpmAction);
            em.flush();
        } else {
            rpmAction = TestUtil.findAll(em, RpmAction.class).get(0);
        }
        rpmComment.setAction(rpmAction);
        // Add required entity
        RpmCharacter rpmCharacter;
        if (TestUtil.findAll(em, RpmCharacter.class).isEmpty()) {
            rpmCharacter = RpmCharacterResourceIT.createUpdatedEntity(em);
            em.persist(rpmCharacter);
            em.flush();
        } else {
            rpmCharacter = TestUtil.findAll(em, RpmCharacter.class).get(0);
        }
        rpmComment.setCharacter(rpmCharacter);
        return rpmComment;
    }

    @BeforeEach
    public void initTest() {
        rpmComment = createEntity(em);
    }

    @Test
    @Transactional
    void createRpmComment() throws Exception {
        int databaseSizeBeforeCreate = rpmCommentRepository.findAll().size();
        // Create the RpmComment
        RpmCommentDTO rpmCommentDTO = rpmCommentMapper.toDto(rpmComment);
        restRpmCommentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmCommentDTO)))
            .andExpect(status().isCreated());

        // Validate the RpmComment in the database
        List<RpmComment> rpmCommentList = rpmCommentRepository.findAll();
        assertThat(rpmCommentList).hasSize(databaseSizeBeforeCreate + 1);
        RpmComment testRpmComment = rpmCommentList.get(rpmCommentList.size() - 1);
        assertThat(testRpmComment.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createRpmCommentWithExistingId() throws Exception {
        // Create the RpmComment with an existing ID
        rpmComment.setId(1L);
        RpmCommentDTO rpmCommentDTO = rpmCommentMapper.toDto(rpmComment);

        int databaseSizeBeforeCreate = rpmCommentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRpmCommentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmCommentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RpmComment in the database
        List<RpmComment> rpmCommentList = rpmCommentRepository.findAll();
        assertThat(rpmCommentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = rpmCommentRepository.findAll().size();
        // set the field null
        rpmComment.setName(null);

        // Create the RpmComment, which fails.
        RpmCommentDTO rpmCommentDTO = rpmCommentMapper.toDto(rpmComment);

        restRpmCommentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmCommentDTO)))
            .andExpect(status().isBadRequest());

        List<RpmComment> rpmCommentList = rpmCommentRepository.findAll();
        assertThat(rpmCommentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRpmComments() throws Exception {
        // Initialize the database
        rpmCommentRepository.saveAndFlush(rpmComment);

        // Get all the rpmCommentList
        restRpmCommentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rpmComment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getRpmComment() throws Exception {
        // Initialize the database
        rpmCommentRepository.saveAndFlush(rpmComment);

        // Get the rpmComment
        restRpmCommentMockMvc
            .perform(get(ENTITY_API_URL_ID, rpmComment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rpmComment.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingRpmComment() throws Exception {
        // Get the rpmComment
        restRpmCommentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRpmComment() throws Exception {
        // Initialize the database
        rpmCommentRepository.saveAndFlush(rpmComment);

        int databaseSizeBeforeUpdate = rpmCommentRepository.findAll().size();

        // Update the rpmComment
        RpmComment updatedRpmComment = rpmCommentRepository.findById(rpmComment.getId()).get();
        // Disconnect from session so that the updates on updatedRpmComment are not directly saved in db
        em.detach(updatedRpmComment);
        updatedRpmComment.name(UPDATED_NAME);
        RpmCommentDTO rpmCommentDTO = rpmCommentMapper.toDto(updatedRpmComment);

        restRpmCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rpmCommentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmCommentDTO))
            )
            .andExpect(status().isOk());

        // Validate the RpmComment in the database
        List<RpmComment> rpmCommentList = rpmCommentRepository.findAll();
        assertThat(rpmCommentList).hasSize(databaseSizeBeforeUpdate);
        RpmComment testRpmComment = rpmCommentList.get(rpmCommentList.size() - 1);
        assertThat(testRpmComment.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingRpmComment() throws Exception {
        int databaseSizeBeforeUpdate = rpmCommentRepository.findAll().size();
        rpmComment.setId(count.incrementAndGet());

        // Create the RpmComment
        RpmCommentDTO rpmCommentDTO = rpmCommentMapper.toDto(rpmComment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRpmCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rpmCommentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmComment in the database
        List<RpmComment> rpmCommentList = rpmCommentRepository.findAll();
        assertThat(rpmCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRpmComment() throws Exception {
        int databaseSizeBeforeUpdate = rpmCommentRepository.findAll().size();
        rpmComment.setId(count.incrementAndGet());

        // Create the RpmComment
        RpmCommentDTO rpmCommentDTO = rpmCommentMapper.toDto(rpmComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmComment in the database
        List<RpmComment> rpmCommentList = rpmCommentRepository.findAll();
        assertThat(rpmCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRpmComment() throws Exception {
        int databaseSizeBeforeUpdate = rpmCommentRepository.findAll().size();
        rpmComment.setId(count.incrementAndGet());

        // Create the RpmComment
        RpmCommentDTO rpmCommentDTO = rpmCommentMapper.toDto(rpmComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmCommentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmCommentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RpmComment in the database
        List<RpmComment> rpmCommentList = rpmCommentRepository.findAll();
        assertThat(rpmCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRpmCommentWithPatch() throws Exception {
        // Initialize the database
        rpmCommentRepository.saveAndFlush(rpmComment);

        int databaseSizeBeforeUpdate = rpmCommentRepository.findAll().size();

        // Update the rpmComment using partial update
        RpmComment partialUpdatedRpmComment = new RpmComment();
        partialUpdatedRpmComment.setId(rpmComment.getId());

        partialUpdatedRpmComment.name(UPDATED_NAME);

        restRpmCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRpmComment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRpmComment))
            )
            .andExpect(status().isOk());

        // Validate the RpmComment in the database
        List<RpmComment> rpmCommentList = rpmCommentRepository.findAll();
        assertThat(rpmCommentList).hasSize(databaseSizeBeforeUpdate);
        RpmComment testRpmComment = rpmCommentList.get(rpmCommentList.size() - 1);
        assertThat(testRpmComment.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateRpmCommentWithPatch() throws Exception {
        // Initialize the database
        rpmCommentRepository.saveAndFlush(rpmComment);

        int databaseSizeBeforeUpdate = rpmCommentRepository.findAll().size();

        // Update the rpmComment using partial update
        RpmComment partialUpdatedRpmComment = new RpmComment();
        partialUpdatedRpmComment.setId(rpmComment.getId());

        partialUpdatedRpmComment.name(UPDATED_NAME);

        restRpmCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRpmComment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRpmComment))
            )
            .andExpect(status().isOk());

        // Validate the RpmComment in the database
        List<RpmComment> rpmCommentList = rpmCommentRepository.findAll();
        assertThat(rpmCommentList).hasSize(databaseSizeBeforeUpdate);
        RpmComment testRpmComment = rpmCommentList.get(rpmCommentList.size() - 1);
        assertThat(testRpmComment.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingRpmComment() throws Exception {
        int databaseSizeBeforeUpdate = rpmCommentRepository.findAll().size();
        rpmComment.setId(count.incrementAndGet());

        // Create the RpmComment
        RpmCommentDTO rpmCommentDTO = rpmCommentMapper.toDto(rpmComment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRpmCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rpmCommentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rpmCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmComment in the database
        List<RpmComment> rpmCommentList = rpmCommentRepository.findAll();
        assertThat(rpmCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRpmComment() throws Exception {
        int databaseSizeBeforeUpdate = rpmCommentRepository.findAll().size();
        rpmComment.setId(count.incrementAndGet());

        // Create the RpmComment
        RpmCommentDTO rpmCommentDTO = rpmCommentMapper.toDto(rpmComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rpmCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmComment in the database
        List<RpmComment> rpmCommentList = rpmCommentRepository.findAll();
        assertThat(rpmCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRpmComment() throws Exception {
        int databaseSizeBeforeUpdate = rpmCommentRepository.findAll().size();
        rpmComment.setId(count.incrementAndGet());

        // Create the RpmComment
        RpmCommentDTO rpmCommentDTO = rpmCommentMapper.toDto(rpmComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmCommentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rpmCommentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RpmComment in the database
        List<RpmComment> rpmCommentList = rpmCommentRepository.findAll();
        assertThat(rpmCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRpmComment() throws Exception {
        // Initialize the database
        rpmCommentRepository.saveAndFlush(rpmComment);

        int databaseSizeBeforeDelete = rpmCommentRepository.findAll().size();

        // Delete the rpmComment
        restRpmCommentMockMvc
            .perform(delete(ENTITY_API_URL_ID, rpmComment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RpmComment> rpmCommentList = rpmCommentRepository.findAll();
        assertThat(rpmCommentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
