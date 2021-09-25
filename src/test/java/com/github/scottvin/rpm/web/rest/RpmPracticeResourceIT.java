package com.github.scottvin.rpm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.github.scottvin.rpm.IntegrationTest;
import com.github.scottvin.rpm.domain.RpmCharacter;
import com.github.scottvin.rpm.domain.RpmPractice;
import com.github.scottvin.rpm.repository.RpmPracticeRepository;
import com.github.scottvin.rpm.service.dto.RpmPracticeDTO;
import com.github.scottvin.rpm.service.mapper.RpmPracticeMapper;
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
 * Integration tests for the {@link RpmPracticeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RpmPracticeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/rpm-practices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RpmPracticeRepository rpmPracticeRepository;

    @Autowired
    private RpmPracticeMapper rpmPracticeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRpmPracticeMockMvc;

    private RpmPractice rpmPractice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RpmPractice createEntity(EntityManager em) {
        RpmPractice rpmPractice = new RpmPractice().name(DEFAULT_NAME);
        // Add required entity
        RpmCharacter rpmCharacter;
        if (TestUtil.findAll(em, RpmCharacter.class).isEmpty()) {
            rpmCharacter = RpmCharacterResourceIT.createEntity(em);
            em.persist(rpmCharacter);
            em.flush();
        } else {
            rpmCharacter = TestUtil.findAll(em, RpmCharacter.class).get(0);
        }
        rpmPractice.setCharacter(rpmCharacter);
        return rpmPractice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RpmPractice createUpdatedEntity(EntityManager em) {
        RpmPractice rpmPractice = new RpmPractice().name(UPDATED_NAME);
        // Add required entity
        RpmCharacter rpmCharacter;
        if (TestUtil.findAll(em, RpmCharacter.class).isEmpty()) {
            rpmCharacter = RpmCharacterResourceIT.createUpdatedEntity(em);
            em.persist(rpmCharacter);
            em.flush();
        } else {
            rpmCharacter = TestUtil.findAll(em, RpmCharacter.class).get(0);
        }
        rpmPractice.setCharacter(rpmCharacter);
        return rpmPractice;
    }

    @BeforeEach
    public void initTest() {
        rpmPractice = createEntity(em);
    }

    @Test
    @Transactional
    void createRpmPractice() throws Exception {
        int databaseSizeBeforeCreate = rpmPracticeRepository.findAll().size();
        // Create the RpmPractice
        RpmPracticeDTO rpmPracticeDTO = rpmPracticeMapper.toDto(rpmPractice);
        restRpmPracticeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmPracticeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the RpmPractice in the database
        List<RpmPractice> rpmPracticeList = rpmPracticeRepository.findAll();
        assertThat(rpmPracticeList).hasSize(databaseSizeBeforeCreate + 1);
        RpmPractice testRpmPractice = rpmPracticeList.get(rpmPracticeList.size() - 1);
        assertThat(testRpmPractice.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createRpmPracticeWithExistingId() throws Exception {
        // Create the RpmPractice with an existing ID
        rpmPractice.setId(1L);
        RpmPracticeDTO rpmPracticeDTO = rpmPracticeMapper.toDto(rpmPractice);

        int databaseSizeBeforeCreate = rpmPracticeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRpmPracticeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmPracticeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmPractice in the database
        List<RpmPractice> rpmPracticeList = rpmPracticeRepository.findAll();
        assertThat(rpmPracticeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = rpmPracticeRepository.findAll().size();
        // set the field null
        rpmPractice.setName(null);

        // Create the RpmPractice, which fails.
        RpmPracticeDTO rpmPracticeDTO = rpmPracticeMapper.toDto(rpmPractice);

        restRpmPracticeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmPracticeDTO))
            )
            .andExpect(status().isBadRequest());

        List<RpmPractice> rpmPracticeList = rpmPracticeRepository.findAll();
        assertThat(rpmPracticeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRpmPractices() throws Exception {
        // Initialize the database
        rpmPracticeRepository.saveAndFlush(rpmPractice);

        // Get all the rpmPracticeList
        restRpmPracticeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rpmPractice.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getRpmPractice() throws Exception {
        // Initialize the database
        rpmPracticeRepository.saveAndFlush(rpmPractice);

        // Get the rpmPractice
        restRpmPracticeMockMvc
            .perform(get(ENTITY_API_URL_ID, rpmPractice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rpmPractice.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingRpmPractice() throws Exception {
        // Get the rpmPractice
        restRpmPracticeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRpmPractice() throws Exception {
        // Initialize the database
        rpmPracticeRepository.saveAndFlush(rpmPractice);

        int databaseSizeBeforeUpdate = rpmPracticeRepository.findAll().size();

        // Update the rpmPractice
        RpmPractice updatedRpmPractice = rpmPracticeRepository.findById(rpmPractice.getId()).get();
        // Disconnect from session so that the updates on updatedRpmPractice are not directly saved in db
        em.detach(updatedRpmPractice);
        updatedRpmPractice.name(UPDATED_NAME);
        RpmPracticeDTO rpmPracticeDTO = rpmPracticeMapper.toDto(updatedRpmPractice);

        restRpmPracticeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rpmPracticeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmPracticeDTO))
            )
            .andExpect(status().isOk());

        // Validate the RpmPractice in the database
        List<RpmPractice> rpmPracticeList = rpmPracticeRepository.findAll();
        assertThat(rpmPracticeList).hasSize(databaseSizeBeforeUpdate);
        RpmPractice testRpmPractice = rpmPracticeList.get(rpmPracticeList.size() - 1);
        assertThat(testRpmPractice.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingRpmPractice() throws Exception {
        int databaseSizeBeforeUpdate = rpmPracticeRepository.findAll().size();
        rpmPractice.setId(count.incrementAndGet());

        // Create the RpmPractice
        RpmPracticeDTO rpmPracticeDTO = rpmPracticeMapper.toDto(rpmPractice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRpmPracticeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rpmPracticeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmPracticeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmPractice in the database
        List<RpmPractice> rpmPracticeList = rpmPracticeRepository.findAll();
        assertThat(rpmPracticeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRpmPractice() throws Exception {
        int databaseSizeBeforeUpdate = rpmPracticeRepository.findAll().size();
        rpmPractice.setId(count.incrementAndGet());

        // Create the RpmPractice
        RpmPracticeDTO rpmPracticeDTO = rpmPracticeMapper.toDto(rpmPractice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmPracticeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmPracticeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmPractice in the database
        List<RpmPractice> rpmPracticeList = rpmPracticeRepository.findAll();
        assertThat(rpmPracticeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRpmPractice() throws Exception {
        int databaseSizeBeforeUpdate = rpmPracticeRepository.findAll().size();
        rpmPractice.setId(count.incrementAndGet());

        // Create the RpmPractice
        RpmPracticeDTO rpmPracticeDTO = rpmPracticeMapper.toDto(rpmPractice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmPracticeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmPracticeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RpmPractice in the database
        List<RpmPractice> rpmPracticeList = rpmPracticeRepository.findAll();
        assertThat(rpmPracticeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRpmPracticeWithPatch() throws Exception {
        // Initialize the database
        rpmPracticeRepository.saveAndFlush(rpmPractice);

        int databaseSizeBeforeUpdate = rpmPracticeRepository.findAll().size();

        // Update the rpmPractice using partial update
        RpmPractice partialUpdatedRpmPractice = new RpmPractice();
        partialUpdatedRpmPractice.setId(rpmPractice.getId());

        restRpmPracticeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRpmPractice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRpmPractice))
            )
            .andExpect(status().isOk());

        // Validate the RpmPractice in the database
        List<RpmPractice> rpmPracticeList = rpmPracticeRepository.findAll();
        assertThat(rpmPracticeList).hasSize(databaseSizeBeforeUpdate);
        RpmPractice testRpmPractice = rpmPracticeList.get(rpmPracticeList.size() - 1);
        assertThat(testRpmPractice.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateRpmPracticeWithPatch() throws Exception {
        // Initialize the database
        rpmPracticeRepository.saveAndFlush(rpmPractice);

        int databaseSizeBeforeUpdate = rpmPracticeRepository.findAll().size();

        // Update the rpmPractice using partial update
        RpmPractice partialUpdatedRpmPractice = new RpmPractice();
        partialUpdatedRpmPractice.setId(rpmPractice.getId());

        partialUpdatedRpmPractice.name(UPDATED_NAME);

        restRpmPracticeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRpmPractice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRpmPractice))
            )
            .andExpect(status().isOk());

        // Validate the RpmPractice in the database
        List<RpmPractice> rpmPracticeList = rpmPracticeRepository.findAll();
        assertThat(rpmPracticeList).hasSize(databaseSizeBeforeUpdate);
        RpmPractice testRpmPractice = rpmPracticeList.get(rpmPracticeList.size() - 1);
        assertThat(testRpmPractice.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingRpmPractice() throws Exception {
        int databaseSizeBeforeUpdate = rpmPracticeRepository.findAll().size();
        rpmPractice.setId(count.incrementAndGet());

        // Create the RpmPractice
        RpmPracticeDTO rpmPracticeDTO = rpmPracticeMapper.toDto(rpmPractice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRpmPracticeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rpmPracticeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rpmPracticeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmPractice in the database
        List<RpmPractice> rpmPracticeList = rpmPracticeRepository.findAll();
        assertThat(rpmPracticeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRpmPractice() throws Exception {
        int databaseSizeBeforeUpdate = rpmPracticeRepository.findAll().size();
        rpmPractice.setId(count.incrementAndGet());

        // Create the RpmPractice
        RpmPracticeDTO rpmPracticeDTO = rpmPracticeMapper.toDto(rpmPractice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmPracticeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rpmPracticeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmPractice in the database
        List<RpmPractice> rpmPracticeList = rpmPracticeRepository.findAll();
        assertThat(rpmPracticeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRpmPractice() throws Exception {
        int databaseSizeBeforeUpdate = rpmPracticeRepository.findAll().size();
        rpmPractice.setId(count.incrementAndGet());

        // Create the RpmPractice
        RpmPracticeDTO rpmPracticeDTO = rpmPracticeMapper.toDto(rpmPractice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmPracticeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rpmPracticeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RpmPractice in the database
        List<RpmPractice> rpmPracticeList = rpmPracticeRepository.findAll();
        assertThat(rpmPracticeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRpmPractice() throws Exception {
        // Initialize the database
        rpmPracticeRepository.saveAndFlush(rpmPractice);

        int databaseSizeBeforeDelete = rpmPracticeRepository.findAll().size();

        // Delete the rpmPractice
        restRpmPracticeMockMvc
            .perform(delete(ENTITY_API_URL_ID, rpmPractice.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RpmPractice> rpmPracticeList = rpmPracticeRepository.findAll();
        assertThat(rpmPracticeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
