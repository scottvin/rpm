package com.github.scottvin.rpm.web.rest;

import static com.github.scottvin.rpm.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.github.scottvin.rpm.IntegrationTest;
import com.github.scottvin.rpm.domain.RpmProject;
import com.github.scottvin.rpm.repository.RpmProjectRepository;
import com.github.scottvin.rpm.service.criteria.RpmProjectCriteria;
import com.github.scottvin.rpm.service.dto.RpmProjectDTO;
import com.github.scottvin.rpm.service.mapper.RpmProjectMapper;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link RpmProjectResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RpmProjectResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Duration DEFAULT_DURATION = Duration.ofHours(6);
    private static final Duration UPDATED_DURATION = Duration.ofHours(12);
    private static final Duration SMALLER_DURATION = Duration.ofHours(5);

    private static final String ENTITY_API_URL = "/api/rpm-projects";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RpmProjectRepository rpmProjectRepository;

    @Autowired
    private RpmProjectMapper rpmProjectMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRpmProjectMockMvc;

    private RpmProject rpmProject;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RpmProject createEntity(EntityManager em) {
        RpmProject rpmProject = new RpmProject().name(DEFAULT_NAME).dateTime(DEFAULT_DATE_TIME).duration(DEFAULT_DURATION);
        return rpmProject;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RpmProject createUpdatedEntity(EntityManager em) {
        RpmProject rpmProject = new RpmProject().name(UPDATED_NAME).dateTime(UPDATED_DATE_TIME).duration(UPDATED_DURATION);
        return rpmProject;
    }

    @BeforeEach
    public void initTest() {
        rpmProject = createEntity(em);
    }

    @Test
    @Transactional
    void createRpmProject() throws Exception {
        int databaseSizeBeforeCreate = rpmProjectRepository.findAll().size();
        // Create the RpmProject
        RpmProjectDTO rpmProjectDTO = rpmProjectMapper.toDto(rpmProject);
        restRpmProjectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmProjectDTO)))
            .andExpect(status().isCreated());

        // Validate the RpmProject in the database
        List<RpmProject> rpmProjectList = rpmProjectRepository.findAll();
        assertThat(rpmProjectList).hasSize(databaseSizeBeforeCreate + 1);
        RpmProject testRpmProject = rpmProjectList.get(rpmProjectList.size() - 1);
        assertThat(testRpmProject.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRpmProject.getDateTime()).isEqualTo(DEFAULT_DATE_TIME);
        assertThat(testRpmProject.getDuration()).isEqualTo(DEFAULT_DURATION);
    }

    @Test
    @Transactional
    void createRpmProjectWithExistingId() throws Exception {
        // Create the RpmProject with an existing ID
        rpmProject.setId(1L);
        RpmProjectDTO rpmProjectDTO = rpmProjectMapper.toDto(rpmProject);

        int databaseSizeBeforeCreate = rpmProjectRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRpmProjectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmProjectDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RpmProject in the database
        List<RpmProject> rpmProjectList = rpmProjectRepository.findAll();
        assertThat(rpmProjectList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = rpmProjectRepository.findAll().size();
        // set the field null
        rpmProject.setName(null);

        // Create the RpmProject, which fails.
        RpmProjectDTO rpmProjectDTO = rpmProjectMapper.toDto(rpmProject);

        restRpmProjectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmProjectDTO)))
            .andExpect(status().isBadRequest());

        List<RpmProject> rpmProjectList = rpmProjectRepository.findAll();
        assertThat(rpmProjectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = rpmProjectRepository.findAll().size();
        // set the field null
        rpmProject.setDateTime(null);

        // Create the RpmProject, which fails.
        RpmProjectDTO rpmProjectDTO = rpmProjectMapper.toDto(rpmProject);

        restRpmProjectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmProjectDTO)))
            .andExpect(status().isBadRequest());

        List<RpmProject> rpmProjectList = rpmProjectRepository.findAll();
        assertThat(rpmProjectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDurationIsRequired() throws Exception {
        int databaseSizeBeforeTest = rpmProjectRepository.findAll().size();
        // set the field null
        rpmProject.setDuration(null);

        // Create the RpmProject, which fails.
        RpmProjectDTO rpmProjectDTO = rpmProjectMapper.toDto(rpmProject);

        restRpmProjectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmProjectDTO)))
            .andExpect(status().isBadRequest());

        List<RpmProject> rpmProjectList = rpmProjectRepository.findAll();
        assertThat(rpmProjectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRpmProjects() throws Exception {
        // Initialize the database
        rpmProjectRepository.saveAndFlush(rpmProject);

        // Get all the rpmProjectList
        restRpmProjectMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rpmProject.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dateTime").value(hasItem(sameInstant(DEFAULT_DATE_TIME))))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.toString())));
    }

    @Test
    @Transactional
    void getRpmProject() throws Exception {
        // Initialize the database
        rpmProjectRepository.saveAndFlush(rpmProject);

        // Get the rpmProject
        restRpmProjectMockMvc
            .perform(get(ENTITY_API_URL_ID, rpmProject.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rpmProject.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.dateTime").value(sameInstant(DEFAULT_DATE_TIME)))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION.toString()));
    }

    @Test
    @Transactional
    void getRpmProjectsByIdFiltering() throws Exception {
        // Initialize the database
        rpmProjectRepository.saveAndFlush(rpmProject);

        Long id = rpmProject.getId();

        defaultRpmProjectShouldBeFound("id.equals=" + id);
        defaultRpmProjectShouldNotBeFound("id.notEquals=" + id);

        defaultRpmProjectShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRpmProjectShouldNotBeFound("id.greaterThan=" + id);

        defaultRpmProjectShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRpmProjectShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRpmProjectsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        rpmProjectRepository.saveAndFlush(rpmProject);

        // Get all the rpmProjectList where name equals to DEFAULT_NAME
        defaultRpmProjectShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the rpmProjectList where name equals to UPDATED_NAME
        defaultRpmProjectShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRpmProjectsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rpmProjectRepository.saveAndFlush(rpmProject);

        // Get all the rpmProjectList where name not equals to DEFAULT_NAME
        defaultRpmProjectShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the rpmProjectList where name not equals to UPDATED_NAME
        defaultRpmProjectShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRpmProjectsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        rpmProjectRepository.saveAndFlush(rpmProject);

        // Get all the rpmProjectList where name in DEFAULT_NAME or UPDATED_NAME
        defaultRpmProjectShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the rpmProjectList where name equals to UPDATED_NAME
        defaultRpmProjectShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRpmProjectsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        rpmProjectRepository.saveAndFlush(rpmProject);

        // Get all the rpmProjectList where name is not null
        defaultRpmProjectShouldBeFound("name.specified=true");

        // Get all the rpmProjectList where name is null
        defaultRpmProjectShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllRpmProjectsByNameContainsSomething() throws Exception {
        // Initialize the database
        rpmProjectRepository.saveAndFlush(rpmProject);

        // Get all the rpmProjectList where name contains DEFAULT_NAME
        defaultRpmProjectShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the rpmProjectList where name contains UPDATED_NAME
        defaultRpmProjectShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRpmProjectsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        rpmProjectRepository.saveAndFlush(rpmProject);

        // Get all the rpmProjectList where name does not contain DEFAULT_NAME
        defaultRpmProjectShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the rpmProjectList where name does not contain UPDATED_NAME
        defaultRpmProjectShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRpmProjectsByDateTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        rpmProjectRepository.saveAndFlush(rpmProject);

        // Get all the rpmProjectList where dateTime equals to DEFAULT_DATE_TIME
        defaultRpmProjectShouldBeFound("dateTime.equals=" + DEFAULT_DATE_TIME);

        // Get all the rpmProjectList where dateTime equals to UPDATED_DATE_TIME
        defaultRpmProjectShouldNotBeFound("dateTime.equals=" + UPDATED_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllRpmProjectsByDateTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rpmProjectRepository.saveAndFlush(rpmProject);

        // Get all the rpmProjectList where dateTime not equals to DEFAULT_DATE_TIME
        defaultRpmProjectShouldNotBeFound("dateTime.notEquals=" + DEFAULT_DATE_TIME);

        // Get all the rpmProjectList where dateTime not equals to UPDATED_DATE_TIME
        defaultRpmProjectShouldBeFound("dateTime.notEquals=" + UPDATED_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllRpmProjectsByDateTimeIsInShouldWork() throws Exception {
        // Initialize the database
        rpmProjectRepository.saveAndFlush(rpmProject);

        // Get all the rpmProjectList where dateTime in DEFAULT_DATE_TIME or UPDATED_DATE_TIME
        defaultRpmProjectShouldBeFound("dateTime.in=" + DEFAULT_DATE_TIME + "," + UPDATED_DATE_TIME);

        // Get all the rpmProjectList where dateTime equals to UPDATED_DATE_TIME
        defaultRpmProjectShouldNotBeFound("dateTime.in=" + UPDATED_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllRpmProjectsByDateTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        rpmProjectRepository.saveAndFlush(rpmProject);

        // Get all the rpmProjectList where dateTime is not null
        defaultRpmProjectShouldBeFound("dateTime.specified=true");

        // Get all the rpmProjectList where dateTime is null
        defaultRpmProjectShouldNotBeFound("dateTime.specified=false");
    }

    @Test
    @Transactional
    void getAllRpmProjectsByDateTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rpmProjectRepository.saveAndFlush(rpmProject);

        // Get all the rpmProjectList where dateTime is greater than or equal to DEFAULT_DATE_TIME
        defaultRpmProjectShouldBeFound("dateTime.greaterThanOrEqual=" + DEFAULT_DATE_TIME);

        // Get all the rpmProjectList where dateTime is greater than or equal to UPDATED_DATE_TIME
        defaultRpmProjectShouldNotBeFound("dateTime.greaterThanOrEqual=" + UPDATED_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllRpmProjectsByDateTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rpmProjectRepository.saveAndFlush(rpmProject);

        // Get all the rpmProjectList where dateTime is less than or equal to DEFAULT_DATE_TIME
        defaultRpmProjectShouldBeFound("dateTime.lessThanOrEqual=" + DEFAULT_DATE_TIME);

        // Get all the rpmProjectList where dateTime is less than or equal to SMALLER_DATE_TIME
        defaultRpmProjectShouldNotBeFound("dateTime.lessThanOrEqual=" + SMALLER_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllRpmProjectsByDateTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        rpmProjectRepository.saveAndFlush(rpmProject);

        // Get all the rpmProjectList where dateTime is less than DEFAULT_DATE_TIME
        defaultRpmProjectShouldNotBeFound("dateTime.lessThan=" + DEFAULT_DATE_TIME);

        // Get all the rpmProjectList where dateTime is less than UPDATED_DATE_TIME
        defaultRpmProjectShouldBeFound("dateTime.lessThan=" + UPDATED_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllRpmProjectsByDateTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rpmProjectRepository.saveAndFlush(rpmProject);

        // Get all the rpmProjectList where dateTime is greater than DEFAULT_DATE_TIME
        defaultRpmProjectShouldNotBeFound("dateTime.greaterThan=" + DEFAULT_DATE_TIME);

        // Get all the rpmProjectList where dateTime is greater than SMALLER_DATE_TIME
        defaultRpmProjectShouldBeFound("dateTime.greaterThan=" + SMALLER_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllRpmProjectsByDurationIsEqualToSomething() throws Exception {
        // Initialize the database
        rpmProjectRepository.saveAndFlush(rpmProject);

        // Get all the rpmProjectList where duration equals to DEFAULT_DURATION
        defaultRpmProjectShouldBeFound("duration.equals=" + DEFAULT_DURATION);

        // Get all the rpmProjectList where duration equals to UPDATED_DURATION
        defaultRpmProjectShouldNotBeFound("duration.equals=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    void getAllRpmProjectsByDurationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rpmProjectRepository.saveAndFlush(rpmProject);

        // Get all the rpmProjectList where duration not equals to DEFAULT_DURATION
        defaultRpmProjectShouldNotBeFound("duration.notEquals=" + DEFAULT_DURATION);

        // Get all the rpmProjectList where duration not equals to UPDATED_DURATION
        defaultRpmProjectShouldBeFound("duration.notEquals=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    void getAllRpmProjectsByDurationIsInShouldWork() throws Exception {
        // Initialize the database
        rpmProjectRepository.saveAndFlush(rpmProject);

        // Get all the rpmProjectList where duration in DEFAULT_DURATION or UPDATED_DURATION
        defaultRpmProjectShouldBeFound("duration.in=" + DEFAULT_DURATION + "," + UPDATED_DURATION);

        // Get all the rpmProjectList where duration equals to UPDATED_DURATION
        defaultRpmProjectShouldNotBeFound("duration.in=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    void getAllRpmProjectsByDurationIsNullOrNotNull() throws Exception {
        // Initialize the database
        rpmProjectRepository.saveAndFlush(rpmProject);

        // Get all the rpmProjectList where duration is not null
        defaultRpmProjectShouldBeFound("duration.specified=true");

        // Get all the rpmProjectList where duration is null
        defaultRpmProjectShouldNotBeFound("duration.specified=false");
    }

    @Test
    @Transactional
    void getAllRpmProjectsByDurationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rpmProjectRepository.saveAndFlush(rpmProject);

        // Get all the rpmProjectList where duration is greater than or equal to DEFAULT_DURATION
        defaultRpmProjectShouldBeFound("duration.greaterThanOrEqual=" + DEFAULT_DURATION);

        // Get all the rpmProjectList where duration is greater than or equal to UPDATED_DURATION
        defaultRpmProjectShouldNotBeFound("duration.greaterThanOrEqual=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    void getAllRpmProjectsByDurationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rpmProjectRepository.saveAndFlush(rpmProject);

        // Get all the rpmProjectList where duration is less than or equal to DEFAULT_DURATION
        defaultRpmProjectShouldBeFound("duration.lessThanOrEqual=" + DEFAULT_DURATION);

        // Get all the rpmProjectList where duration is less than or equal to SMALLER_DURATION
        defaultRpmProjectShouldNotBeFound("duration.lessThanOrEqual=" + SMALLER_DURATION);
    }

    @Test
    @Transactional
    void getAllRpmProjectsByDurationIsLessThanSomething() throws Exception {
        // Initialize the database
        rpmProjectRepository.saveAndFlush(rpmProject);

        // Get all the rpmProjectList where duration is less than DEFAULT_DURATION
        defaultRpmProjectShouldNotBeFound("duration.lessThan=" + DEFAULT_DURATION);

        // Get all the rpmProjectList where duration is less than UPDATED_DURATION
        defaultRpmProjectShouldBeFound("duration.lessThan=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    void getAllRpmProjectsByDurationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rpmProjectRepository.saveAndFlush(rpmProject);

        // Get all the rpmProjectList where duration is greater than DEFAULT_DURATION
        defaultRpmProjectShouldNotBeFound("duration.greaterThan=" + DEFAULT_DURATION);

        // Get all the rpmProjectList where duration is greater than SMALLER_DURATION
        defaultRpmProjectShouldBeFound("duration.greaterThan=" + SMALLER_DURATION);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRpmProjectShouldBeFound(String filter) throws Exception {
        restRpmProjectMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rpmProject.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dateTime").value(hasItem(sameInstant(DEFAULT_DATE_TIME))))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.toString())));

        // Check, that the count call also returns 1
        restRpmProjectMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRpmProjectShouldNotBeFound(String filter) throws Exception {
        restRpmProjectMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRpmProjectMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRpmProject() throws Exception {
        // Get the rpmProject
        restRpmProjectMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRpmProject() throws Exception {
        // Initialize the database
        rpmProjectRepository.saveAndFlush(rpmProject);

        int databaseSizeBeforeUpdate = rpmProjectRepository.findAll().size();

        // Update the rpmProject
        RpmProject updatedRpmProject = rpmProjectRepository.findById(rpmProject.getId()).get();
        // Disconnect from session so that the updates on updatedRpmProject are not directly saved in db
        em.detach(updatedRpmProject);
        updatedRpmProject.name(UPDATED_NAME).dateTime(UPDATED_DATE_TIME).duration(UPDATED_DURATION);
        RpmProjectDTO rpmProjectDTO = rpmProjectMapper.toDto(updatedRpmProject);

        restRpmProjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rpmProjectDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmProjectDTO))
            )
            .andExpect(status().isOk());

        // Validate the RpmProject in the database
        List<RpmProject> rpmProjectList = rpmProjectRepository.findAll();
        assertThat(rpmProjectList).hasSize(databaseSizeBeforeUpdate);
        RpmProject testRpmProject = rpmProjectList.get(rpmProjectList.size() - 1);
        assertThat(testRpmProject.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRpmProject.getDateTime()).isEqualTo(UPDATED_DATE_TIME);
        assertThat(testRpmProject.getDuration()).isEqualTo(UPDATED_DURATION);
    }

    @Test
    @Transactional
    void putNonExistingRpmProject() throws Exception {
        int databaseSizeBeforeUpdate = rpmProjectRepository.findAll().size();
        rpmProject.setId(count.incrementAndGet());

        // Create the RpmProject
        RpmProjectDTO rpmProjectDTO = rpmProjectMapper.toDto(rpmProject);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRpmProjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rpmProjectDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmProjectDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmProject in the database
        List<RpmProject> rpmProjectList = rpmProjectRepository.findAll();
        assertThat(rpmProjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRpmProject() throws Exception {
        int databaseSizeBeforeUpdate = rpmProjectRepository.findAll().size();
        rpmProject.setId(count.incrementAndGet());

        // Create the RpmProject
        RpmProjectDTO rpmProjectDTO = rpmProjectMapper.toDto(rpmProject);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmProjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmProjectDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmProject in the database
        List<RpmProject> rpmProjectList = rpmProjectRepository.findAll();
        assertThat(rpmProjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRpmProject() throws Exception {
        int databaseSizeBeforeUpdate = rpmProjectRepository.findAll().size();
        rpmProject.setId(count.incrementAndGet());

        // Create the RpmProject
        RpmProjectDTO rpmProjectDTO = rpmProjectMapper.toDto(rpmProject);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmProjectMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmProjectDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RpmProject in the database
        List<RpmProject> rpmProjectList = rpmProjectRepository.findAll();
        assertThat(rpmProjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRpmProjectWithPatch() throws Exception {
        // Initialize the database
        rpmProjectRepository.saveAndFlush(rpmProject);

        int databaseSizeBeforeUpdate = rpmProjectRepository.findAll().size();

        // Update the rpmProject using partial update
        RpmProject partialUpdatedRpmProject = new RpmProject();
        partialUpdatedRpmProject.setId(rpmProject.getId());

        partialUpdatedRpmProject.duration(UPDATED_DURATION);

        restRpmProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRpmProject.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRpmProject))
            )
            .andExpect(status().isOk());

        // Validate the RpmProject in the database
        List<RpmProject> rpmProjectList = rpmProjectRepository.findAll();
        assertThat(rpmProjectList).hasSize(databaseSizeBeforeUpdate);
        RpmProject testRpmProject = rpmProjectList.get(rpmProjectList.size() - 1);
        assertThat(testRpmProject.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRpmProject.getDateTime()).isEqualTo(DEFAULT_DATE_TIME);
        assertThat(testRpmProject.getDuration()).isEqualTo(UPDATED_DURATION);
    }

    @Test
    @Transactional
    void fullUpdateRpmProjectWithPatch() throws Exception {
        // Initialize the database
        rpmProjectRepository.saveAndFlush(rpmProject);

        int databaseSizeBeforeUpdate = rpmProjectRepository.findAll().size();

        // Update the rpmProject using partial update
        RpmProject partialUpdatedRpmProject = new RpmProject();
        partialUpdatedRpmProject.setId(rpmProject.getId());

        partialUpdatedRpmProject.name(UPDATED_NAME).dateTime(UPDATED_DATE_TIME).duration(UPDATED_DURATION);

        restRpmProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRpmProject.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRpmProject))
            )
            .andExpect(status().isOk());

        // Validate the RpmProject in the database
        List<RpmProject> rpmProjectList = rpmProjectRepository.findAll();
        assertThat(rpmProjectList).hasSize(databaseSizeBeforeUpdate);
        RpmProject testRpmProject = rpmProjectList.get(rpmProjectList.size() - 1);
        assertThat(testRpmProject.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRpmProject.getDateTime()).isEqualTo(UPDATED_DATE_TIME);
        assertThat(testRpmProject.getDuration()).isEqualTo(UPDATED_DURATION);
    }

    @Test
    @Transactional
    void patchNonExistingRpmProject() throws Exception {
        int databaseSizeBeforeUpdate = rpmProjectRepository.findAll().size();
        rpmProject.setId(count.incrementAndGet());

        // Create the RpmProject
        RpmProjectDTO rpmProjectDTO = rpmProjectMapper.toDto(rpmProject);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRpmProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rpmProjectDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rpmProjectDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmProject in the database
        List<RpmProject> rpmProjectList = rpmProjectRepository.findAll();
        assertThat(rpmProjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRpmProject() throws Exception {
        int databaseSizeBeforeUpdate = rpmProjectRepository.findAll().size();
        rpmProject.setId(count.incrementAndGet());

        // Create the RpmProject
        RpmProjectDTO rpmProjectDTO = rpmProjectMapper.toDto(rpmProject);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rpmProjectDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmProject in the database
        List<RpmProject> rpmProjectList = rpmProjectRepository.findAll();
        assertThat(rpmProjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRpmProject() throws Exception {
        int databaseSizeBeforeUpdate = rpmProjectRepository.findAll().size();
        rpmProject.setId(count.incrementAndGet());

        // Create the RpmProject
        RpmProjectDTO rpmProjectDTO = rpmProjectMapper.toDto(rpmProject);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmProjectMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rpmProjectDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RpmProject in the database
        List<RpmProject> rpmProjectList = rpmProjectRepository.findAll();
        assertThat(rpmProjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRpmProject() throws Exception {
        // Initialize the database
        rpmProjectRepository.saveAndFlush(rpmProject);

        int databaseSizeBeforeDelete = rpmProjectRepository.findAll().size();

        // Delete the rpmProject
        restRpmProjectMockMvc
            .perform(delete(ENTITY_API_URL_ID, rpmProject.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RpmProject> rpmProjectList = rpmProjectRepository.findAll();
        assertThat(rpmProjectList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
