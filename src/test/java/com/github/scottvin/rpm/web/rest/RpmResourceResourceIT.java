package com.github.scottvin.rpm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.github.scottvin.rpm.IntegrationTest;
import com.github.scottvin.rpm.domain.RpmAction;
import com.github.scottvin.rpm.domain.RpmResource;
import com.github.scottvin.rpm.repository.RpmResourceRepository;
import com.github.scottvin.rpm.service.dto.RpmResourceDTO;
import com.github.scottvin.rpm.service.mapper.RpmResourceMapper;
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
 * Integration tests for the {@link RpmResourceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RpmResourceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/rpm-resources";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RpmResourceRepository rpmResourceRepository;

    @Autowired
    private RpmResourceMapper rpmResourceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRpmResourceMockMvc;

    private RpmResource rpmResource;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RpmResource createEntity(EntityManager em) {
        RpmResource rpmResource = new RpmResource().name(DEFAULT_NAME);
        // Add required entity
        RpmAction rpmAction;
        if (TestUtil.findAll(em, RpmAction.class).isEmpty()) {
            rpmAction = RpmActionResourceIT.createEntity(em);
            em.persist(rpmAction);
            em.flush();
        } else {
            rpmAction = TestUtil.findAll(em, RpmAction.class).get(0);
        }
        rpmResource.setAction(rpmAction);
        return rpmResource;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RpmResource createUpdatedEntity(EntityManager em) {
        RpmResource rpmResource = new RpmResource().name(UPDATED_NAME);
        // Add required entity
        RpmAction rpmAction;
        if (TestUtil.findAll(em, RpmAction.class).isEmpty()) {
            rpmAction = RpmActionResourceIT.createUpdatedEntity(em);
            em.persist(rpmAction);
            em.flush();
        } else {
            rpmAction = TestUtil.findAll(em, RpmAction.class).get(0);
        }
        rpmResource.setAction(rpmAction);
        return rpmResource;
    }

    @BeforeEach
    public void initTest() {
        rpmResource = createEntity(em);
    }

    @Test
    @Transactional
    void createRpmResource() throws Exception {
        int databaseSizeBeforeCreate = rpmResourceRepository.findAll().size();
        // Create the RpmResource
        RpmResourceDTO rpmResourceDTO = rpmResourceMapper.toDto(rpmResource);
        restRpmResourceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmResourceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the RpmResource in the database
        List<RpmResource> rpmResourceList = rpmResourceRepository.findAll();
        assertThat(rpmResourceList).hasSize(databaseSizeBeforeCreate + 1);
        RpmResource testRpmResource = rpmResourceList.get(rpmResourceList.size() - 1);
        assertThat(testRpmResource.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createRpmResourceWithExistingId() throws Exception {
        // Create the RpmResource with an existing ID
        rpmResource.setId(1L);
        RpmResourceDTO rpmResourceDTO = rpmResourceMapper.toDto(rpmResource);

        int databaseSizeBeforeCreate = rpmResourceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRpmResourceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmResourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmResource in the database
        List<RpmResource> rpmResourceList = rpmResourceRepository.findAll();
        assertThat(rpmResourceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = rpmResourceRepository.findAll().size();
        // set the field null
        rpmResource.setName(null);

        // Create the RpmResource, which fails.
        RpmResourceDTO rpmResourceDTO = rpmResourceMapper.toDto(rpmResource);

        restRpmResourceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmResourceDTO))
            )
            .andExpect(status().isBadRequest());

        List<RpmResource> rpmResourceList = rpmResourceRepository.findAll();
        assertThat(rpmResourceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRpmResources() throws Exception {
        // Initialize the database
        rpmResourceRepository.saveAndFlush(rpmResource);

        // Get all the rpmResourceList
        restRpmResourceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rpmResource.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getRpmResource() throws Exception {
        // Initialize the database
        rpmResourceRepository.saveAndFlush(rpmResource);

        // Get the rpmResource
        restRpmResourceMockMvc
            .perform(get(ENTITY_API_URL_ID, rpmResource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rpmResource.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingRpmResource() throws Exception {
        // Get the rpmResource
        restRpmResourceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRpmResource() throws Exception {
        // Initialize the database
        rpmResourceRepository.saveAndFlush(rpmResource);

        int databaseSizeBeforeUpdate = rpmResourceRepository.findAll().size();

        // Update the rpmResource
        RpmResource updatedRpmResource = rpmResourceRepository.findById(rpmResource.getId()).get();
        // Disconnect from session so that the updates on updatedRpmResource are not directly saved in db
        em.detach(updatedRpmResource);
        updatedRpmResource.name(UPDATED_NAME);
        RpmResourceDTO rpmResourceDTO = rpmResourceMapper.toDto(updatedRpmResource);

        restRpmResourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rpmResourceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmResourceDTO))
            )
            .andExpect(status().isOk());

        // Validate the RpmResource in the database
        List<RpmResource> rpmResourceList = rpmResourceRepository.findAll();
        assertThat(rpmResourceList).hasSize(databaseSizeBeforeUpdate);
        RpmResource testRpmResource = rpmResourceList.get(rpmResourceList.size() - 1);
        assertThat(testRpmResource.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingRpmResource() throws Exception {
        int databaseSizeBeforeUpdate = rpmResourceRepository.findAll().size();
        rpmResource.setId(count.incrementAndGet());

        // Create the RpmResource
        RpmResourceDTO rpmResourceDTO = rpmResourceMapper.toDto(rpmResource);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRpmResourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rpmResourceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmResourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmResource in the database
        List<RpmResource> rpmResourceList = rpmResourceRepository.findAll();
        assertThat(rpmResourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRpmResource() throws Exception {
        int databaseSizeBeforeUpdate = rpmResourceRepository.findAll().size();
        rpmResource.setId(count.incrementAndGet());

        // Create the RpmResource
        RpmResourceDTO rpmResourceDTO = rpmResourceMapper.toDto(rpmResource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmResourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmResourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmResource in the database
        List<RpmResource> rpmResourceList = rpmResourceRepository.findAll();
        assertThat(rpmResourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRpmResource() throws Exception {
        int databaseSizeBeforeUpdate = rpmResourceRepository.findAll().size();
        rpmResource.setId(count.incrementAndGet());

        // Create the RpmResource
        RpmResourceDTO rpmResourceDTO = rpmResourceMapper.toDto(rpmResource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmResourceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmResourceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RpmResource in the database
        List<RpmResource> rpmResourceList = rpmResourceRepository.findAll();
        assertThat(rpmResourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRpmResourceWithPatch() throws Exception {
        // Initialize the database
        rpmResourceRepository.saveAndFlush(rpmResource);

        int databaseSizeBeforeUpdate = rpmResourceRepository.findAll().size();

        // Update the rpmResource using partial update
        RpmResource partialUpdatedRpmResource = new RpmResource();
        partialUpdatedRpmResource.setId(rpmResource.getId());

        partialUpdatedRpmResource.name(UPDATED_NAME);

        restRpmResourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRpmResource.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRpmResource))
            )
            .andExpect(status().isOk());

        // Validate the RpmResource in the database
        List<RpmResource> rpmResourceList = rpmResourceRepository.findAll();
        assertThat(rpmResourceList).hasSize(databaseSizeBeforeUpdate);
        RpmResource testRpmResource = rpmResourceList.get(rpmResourceList.size() - 1);
        assertThat(testRpmResource.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateRpmResourceWithPatch() throws Exception {
        // Initialize the database
        rpmResourceRepository.saveAndFlush(rpmResource);

        int databaseSizeBeforeUpdate = rpmResourceRepository.findAll().size();

        // Update the rpmResource using partial update
        RpmResource partialUpdatedRpmResource = new RpmResource();
        partialUpdatedRpmResource.setId(rpmResource.getId());

        partialUpdatedRpmResource.name(UPDATED_NAME);

        restRpmResourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRpmResource.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRpmResource))
            )
            .andExpect(status().isOk());

        // Validate the RpmResource in the database
        List<RpmResource> rpmResourceList = rpmResourceRepository.findAll();
        assertThat(rpmResourceList).hasSize(databaseSizeBeforeUpdate);
        RpmResource testRpmResource = rpmResourceList.get(rpmResourceList.size() - 1);
        assertThat(testRpmResource.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingRpmResource() throws Exception {
        int databaseSizeBeforeUpdate = rpmResourceRepository.findAll().size();
        rpmResource.setId(count.incrementAndGet());

        // Create the RpmResource
        RpmResourceDTO rpmResourceDTO = rpmResourceMapper.toDto(rpmResource);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRpmResourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rpmResourceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rpmResourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmResource in the database
        List<RpmResource> rpmResourceList = rpmResourceRepository.findAll();
        assertThat(rpmResourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRpmResource() throws Exception {
        int databaseSizeBeforeUpdate = rpmResourceRepository.findAll().size();
        rpmResource.setId(count.incrementAndGet());

        // Create the RpmResource
        RpmResourceDTO rpmResourceDTO = rpmResourceMapper.toDto(rpmResource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmResourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rpmResourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmResource in the database
        List<RpmResource> rpmResourceList = rpmResourceRepository.findAll();
        assertThat(rpmResourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRpmResource() throws Exception {
        int databaseSizeBeforeUpdate = rpmResourceRepository.findAll().size();
        rpmResource.setId(count.incrementAndGet());

        // Create the RpmResource
        RpmResourceDTO rpmResourceDTO = rpmResourceMapper.toDto(rpmResource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmResourceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rpmResourceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RpmResource in the database
        List<RpmResource> rpmResourceList = rpmResourceRepository.findAll();
        assertThat(rpmResourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRpmResource() throws Exception {
        // Initialize the database
        rpmResourceRepository.saveAndFlush(rpmResource);

        int databaseSizeBeforeDelete = rpmResourceRepository.findAll().size();

        // Delete the rpmResource
        restRpmResourceMockMvc
            .perform(delete(ENTITY_API_URL_ID, rpmResource.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RpmResource> rpmResourceList = rpmResourceRepository.findAll();
        assertThat(rpmResourceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
