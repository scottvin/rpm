package com.github.scottvin.rpm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.github.scottvin.rpm.IntegrationTest;
import com.github.scottvin.rpm.domain.RpmCategory;
import com.github.scottvin.rpm.repository.RpmCategoryRepository;
import com.github.scottvin.rpm.service.dto.RpmCategoryDTO;
import com.github.scottvin.rpm.service.mapper.RpmCategoryMapper;
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
 * Integration tests for the {@link RpmCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RpmCategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/rpm-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RpmCategoryRepository rpmCategoryRepository;

    @Autowired
    private RpmCategoryMapper rpmCategoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRpmCategoryMockMvc;

    private RpmCategory rpmCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RpmCategory createEntity(EntityManager em) {
        RpmCategory rpmCategory = new RpmCategory().name(DEFAULT_NAME);
        return rpmCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RpmCategory createUpdatedEntity(EntityManager em) {
        RpmCategory rpmCategory = new RpmCategory().name(UPDATED_NAME);
        return rpmCategory;
    }

    @BeforeEach
    public void initTest() {
        rpmCategory = createEntity(em);
    }

    @Test
    @Transactional
    void createRpmCategory() throws Exception {
        int databaseSizeBeforeCreate = rpmCategoryRepository.findAll().size();
        // Create the RpmCategory
        RpmCategoryDTO rpmCategoryDTO = rpmCategoryMapper.toDto(rpmCategory);
        restRpmCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmCategoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the RpmCategory in the database
        List<RpmCategory> rpmCategoryList = rpmCategoryRepository.findAll();
        assertThat(rpmCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        RpmCategory testRpmCategory = rpmCategoryList.get(rpmCategoryList.size() - 1);
        assertThat(testRpmCategory.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createRpmCategoryWithExistingId() throws Exception {
        // Create the RpmCategory with an existing ID
        rpmCategory.setId(1L);
        RpmCategoryDTO rpmCategoryDTO = rpmCategoryMapper.toDto(rpmCategory);

        int databaseSizeBeforeCreate = rpmCategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRpmCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmCategory in the database
        List<RpmCategory> rpmCategoryList = rpmCategoryRepository.findAll();
        assertThat(rpmCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = rpmCategoryRepository.findAll().size();
        // set the field null
        rpmCategory.setName(null);

        // Create the RpmCategory, which fails.
        RpmCategoryDTO rpmCategoryDTO = rpmCategoryMapper.toDto(rpmCategory);

        restRpmCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<RpmCategory> rpmCategoryList = rpmCategoryRepository.findAll();
        assertThat(rpmCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRpmCategories() throws Exception {
        // Initialize the database
        rpmCategoryRepository.saveAndFlush(rpmCategory);

        // Get all the rpmCategoryList
        restRpmCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rpmCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getRpmCategory() throws Exception {
        // Initialize the database
        rpmCategoryRepository.saveAndFlush(rpmCategory);

        // Get the rpmCategory
        restRpmCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, rpmCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rpmCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingRpmCategory() throws Exception {
        // Get the rpmCategory
        restRpmCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRpmCategory() throws Exception {
        // Initialize the database
        rpmCategoryRepository.saveAndFlush(rpmCategory);

        int databaseSizeBeforeUpdate = rpmCategoryRepository.findAll().size();

        // Update the rpmCategory
        RpmCategory updatedRpmCategory = rpmCategoryRepository.findById(rpmCategory.getId()).get();
        // Disconnect from session so that the updates on updatedRpmCategory are not directly saved in db
        em.detach(updatedRpmCategory);
        updatedRpmCategory.name(UPDATED_NAME);
        RpmCategoryDTO rpmCategoryDTO = rpmCategoryMapper.toDto(updatedRpmCategory);

        restRpmCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rpmCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmCategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the RpmCategory in the database
        List<RpmCategory> rpmCategoryList = rpmCategoryRepository.findAll();
        assertThat(rpmCategoryList).hasSize(databaseSizeBeforeUpdate);
        RpmCategory testRpmCategory = rpmCategoryList.get(rpmCategoryList.size() - 1);
        assertThat(testRpmCategory.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingRpmCategory() throws Exception {
        int databaseSizeBeforeUpdate = rpmCategoryRepository.findAll().size();
        rpmCategory.setId(count.incrementAndGet());

        // Create the RpmCategory
        RpmCategoryDTO rpmCategoryDTO = rpmCategoryMapper.toDto(rpmCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRpmCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rpmCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmCategory in the database
        List<RpmCategory> rpmCategoryList = rpmCategoryRepository.findAll();
        assertThat(rpmCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRpmCategory() throws Exception {
        int databaseSizeBeforeUpdate = rpmCategoryRepository.findAll().size();
        rpmCategory.setId(count.incrementAndGet());

        // Create the RpmCategory
        RpmCategoryDTO rpmCategoryDTO = rpmCategoryMapper.toDto(rpmCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmCategory in the database
        List<RpmCategory> rpmCategoryList = rpmCategoryRepository.findAll();
        assertThat(rpmCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRpmCategory() throws Exception {
        int databaseSizeBeforeUpdate = rpmCategoryRepository.findAll().size();
        rpmCategory.setId(count.incrementAndGet());

        // Create the RpmCategory
        RpmCategoryDTO rpmCategoryDTO = rpmCategoryMapper.toDto(rpmCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmCategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmCategoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RpmCategory in the database
        List<RpmCategory> rpmCategoryList = rpmCategoryRepository.findAll();
        assertThat(rpmCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRpmCategoryWithPatch() throws Exception {
        // Initialize the database
        rpmCategoryRepository.saveAndFlush(rpmCategory);

        int databaseSizeBeforeUpdate = rpmCategoryRepository.findAll().size();

        // Update the rpmCategory using partial update
        RpmCategory partialUpdatedRpmCategory = new RpmCategory();
        partialUpdatedRpmCategory.setId(rpmCategory.getId());

        restRpmCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRpmCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRpmCategory))
            )
            .andExpect(status().isOk());

        // Validate the RpmCategory in the database
        List<RpmCategory> rpmCategoryList = rpmCategoryRepository.findAll();
        assertThat(rpmCategoryList).hasSize(databaseSizeBeforeUpdate);
        RpmCategory testRpmCategory = rpmCategoryList.get(rpmCategoryList.size() - 1);
        assertThat(testRpmCategory.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateRpmCategoryWithPatch() throws Exception {
        // Initialize the database
        rpmCategoryRepository.saveAndFlush(rpmCategory);

        int databaseSizeBeforeUpdate = rpmCategoryRepository.findAll().size();

        // Update the rpmCategory using partial update
        RpmCategory partialUpdatedRpmCategory = new RpmCategory();
        partialUpdatedRpmCategory.setId(rpmCategory.getId());

        partialUpdatedRpmCategory.name(UPDATED_NAME);

        restRpmCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRpmCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRpmCategory))
            )
            .andExpect(status().isOk());

        // Validate the RpmCategory in the database
        List<RpmCategory> rpmCategoryList = rpmCategoryRepository.findAll();
        assertThat(rpmCategoryList).hasSize(databaseSizeBeforeUpdate);
        RpmCategory testRpmCategory = rpmCategoryList.get(rpmCategoryList.size() - 1);
        assertThat(testRpmCategory.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingRpmCategory() throws Exception {
        int databaseSizeBeforeUpdate = rpmCategoryRepository.findAll().size();
        rpmCategory.setId(count.incrementAndGet());

        // Create the RpmCategory
        RpmCategoryDTO rpmCategoryDTO = rpmCategoryMapper.toDto(rpmCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRpmCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rpmCategoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rpmCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmCategory in the database
        List<RpmCategory> rpmCategoryList = rpmCategoryRepository.findAll();
        assertThat(rpmCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRpmCategory() throws Exception {
        int databaseSizeBeforeUpdate = rpmCategoryRepository.findAll().size();
        rpmCategory.setId(count.incrementAndGet());

        // Create the RpmCategory
        RpmCategoryDTO rpmCategoryDTO = rpmCategoryMapper.toDto(rpmCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rpmCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmCategory in the database
        List<RpmCategory> rpmCategoryList = rpmCategoryRepository.findAll();
        assertThat(rpmCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRpmCategory() throws Exception {
        int databaseSizeBeforeUpdate = rpmCategoryRepository.findAll().size();
        rpmCategory.setId(count.incrementAndGet());

        // Create the RpmCategory
        RpmCategoryDTO rpmCategoryDTO = rpmCategoryMapper.toDto(rpmCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rpmCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RpmCategory in the database
        List<RpmCategory> rpmCategoryList = rpmCategoryRepository.findAll();
        assertThat(rpmCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRpmCategory() throws Exception {
        // Initialize the database
        rpmCategoryRepository.saveAndFlush(rpmCategory);

        int databaseSizeBeforeDelete = rpmCategoryRepository.findAll().size();

        // Delete the rpmCategory
        restRpmCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, rpmCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RpmCategory> rpmCategoryList = rpmCategoryRepository.findAll();
        assertThat(rpmCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
