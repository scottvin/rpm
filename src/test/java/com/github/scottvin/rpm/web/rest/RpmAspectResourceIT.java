package com.github.scottvin.rpm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.github.scottvin.rpm.IntegrationTest;
import com.github.scottvin.rpm.domain.RpmAspect;
import com.github.scottvin.rpm.repository.RpmAspectRepository;
import com.github.scottvin.rpm.service.dto.RpmAspectDTO;
import com.github.scottvin.rpm.service.mapper.RpmAspectMapper;
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
 * Integration tests for the {@link RpmAspectResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RpmAspectResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/rpm-aspects";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RpmAspectRepository rpmAspectRepository;

    @Autowired
    private RpmAspectMapper rpmAspectMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRpmAspectMockMvc;

    private RpmAspect rpmAspect;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RpmAspect createEntity(EntityManager em) {
        RpmAspect rpmAspect = new RpmAspect().name(DEFAULT_NAME);
        return rpmAspect;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RpmAspect createUpdatedEntity(EntityManager em) {
        RpmAspect rpmAspect = new RpmAspect().name(UPDATED_NAME);
        return rpmAspect;
    }

    @BeforeEach
    public void initTest() {
        rpmAspect = createEntity(em);
    }

    @Test
    @Transactional
    void createRpmAspect() throws Exception {
        int databaseSizeBeforeCreate = rpmAspectRepository.findAll().size();
        // Create the RpmAspect
        RpmAspectDTO rpmAspectDTO = rpmAspectMapper.toDto(rpmAspect);
        restRpmAspectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmAspectDTO)))
            .andExpect(status().isCreated());

        // Validate the RpmAspect in the database
        List<RpmAspect> rpmAspectList = rpmAspectRepository.findAll();
        assertThat(rpmAspectList).hasSize(databaseSizeBeforeCreate + 1);
        RpmAspect testRpmAspect = rpmAspectList.get(rpmAspectList.size() - 1);
        assertThat(testRpmAspect.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createRpmAspectWithExistingId() throws Exception {
        // Create the RpmAspect with an existing ID
        rpmAspect.setId(1L);
        RpmAspectDTO rpmAspectDTO = rpmAspectMapper.toDto(rpmAspect);

        int databaseSizeBeforeCreate = rpmAspectRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRpmAspectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmAspectDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RpmAspect in the database
        List<RpmAspect> rpmAspectList = rpmAspectRepository.findAll();
        assertThat(rpmAspectList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = rpmAspectRepository.findAll().size();
        // set the field null
        rpmAspect.setName(null);

        // Create the RpmAspect, which fails.
        RpmAspectDTO rpmAspectDTO = rpmAspectMapper.toDto(rpmAspect);

        restRpmAspectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmAspectDTO)))
            .andExpect(status().isBadRequest());

        List<RpmAspect> rpmAspectList = rpmAspectRepository.findAll();
        assertThat(rpmAspectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRpmAspects() throws Exception {
        // Initialize the database
        rpmAspectRepository.saveAndFlush(rpmAspect);

        // Get all the rpmAspectList
        restRpmAspectMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rpmAspect.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getRpmAspect() throws Exception {
        // Initialize the database
        rpmAspectRepository.saveAndFlush(rpmAspect);

        // Get the rpmAspect
        restRpmAspectMockMvc
            .perform(get(ENTITY_API_URL_ID, rpmAspect.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rpmAspect.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingRpmAspect() throws Exception {
        // Get the rpmAspect
        restRpmAspectMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRpmAspect() throws Exception {
        // Initialize the database
        rpmAspectRepository.saveAndFlush(rpmAspect);

        int databaseSizeBeforeUpdate = rpmAspectRepository.findAll().size();

        // Update the rpmAspect
        RpmAspect updatedRpmAspect = rpmAspectRepository.findById(rpmAspect.getId()).get();
        // Disconnect from session so that the updates on updatedRpmAspect are not directly saved in db
        em.detach(updatedRpmAspect);
        updatedRpmAspect.name(UPDATED_NAME);
        RpmAspectDTO rpmAspectDTO = rpmAspectMapper.toDto(updatedRpmAspect);

        restRpmAspectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rpmAspectDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmAspectDTO))
            )
            .andExpect(status().isOk());

        // Validate the RpmAspect in the database
        List<RpmAspect> rpmAspectList = rpmAspectRepository.findAll();
        assertThat(rpmAspectList).hasSize(databaseSizeBeforeUpdate);
        RpmAspect testRpmAspect = rpmAspectList.get(rpmAspectList.size() - 1);
        assertThat(testRpmAspect.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingRpmAspect() throws Exception {
        int databaseSizeBeforeUpdate = rpmAspectRepository.findAll().size();
        rpmAspect.setId(count.incrementAndGet());

        // Create the RpmAspect
        RpmAspectDTO rpmAspectDTO = rpmAspectMapper.toDto(rpmAspect);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRpmAspectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rpmAspectDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmAspectDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmAspect in the database
        List<RpmAspect> rpmAspectList = rpmAspectRepository.findAll();
        assertThat(rpmAspectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRpmAspect() throws Exception {
        int databaseSizeBeforeUpdate = rpmAspectRepository.findAll().size();
        rpmAspect.setId(count.incrementAndGet());

        // Create the RpmAspect
        RpmAspectDTO rpmAspectDTO = rpmAspectMapper.toDto(rpmAspect);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmAspectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmAspectDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmAspect in the database
        List<RpmAspect> rpmAspectList = rpmAspectRepository.findAll();
        assertThat(rpmAspectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRpmAspect() throws Exception {
        int databaseSizeBeforeUpdate = rpmAspectRepository.findAll().size();
        rpmAspect.setId(count.incrementAndGet());

        // Create the RpmAspect
        RpmAspectDTO rpmAspectDTO = rpmAspectMapper.toDto(rpmAspect);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmAspectMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmAspectDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RpmAspect in the database
        List<RpmAspect> rpmAspectList = rpmAspectRepository.findAll();
        assertThat(rpmAspectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRpmAspectWithPatch() throws Exception {
        // Initialize the database
        rpmAspectRepository.saveAndFlush(rpmAspect);

        int databaseSizeBeforeUpdate = rpmAspectRepository.findAll().size();

        // Update the rpmAspect using partial update
        RpmAspect partialUpdatedRpmAspect = new RpmAspect();
        partialUpdatedRpmAspect.setId(rpmAspect.getId());

        restRpmAspectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRpmAspect.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRpmAspect))
            )
            .andExpect(status().isOk());

        // Validate the RpmAspect in the database
        List<RpmAspect> rpmAspectList = rpmAspectRepository.findAll();
        assertThat(rpmAspectList).hasSize(databaseSizeBeforeUpdate);
        RpmAspect testRpmAspect = rpmAspectList.get(rpmAspectList.size() - 1);
        assertThat(testRpmAspect.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateRpmAspectWithPatch() throws Exception {
        // Initialize the database
        rpmAspectRepository.saveAndFlush(rpmAspect);

        int databaseSizeBeforeUpdate = rpmAspectRepository.findAll().size();

        // Update the rpmAspect using partial update
        RpmAspect partialUpdatedRpmAspect = new RpmAspect();
        partialUpdatedRpmAspect.setId(rpmAspect.getId());

        partialUpdatedRpmAspect.name(UPDATED_NAME);

        restRpmAspectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRpmAspect.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRpmAspect))
            )
            .andExpect(status().isOk());

        // Validate the RpmAspect in the database
        List<RpmAspect> rpmAspectList = rpmAspectRepository.findAll();
        assertThat(rpmAspectList).hasSize(databaseSizeBeforeUpdate);
        RpmAspect testRpmAspect = rpmAspectList.get(rpmAspectList.size() - 1);
        assertThat(testRpmAspect.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingRpmAspect() throws Exception {
        int databaseSizeBeforeUpdate = rpmAspectRepository.findAll().size();
        rpmAspect.setId(count.incrementAndGet());

        // Create the RpmAspect
        RpmAspectDTO rpmAspectDTO = rpmAspectMapper.toDto(rpmAspect);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRpmAspectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rpmAspectDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rpmAspectDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmAspect in the database
        List<RpmAspect> rpmAspectList = rpmAspectRepository.findAll();
        assertThat(rpmAspectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRpmAspect() throws Exception {
        int databaseSizeBeforeUpdate = rpmAspectRepository.findAll().size();
        rpmAspect.setId(count.incrementAndGet());

        // Create the RpmAspect
        RpmAspectDTO rpmAspectDTO = rpmAspectMapper.toDto(rpmAspect);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmAspectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rpmAspectDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmAspect in the database
        List<RpmAspect> rpmAspectList = rpmAspectRepository.findAll();
        assertThat(rpmAspectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRpmAspect() throws Exception {
        int databaseSizeBeforeUpdate = rpmAspectRepository.findAll().size();
        rpmAspect.setId(count.incrementAndGet());

        // Create the RpmAspect
        RpmAspectDTO rpmAspectDTO = rpmAspectMapper.toDto(rpmAspect);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmAspectMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rpmAspectDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RpmAspect in the database
        List<RpmAspect> rpmAspectList = rpmAspectRepository.findAll();
        assertThat(rpmAspectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRpmAspect() throws Exception {
        // Initialize the database
        rpmAspectRepository.saveAndFlush(rpmAspect);

        int databaseSizeBeforeDelete = rpmAspectRepository.findAll().size();

        // Delete the rpmAspect
        restRpmAspectMockMvc
            .perform(delete(ENTITY_API_URL_ID, rpmAspect.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RpmAspect> rpmAspectList = rpmAspectRepository.findAll();
        assertThat(rpmAspectList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
