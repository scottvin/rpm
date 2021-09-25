package com.github.scottvin.rpm.web.rest;

import static com.github.scottvin.rpm.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.github.scottvin.rpm.IntegrationTest;
import com.github.scottvin.rpm.domain.RpmPlan;
import com.github.scottvin.rpm.domain.RpmProject;
import com.github.scottvin.rpm.repository.RpmPlanRepository;
import com.github.scottvin.rpm.service.dto.RpmPlanDTO;
import com.github.scottvin.rpm.service.mapper.RpmPlanMapper;
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
 * Integration tests for the {@link RpmPlanResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RpmPlanResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Duration DEFAULT_DURATION = Duration.ofHours(6);
    private static final Duration UPDATED_DURATION = Duration.ofHours(12);

    private static final String ENTITY_API_URL = "/api/rpm-plans";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RpmPlanRepository rpmPlanRepository;

    @Autowired
    private RpmPlanMapper rpmPlanMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRpmPlanMockMvc;

    private RpmPlan rpmPlan;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RpmPlan createEntity(EntityManager em) {
        RpmPlan rpmPlan = new RpmPlan().name(DEFAULT_NAME).dateTime(DEFAULT_DATE_TIME).duration(DEFAULT_DURATION);
        // Add required entity
        RpmProject rpmProject;
        if (TestUtil.findAll(em, RpmProject.class).isEmpty()) {
            rpmProject = RpmProjectResourceIT.createEntity(em);
            em.persist(rpmProject);
            em.flush();
        } else {
            rpmProject = TestUtil.findAll(em, RpmProject.class).get(0);
        }
        rpmPlan.setProject(rpmProject);
        return rpmPlan;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RpmPlan createUpdatedEntity(EntityManager em) {
        RpmPlan rpmPlan = new RpmPlan().name(UPDATED_NAME).dateTime(UPDATED_DATE_TIME).duration(UPDATED_DURATION);
        // Add required entity
        RpmProject rpmProject;
        if (TestUtil.findAll(em, RpmProject.class).isEmpty()) {
            rpmProject = RpmProjectResourceIT.createUpdatedEntity(em);
            em.persist(rpmProject);
            em.flush();
        } else {
            rpmProject = TestUtil.findAll(em, RpmProject.class).get(0);
        }
        rpmPlan.setProject(rpmProject);
        return rpmPlan;
    }

    @BeforeEach
    public void initTest() {
        rpmPlan = createEntity(em);
    }

    @Test
    @Transactional
    void createRpmPlan() throws Exception {
        int databaseSizeBeforeCreate = rpmPlanRepository.findAll().size();
        // Create the RpmPlan
        RpmPlanDTO rpmPlanDTO = rpmPlanMapper.toDto(rpmPlan);
        restRpmPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmPlanDTO)))
            .andExpect(status().isCreated());

        // Validate the RpmPlan in the database
        List<RpmPlan> rpmPlanList = rpmPlanRepository.findAll();
        assertThat(rpmPlanList).hasSize(databaseSizeBeforeCreate + 1);
        RpmPlan testRpmPlan = rpmPlanList.get(rpmPlanList.size() - 1);
        assertThat(testRpmPlan.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRpmPlan.getDateTime()).isEqualTo(DEFAULT_DATE_TIME);
        assertThat(testRpmPlan.getDuration()).isEqualTo(DEFAULT_DURATION);
    }

    @Test
    @Transactional
    void createRpmPlanWithExistingId() throws Exception {
        // Create the RpmPlan with an existing ID
        rpmPlan.setId(1L);
        RpmPlanDTO rpmPlanDTO = rpmPlanMapper.toDto(rpmPlan);

        int databaseSizeBeforeCreate = rpmPlanRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRpmPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmPlanDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RpmPlan in the database
        List<RpmPlan> rpmPlanList = rpmPlanRepository.findAll();
        assertThat(rpmPlanList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = rpmPlanRepository.findAll().size();
        // set the field null
        rpmPlan.setName(null);

        // Create the RpmPlan, which fails.
        RpmPlanDTO rpmPlanDTO = rpmPlanMapper.toDto(rpmPlan);

        restRpmPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmPlanDTO)))
            .andExpect(status().isBadRequest());

        List<RpmPlan> rpmPlanList = rpmPlanRepository.findAll();
        assertThat(rpmPlanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = rpmPlanRepository.findAll().size();
        // set the field null
        rpmPlan.setDateTime(null);

        // Create the RpmPlan, which fails.
        RpmPlanDTO rpmPlanDTO = rpmPlanMapper.toDto(rpmPlan);

        restRpmPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmPlanDTO)))
            .andExpect(status().isBadRequest());

        List<RpmPlan> rpmPlanList = rpmPlanRepository.findAll();
        assertThat(rpmPlanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDurationIsRequired() throws Exception {
        int databaseSizeBeforeTest = rpmPlanRepository.findAll().size();
        // set the field null
        rpmPlan.setDuration(null);

        // Create the RpmPlan, which fails.
        RpmPlanDTO rpmPlanDTO = rpmPlanMapper.toDto(rpmPlan);

        restRpmPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmPlanDTO)))
            .andExpect(status().isBadRequest());

        List<RpmPlan> rpmPlanList = rpmPlanRepository.findAll();
        assertThat(rpmPlanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRpmPlans() throws Exception {
        // Initialize the database
        rpmPlanRepository.saveAndFlush(rpmPlan);

        // Get all the rpmPlanList
        restRpmPlanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rpmPlan.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dateTime").value(hasItem(sameInstant(DEFAULT_DATE_TIME))))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.toString())));
    }

    @Test
    @Transactional
    void getRpmPlan() throws Exception {
        // Initialize the database
        rpmPlanRepository.saveAndFlush(rpmPlan);

        // Get the rpmPlan
        restRpmPlanMockMvc
            .perform(get(ENTITY_API_URL_ID, rpmPlan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rpmPlan.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.dateTime").value(sameInstant(DEFAULT_DATE_TIME)))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRpmPlan() throws Exception {
        // Get the rpmPlan
        restRpmPlanMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRpmPlan() throws Exception {
        // Initialize the database
        rpmPlanRepository.saveAndFlush(rpmPlan);

        int databaseSizeBeforeUpdate = rpmPlanRepository.findAll().size();

        // Update the rpmPlan
        RpmPlan updatedRpmPlan = rpmPlanRepository.findById(rpmPlan.getId()).get();
        // Disconnect from session so that the updates on updatedRpmPlan are not directly saved in db
        em.detach(updatedRpmPlan);
        updatedRpmPlan.name(UPDATED_NAME).dateTime(UPDATED_DATE_TIME).duration(UPDATED_DURATION);
        RpmPlanDTO rpmPlanDTO = rpmPlanMapper.toDto(updatedRpmPlan);

        restRpmPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rpmPlanDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmPlanDTO))
            )
            .andExpect(status().isOk());

        // Validate the RpmPlan in the database
        List<RpmPlan> rpmPlanList = rpmPlanRepository.findAll();
        assertThat(rpmPlanList).hasSize(databaseSizeBeforeUpdate);
        RpmPlan testRpmPlan = rpmPlanList.get(rpmPlanList.size() - 1);
        assertThat(testRpmPlan.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRpmPlan.getDateTime()).isEqualTo(UPDATED_DATE_TIME);
        assertThat(testRpmPlan.getDuration()).isEqualTo(UPDATED_DURATION);
    }

    @Test
    @Transactional
    void putNonExistingRpmPlan() throws Exception {
        int databaseSizeBeforeUpdate = rpmPlanRepository.findAll().size();
        rpmPlan.setId(count.incrementAndGet());

        // Create the RpmPlan
        RpmPlanDTO rpmPlanDTO = rpmPlanMapper.toDto(rpmPlan);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRpmPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rpmPlanDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmPlan in the database
        List<RpmPlan> rpmPlanList = rpmPlanRepository.findAll();
        assertThat(rpmPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRpmPlan() throws Exception {
        int databaseSizeBeforeUpdate = rpmPlanRepository.findAll().size();
        rpmPlan.setId(count.incrementAndGet());

        // Create the RpmPlan
        RpmPlanDTO rpmPlanDTO = rpmPlanMapper.toDto(rpmPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmPlan in the database
        List<RpmPlan> rpmPlanList = rpmPlanRepository.findAll();
        assertThat(rpmPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRpmPlan() throws Exception {
        int databaseSizeBeforeUpdate = rpmPlanRepository.findAll().size();
        rpmPlan.setId(count.incrementAndGet());

        // Create the RpmPlan
        RpmPlanDTO rpmPlanDTO = rpmPlanMapper.toDto(rpmPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmPlanMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmPlanDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RpmPlan in the database
        List<RpmPlan> rpmPlanList = rpmPlanRepository.findAll();
        assertThat(rpmPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRpmPlanWithPatch() throws Exception {
        // Initialize the database
        rpmPlanRepository.saveAndFlush(rpmPlan);

        int databaseSizeBeforeUpdate = rpmPlanRepository.findAll().size();

        // Update the rpmPlan using partial update
        RpmPlan partialUpdatedRpmPlan = new RpmPlan();
        partialUpdatedRpmPlan.setId(rpmPlan.getId());

        partialUpdatedRpmPlan.name(UPDATED_NAME).dateTime(UPDATED_DATE_TIME);

        restRpmPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRpmPlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRpmPlan))
            )
            .andExpect(status().isOk());

        // Validate the RpmPlan in the database
        List<RpmPlan> rpmPlanList = rpmPlanRepository.findAll();
        assertThat(rpmPlanList).hasSize(databaseSizeBeforeUpdate);
        RpmPlan testRpmPlan = rpmPlanList.get(rpmPlanList.size() - 1);
        assertThat(testRpmPlan.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRpmPlan.getDateTime()).isEqualTo(UPDATED_DATE_TIME);
        assertThat(testRpmPlan.getDuration()).isEqualTo(DEFAULT_DURATION);
    }

    @Test
    @Transactional
    void fullUpdateRpmPlanWithPatch() throws Exception {
        // Initialize the database
        rpmPlanRepository.saveAndFlush(rpmPlan);

        int databaseSizeBeforeUpdate = rpmPlanRepository.findAll().size();

        // Update the rpmPlan using partial update
        RpmPlan partialUpdatedRpmPlan = new RpmPlan();
        partialUpdatedRpmPlan.setId(rpmPlan.getId());

        partialUpdatedRpmPlan.name(UPDATED_NAME).dateTime(UPDATED_DATE_TIME).duration(UPDATED_DURATION);

        restRpmPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRpmPlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRpmPlan))
            )
            .andExpect(status().isOk());

        // Validate the RpmPlan in the database
        List<RpmPlan> rpmPlanList = rpmPlanRepository.findAll();
        assertThat(rpmPlanList).hasSize(databaseSizeBeforeUpdate);
        RpmPlan testRpmPlan = rpmPlanList.get(rpmPlanList.size() - 1);
        assertThat(testRpmPlan.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRpmPlan.getDateTime()).isEqualTo(UPDATED_DATE_TIME);
        assertThat(testRpmPlan.getDuration()).isEqualTo(UPDATED_DURATION);
    }

    @Test
    @Transactional
    void patchNonExistingRpmPlan() throws Exception {
        int databaseSizeBeforeUpdate = rpmPlanRepository.findAll().size();
        rpmPlan.setId(count.incrementAndGet());

        // Create the RpmPlan
        RpmPlanDTO rpmPlanDTO = rpmPlanMapper.toDto(rpmPlan);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRpmPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rpmPlanDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rpmPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmPlan in the database
        List<RpmPlan> rpmPlanList = rpmPlanRepository.findAll();
        assertThat(rpmPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRpmPlan() throws Exception {
        int databaseSizeBeforeUpdate = rpmPlanRepository.findAll().size();
        rpmPlan.setId(count.incrementAndGet());

        // Create the RpmPlan
        RpmPlanDTO rpmPlanDTO = rpmPlanMapper.toDto(rpmPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rpmPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmPlan in the database
        List<RpmPlan> rpmPlanList = rpmPlanRepository.findAll();
        assertThat(rpmPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRpmPlan() throws Exception {
        int databaseSizeBeforeUpdate = rpmPlanRepository.findAll().size();
        rpmPlan.setId(count.incrementAndGet());

        // Create the RpmPlan
        RpmPlanDTO rpmPlanDTO = rpmPlanMapper.toDto(rpmPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmPlanMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rpmPlanDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RpmPlan in the database
        List<RpmPlan> rpmPlanList = rpmPlanRepository.findAll();
        assertThat(rpmPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRpmPlan() throws Exception {
        // Initialize the database
        rpmPlanRepository.saveAndFlush(rpmPlan);

        int databaseSizeBeforeDelete = rpmPlanRepository.findAll().size();

        // Delete the rpmPlan
        restRpmPlanMockMvc
            .perform(delete(ENTITY_API_URL_ID, rpmPlan.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RpmPlan> rpmPlanList = rpmPlanRepository.findAll();
        assertThat(rpmPlanList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
