package com.github.scottvin.rpm.web.rest;

import static com.github.scottvin.rpm.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.github.scottvin.rpm.IntegrationTest;
import com.github.scottvin.rpm.domain.RpmOutcome;
import com.github.scottvin.rpm.domain.RpmProject;
import com.github.scottvin.rpm.repository.RpmOutcomeRepository;
import com.github.scottvin.rpm.service.dto.RpmOutcomeDTO;
import com.github.scottvin.rpm.service.mapper.RpmOutcomeMapper;
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
 * Integration tests for the {@link RpmOutcomeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RpmOutcomeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Duration DEFAULT_DURATION = Duration.ofHours(6);
    private static final Duration UPDATED_DURATION = Duration.ofHours(12);

    private static final String ENTITY_API_URL = "/api/rpm-outcomes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RpmOutcomeRepository rpmOutcomeRepository;

    @Autowired
    private RpmOutcomeMapper rpmOutcomeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRpmOutcomeMockMvc;

    private RpmOutcome rpmOutcome;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RpmOutcome createEntity(EntityManager em) {
        RpmOutcome rpmOutcome = new RpmOutcome().name(DEFAULT_NAME).dateTime(DEFAULT_DATE_TIME).duration(DEFAULT_DURATION);
        // Add required entity
        RpmProject rpmProject;
        if (TestUtil.findAll(em, RpmProject.class).isEmpty()) {
            rpmProject = RpmProjectResourceIT.createEntity(em);
            em.persist(rpmProject);
            em.flush();
        } else {
            rpmProject = TestUtil.findAll(em, RpmProject.class).get(0);
        }
        rpmOutcome.setProject(rpmProject);
        return rpmOutcome;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RpmOutcome createUpdatedEntity(EntityManager em) {
        RpmOutcome rpmOutcome = new RpmOutcome().name(UPDATED_NAME).dateTime(UPDATED_DATE_TIME).duration(UPDATED_DURATION);
        // Add required entity
        RpmProject rpmProject;
        if (TestUtil.findAll(em, RpmProject.class).isEmpty()) {
            rpmProject = RpmProjectResourceIT.createUpdatedEntity(em);
            em.persist(rpmProject);
            em.flush();
        } else {
            rpmProject = TestUtil.findAll(em, RpmProject.class).get(0);
        }
        rpmOutcome.setProject(rpmProject);
        return rpmOutcome;
    }

    @BeforeEach
    public void initTest() {
        rpmOutcome = createEntity(em);
    }

    @Test
    @Transactional
    void createRpmOutcome() throws Exception {
        int databaseSizeBeforeCreate = rpmOutcomeRepository.findAll().size();
        // Create the RpmOutcome
        RpmOutcomeDTO rpmOutcomeDTO = rpmOutcomeMapper.toDto(rpmOutcome);
        restRpmOutcomeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmOutcomeDTO)))
            .andExpect(status().isCreated());

        // Validate the RpmOutcome in the database
        List<RpmOutcome> rpmOutcomeList = rpmOutcomeRepository.findAll();
        assertThat(rpmOutcomeList).hasSize(databaseSizeBeforeCreate + 1);
        RpmOutcome testRpmOutcome = rpmOutcomeList.get(rpmOutcomeList.size() - 1);
        assertThat(testRpmOutcome.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRpmOutcome.getDateTime()).isEqualTo(DEFAULT_DATE_TIME);
        assertThat(testRpmOutcome.getDuration()).isEqualTo(DEFAULT_DURATION);
    }

    @Test
    @Transactional
    void createRpmOutcomeWithExistingId() throws Exception {
        // Create the RpmOutcome with an existing ID
        rpmOutcome.setId(1L);
        RpmOutcomeDTO rpmOutcomeDTO = rpmOutcomeMapper.toDto(rpmOutcome);

        int databaseSizeBeforeCreate = rpmOutcomeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRpmOutcomeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmOutcomeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RpmOutcome in the database
        List<RpmOutcome> rpmOutcomeList = rpmOutcomeRepository.findAll();
        assertThat(rpmOutcomeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = rpmOutcomeRepository.findAll().size();
        // set the field null
        rpmOutcome.setName(null);

        // Create the RpmOutcome, which fails.
        RpmOutcomeDTO rpmOutcomeDTO = rpmOutcomeMapper.toDto(rpmOutcome);

        restRpmOutcomeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmOutcomeDTO)))
            .andExpect(status().isBadRequest());

        List<RpmOutcome> rpmOutcomeList = rpmOutcomeRepository.findAll();
        assertThat(rpmOutcomeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = rpmOutcomeRepository.findAll().size();
        // set the field null
        rpmOutcome.setDateTime(null);

        // Create the RpmOutcome, which fails.
        RpmOutcomeDTO rpmOutcomeDTO = rpmOutcomeMapper.toDto(rpmOutcome);

        restRpmOutcomeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmOutcomeDTO)))
            .andExpect(status().isBadRequest());

        List<RpmOutcome> rpmOutcomeList = rpmOutcomeRepository.findAll();
        assertThat(rpmOutcomeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDurationIsRequired() throws Exception {
        int databaseSizeBeforeTest = rpmOutcomeRepository.findAll().size();
        // set the field null
        rpmOutcome.setDuration(null);

        // Create the RpmOutcome, which fails.
        RpmOutcomeDTO rpmOutcomeDTO = rpmOutcomeMapper.toDto(rpmOutcome);

        restRpmOutcomeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmOutcomeDTO)))
            .andExpect(status().isBadRequest());

        List<RpmOutcome> rpmOutcomeList = rpmOutcomeRepository.findAll();
        assertThat(rpmOutcomeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRpmOutcomes() throws Exception {
        // Initialize the database
        rpmOutcomeRepository.saveAndFlush(rpmOutcome);

        // Get all the rpmOutcomeList
        restRpmOutcomeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rpmOutcome.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dateTime").value(hasItem(sameInstant(DEFAULT_DATE_TIME))))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.toString())));
    }

    @Test
    @Transactional
    void getRpmOutcome() throws Exception {
        // Initialize the database
        rpmOutcomeRepository.saveAndFlush(rpmOutcome);

        // Get the rpmOutcome
        restRpmOutcomeMockMvc
            .perform(get(ENTITY_API_URL_ID, rpmOutcome.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rpmOutcome.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.dateTime").value(sameInstant(DEFAULT_DATE_TIME)))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRpmOutcome() throws Exception {
        // Get the rpmOutcome
        restRpmOutcomeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRpmOutcome() throws Exception {
        // Initialize the database
        rpmOutcomeRepository.saveAndFlush(rpmOutcome);

        int databaseSizeBeforeUpdate = rpmOutcomeRepository.findAll().size();

        // Update the rpmOutcome
        RpmOutcome updatedRpmOutcome = rpmOutcomeRepository.findById(rpmOutcome.getId()).get();
        // Disconnect from session so that the updates on updatedRpmOutcome are not directly saved in db
        em.detach(updatedRpmOutcome);
        updatedRpmOutcome.name(UPDATED_NAME).dateTime(UPDATED_DATE_TIME).duration(UPDATED_DURATION);
        RpmOutcomeDTO rpmOutcomeDTO = rpmOutcomeMapper.toDto(updatedRpmOutcome);

        restRpmOutcomeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rpmOutcomeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmOutcomeDTO))
            )
            .andExpect(status().isOk());

        // Validate the RpmOutcome in the database
        List<RpmOutcome> rpmOutcomeList = rpmOutcomeRepository.findAll();
        assertThat(rpmOutcomeList).hasSize(databaseSizeBeforeUpdate);
        RpmOutcome testRpmOutcome = rpmOutcomeList.get(rpmOutcomeList.size() - 1);
        assertThat(testRpmOutcome.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRpmOutcome.getDateTime()).isEqualTo(UPDATED_DATE_TIME);
        assertThat(testRpmOutcome.getDuration()).isEqualTo(UPDATED_DURATION);
    }

    @Test
    @Transactional
    void putNonExistingRpmOutcome() throws Exception {
        int databaseSizeBeforeUpdate = rpmOutcomeRepository.findAll().size();
        rpmOutcome.setId(count.incrementAndGet());

        // Create the RpmOutcome
        RpmOutcomeDTO rpmOutcomeDTO = rpmOutcomeMapper.toDto(rpmOutcome);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRpmOutcomeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rpmOutcomeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmOutcomeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmOutcome in the database
        List<RpmOutcome> rpmOutcomeList = rpmOutcomeRepository.findAll();
        assertThat(rpmOutcomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRpmOutcome() throws Exception {
        int databaseSizeBeforeUpdate = rpmOutcomeRepository.findAll().size();
        rpmOutcome.setId(count.incrementAndGet());

        // Create the RpmOutcome
        RpmOutcomeDTO rpmOutcomeDTO = rpmOutcomeMapper.toDto(rpmOutcome);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmOutcomeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmOutcomeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmOutcome in the database
        List<RpmOutcome> rpmOutcomeList = rpmOutcomeRepository.findAll();
        assertThat(rpmOutcomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRpmOutcome() throws Exception {
        int databaseSizeBeforeUpdate = rpmOutcomeRepository.findAll().size();
        rpmOutcome.setId(count.incrementAndGet());

        // Create the RpmOutcome
        RpmOutcomeDTO rpmOutcomeDTO = rpmOutcomeMapper.toDto(rpmOutcome);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmOutcomeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmOutcomeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RpmOutcome in the database
        List<RpmOutcome> rpmOutcomeList = rpmOutcomeRepository.findAll();
        assertThat(rpmOutcomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRpmOutcomeWithPatch() throws Exception {
        // Initialize the database
        rpmOutcomeRepository.saveAndFlush(rpmOutcome);

        int databaseSizeBeforeUpdate = rpmOutcomeRepository.findAll().size();

        // Update the rpmOutcome using partial update
        RpmOutcome partialUpdatedRpmOutcome = new RpmOutcome();
        partialUpdatedRpmOutcome.setId(rpmOutcome.getId());

        partialUpdatedRpmOutcome.name(UPDATED_NAME).duration(UPDATED_DURATION);

        restRpmOutcomeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRpmOutcome.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRpmOutcome))
            )
            .andExpect(status().isOk());

        // Validate the RpmOutcome in the database
        List<RpmOutcome> rpmOutcomeList = rpmOutcomeRepository.findAll();
        assertThat(rpmOutcomeList).hasSize(databaseSizeBeforeUpdate);
        RpmOutcome testRpmOutcome = rpmOutcomeList.get(rpmOutcomeList.size() - 1);
        assertThat(testRpmOutcome.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRpmOutcome.getDateTime()).isEqualTo(DEFAULT_DATE_TIME);
        assertThat(testRpmOutcome.getDuration()).isEqualTo(UPDATED_DURATION);
    }

    @Test
    @Transactional
    void fullUpdateRpmOutcomeWithPatch() throws Exception {
        // Initialize the database
        rpmOutcomeRepository.saveAndFlush(rpmOutcome);

        int databaseSizeBeforeUpdate = rpmOutcomeRepository.findAll().size();

        // Update the rpmOutcome using partial update
        RpmOutcome partialUpdatedRpmOutcome = new RpmOutcome();
        partialUpdatedRpmOutcome.setId(rpmOutcome.getId());

        partialUpdatedRpmOutcome.name(UPDATED_NAME).dateTime(UPDATED_DATE_TIME).duration(UPDATED_DURATION);

        restRpmOutcomeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRpmOutcome.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRpmOutcome))
            )
            .andExpect(status().isOk());

        // Validate the RpmOutcome in the database
        List<RpmOutcome> rpmOutcomeList = rpmOutcomeRepository.findAll();
        assertThat(rpmOutcomeList).hasSize(databaseSizeBeforeUpdate);
        RpmOutcome testRpmOutcome = rpmOutcomeList.get(rpmOutcomeList.size() - 1);
        assertThat(testRpmOutcome.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRpmOutcome.getDateTime()).isEqualTo(UPDATED_DATE_TIME);
        assertThat(testRpmOutcome.getDuration()).isEqualTo(UPDATED_DURATION);
    }

    @Test
    @Transactional
    void patchNonExistingRpmOutcome() throws Exception {
        int databaseSizeBeforeUpdate = rpmOutcomeRepository.findAll().size();
        rpmOutcome.setId(count.incrementAndGet());

        // Create the RpmOutcome
        RpmOutcomeDTO rpmOutcomeDTO = rpmOutcomeMapper.toDto(rpmOutcome);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRpmOutcomeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rpmOutcomeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rpmOutcomeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmOutcome in the database
        List<RpmOutcome> rpmOutcomeList = rpmOutcomeRepository.findAll();
        assertThat(rpmOutcomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRpmOutcome() throws Exception {
        int databaseSizeBeforeUpdate = rpmOutcomeRepository.findAll().size();
        rpmOutcome.setId(count.incrementAndGet());

        // Create the RpmOutcome
        RpmOutcomeDTO rpmOutcomeDTO = rpmOutcomeMapper.toDto(rpmOutcome);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmOutcomeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rpmOutcomeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmOutcome in the database
        List<RpmOutcome> rpmOutcomeList = rpmOutcomeRepository.findAll();
        assertThat(rpmOutcomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRpmOutcome() throws Exception {
        int databaseSizeBeforeUpdate = rpmOutcomeRepository.findAll().size();
        rpmOutcome.setId(count.incrementAndGet());

        // Create the RpmOutcome
        RpmOutcomeDTO rpmOutcomeDTO = rpmOutcomeMapper.toDto(rpmOutcome);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmOutcomeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rpmOutcomeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RpmOutcome in the database
        List<RpmOutcome> rpmOutcomeList = rpmOutcomeRepository.findAll();
        assertThat(rpmOutcomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRpmOutcome() throws Exception {
        // Initialize the database
        rpmOutcomeRepository.saveAndFlush(rpmOutcome);

        int databaseSizeBeforeDelete = rpmOutcomeRepository.findAll().size();

        // Delete the rpmOutcome
        restRpmOutcomeMockMvc
            .perform(delete(ENTITY_API_URL_ID, rpmOutcome.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RpmOutcome> rpmOutcomeList = rpmOutcomeRepository.findAll();
        assertThat(rpmOutcomeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
