package com.github.scottvin.rpm.web.rest;

import static com.github.scottvin.rpm.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.github.scottvin.rpm.IntegrationTest;
import com.github.scottvin.rpm.domain.RpmCapture;
import com.github.scottvin.rpm.repository.RpmCaptureRepository;
import com.github.scottvin.rpm.service.dto.RpmCaptureDTO;
import com.github.scottvin.rpm.service.mapper.RpmCaptureMapper;
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
 * Integration tests for the {@link RpmCaptureResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RpmCaptureResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Duration DEFAULT_DURATION = Duration.ofHours(6);
    private static final Duration UPDATED_DURATION = Duration.ofHours(12);

    private static final String ENTITY_API_URL = "/api/rpm-captures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RpmCaptureRepository rpmCaptureRepository;

    @Autowired
    private RpmCaptureMapper rpmCaptureMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRpmCaptureMockMvc;

    private RpmCapture rpmCapture;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RpmCapture createEntity(EntityManager em) {
        RpmCapture rpmCapture = new RpmCapture().name(DEFAULT_NAME).dateTime(DEFAULT_DATE_TIME).duration(DEFAULT_DURATION);
        return rpmCapture;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RpmCapture createUpdatedEntity(EntityManager em) {
        RpmCapture rpmCapture = new RpmCapture().name(UPDATED_NAME).dateTime(UPDATED_DATE_TIME).duration(UPDATED_DURATION);
        return rpmCapture;
    }

    @BeforeEach
    public void initTest() {
        rpmCapture = createEntity(em);
    }

    @Test
    @Transactional
    void createRpmCapture() throws Exception {
        int databaseSizeBeforeCreate = rpmCaptureRepository.findAll().size();
        // Create the RpmCapture
        RpmCaptureDTO rpmCaptureDTO = rpmCaptureMapper.toDto(rpmCapture);
        restRpmCaptureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmCaptureDTO)))
            .andExpect(status().isCreated());

        // Validate the RpmCapture in the database
        List<RpmCapture> rpmCaptureList = rpmCaptureRepository.findAll();
        assertThat(rpmCaptureList).hasSize(databaseSizeBeforeCreate + 1);
        RpmCapture testRpmCapture = rpmCaptureList.get(rpmCaptureList.size() - 1);
        assertThat(testRpmCapture.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRpmCapture.getDateTime()).isEqualTo(DEFAULT_DATE_TIME);
        assertThat(testRpmCapture.getDuration()).isEqualTo(DEFAULT_DURATION);
    }

    @Test
    @Transactional
    void createRpmCaptureWithExistingId() throws Exception {
        // Create the RpmCapture with an existing ID
        rpmCapture.setId(1L);
        RpmCaptureDTO rpmCaptureDTO = rpmCaptureMapper.toDto(rpmCapture);

        int databaseSizeBeforeCreate = rpmCaptureRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRpmCaptureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmCaptureDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RpmCapture in the database
        List<RpmCapture> rpmCaptureList = rpmCaptureRepository.findAll();
        assertThat(rpmCaptureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = rpmCaptureRepository.findAll().size();
        // set the field null
        rpmCapture.setName(null);

        // Create the RpmCapture, which fails.
        RpmCaptureDTO rpmCaptureDTO = rpmCaptureMapper.toDto(rpmCapture);

        restRpmCaptureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmCaptureDTO)))
            .andExpect(status().isBadRequest());

        List<RpmCapture> rpmCaptureList = rpmCaptureRepository.findAll();
        assertThat(rpmCaptureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = rpmCaptureRepository.findAll().size();
        // set the field null
        rpmCapture.setDateTime(null);

        // Create the RpmCapture, which fails.
        RpmCaptureDTO rpmCaptureDTO = rpmCaptureMapper.toDto(rpmCapture);

        restRpmCaptureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmCaptureDTO)))
            .andExpect(status().isBadRequest());

        List<RpmCapture> rpmCaptureList = rpmCaptureRepository.findAll();
        assertThat(rpmCaptureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDurationIsRequired() throws Exception {
        int databaseSizeBeforeTest = rpmCaptureRepository.findAll().size();
        // set the field null
        rpmCapture.setDuration(null);

        // Create the RpmCapture, which fails.
        RpmCaptureDTO rpmCaptureDTO = rpmCaptureMapper.toDto(rpmCapture);

        restRpmCaptureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmCaptureDTO)))
            .andExpect(status().isBadRequest());

        List<RpmCapture> rpmCaptureList = rpmCaptureRepository.findAll();
        assertThat(rpmCaptureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRpmCaptures() throws Exception {
        // Initialize the database
        rpmCaptureRepository.saveAndFlush(rpmCapture);

        // Get all the rpmCaptureList
        restRpmCaptureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rpmCapture.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dateTime").value(hasItem(sameInstant(DEFAULT_DATE_TIME))))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.toString())));
    }

    @Test
    @Transactional
    void getRpmCapture() throws Exception {
        // Initialize the database
        rpmCaptureRepository.saveAndFlush(rpmCapture);

        // Get the rpmCapture
        restRpmCaptureMockMvc
            .perform(get(ENTITY_API_URL_ID, rpmCapture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rpmCapture.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.dateTime").value(sameInstant(DEFAULT_DATE_TIME)))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRpmCapture() throws Exception {
        // Get the rpmCapture
        restRpmCaptureMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRpmCapture() throws Exception {
        // Initialize the database
        rpmCaptureRepository.saveAndFlush(rpmCapture);

        int databaseSizeBeforeUpdate = rpmCaptureRepository.findAll().size();

        // Update the rpmCapture
        RpmCapture updatedRpmCapture = rpmCaptureRepository.findById(rpmCapture.getId()).get();
        // Disconnect from session so that the updates on updatedRpmCapture are not directly saved in db
        em.detach(updatedRpmCapture);
        updatedRpmCapture.name(UPDATED_NAME).dateTime(UPDATED_DATE_TIME).duration(UPDATED_DURATION);
        RpmCaptureDTO rpmCaptureDTO = rpmCaptureMapper.toDto(updatedRpmCapture);

        restRpmCaptureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rpmCaptureDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmCaptureDTO))
            )
            .andExpect(status().isOk());

        // Validate the RpmCapture in the database
        List<RpmCapture> rpmCaptureList = rpmCaptureRepository.findAll();
        assertThat(rpmCaptureList).hasSize(databaseSizeBeforeUpdate);
        RpmCapture testRpmCapture = rpmCaptureList.get(rpmCaptureList.size() - 1);
        assertThat(testRpmCapture.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRpmCapture.getDateTime()).isEqualTo(UPDATED_DATE_TIME);
        assertThat(testRpmCapture.getDuration()).isEqualTo(UPDATED_DURATION);
    }

    @Test
    @Transactional
    void putNonExistingRpmCapture() throws Exception {
        int databaseSizeBeforeUpdate = rpmCaptureRepository.findAll().size();
        rpmCapture.setId(count.incrementAndGet());

        // Create the RpmCapture
        RpmCaptureDTO rpmCaptureDTO = rpmCaptureMapper.toDto(rpmCapture);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRpmCaptureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rpmCaptureDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmCaptureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmCapture in the database
        List<RpmCapture> rpmCaptureList = rpmCaptureRepository.findAll();
        assertThat(rpmCaptureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRpmCapture() throws Exception {
        int databaseSizeBeforeUpdate = rpmCaptureRepository.findAll().size();
        rpmCapture.setId(count.incrementAndGet());

        // Create the RpmCapture
        RpmCaptureDTO rpmCaptureDTO = rpmCaptureMapper.toDto(rpmCapture);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmCaptureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmCaptureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmCapture in the database
        List<RpmCapture> rpmCaptureList = rpmCaptureRepository.findAll();
        assertThat(rpmCaptureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRpmCapture() throws Exception {
        int databaseSizeBeforeUpdate = rpmCaptureRepository.findAll().size();
        rpmCapture.setId(count.incrementAndGet());

        // Create the RpmCapture
        RpmCaptureDTO rpmCaptureDTO = rpmCaptureMapper.toDto(rpmCapture);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmCaptureMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmCaptureDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RpmCapture in the database
        List<RpmCapture> rpmCaptureList = rpmCaptureRepository.findAll();
        assertThat(rpmCaptureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRpmCaptureWithPatch() throws Exception {
        // Initialize the database
        rpmCaptureRepository.saveAndFlush(rpmCapture);

        int databaseSizeBeforeUpdate = rpmCaptureRepository.findAll().size();

        // Update the rpmCapture using partial update
        RpmCapture partialUpdatedRpmCapture = new RpmCapture();
        partialUpdatedRpmCapture.setId(rpmCapture.getId());

        partialUpdatedRpmCapture.name(UPDATED_NAME);

        restRpmCaptureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRpmCapture.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRpmCapture))
            )
            .andExpect(status().isOk());

        // Validate the RpmCapture in the database
        List<RpmCapture> rpmCaptureList = rpmCaptureRepository.findAll();
        assertThat(rpmCaptureList).hasSize(databaseSizeBeforeUpdate);
        RpmCapture testRpmCapture = rpmCaptureList.get(rpmCaptureList.size() - 1);
        assertThat(testRpmCapture.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRpmCapture.getDateTime()).isEqualTo(DEFAULT_DATE_TIME);
        assertThat(testRpmCapture.getDuration()).isEqualTo(DEFAULT_DURATION);
    }

    @Test
    @Transactional
    void fullUpdateRpmCaptureWithPatch() throws Exception {
        // Initialize the database
        rpmCaptureRepository.saveAndFlush(rpmCapture);

        int databaseSizeBeforeUpdate = rpmCaptureRepository.findAll().size();

        // Update the rpmCapture using partial update
        RpmCapture partialUpdatedRpmCapture = new RpmCapture();
        partialUpdatedRpmCapture.setId(rpmCapture.getId());

        partialUpdatedRpmCapture.name(UPDATED_NAME).dateTime(UPDATED_DATE_TIME).duration(UPDATED_DURATION);

        restRpmCaptureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRpmCapture.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRpmCapture))
            )
            .andExpect(status().isOk());

        // Validate the RpmCapture in the database
        List<RpmCapture> rpmCaptureList = rpmCaptureRepository.findAll();
        assertThat(rpmCaptureList).hasSize(databaseSizeBeforeUpdate);
        RpmCapture testRpmCapture = rpmCaptureList.get(rpmCaptureList.size() - 1);
        assertThat(testRpmCapture.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRpmCapture.getDateTime()).isEqualTo(UPDATED_DATE_TIME);
        assertThat(testRpmCapture.getDuration()).isEqualTo(UPDATED_DURATION);
    }

    @Test
    @Transactional
    void patchNonExistingRpmCapture() throws Exception {
        int databaseSizeBeforeUpdate = rpmCaptureRepository.findAll().size();
        rpmCapture.setId(count.incrementAndGet());

        // Create the RpmCapture
        RpmCaptureDTO rpmCaptureDTO = rpmCaptureMapper.toDto(rpmCapture);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRpmCaptureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rpmCaptureDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rpmCaptureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmCapture in the database
        List<RpmCapture> rpmCaptureList = rpmCaptureRepository.findAll();
        assertThat(rpmCaptureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRpmCapture() throws Exception {
        int databaseSizeBeforeUpdate = rpmCaptureRepository.findAll().size();
        rpmCapture.setId(count.incrementAndGet());

        // Create the RpmCapture
        RpmCaptureDTO rpmCaptureDTO = rpmCaptureMapper.toDto(rpmCapture);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmCaptureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rpmCaptureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmCapture in the database
        List<RpmCapture> rpmCaptureList = rpmCaptureRepository.findAll();
        assertThat(rpmCaptureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRpmCapture() throws Exception {
        int databaseSizeBeforeUpdate = rpmCaptureRepository.findAll().size();
        rpmCapture.setId(count.incrementAndGet());

        // Create the RpmCapture
        RpmCaptureDTO rpmCaptureDTO = rpmCaptureMapper.toDto(rpmCapture);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmCaptureMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rpmCaptureDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RpmCapture in the database
        List<RpmCapture> rpmCaptureList = rpmCaptureRepository.findAll();
        assertThat(rpmCaptureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRpmCapture() throws Exception {
        // Initialize the database
        rpmCaptureRepository.saveAndFlush(rpmCapture);

        int databaseSizeBeforeDelete = rpmCaptureRepository.findAll().size();

        // Delete the rpmCapture
        restRpmCaptureMockMvc
            .perform(delete(ENTITY_API_URL_ID, rpmCapture.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RpmCapture> rpmCaptureList = rpmCaptureRepository.findAll();
        assertThat(rpmCaptureList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
