package com.github.scottvin.rpm.web.rest;

import static com.github.scottvin.rpm.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.github.scottvin.rpm.IntegrationTest;
import com.github.scottvin.rpm.domain.RpmAction;
import com.github.scottvin.rpm.domain.RpmCapture;
import com.github.scottvin.rpm.domain.RpmPlan;
import com.github.scottvin.rpm.domain.RpmProject;
import com.github.scottvin.rpm.domain.RpmReason;
import com.github.scottvin.rpm.domain.RpmResult;
import com.github.scottvin.rpm.repository.RpmActionRepository;
import com.github.scottvin.rpm.service.dto.RpmActionDTO;
import com.github.scottvin.rpm.service.mapper.RpmActionMapper;
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
 * Integration tests for the {@link RpmActionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RpmActionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRIORITY = 1;
    private static final Integer UPDATED_PRIORITY = 2;

    private static final ZonedDateTime DEFAULT_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Duration DEFAULT_DURATION = Duration.ofHours(6);
    private static final Duration UPDATED_DURATION = Duration.ofHours(12);

    private static final String ENTITY_API_URL = "/api/rpm-actions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RpmActionRepository rpmActionRepository;

    @Autowired
    private RpmActionMapper rpmActionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRpmActionMockMvc;

    private RpmAction rpmAction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RpmAction createEntity(EntityManager em) {
        RpmAction rpmAction = new RpmAction()
            .name(DEFAULT_NAME)
            .priority(DEFAULT_PRIORITY)
            .dateTime(DEFAULT_DATE_TIME)
            .duration(DEFAULT_DURATION);
        // Add required entity
        RpmPlan rpmPlan;
        if (TestUtil.findAll(em, RpmPlan.class).isEmpty()) {
            rpmPlan = RpmPlanResourceIT.createEntity(em);
            em.persist(rpmPlan);
            em.flush();
        } else {
            rpmPlan = TestUtil.findAll(em, RpmPlan.class).get(0);
        }
        rpmAction.setPlan(rpmPlan);
        // Add required entity
        RpmReason rpmReason;
        if (TestUtil.findAll(em, RpmReason.class).isEmpty()) {
            rpmReason = RpmReasonResourceIT.createEntity(em);
            em.persist(rpmReason);
            em.flush();
        } else {
            rpmReason = TestUtil.findAll(em, RpmReason.class).get(0);
        }
        rpmAction.setReason(rpmReason);
        // Add required entity
        RpmCapture rpmCapture;
        if (TestUtil.findAll(em, RpmCapture.class).isEmpty()) {
            rpmCapture = RpmCaptureResourceIT.createEntity(em);
            em.persist(rpmCapture);
            em.flush();
        } else {
            rpmCapture = TestUtil.findAll(em, RpmCapture.class).get(0);
        }
        rpmAction.setCaptures(rpmCapture);
        // Add required entity
        RpmResult rpmResult;
        if (TestUtil.findAll(em, RpmResult.class).isEmpty()) {
            rpmResult = RpmResultResourceIT.createEntity(em);
            em.persist(rpmResult);
            em.flush();
        } else {
            rpmResult = TestUtil.findAll(em, RpmResult.class).get(0);
        }
        rpmAction.setResult(rpmResult);
        // Add required entity
        RpmProject rpmProject;
        if (TestUtil.findAll(em, RpmProject.class).isEmpty()) {
            rpmProject = RpmProjectResourceIT.createEntity(em);
            em.persist(rpmProject);
            em.flush();
        } else {
            rpmProject = TestUtil.findAll(em, RpmProject.class).get(0);
        }
        rpmAction.setProject(rpmProject);
        return rpmAction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RpmAction createUpdatedEntity(EntityManager em) {
        RpmAction rpmAction = new RpmAction()
            .name(UPDATED_NAME)
            .priority(UPDATED_PRIORITY)
            .dateTime(UPDATED_DATE_TIME)
            .duration(UPDATED_DURATION);
        // Add required entity
        RpmPlan rpmPlan;
        if (TestUtil.findAll(em, RpmPlan.class).isEmpty()) {
            rpmPlan = RpmPlanResourceIT.createUpdatedEntity(em);
            em.persist(rpmPlan);
            em.flush();
        } else {
            rpmPlan = TestUtil.findAll(em, RpmPlan.class).get(0);
        }
        rpmAction.setPlan(rpmPlan);
        // Add required entity
        RpmReason rpmReason;
        if (TestUtil.findAll(em, RpmReason.class).isEmpty()) {
            rpmReason = RpmReasonResourceIT.createUpdatedEntity(em);
            em.persist(rpmReason);
            em.flush();
        } else {
            rpmReason = TestUtil.findAll(em, RpmReason.class).get(0);
        }
        rpmAction.setReason(rpmReason);
        // Add required entity
        RpmCapture rpmCapture;
        if (TestUtil.findAll(em, RpmCapture.class).isEmpty()) {
            rpmCapture = RpmCaptureResourceIT.createUpdatedEntity(em);
            em.persist(rpmCapture);
            em.flush();
        } else {
            rpmCapture = TestUtil.findAll(em, RpmCapture.class).get(0);
        }
        rpmAction.setCaptures(rpmCapture);
        // Add required entity
        RpmResult rpmResult;
        if (TestUtil.findAll(em, RpmResult.class).isEmpty()) {
            rpmResult = RpmResultResourceIT.createUpdatedEntity(em);
            em.persist(rpmResult);
            em.flush();
        } else {
            rpmResult = TestUtil.findAll(em, RpmResult.class).get(0);
        }
        rpmAction.setResult(rpmResult);
        // Add required entity
        RpmProject rpmProject;
        if (TestUtil.findAll(em, RpmProject.class).isEmpty()) {
            rpmProject = RpmProjectResourceIT.createUpdatedEntity(em);
            em.persist(rpmProject);
            em.flush();
        } else {
            rpmProject = TestUtil.findAll(em, RpmProject.class).get(0);
        }
        rpmAction.setProject(rpmProject);
        return rpmAction;
    }

    @BeforeEach
    public void initTest() {
        rpmAction = createEntity(em);
    }

    @Test
    @Transactional
    void createRpmAction() throws Exception {
        int databaseSizeBeforeCreate = rpmActionRepository.findAll().size();
        // Create the RpmAction
        RpmActionDTO rpmActionDTO = rpmActionMapper.toDto(rpmAction);
        restRpmActionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmActionDTO)))
            .andExpect(status().isCreated());

        // Validate the RpmAction in the database
        List<RpmAction> rpmActionList = rpmActionRepository.findAll();
        assertThat(rpmActionList).hasSize(databaseSizeBeforeCreate + 1);
        RpmAction testRpmAction = rpmActionList.get(rpmActionList.size() - 1);
        assertThat(testRpmAction.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRpmAction.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testRpmAction.getDateTime()).isEqualTo(DEFAULT_DATE_TIME);
        assertThat(testRpmAction.getDuration()).isEqualTo(DEFAULT_DURATION);
    }

    @Test
    @Transactional
    void createRpmActionWithExistingId() throws Exception {
        // Create the RpmAction with an existing ID
        rpmAction.setId(1L);
        RpmActionDTO rpmActionDTO = rpmActionMapper.toDto(rpmAction);

        int databaseSizeBeforeCreate = rpmActionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRpmActionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmActionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RpmAction in the database
        List<RpmAction> rpmActionList = rpmActionRepository.findAll();
        assertThat(rpmActionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = rpmActionRepository.findAll().size();
        // set the field null
        rpmAction.setName(null);

        // Create the RpmAction, which fails.
        RpmActionDTO rpmActionDTO = rpmActionMapper.toDto(rpmAction);

        restRpmActionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmActionDTO)))
            .andExpect(status().isBadRequest());

        List<RpmAction> rpmActionList = rpmActionRepository.findAll();
        assertThat(rpmActionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriorityIsRequired() throws Exception {
        int databaseSizeBeforeTest = rpmActionRepository.findAll().size();
        // set the field null
        rpmAction.setPriority(null);

        // Create the RpmAction, which fails.
        RpmActionDTO rpmActionDTO = rpmActionMapper.toDto(rpmAction);

        restRpmActionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmActionDTO)))
            .andExpect(status().isBadRequest());

        List<RpmAction> rpmActionList = rpmActionRepository.findAll();
        assertThat(rpmActionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = rpmActionRepository.findAll().size();
        // set the field null
        rpmAction.setDateTime(null);

        // Create the RpmAction, which fails.
        RpmActionDTO rpmActionDTO = rpmActionMapper.toDto(rpmAction);

        restRpmActionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmActionDTO)))
            .andExpect(status().isBadRequest());

        List<RpmAction> rpmActionList = rpmActionRepository.findAll();
        assertThat(rpmActionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDurationIsRequired() throws Exception {
        int databaseSizeBeforeTest = rpmActionRepository.findAll().size();
        // set the field null
        rpmAction.setDuration(null);

        // Create the RpmAction, which fails.
        RpmActionDTO rpmActionDTO = rpmActionMapper.toDto(rpmAction);

        restRpmActionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmActionDTO)))
            .andExpect(status().isBadRequest());

        List<RpmAction> rpmActionList = rpmActionRepository.findAll();
        assertThat(rpmActionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRpmActions() throws Exception {
        // Initialize the database
        rpmActionRepository.saveAndFlush(rpmAction);

        // Get all the rpmActionList
        restRpmActionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rpmAction.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].dateTime").value(hasItem(sameInstant(DEFAULT_DATE_TIME))))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.toString())));
    }

    @Test
    @Transactional
    void getRpmAction() throws Exception {
        // Initialize the database
        rpmActionRepository.saveAndFlush(rpmAction);

        // Get the rpmAction
        restRpmActionMockMvc
            .perform(get(ENTITY_API_URL_ID, rpmAction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rpmAction.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY))
            .andExpect(jsonPath("$.dateTime").value(sameInstant(DEFAULT_DATE_TIME)))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRpmAction() throws Exception {
        // Get the rpmAction
        restRpmActionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRpmAction() throws Exception {
        // Initialize the database
        rpmActionRepository.saveAndFlush(rpmAction);

        int databaseSizeBeforeUpdate = rpmActionRepository.findAll().size();

        // Update the rpmAction
        RpmAction updatedRpmAction = rpmActionRepository.findById(rpmAction.getId()).get();
        // Disconnect from session so that the updates on updatedRpmAction are not directly saved in db
        em.detach(updatedRpmAction);
        updatedRpmAction.name(UPDATED_NAME).priority(UPDATED_PRIORITY).dateTime(UPDATED_DATE_TIME).duration(UPDATED_DURATION);
        RpmActionDTO rpmActionDTO = rpmActionMapper.toDto(updatedRpmAction);

        restRpmActionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rpmActionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmActionDTO))
            )
            .andExpect(status().isOk());

        // Validate the RpmAction in the database
        List<RpmAction> rpmActionList = rpmActionRepository.findAll();
        assertThat(rpmActionList).hasSize(databaseSizeBeforeUpdate);
        RpmAction testRpmAction = rpmActionList.get(rpmActionList.size() - 1);
        assertThat(testRpmAction.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRpmAction.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testRpmAction.getDateTime()).isEqualTo(UPDATED_DATE_TIME);
        assertThat(testRpmAction.getDuration()).isEqualTo(UPDATED_DURATION);
    }

    @Test
    @Transactional
    void putNonExistingRpmAction() throws Exception {
        int databaseSizeBeforeUpdate = rpmActionRepository.findAll().size();
        rpmAction.setId(count.incrementAndGet());

        // Create the RpmAction
        RpmActionDTO rpmActionDTO = rpmActionMapper.toDto(rpmAction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRpmActionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rpmActionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmActionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmAction in the database
        List<RpmAction> rpmActionList = rpmActionRepository.findAll();
        assertThat(rpmActionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRpmAction() throws Exception {
        int databaseSizeBeforeUpdate = rpmActionRepository.findAll().size();
        rpmAction.setId(count.incrementAndGet());

        // Create the RpmAction
        RpmActionDTO rpmActionDTO = rpmActionMapper.toDto(rpmAction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmActionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmActionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmAction in the database
        List<RpmAction> rpmActionList = rpmActionRepository.findAll();
        assertThat(rpmActionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRpmAction() throws Exception {
        int databaseSizeBeforeUpdate = rpmActionRepository.findAll().size();
        rpmAction.setId(count.incrementAndGet());

        // Create the RpmAction
        RpmActionDTO rpmActionDTO = rpmActionMapper.toDto(rpmAction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmActionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmActionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RpmAction in the database
        List<RpmAction> rpmActionList = rpmActionRepository.findAll();
        assertThat(rpmActionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRpmActionWithPatch() throws Exception {
        // Initialize the database
        rpmActionRepository.saveAndFlush(rpmAction);

        int databaseSizeBeforeUpdate = rpmActionRepository.findAll().size();

        // Update the rpmAction using partial update
        RpmAction partialUpdatedRpmAction = new RpmAction();
        partialUpdatedRpmAction.setId(rpmAction.getId());

        partialUpdatedRpmAction.priority(UPDATED_PRIORITY);

        restRpmActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRpmAction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRpmAction))
            )
            .andExpect(status().isOk());

        // Validate the RpmAction in the database
        List<RpmAction> rpmActionList = rpmActionRepository.findAll();
        assertThat(rpmActionList).hasSize(databaseSizeBeforeUpdate);
        RpmAction testRpmAction = rpmActionList.get(rpmActionList.size() - 1);
        assertThat(testRpmAction.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRpmAction.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testRpmAction.getDateTime()).isEqualTo(DEFAULT_DATE_TIME);
        assertThat(testRpmAction.getDuration()).isEqualTo(DEFAULT_DURATION);
    }

    @Test
    @Transactional
    void fullUpdateRpmActionWithPatch() throws Exception {
        // Initialize the database
        rpmActionRepository.saveAndFlush(rpmAction);

        int databaseSizeBeforeUpdate = rpmActionRepository.findAll().size();

        // Update the rpmAction using partial update
        RpmAction partialUpdatedRpmAction = new RpmAction();
        partialUpdatedRpmAction.setId(rpmAction.getId());

        partialUpdatedRpmAction.name(UPDATED_NAME).priority(UPDATED_PRIORITY).dateTime(UPDATED_DATE_TIME).duration(UPDATED_DURATION);

        restRpmActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRpmAction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRpmAction))
            )
            .andExpect(status().isOk());

        // Validate the RpmAction in the database
        List<RpmAction> rpmActionList = rpmActionRepository.findAll();
        assertThat(rpmActionList).hasSize(databaseSizeBeforeUpdate);
        RpmAction testRpmAction = rpmActionList.get(rpmActionList.size() - 1);
        assertThat(testRpmAction.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRpmAction.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testRpmAction.getDateTime()).isEqualTo(UPDATED_DATE_TIME);
        assertThat(testRpmAction.getDuration()).isEqualTo(UPDATED_DURATION);
    }

    @Test
    @Transactional
    void patchNonExistingRpmAction() throws Exception {
        int databaseSizeBeforeUpdate = rpmActionRepository.findAll().size();
        rpmAction.setId(count.incrementAndGet());

        // Create the RpmAction
        RpmActionDTO rpmActionDTO = rpmActionMapper.toDto(rpmAction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRpmActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rpmActionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rpmActionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmAction in the database
        List<RpmAction> rpmActionList = rpmActionRepository.findAll();
        assertThat(rpmActionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRpmAction() throws Exception {
        int databaseSizeBeforeUpdate = rpmActionRepository.findAll().size();
        rpmAction.setId(count.incrementAndGet());

        // Create the RpmAction
        RpmActionDTO rpmActionDTO = rpmActionMapper.toDto(rpmAction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rpmActionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmAction in the database
        List<RpmAction> rpmActionList = rpmActionRepository.findAll();
        assertThat(rpmActionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRpmAction() throws Exception {
        int databaseSizeBeforeUpdate = rpmActionRepository.findAll().size();
        rpmAction.setId(count.incrementAndGet());

        // Create the RpmAction
        RpmActionDTO rpmActionDTO = rpmActionMapper.toDto(rpmAction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmActionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rpmActionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RpmAction in the database
        List<RpmAction> rpmActionList = rpmActionRepository.findAll();
        assertThat(rpmActionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRpmAction() throws Exception {
        // Initialize the database
        rpmActionRepository.saveAndFlush(rpmAction);

        int databaseSizeBeforeDelete = rpmActionRepository.findAll().size();

        // Delete the rpmAction
        restRpmActionMockMvc
            .perform(delete(ENTITY_API_URL_ID, rpmAction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RpmAction> rpmActionList = rpmActionRepository.findAll();
        assertThat(rpmActionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
