package com.github.scottvin.rpm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.github.scottvin.rpm.IntegrationTest;
import com.github.scottvin.rpm.domain.RpmVision;
import com.github.scottvin.rpm.repository.RpmVisionRepository;
import com.github.scottvin.rpm.service.dto.RpmVisionDTO;
import com.github.scottvin.rpm.service.mapper.RpmVisionMapper;
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
 * Integration tests for the {@link RpmVisionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RpmVisionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/rpm-visions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RpmVisionRepository rpmVisionRepository;

    @Autowired
    private RpmVisionMapper rpmVisionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRpmVisionMockMvc;

    private RpmVision rpmVision;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RpmVision createEntity(EntityManager em) {
        RpmVision rpmVision = new RpmVision().name(DEFAULT_NAME);
        return rpmVision;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RpmVision createUpdatedEntity(EntityManager em) {
        RpmVision rpmVision = new RpmVision().name(UPDATED_NAME);
        return rpmVision;
    }

    @BeforeEach
    public void initTest() {
        rpmVision = createEntity(em);
    }

    @Test
    @Transactional
    void createRpmVision() throws Exception {
        int databaseSizeBeforeCreate = rpmVisionRepository.findAll().size();
        // Create the RpmVision
        RpmVisionDTO rpmVisionDTO = rpmVisionMapper.toDto(rpmVision);
        restRpmVisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmVisionDTO)))
            .andExpect(status().isCreated());

        // Validate the RpmVision in the database
        List<RpmVision> rpmVisionList = rpmVisionRepository.findAll();
        assertThat(rpmVisionList).hasSize(databaseSizeBeforeCreate + 1);
        RpmVision testRpmVision = rpmVisionList.get(rpmVisionList.size() - 1);
        assertThat(testRpmVision.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createRpmVisionWithExistingId() throws Exception {
        // Create the RpmVision with an existing ID
        rpmVision.setId(1L);
        RpmVisionDTO rpmVisionDTO = rpmVisionMapper.toDto(rpmVision);

        int databaseSizeBeforeCreate = rpmVisionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRpmVisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmVisionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RpmVision in the database
        List<RpmVision> rpmVisionList = rpmVisionRepository.findAll();
        assertThat(rpmVisionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = rpmVisionRepository.findAll().size();
        // set the field null
        rpmVision.setName(null);

        // Create the RpmVision, which fails.
        RpmVisionDTO rpmVisionDTO = rpmVisionMapper.toDto(rpmVision);

        restRpmVisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmVisionDTO)))
            .andExpect(status().isBadRequest());

        List<RpmVision> rpmVisionList = rpmVisionRepository.findAll();
        assertThat(rpmVisionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRpmVisions() throws Exception {
        // Initialize the database
        rpmVisionRepository.saveAndFlush(rpmVision);

        // Get all the rpmVisionList
        restRpmVisionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rpmVision.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getRpmVision() throws Exception {
        // Initialize the database
        rpmVisionRepository.saveAndFlush(rpmVision);

        // Get the rpmVision
        restRpmVisionMockMvc
            .perform(get(ENTITY_API_URL_ID, rpmVision.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rpmVision.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingRpmVision() throws Exception {
        // Get the rpmVision
        restRpmVisionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRpmVision() throws Exception {
        // Initialize the database
        rpmVisionRepository.saveAndFlush(rpmVision);

        int databaseSizeBeforeUpdate = rpmVisionRepository.findAll().size();

        // Update the rpmVision
        RpmVision updatedRpmVision = rpmVisionRepository.findById(rpmVision.getId()).get();
        // Disconnect from session so that the updates on updatedRpmVision are not directly saved in db
        em.detach(updatedRpmVision);
        updatedRpmVision.name(UPDATED_NAME);
        RpmVisionDTO rpmVisionDTO = rpmVisionMapper.toDto(updatedRpmVision);

        restRpmVisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rpmVisionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmVisionDTO))
            )
            .andExpect(status().isOk());

        // Validate the RpmVision in the database
        List<RpmVision> rpmVisionList = rpmVisionRepository.findAll();
        assertThat(rpmVisionList).hasSize(databaseSizeBeforeUpdate);
        RpmVision testRpmVision = rpmVisionList.get(rpmVisionList.size() - 1);
        assertThat(testRpmVision.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingRpmVision() throws Exception {
        int databaseSizeBeforeUpdate = rpmVisionRepository.findAll().size();
        rpmVision.setId(count.incrementAndGet());

        // Create the RpmVision
        RpmVisionDTO rpmVisionDTO = rpmVisionMapper.toDto(rpmVision);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRpmVisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rpmVisionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmVisionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmVision in the database
        List<RpmVision> rpmVisionList = rpmVisionRepository.findAll();
        assertThat(rpmVisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRpmVision() throws Exception {
        int databaseSizeBeforeUpdate = rpmVisionRepository.findAll().size();
        rpmVision.setId(count.incrementAndGet());

        // Create the RpmVision
        RpmVisionDTO rpmVisionDTO = rpmVisionMapper.toDto(rpmVision);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmVisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmVisionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmVision in the database
        List<RpmVision> rpmVisionList = rpmVisionRepository.findAll();
        assertThat(rpmVisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRpmVision() throws Exception {
        int databaseSizeBeforeUpdate = rpmVisionRepository.findAll().size();
        rpmVision.setId(count.incrementAndGet());

        // Create the RpmVision
        RpmVisionDTO rpmVisionDTO = rpmVisionMapper.toDto(rpmVision);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmVisionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmVisionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RpmVision in the database
        List<RpmVision> rpmVisionList = rpmVisionRepository.findAll();
        assertThat(rpmVisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRpmVisionWithPatch() throws Exception {
        // Initialize the database
        rpmVisionRepository.saveAndFlush(rpmVision);

        int databaseSizeBeforeUpdate = rpmVisionRepository.findAll().size();

        // Update the rpmVision using partial update
        RpmVision partialUpdatedRpmVision = new RpmVision();
        partialUpdatedRpmVision.setId(rpmVision.getId());

        restRpmVisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRpmVision.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRpmVision))
            )
            .andExpect(status().isOk());

        // Validate the RpmVision in the database
        List<RpmVision> rpmVisionList = rpmVisionRepository.findAll();
        assertThat(rpmVisionList).hasSize(databaseSizeBeforeUpdate);
        RpmVision testRpmVision = rpmVisionList.get(rpmVisionList.size() - 1);
        assertThat(testRpmVision.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateRpmVisionWithPatch() throws Exception {
        // Initialize the database
        rpmVisionRepository.saveAndFlush(rpmVision);

        int databaseSizeBeforeUpdate = rpmVisionRepository.findAll().size();

        // Update the rpmVision using partial update
        RpmVision partialUpdatedRpmVision = new RpmVision();
        partialUpdatedRpmVision.setId(rpmVision.getId());

        partialUpdatedRpmVision.name(UPDATED_NAME);

        restRpmVisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRpmVision.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRpmVision))
            )
            .andExpect(status().isOk());

        // Validate the RpmVision in the database
        List<RpmVision> rpmVisionList = rpmVisionRepository.findAll();
        assertThat(rpmVisionList).hasSize(databaseSizeBeforeUpdate);
        RpmVision testRpmVision = rpmVisionList.get(rpmVisionList.size() - 1);
        assertThat(testRpmVision.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingRpmVision() throws Exception {
        int databaseSizeBeforeUpdate = rpmVisionRepository.findAll().size();
        rpmVision.setId(count.incrementAndGet());

        // Create the RpmVision
        RpmVisionDTO rpmVisionDTO = rpmVisionMapper.toDto(rpmVision);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRpmVisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rpmVisionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rpmVisionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmVision in the database
        List<RpmVision> rpmVisionList = rpmVisionRepository.findAll();
        assertThat(rpmVisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRpmVision() throws Exception {
        int databaseSizeBeforeUpdate = rpmVisionRepository.findAll().size();
        rpmVision.setId(count.incrementAndGet());

        // Create the RpmVision
        RpmVisionDTO rpmVisionDTO = rpmVisionMapper.toDto(rpmVision);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmVisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rpmVisionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmVision in the database
        List<RpmVision> rpmVisionList = rpmVisionRepository.findAll();
        assertThat(rpmVisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRpmVision() throws Exception {
        int databaseSizeBeforeUpdate = rpmVisionRepository.findAll().size();
        rpmVision.setId(count.incrementAndGet());

        // Create the RpmVision
        RpmVisionDTO rpmVisionDTO = rpmVisionMapper.toDto(rpmVision);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmVisionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rpmVisionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RpmVision in the database
        List<RpmVision> rpmVisionList = rpmVisionRepository.findAll();
        assertThat(rpmVisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRpmVision() throws Exception {
        // Initialize the database
        rpmVisionRepository.saveAndFlush(rpmVision);

        int databaseSizeBeforeDelete = rpmVisionRepository.findAll().size();

        // Delete the rpmVision
        restRpmVisionMockMvc
            .perform(delete(ENTITY_API_URL_ID, rpmVision.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RpmVision> rpmVisionList = rpmVisionRepository.findAll();
        assertThat(rpmVisionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
