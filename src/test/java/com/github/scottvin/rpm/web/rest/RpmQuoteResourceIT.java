package com.github.scottvin.rpm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.github.scottvin.rpm.IntegrationTest;
import com.github.scottvin.rpm.domain.RpmCharacter;
import com.github.scottvin.rpm.domain.RpmQuote;
import com.github.scottvin.rpm.repository.RpmQuoteRepository;
import com.github.scottvin.rpm.service.dto.RpmQuoteDTO;
import com.github.scottvin.rpm.service.mapper.RpmQuoteMapper;
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
 * Integration tests for the {@link RpmQuoteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RpmQuoteResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/rpm-quotes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RpmQuoteRepository rpmQuoteRepository;

    @Autowired
    private RpmQuoteMapper rpmQuoteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRpmQuoteMockMvc;

    private RpmQuote rpmQuote;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RpmQuote createEntity(EntityManager em) {
        RpmQuote rpmQuote = new RpmQuote().name(DEFAULT_NAME);
        // Add required entity
        RpmCharacter rpmCharacter;
        if (TestUtil.findAll(em, RpmCharacter.class).isEmpty()) {
            rpmCharacter = RpmCharacterResourceIT.createEntity(em);
            em.persist(rpmCharacter);
            em.flush();
        } else {
            rpmCharacter = TestUtil.findAll(em, RpmCharacter.class).get(0);
        }
        rpmQuote.setCharacter(rpmCharacter);
        return rpmQuote;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RpmQuote createUpdatedEntity(EntityManager em) {
        RpmQuote rpmQuote = new RpmQuote().name(UPDATED_NAME);
        // Add required entity
        RpmCharacter rpmCharacter;
        if (TestUtil.findAll(em, RpmCharacter.class).isEmpty()) {
            rpmCharacter = RpmCharacterResourceIT.createUpdatedEntity(em);
            em.persist(rpmCharacter);
            em.flush();
        } else {
            rpmCharacter = TestUtil.findAll(em, RpmCharacter.class).get(0);
        }
        rpmQuote.setCharacter(rpmCharacter);
        return rpmQuote;
    }

    @BeforeEach
    public void initTest() {
        rpmQuote = createEntity(em);
    }

    @Test
    @Transactional
    void createRpmQuote() throws Exception {
        int databaseSizeBeforeCreate = rpmQuoteRepository.findAll().size();
        // Create the RpmQuote
        RpmQuoteDTO rpmQuoteDTO = rpmQuoteMapper.toDto(rpmQuote);
        restRpmQuoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmQuoteDTO)))
            .andExpect(status().isCreated());

        // Validate the RpmQuote in the database
        List<RpmQuote> rpmQuoteList = rpmQuoteRepository.findAll();
        assertThat(rpmQuoteList).hasSize(databaseSizeBeforeCreate + 1);
        RpmQuote testRpmQuote = rpmQuoteList.get(rpmQuoteList.size() - 1);
        assertThat(testRpmQuote.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createRpmQuoteWithExistingId() throws Exception {
        // Create the RpmQuote with an existing ID
        rpmQuote.setId(1L);
        RpmQuoteDTO rpmQuoteDTO = rpmQuoteMapper.toDto(rpmQuote);

        int databaseSizeBeforeCreate = rpmQuoteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRpmQuoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmQuoteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RpmQuote in the database
        List<RpmQuote> rpmQuoteList = rpmQuoteRepository.findAll();
        assertThat(rpmQuoteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = rpmQuoteRepository.findAll().size();
        // set the field null
        rpmQuote.setName(null);

        // Create the RpmQuote, which fails.
        RpmQuoteDTO rpmQuoteDTO = rpmQuoteMapper.toDto(rpmQuote);

        restRpmQuoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmQuoteDTO)))
            .andExpect(status().isBadRequest());

        List<RpmQuote> rpmQuoteList = rpmQuoteRepository.findAll();
        assertThat(rpmQuoteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRpmQuotes() throws Exception {
        // Initialize the database
        rpmQuoteRepository.saveAndFlush(rpmQuote);

        // Get all the rpmQuoteList
        restRpmQuoteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rpmQuote.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getRpmQuote() throws Exception {
        // Initialize the database
        rpmQuoteRepository.saveAndFlush(rpmQuote);

        // Get the rpmQuote
        restRpmQuoteMockMvc
            .perform(get(ENTITY_API_URL_ID, rpmQuote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rpmQuote.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingRpmQuote() throws Exception {
        // Get the rpmQuote
        restRpmQuoteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRpmQuote() throws Exception {
        // Initialize the database
        rpmQuoteRepository.saveAndFlush(rpmQuote);

        int databaseSizeBeforeUpdate = rpmQuoteRepository.findAll().size();

        // Update the rpmQuote
        RpmQuote updatedRpmQuote = rpmQuoteRepository.findById(rpmQuote.getId()).get();
        // Disconnect from session so that the updates on updatedRpmQuote are not directly saved in db
        em.detach(updatedRpmQuote);
        updatedRpmQuote.name(UPDATED_NAME);
        RpmQuoteDTO rpmQuoteDTO = rpmQuoteMapper.toDto(updatedRpmQuote);

        restRpmQuoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rpmQuoteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmQuoteDTO))
            )
            .andExpect(status().isOk());

        // Validate the RpmQuote in the database
        List<RpmQuote> rpmQuoteList = rpmQuoteRepository.findAll();
        assertThat(rpmQuoteList).hasSize(databaseSizeBeforeUpdate);
        RpmQuote testRpmQuote = rpmQuoteList.get(rpmQuoteList.size() - 1);
        assertThat(testRpmQuote.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingRpmQuote() throws Exception {
        int databaseSizeBeforeUpdate = rpmQuoteRepository.findAll().size();
        rpmQuote.setId(count.incrementAndGet());

        // Create the RpmQuote
        RpmQuoteDTO rpmQuoteDTO = rpmQuoteMapper.toDto(rpmQuote);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRpmQuoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rpmQuoteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmQuoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmQuote in the database
        List<RpmQuote> rpmQuoteList = rpmQuoteRepository.findAll();
        assertThat(rpmQuoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRpmQuote() throws Exception {
        int databaseSizeBeforeUpdate = rpmQuoteRepository.findAll().size();
        rpmQuote.setId(count.incrementAndGet());

        // Create the RpmQuote
        RpmQuoteDTO rpmQuoteDTO = rpmQuoteMapper.toDto(rpmQuote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmQuoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmQuoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmQuote in the database
        List<RpmQuote> rpmQuoteList = rpmQuoteRepository.findAll();
        assertThat(rpmQuoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRpmQuote() throws Exception {
        int databaseSizeBeforeUpdate = rpmQuoteRepository.findAll().size();
        rpmQuote.setId(count.incrementAndGet());

        // Create the RpmQuote
        RpmQuoteDTO rpmQuoteDTO = rpmQuoteMapper.toDto(rpmQuote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmQuoteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmQuoteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RpmQuote in the database
        List<RpmQuote> rpmQuoteList = rpmQuoteRepository.findAll();
        assertThat(rpmQuoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRpmQuoteWithPatch() throws Exception {
        // Initialize the database
        rpmQuoteRepository.saveAndFlush(rpmQuote);

        int databaseSizeBeforeUpdate = rpmQuoteRepository.findAll().size();

        // Update the rpmQuote using partial update
        RpmQuote partialUpdatedRpmQuote = new RpmQuote();
        partialUpdatedRpmQuote.setId(rpmQuote.getId());

        partialUpdatedRpmQuote.name(UPDATED_NAME);

        restRpmQuoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRpmQuote.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRpmQuote))
            )
            .andExpect(status().isOk());

        // Validate the RpmQuote in the database
        List<RpmQuote> rpmQuoteList = rpmQuoteRepository.findAll();
        assertThat(rpmQuoteList).hasSize(databaseSizeBeforeUpdate);
        RpmQuote testRpmQuote = rpmQuoteList.get(rpmQuoteList.size() - 1);
        assertThat(testRpmQuote.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateRpmQuoteWithPatch() throws Exception {
        // Initialize the database
        rpmQuoteRepository.saveAndFlush(rpmQuote);

        int databaseSizeBeforeUpdate = rpmQuoteRepository.findAll().size();

        // Update the rpmQuote using partial update
        RpmQuote partialUpdatedRpmQuote = new RpmQuote();
        partialUpdatedRpmQuote.setId(rpmQuote.getId());

        partialUpdatedRpmQuote.name(UPDATED_NAME);

        restRpmQuoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRpmQuote.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRpmQuote))
            )
            .andExpect(status().isOk());

        // Validate the RpmQuote in the database
        List<RpmQuote> rpmQuoteList = rpmQuoteRepository.findAll();
        assertThat(rpmQuoteList).hasSize(databaseSizeBeforeUpdate);
        RpmQuote testRpmQuote = rpmQuoteList.get(rpmQuoteList.size() - 1);
        assertThat(testRpmQuote.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingRpmQuote() throws Exception {
        int databaseSizeBeforeUpdate = rpmQuoteRepository.findAll().size();
        rpmQuote.setId(count.incrementAndGet());

        // Create the RpmQuote
        RpmQuoteDTO rpmQuoteDTO = rpmQuoteMapper.toDto(rpmQuote);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRpmQuoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rpmQuoteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rpmQuoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmQuote in the database
        List<RpmQuote> rpmQuoteList = rpmQuoteRepository.findAll();
        assertThat(rpmQuoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRpmQuote() throws Exception {
        int databaseSizeBeforeUpdate = rpmQuoteRepository.findAll().size();
        rpmQuote.setId(count.incrementAndGet());

        // Create the RpmQuote
        RpmQuoteDTO rpmQuoteDTO = rpmQuoteMapper.toDto(rpmQuote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmQuoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rpmQuoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmQuote in the database
        List<RpmQuote> rpmQuoteList = rpmQuoteRepository.findAll();
        assertThat(rpmQuoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRpmQuote() throws Exception {
        int databaseSizeBeforeUpdate = rpmQuoteRepository.findAll().size();
        rpmQuote.setId(count.incrementAndGet());

        // Create the RpmQuote
        RpmQuoteDTO rpmQuoteDTO = rpmQuoteMapper.toDto(rpmQuote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmQuoteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rpmQuoteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RpmQuote in the database
        List<RpmQuote> rpmQuoteList = rpmQuoteRepository.findAll();
        assertThat(rpmQuoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRpmQuote() throws Exception {
        // Initialize the database
        rpmQuoteRepository.saveAndFlush(rpmQuote);

        int databaseSizeBeforeDelete = rpmQuoteRepository.findAll().size();

        // Delete the rpmQuote
        restRpmQuoteMockMvc
            .perform(delete(ENTITY_API_URL_ID, rpmQuote.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RpmQuote> rpmQuoteList = rpmQuoteRepository.findAll();
        assertThat(rpmQuoteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
