package com.github.scottvin.rpm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.github.scottvin.rpm.IntegrationTest;
import com.github.scottvin.rpm.domain.RpmPurpose;
import com.github.scottvin.rpm.repository.RpmPurposeRepository;
import com.github.scottvin.rpm.service.dto.RpmPurposeDTO;
import com.github.scottvin.rpm.service.mapper.RpmPurposeMapper;
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
 * Integration tests for the {@link RpmPurposeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RpmPurposeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/rpm-purposes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RpmPurposeRepository rpmPurposeRepository;

    @Autowired
    private RpmPurposeMapper rpmPurposeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRpmPurposeMockMvc;

    private RpmPurpose rpmPurpose;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RpmPurpose createEntity(EntityManager em) {
        RpmPurpose rpmPurpose = new RpmPurpose().name(DEFAULT_NAME);
        return rpmPurpose;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RpmPurpose createUpdatedEntity(EntityManager em) {
        RpmPurpose rpmPurpose = new RpmPurpose().name(UPDATED_NAME);
        return rpmPurpose;
    }

    @BeforeEach
    public void initTest() {
        rpmPurpose = createEntity(em);
    }

    @Test
    @Transactional
    void createRpmPurpose() throws Exception {
        int databaseSizeBeforeCreate = rpmPurposeRepository.findAll().size();
        // Create the RpmPurpose
        RpmPurposeDTO rpmPurposeDTO = rpmPurposeMapper.toDto(rpmPurpose);
        restRpmPurposeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmPurposeDTO)))
            .andExpect(status().isCreated());

        // Validate the RpmPurpose in the database
        List<RpmPurpose> rpmPurposeList = rpmPurposeRepository.findAll();
        assertThat(rpmPurposeList).hasSize(databaseSizeBeforeCreate + 1);
        RpmPurpose testRpmPurpose = rpmPurposeList.get(rpmPurposeList.size() - 1);
        assertThat(testRpmPurpose.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createRpmPurposeWithExistingId() throws Exception {
        // Create the RpmPurpose with an existing ID
        rpmPurpose.setId(1L);
        RpmPurposeDTO rpmPurposeDTO = rpmPurposeMapper.toDto(rpmPurpose);

        int databaseSizeBeforeCreate = rpmPurposeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRpmPurposeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmPurposeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RpmPurpose in the database
        List<RpmPurpose> rpmPurposeList = rpmPurposeRepository.findAll();
        assertThat(rpmPurposeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = rpmPurposeRepository.findAll().size();
        // set the field null
        rpmPurpose.setName(null);

        // Create the RpmPurpose, which fails.
        RpmPurposeDTO rpmPurposeDTO = rpmPurposeMapper.toDto(rpmPurpose);

        restRpmPurposeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmPurposeDTO)))
            .andExpect(status().isBadRequest());

        List<RpmPurpose> rpmPurposeList = rpmPurposeRepository.findAll();
        assertThat(rpmPurposeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRpmPurposes() throws Exception {
        // Initialize the database
        rpmPurposeRepository.saveAndFlush(rpmPurpose);

        // Get all the rpmPurposeList
        restRpmPurposeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rpmPurpose.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getRpmPurpose() throws Exception {
        // Initialize the database
        rpmPurposeRepository.saveAndFlush(rpmPurpose);

        // Get the rpmPurpose
        restRpmPurposeMockMvc
            .perform(get(ENTITY_API_URL_ID, rpmPurpose.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rpmPurpose.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingRpmPurpose() throws Exception {
        // Get the rpmPurpose
        restRpmPurposeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRpmPurpose() throws Exception {
        // Initialize the database
        rpmPurposeRepository.saveAndFlush(rpmPurpose);

        int databaseSizeBeforeUpdate = rpmPurposeRepository.findAll().size();

        // Update the rpmPurpose
        RpmPurpose updatedRpmPurpose = rpmPurposeRepository.findById(rpmPurpose.getId()).get();
        // Disconnect from session so that the updates on updatedRpmPurpose are not directly saved in db
        em.detach(updatedRpmPurpose);
        updatedRpmPurpose.name(UPDATED_NAME);
        RpmPurposeDTO rpmPurposeDTO = rpmPurposeMapper.toDto(updatedRpmPurpose);

        restRpmPurposeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rpmPurposeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmPurposeDTO))
            )
            .andExpect(status().isOk());

        // Validate the RpmPurpose in the database
        List<RpmPurpose> rpmPurposeList = rpmPurposeRepository.findAll();
        assertThat(rpmPurposeList).hasSize(databaseSizeBeforeUpdate);
        RpmPurpose testRpmPurpose = rpmPurposeList.get(rpmPurposeList.size() - 1);
        assertThat(testRpmPurpose.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingRpmPurpose() throws Exception {
        int databaseSizeBeforeUpdate = rpmPurposeRepository.findAll().size();
        rpmPurpose.setId(count.incrementAndGet());

        // Create the RpmPurpose
        RpmPurposeDTO rpmPurposeDTO = rpmPurposeMapper.toDto(rpmPurpose);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRpmPurposeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rpmPurposeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmPurposeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmPurpose in the database
        List<RpmPurpose> rpmPurposeList = rpmPurposeRepository.findAll();
        assertThat(rpmPurposeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRpmPurpose() throws Exception {
        int databaseSizeBeforeUpdate = rpmPurposeRepository.findAll().size();
        rpmPurpose.setId(count.incrementAndGet());

        // Create the RpmPurpose
        RpmPurposeDTO rpmPurposeDTO = rpmPurposeMapper.toDto(rpmPurpose);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmPurposeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmPurposeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmPurpose in the database
        List<RpmPurpose> rpmPurposeList = rpmPurposeRepository.findAll();
        assertThat(rpmPurposeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRpmPurpose() throws Exception {
        int databaseSizeBeforeUpdate = rpmPurposeRepository.findAll().size();
        rpmPurpose.setId(count.incrementAndGet());

        // Create the RpmPurpose
        RpmPurposeDTO rpmPurposeDTO = rpmPurposeMapper.toDto(rpmPurpose);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmPurposeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmPurposeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RpmPurpose in the database
        List<RpmPurpose> rpmPurposeList = rpmPurposeRepository.findAll();
        assertThat(rpmPurposeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRpmPurposeWithPatch() throws Exception {
        // Initialize the database
        rpmPurposeRepository.saveAndFlush(rpmPurpose);

        int databaseSizeBeforeUpdate = rpmPurposeRepository.findAll().size();

        // Update the rpmPurpose using partial update
        RpmPurpose partialUpdatedRpmPurpose = new RpmPurpose();
        partialUpdatedRpmPurpose.setId(rpmPurpose.getId());

        restRpmPurposeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRpmPurpose.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRpmPurpose))
            )
            .andExpect(status().isOk());

        // Validate the RpmPurpose in the database
        List<RpmPurpose> rpmPurposeList = rpmPurposeRepository.findAll();
        assertThat(rpmPurposeList).hasSize(databaseSizeBeforeUpdate);
        RpmPurpose testRpmPurpose = rpmPurposeList.get(rpmPurposeList.size() - 1);
        assertThat(testRpmPurpose.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateRpmPurposeWithPatch() throws Exception {
        // Initialize the database
        rpmPurposeRepository.saveAndFlush(rpmPurpose);

        int databaseSizeBeforeUpdate = rpmPurposeRepository.findAll().size();

        // Update the rpmPurpose using partial update
        RpmPurpose partialUpdatedRpmPurpose = new RpmPurpose();
        partialUpdatedRpmPurpose.setId(rpmPurpose.getId());

        partialUpdatedRpmPurpose.name(UPDATED_NAME);

        restRpmPurposeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRpmPurpose.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRpmPurpose))
            )
            .andExpect(status().isOk());

        // Validate the RpmPurpose in the database
        List<RpmPurpose> rpmPurposeList = rpmPurposeRepository.findAll();
        assertThat(rpmPurposeList).hasSize(databaseSizeBeforeUpdate);
        RpmPurpose testRpmPurpose = rpmPurposeList.get(rpmPurposeList.size() - 1);
        assertThat(testRpmPurpose.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingRpmPurpose() throws Exception {
        int databaseSizeBeforeUpdate = rpmPurposeRepository.findAll().size();
        rpmPurpose.setId(count.incrementAndGet());

        // Create the RpmPurpose
        RpmPurposeDTO rpmPurposeDTO = rpmPurposeMapper.toDto(rpmPurpose);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRpmPurposeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rpmPurposeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rpmPurposeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmPurpose in the database
        List<RpmPurpose> rpmPurposeList = rpmPurposeRepository.findAll();
        assertThat(rpmPurposeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRpmPurpose() throws Exception {
        int databaseSizeBeforeUpdate = rpmPurposeRepository.findAll().size();
        rpmPurpose.setId(count.incrementAndGet());

        // Create the RpmPurpose
        RpmPurposeDTO rpmPurposeDTO = rpmPurposeMapper.toDto(rpmPurpose);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmPurposeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rpmPurposeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmPurpose in the database
        List<RpmPurpose> rpmPurposeList = rpmPurposeRepository.findAll();
        assertThat(rpmPurposeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRpmPurpose() throws Exception {
        int databaseSizeBeforeUpdate = rpmPurposeRepository.findAll().size();
        rpmPurpose.setId(count.incrementAndGet());

        // Create the RpmPurpose
        RpmPurposeDTO rpmPurposeDTO = rpmPurposeMapper.toDto(rpmPurpose);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmPurposeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rpmPurposeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RpmPurpose in the database
        List<RpmPurpose> rpmPurposeList = rpmPurposeRepository.findAll();
        assertThat(rpmPurposeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRpmPurpose() throws Exception {
        // Initialize the database
        rpmPurposeRepository.saveAndFlush(rpmPurpose);

        int databaseSizeBeforeDelete = rpmPurposeRepository.findAll().size();

        // Delete the rpmPurpose
        restRpmPurposeMockMvc
            .perform(delete(ENTITY_API_URL_ID, rpmPurpose.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RpmPurpose> rpmPurposeList = rpmPurposeRepository.findAll();
        assertThat(rpmPurposeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
