package com.github.scottvin.rpm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.github.scottvin.rpm.IntegrationTest;
import com.github.scottvin.rpm.domain.RpmReason;
import com.github.scottvin.rpm.repository.RpmReasonRepository;
import com.github.scottvin.rpm.service.dto.RpmReasonDTO;
import com.github.scottvin.rpm.service.mapper.RpmReasonMapper;
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
 * Integration tests for the {@link RpmReasonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RpmReasonResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/rpm-reasons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RpmReasonRepository rpmReasonRepository;

    @Autowired
    private RpmReasonMapper rpmReasonMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRpmReasonMockMvc;

    private RpmReason rpmReason;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RpmReason createEntity(EntityManager em) {
        RpmReason rpmReason = new RpmReason().name(DEFAULT_NAME);
        return rpmReason;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RpmReason createUpdatedEntity(EntityManager em) {
        RpmReason rpmReason = new RpmReason().name(UPDATED_NAME);
        return rpmReason;
    }

    @BeforeEach
    public void initTest() {
        rpmReason = createEntity(em);
    }

    @Test
    @Transactional
    void createRpmReason() throws Exception {
        int databaseSizeBeforeCreate = rpmReasonRepository.findAll().size();
        // Create the RpmReason
        RpmReasonDTO rpmReasonDTO = rpmReasonMapper.toDto(rpmReason);
        restRpmReasonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmReasonDTO)))
            .andExpect(status().isCreated());

        // Validate the RpmReason in the database
        List<RpmReason> rpmReasonList = rpmReasonRepository.findAll();
        assertThat(rpmReasonList).hasSize(databaseSizeBeforeCreate + 1);
        RpmReason testRpmReason = rpmReasonList.get(rpmReasonList.size() - 1);
        assertThat(testRpmReason.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createRpmReasonWithExistingId() throws Exception {
        // Create the RpmReason with an existing ID
        rpmReason.setId(1L);
        RpmReasonDTO rpmReasonDTO = rpmReasonMapper.toDto(rpmReason);

        int databaseSizeBeforeCreate = rpmReasonRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRpmReasonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmReasonDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RpmReason in the database
        List<RpmReason> rpmReasonList = rpmReasonRepository.findAll();
        assertThat(rpmReasonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = rpmReasonRepository.findAll().size();
        // set the field null
        rpmReason.setName(null);

        // Create the RpmReason, which fails.
        RpmReasonDTO rpmReasonDTO = rpmReasonMapper.toDto(rpmReason);

        restRpmReasonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmReasonDTO)))
            .andExpect(status().isBadRequest());

        List<RpmReason> rpmReasonList = rpmReasonRepository.findAll();
        assertThat(rpmReasonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRpmReasons() throws Exception {
        // Initialize the database
        rpmReasonRepository.saveAndFlush(rpmReason);

        // Get all the rpmReasonList
        restRpmReasonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rpmReason.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getRpmReason() throws Exception {
        // Initialize the database
        rpmReasonRepository.saveAndFlush(rpmReason);

        // Get the rpmReason
        restRpmReasonMockMvc
            .perform(get(ENTITY_API_URL_ID, rpmReason.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rpmReason.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingRpmReason() throws Exception {
        // Get the rpmReason
        restRpmReasonMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRpmReason() throws Exception {
        // Initialize the database
        rpmReasonRepository.saveAndFlush(rpmReason);

        int databaseSizeBeforeUpdate = rpmReasonRepository.findAll().size();

        // Update the rpmReason
        RpmReason updatedRpmReason = rpmReasonRepository.findById(rpmReason.getId()).get();
        // Disconnect from session so that the updates on updatedRpmReason are not directly saved in db
        em.detach(updatedRpmReason);
        updatedRpmReason.name(UPDATED_NAME);
        RpmReasonDTO rpmReasonDTO = rpmReasonMapper.toDto(updatedRpmReason);

        restRpmReasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rpmReasonDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmReasonDTO))
            )
            .andExpect(status().isOk());

        // Validate the RpmReason in the database
        List<RpmReason> rpmReasonList = rpmReasonRepository.findAll();
        assertThat(rpmReasonList).hasSize(databaseSizeBeforeUpdate);
        RpmReason testRpmReason = rpmReasonList.get(rpmReasonList.size() - 1);
        assertThat(testRpmReason.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingRpmReason() throws Exception {
        int databaseSizeBeforeUpdate = rpmReasonRepository.findAll().size();
        rpmReason.setId(count.incrementAndGet());

        // Create the RpmReason
        RpmReasonDTO rpmReasonDTO = rpmReasonMapper.toDto(rpmReason);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRpmReasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rpmReasonDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmReasonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmReason in the database
        List<RpmReason> rpmReasonList = rpmReasonRepository.findAll();
        assertThat(rpmReasonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRpmReason() throws Exception {
        int databaseSizeBeforeUpdate = rpmReasonRepository.findAll().size();
        rpmReason.setId(count.incrementAndGet());

        // Create the RpmReason
        RpmReasonDTO rpmReasonDTO = rpmReasonMapper.toDto(rpmReason);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmReasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmReasonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmReason in the database
        List<RpmReason> rpmReasonList = rpmReasonRepository.findAll();
        assertThat(rpmReasonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRpmReason() throws Exception {
        int databaseSizeBeforeUpdate = rpmReasonRepository.findAll().size();
        rpmReason.setId(count.incrementAndGet());

        // Create the RpmReason
        RpmReasonDTO rpmReasonDTO = rpmReasonMapper.toDto(rpmReason);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmReasonMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmReasonDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RpmReason in the database
        List<RpmReason> rpmReasonList = rpmReasonRepository.findAll();
        assertThat(rpmReasonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRpmReasonWithPatch() throws Exception {
        // Initialize the database
        rpmReasonRepository.saveAndFlush(rpmReason);

        int databaseSizeBeforeUpdate = rpmReasonRepository.findAll().size();

        // Update the rpmReason using partial update
        RpmReason partialUpdatedRpmReason = new RpmReason();
        partialUpdatedRpmReason.setId(rpmReason.getId());

        restRpmReasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRpmReason.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRpmReason))
            )
            .andExpect(status().isOk());

        // Validate the RpmReason in the database
        List<RpmReason> rpmReasonList = rpmReasonRepository.findAll();
        assertThat(rpmReasonList).hasSize(databaseSizeBeforeUpdate);
        RpmReason testRpmReason = rpmReasonList.get(rpmReasonList.size() - 1);
        assertThat(testRpmReason.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateRpmReasonWithPatch() throws Exception {
        // Initialize the database
        rpmReasonRepository.saveAndFlush(rpmReason);

        int databaseSizeBeforeUpdate = rpmReasonRepository.findAll().size();

        // Update the rpmReason using partial update
        RpmReason partialUpdatedRpmReason = new RpmReason();
        partialUpdatedRpmReason.setId(rpmReason.getId());

        partialUpdatedRpmReason.name(UPDATED_NAME);

        restRpmReasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRpmReason.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRpmReason))
            )
            .andExpect(status().isOk());

        // Validate the RpmReason in the database
        List<RpmReason> rpmReasonList = rpmReasonRepository.findAll();
        assertThat(rpmReasonList).hasSize(databaseSizeBeforeUpdate);
        RpmReason testRpmReason = rpmReasonList.get(rpmReasonList.size() - 1);
        assertThat(testRpmReason.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingRpmReason() throws Exception {
        int databaseSizeBeforeUpdate = rpmReasonRepository.findAll().size();
        rpmReason.setId(count.incrementAndGet());

        // Create the RpmReason
        RpmReasonDTO rpmReasonDTO = rpmReasonMapper.toDto(rpmReason);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRpmReasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rpmReasonDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rpmReasonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmReason in the database
        List<RpmReason> rpmReasonList = rpmReasonRepository.findAll();
        assertThat(rpmReasonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRpmReason() throws Exception {
        int databaseSizeBeforeUpdate = rpmReasonRepository.findAll().size();
        rpmReason.setId(count.incrementAndGet());

        // Create the RpmReason
        RpmReasonDTO rpmReasonDTO = rpmReasonMapper.toDto(rpmReason);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmReasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rpmReasonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmReason in the database
        List<RpmReason> rpmReasonList = rpmReasonRepository.findAll();
        assertThat(rpmReasonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRpmReason() throws Exception {
        int databaseSizeBeforeUpdate = rpmReasonRepository.findAll().size();
        rpmReason.setId(count.incrementAndGet());

        // Create the RpmReason
        RpmReasonDTO rpmReasonDTO = rpmReasonMapper.toDto(rpmReason);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmReasonMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rpmReasonDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RpmReason in the database
        List<RpmReason> rpmReasonList = rpmReasonRepository.findAll();
        assertThat(rpmReasonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRpmReason() throws Exception {
        // Initialize the database
        rpmReasonRepository.saveAndFlush(rpmReason);

        int databaseSizeBeforeDelete = rpmReasonRepository.findAll().size();

        // Delete the rpmReason
        restRpmReasonMockMvc
            .perform(delete(ENTITY_API_URL_ID, rpmReason.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RpmReason> rpmReasonList = rpmReasonRepository.findAll();
        assertThat(rpmReasonList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
