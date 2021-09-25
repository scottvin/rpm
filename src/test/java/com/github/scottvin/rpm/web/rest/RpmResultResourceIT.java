package com.github.scottvin.rpm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.github.scottvin.rpm.IntegrationTest;
import com.github.scottvin.rpm.domain.RpmAspect;
import com.github.scottvin.rpm.domain.RpmCategory;
import com.github.scottvin.rpm.domain.RpmPurpose;
import com.github.scottvin.rpm.domain.RpmResult;
import com.github.scottvin.rpm.domain.RpmRole;
import com.github.scottvin.rpm.domain.RpmVision;
import com.github.scottvin.rpm.repository.RpmResultRepository;
import com.github.scottvin.rpm.service.criteria.RpmResultCriteria;
import com.github.scottvin.rpm.service.dto.RpmResultDTO;
import com.github.scottvin.rpm.service.mapper.RpmResultMapper;
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
 * Integration tests for the {@link RpmResultResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RpmResultResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/rpm-results";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RpmResultRepository rpmResultRepository;

    @Autowired
    private RpmResultMapper rpmResultMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRpmResultMockMvc;

    private RpmResult rpmResult;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RpmResult createEntity(EntityManager em) {
        RpmResult rpmResult = new RpmResult().name(DEFAULT_NAME);
        // Add required entity
        RpmCategory rpmCategory;
        if (TestUtil.findAll(em, RpmCategory.class).isEmpty()) {
            rpmCategory = RpmCategoryResourceIT.createEntity(em);
            em.persist(rpmCategory);
            em.flush();
        } else {
            rpmCategory = TestUtil.findAll(em, RpmCategory.class).get(0);
        }
        rpmResult.setCategory(rpmCategory);
        // Add required entity
        RpmAspect rpmAspect;
        if (TestUtil.findAll(em, RpmAspect.class).isEmpty()) {
            rpmAspect = RpmAspectResourceIT.createEntity(em);
            em.persist(rpmAspect);
            em.flush();
        } else {
            rpmAspect = TestUtil.findAll(em, RpmAspect.class).get(0);
        }
        rpmResult.setAspect(rpmAspect);
        // Add required entity
        RpmVision rpmVision;
        if (TestUtil.findAll(em, RpmVision.class).isEmpty()) {
            rpmVision = RpmVisionResourceIT.createEntity(em);
            em.persist(rpmVision);
            em.flush();
        } else {
            rpmVision = TestUtil.findAll(em, RpmVision.class).get(0);
        }
        rpmResult.setVision(rpmVision);
        // Add required entity
        RpmPurpose rpmPurpose;
        if (TestUtil.findAll(em, RpmPurpose.class).isEmpty()) {
            rpmPurpose = RpmPurposeResourceIT.createEntity(em);
            em.persist(rpmPurpose);
            em.flush();
        } else {
            rpmPurpose = TestUtil.findAll(em, RpmPurpose.class).get(0);
        }
        rpmResult.setPurpose(rpmPurpose);
        // Add required entity
        RpmRole rpmRole;
        if (TestUtil.findAll(em, RpmRole.class).isEmpty()) {
            rpmRole = RpmRoleResourceIT.createEntity(em);
            em.persist(rpmRole);
            em.flush();
        } else {
            rpmRole = TestUtil.findAll(em, RpmRole.class).get(0);
        }
        rpmResult.setRole(rpmRole);
        return rpmResult;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RpmResult createUpdatedEntity(EntityManager em) {
        RpmResult rpmResult = new RpmResult().name(UPDATED_NAME);
        // Add required entity
        RpmCategory rpmCategory;
        if (TestUtil.findAll(em, RpmCategory.class).isEmpty()) {
            rpmCategory = RpmCategoryResourceIT.createUpdatedEntity(em);
            em.persist(rpmCategory);
            em.flush();
        } else {
            rpmCategory = TestUtil.findAll(em, RpmCategory.class).get(0);
        }
        rpmResult.setCategory(rpmCategory);
        // Add required entity
        RpmAspect rpmAspect;
        if (TestUtil.findAll(em, RpmAspect.class).isEmpty()) {
            rpmAspect = RpmAspectResourceIT.createUpdatedEntity(em);
            em.persist(rpmAspect);
            em.flush();
        } else {
            rpmAspect = TestUtil.findAll(em, RpmAspect.class).get(0);
        }
        rpmResult.setAspect(rpmAspect);
        // Add required entity
        RpmVision rpmVision;
        if (TestUtil.findAll(em, RpmVision.class).isEmpty()) {
            rpmVision = RpmVisionResourceIT.createUpdatedEntity(em);
            em.persist(rpmVision);
            em.flush();
        } else {
            rpmVision = TestUtil.findAll(em, RpmVision.class).get(0);
        }
        rpmResult.setVision(rpmVision);
        // Add required entity
        RpmPurpose rpmPurpose;
        if (TestUtil.findAll(em, RpmPurpose.class).isEmpty()) {
            rpmPurpose = RpmPurposeResourceIT.createUpdatedEntity(em);
            em.persist(rpmPurpose);
            em.flush();
        } else {
            rpmPurpose = TestUtil.findAll(em, RpmPurpose.class).get(0);
        }
        rpmResult.setPurpose(rpmPurpose);
        // Add required entity
        RpmRole rpmRole;
        if (TestUtil.findAll(em, RpmRole.class).isEmpty()) {
            rpmRole = RpmRoleResourceIT.createUpdatedEntity(em);
            em.persist(rpmRole);
            em.flush();
        } else {
            rpmRole = TestUtil.findAll(em, RpmRole.class).get(0);
        }
        rpmResult.setRole(rpmRole);
        return rpmResult;
    }

    @BeforeEach
    public void initTest() {
        rpmResult = createEntity(em);
    }

    @Test
    @Transactional
    void createRpmResult() throws Exception {
        int databaseSizeBeforeCreate = rpmResultRepository.findAll().size();
        // Create the RpmResult
        RpmResultDTO rpmResultDTO = rpmResultMapper.toDto(rpmResult);
        restRpmResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmResultDTO)))
            .andExpect(status().isCreated());

        // Validate the RpmResult in the database
        List<RpmResult> rpmResultList = rpmResultRepository.findAll();
        assertThat(rpmResultList).hasSize(databaseSizeBeforeCreate + 1);
        RpmResult testRpmResult = rpmResultList.get(rpmResultList.size() - 1);
        assertThat(testRpmResult.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createRpmResultWithExistingId() throws Exception {
        // Create the RpmResult with an existing ID
        rpmResult.setId(1L);
        RpmResultDTO rpmResultDTO = rpmResultMapper.toDto(rpmResult);

        int databaseSizeBeforeCreate = rpmResultRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRpmResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmResultDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RpmResult in the database
        List<RpmResult> rpmResultList = rpmResultRepository.findAll();
        assertThat(rpmResultList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = rpmResultRepository.findAll().size();
        // set the field null
        rpmResult.setName(null);

        // Create the RpmResult, which fails.
        RpmResultDTO rpmResultDTO = rpmResultMapper.toDto(rpmResult);

        restRpmResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmResultDTO)))
            .andExpect(status().isBadRequest());

        List<RpmResult> rpmResultList = rpmResultRepository.findAll();
        assertThat(rpmResultList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRpmResults() throws Exception {
        // Initialize the database
        rpmResultRepository.saveAndFlush(rpmResult);

        // Get all the rpmResultList
        restRpmResultMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rpmResult.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getRpmResult() throws Exception {
        // Initialize the database
        rpmResultRepository.saveAndFlush(rpmResult);

        // Get the rpmResult
        restRpmResultMockMvc
            .perform(get(ENTITY_API_URL_ID, rpmResult.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rpmResult.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getRpmResultsByIdFiltering() throws Exception {
        // Initialize the database
        rpmResultRepository.saveAndFlush(rpmResult);

        Long id = rpmResult.getId();

        defaultRpmResultShouldBeFound("id.equals=" + id);
        defaultRpmResultShouldNotBeFound("id.notEquals=" + id);

        defaultRpmResultShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRpmResultShouldNotBeFound("id.greaterThan=" + id);

        defaultRpmResultShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRpmResultShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRpmResultsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        rpmResultRepository.saveAndFlush(rpmResult);

        // Get all the rpmResultList where name equals to DEFAULT_NAME
        defaultRpmResultShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the rpmResultList where name equals to UPDATED_NAME
        defaultRpmResultShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRpmResultsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rpmResultRepository.saveAndFlush(rpmResult);

        // Get all the rpmResultList where name not equals to DEFAULT_NAME
        defaultRpmResultShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the rpmResultList where name not equals to UPDATED_NAME
        defaultRpmResultShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRpmResultsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        rpmResultRepository.saveAndFlush(rpmResult);

        // Get all the rpmResultList where name in DEFAULT_NAME or UPDATED_NAME
        defaultRpmResultShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the rpmResultList where name equals to UPDATED_NAME
        defaultRpmResultShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRpmResultsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        rpmResultRepository.saveAndFlush(rpmResult);

        // Get all the rpmResultList where name is not null
        defaultRpmResultShouldBeFound("name.specified=true");

        // Get all the rpmResultList where name is null
        defaultRpmResultShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllRpmResultsByNameContainsSomething() throws Exception {
        // Initialize the database
        rpmResultRepository.saveAndFlush(rpmResult);

        // Get all the rpmResultList where name contains DEFAULT_NAME
        defaultRpmResultShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the rpmResultList where name contains UPDATED_NAME
        defaultRpmResultShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRpmResultsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        rpmResultRepository.saveAndFlush(rpmResult);

        // Get all the rpmResultList where name does not contain DEFAULT_NAME
        defaultRpmResultShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the rpmResultList where name does not contain UPDATED_NAME
        defaultRpmResultShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRpmResultsByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        rpmResultRepository.saveAndFlush(rpmResult);
        RpmCategory category;
        if (TestUtil.findAll(em, RpmCategory.class).isEmpty()) {
            category = RpmCategoryResourceIT.createEntity(em);
            em.persist(category);
            em.flush();
        } else {
            category = TestUtil.findAll(em, RpmCategory.class).get(0);
        }
        em.persist(category);
        em.flush();
        rpmResult.setCategory(category);
        rpmResultRepository.saveAndFlush(rpmResult);
        Long categoryId = category.getId();

        // Get all the rpmResultList where category equals to categoryId
        defaultRpmResultShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the rpmResultList where category equals to (categoryId + 1)
        defaultRpmResultShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    @Test
    @Transactional
    void getAllRpmResultsByAspectIsEqualToSomething() throws Exception {
        // Initialize the database
        rpmResultRepository.saveAndFlush(rpmResult);
        RpmAspect aspect;
        if (TestUtil.findAll(em, RpmAspect.class).isEmpty()) {
            aspect = RpmAspectResourceIT.createEntity(em);
            em.persist(aspect);
            em.flush();
        } else {
            aspect = TestUtil.findAll(em, RpmAspect.class).get(0);
        }
        em.persist(aspect);
        em.flush();
        rpmResult.setAspect(aspect);
        rpmResultRepository.saveAndFlush(rpmResult);
        Long aspectId = aspect.getId();

        // Get all the rpmResultList where aspect equals to aspectId
        defaultRpmResultShouldBeFound("aspectId.equals=" + aspectId);

        // Get all the rpmResultList where aspect equals to (aspectId + 1)
        defaultRpmResultShouldNotBeFound("aspectId.equals=" + (aspectId + 1));
    }

    @Test
    @Transactional
    void getAllRpmResultsByVisionIsEqualToSomething() throws Exception {
        // Initialize the database
        rpmResultRepository.saveAndFlush(rpmResult);
        RpmVision vision;
        if (TestUtil.findAll(em, RpmVision.class).isEmpty()) {
            vision = RpmVisionResourceIT.createEntity(em);
            em.persist(vision);
            em.flush();
        } else {
            vision = TestUtil.findAll(em, RpmVision.class).get(0);
        }
        em.persist(vision);
        em.flush();
        rpmResult.setVision(vision);
        rpmResultRepository.saveAndFlush(rpmResult);
        Long visionId = vision.getId();

        // Get all the rpmResultList where vision equals to visionId
        defaultRpmResultShouldBeFound("visionId.equals=" + visionId);

        // Get all the rpmResultList where vision equals to (visionId + 1)
        defaultRpmResultShouldNotBeFound("visionId.equals=" + (visionId + 1));
    }

    @Test
    @Transactional
    void getAllRpmResultsByPurposeIsEqualToSomething() throws Exception {
        // Initialize the database
        rpmResultRepository.saveAndFlush(rpmResult);
        RpmPurpose purpose;
        if (TestUtil.findAll(em, RpmPurpose.class).isEmpty()) {
            purpose = RpmPurposeResourceIT.createEntity(em);
            em.persist(purpose);
            em.flush();
        } else {
            purpose = TestUtil.findAll(em, RpmPurpose.class).get(0);
        }
        em.persist(purpose);
        em.flush();
        rpmResult.setPurpose(purpose);
        rpmResultRepository.saveAndFlush(rpmResult);
        Long purposeId = purpose.getId();

        // Get all the rpmResultList where purpose equals to purposeId
        defaultRpmResultShouldBeFound("purposeId.equals=" + purposeId);

        // Get all the rpmResultList where purpose equals to (purposeId + 1)
        defaultRpmResultShouldNotBeFound("purposeId.equals=" + (purposeId + 1));
    }

    @Test
    @Transactional
    void getAllRpmResultsByRoleIsEqualToSomething() throws Exception {
        // Initialize the database
        rpmResultRepository.saveAndFlush(rpmResult);
        RpmRole role;
        if (TestUtil.findAll(em, RpmRole.class).isEmpty()) {
            role = RpmRoleResourceIT.createEntity(em);
            em.persist(role);
            em.flush();
        } else {
            role = TestUtil.findAll(em, RpmRole.class).get(0);
        }
        em.persist(role);
        em.flush();
        rpmResult.setRole(role);
        rpmResultRepository.saveAndFlush(rpmResult);
        Long roleId = role.getId();

        // Get all the rpmResultList where role equals to roleId
        defaultRpmResultShouldBeFound("roleId.equals=" + roleId);

        // Get all the rpmResultList where role equals to (roleId + 1)
        defaultRpmResultShouldNotBeFound("roleId.equals=" + (roleId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRpmResultShouldBeFound(String filter) throws Exception {
        restRpmResultMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rpmResult.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restRpmResultMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRpmResultShouldNotBeFound(String filter) throws Exception {
        restRpmResultMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRpmResultMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRpmResult() throws Exception {
        // Get the rpmResult
        restRpmResultMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRpmResult() throws Exception {
        // Initialize the database
        rpmResultRepository.saveAndFlush(rpmResult);

        int databaseSizeBeforeUpdate = rpmResultRepository.findAll().size();

        // Update the rpmResult
        RpmResult updatedRpmResult = rpmResultRepository.findById(rpmResult.getId()).get();
        // Disconnect from session so that the updates on updatedRpmResult are not directly saved in db
        em.detach(updatedRpmResult);
        updatedRpmResult.name(UPDATED_NAME);
        RpmResultDTO rpmResultDTO = rpmResultMapper.toDto(updatedRpmResult);

        restRpmResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rpmResultDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmResultDTO))
            )
            .andExpect(status().isOk());

        // Validate the RpmResult in the database
        List<RpmResult> rpmResultList = rpmResultRepository.findAll();
        assertThat(rpmResultList).hasSize(databaseSizeBeforeUpdate);
        RpmResult testRpmResult = rpmResultList.get(rpmResultList.size() - 1);
        assertThat(testRpmResult.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingRpmResult() throws Exception {
        int databaseSizeBeforeUpdate = rpmResultRepository.findAll().size();
        rpmResult.setId(count.incrementAndGet());

        // Create the RpmResult
        RpmResultDTO rpmResultDTO = rpmResultMapper.toDto(rpmResult);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRpmResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rpmResultDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmResult in the database
        List<RpmResult> rpmResultList = rpmResultRepository.findAll();
        assertThat(rpmResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRpmResult() throws Exception {
        int databaseSizeBeforeUpdate = rpmResultRepository.findAll().size();
        rpmResult.setId(count.incrementAndGet());

        // Create the RpmResult
        RpmResultDTO rpmResultDTO = rpmResultMapper.toDto(rpmResult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmResult in the database
        List<RpmResult> rpmResultList = rpmResultRepository.findAll();
        assertThat(rpmResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRpmResult() throws Exception {
        int databaseSizeBeforeUpdate = rpmResultRepository.findAll().size();
        rpmResult.setId(count.incrementAndGet());

        // Create the RpmResult
        RpmResultDTO rpmResultDTO = rpmResultMapper.toDto(rpmResult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmResultMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmResultDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RpmResult in the database
        List<RpmResult> rpmResultList = rpmResultRepository.findAll();
        assertThat(rpmResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRpmResultWithPatch() throws Exception {
        // Initialize the database
        rpmResultRepository.saveAndFlush(rpmResult);

        int databaseSizeBeforeUpdate = rpmResultRepository.findAll().size();

        // Update the rpmResult using partial update
        RpmResult partialUpdatedRpmResult = new RpmResult();
        partialUpdatedRpmResult.setId(rpmResult.getId());

        restRpmResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRpmResult.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRpmResult))
            )
            .andExpect(status().isOk());

        // Validate the RpmResult in the database
        List<RpmResult> rpmResultList = rpmResultRepository.findAll();
        assertThat(rpmResultList).hasSize(databaseSizeBeforeUpdate);
        RpmResult testRpmResult = rpmResultList.get(rpmResultList.size() - 1);
        assertThat(testRpmResult.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateRpmResultWithPatch() throws Exception {
        // Initialize the database
        rpmResultRepository.saveAndFlush(rpmResult);

        int databaseSizeBeforeUpdate = rpmResultRepository.findAll().size();

        // Update the rpmResult using partial update
        RpmResult partialUpdatedRpmResult = new RpmResult();
        partialUpdatedRpmResult.setId(rpmResult.getId());

        partialUpdatedRpmResult.name(UPDATED_NAME);

        restRpmResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRpmResult.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRpmResult))
            )
            .andExpect(status().isOk());

        // Validate the RpmResult in the database
        List<RpmResult> rpmResultList = rpmResultRepository.findAll();
        assertThat(rpmResultList).hasSize(databaseSizeBeforeUpdate);
        RpmResult testRpmResult = rpmResultList.get(rpmResultList.size() - 1);
        assertThat(testRpmResult.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingRpmResult() throws Exception {
        int databaseSizeBeforeUpdate = rpmResultRepository.findAll().size();
        rpmResult.setId(count.incrementAndGet());

        // Create the RpmResult
        RpmResultDTO rpmResultDTO = rpmResultMapper.toDto(rpmResult);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRpmResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rpmResultDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rpmResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmResult in the database
        List<RpmResult> rpmResultList = rpmResultRepository.findAll();
        assertThat(rpmResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRpmResult() throws Exception {
        int databaseSizeBeforeUpdate = rpmResultRepository.findAll().size();
        rpmResult.setId(count.incrementAndGet());

        // Create the RpmResult
        RpmResultDTO rpmResultDTO = rpmResultMapper.toDto(rpmResult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rpmResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmResult in the database
        List<RpmResult> rpmResultList = rpmResultRepository.findAll();
        assertThat(rpmResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRpmResult() throws Exception {
        int databaseSizeBeforeUpdate = rpmResultRepository.findAll().size();
        rpmResult.setId(count.incrementAndGet());

        // Create the RpmResult
        RpmResultDTO rpmResultDTO = rpmResultMapper.toDto(rpmResult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmResultMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rpmResultDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RpmResult in the database
        List<RpmResult> rpmResultList = rpmResultRepository.findAll();
        assertThat(rpmResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRpmResult() throws Exception {
        // Initialize the database
        rpmResultRepository.saveAndFlush(rpmResult);

        int databaseSizeBeforeDelete = rpmResultRepository.findAll().size();

        // Delete the rpmResult
        restRpmResultMockMvc
            .perform(delete(ENTITY_API_URL_ID, rpmResult.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RpmResult> rpmResultList = rpmResultRepository.findAll();
        assertThat(rpmResultList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
