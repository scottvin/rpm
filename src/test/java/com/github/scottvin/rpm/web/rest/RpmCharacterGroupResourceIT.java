package com.github.scottvin.rpm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.github.scottvin.rpm.IntegrationTest;
import com.github.scottvin.rpm.domain.RpmCharacterGroup;
import com.github.scottvin.rpm.repository.RpmCharacterGroupRepository;
import com.github.scottvin.rpm.service.dto.RpmCharacterGroupDTO;
import com.github.scottvin.rpm.service.mapper.RpmCharacterGroupMapper;
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
 * Integration tests for the {@link RpmCharacterGroupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RpmCharacterGroupResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRIORITY = 1;
    private static final Integer UPDATED_PRIORITY = 2;

    private static final String ENTITY_API_URL = "/api/rpm-character-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RpmCharacterGroupRepository rpmCharacterGroupRepository;

    @Autowired
    private RpmCharacterGroupMapper rpmCharacterGroupMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRpmCharacterGroupMockMvc;

    private RpmCharacterGroup rpmCharacterGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RpmCharacterGroup createEntity(EntityManager em) {
        RpmCharacterGroup rpmCharacterGroup = new RpmCharacterGroup().name(DEFAULT_NAME).priority(DEFAULT_PRIORITY);
        return rpmCharacterGroup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RpmCharacterGroup createUpdatedEntity(EntityManager em) {
        RpmCharacterGroup rpmCharacterGroup = new RpmCharacterGroup().name(UPDATED_NAME).priority(UPDATED_PRIORITY);
        return rpmCharacterGroup;
    }

    @BeforeEach
    public void initTest() {
        rpmCharacterGroup = createEntity(em);
    }

    @Test
    @Transactional
    void createRpmCharacterGroup() throws Exception {
        int databaseSizeBeforeCreate = rpmCharacterGroupRepository.findAll().size();
        // Create the RpmCharacterGroup
        RpmCharacterGroupDTO rpmCharacterGroupDTO = rpmCharacterGroupMapper.toDto(rpmCharacterGroup);
        restRpmCharacterGroupMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmCharacterGroupDTO))
            )
            .andExpect(status().isCreated());

        // Validate the RpmCharacterGroup in the database
        List<RpmCharacterGroup> rpmCharacterGroupList = rpmCharacterGroupRepository.findAll();
        assertThat(rpmCharacterGroupList).hasSize(databaseSizeBeforeCreate + 1);
        RpmCharacterGroup testRpmCharacterGroup = rpmCharacterGroupList.get(rpmCharacterGroupList.size() - 1);
        assertThat(testRpmCharacterGroup.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRpmCharacterGroup.getPriority()).isEqualTo(DEFAULT_PRIORITY);
    }

    @Test
    @Transactional
    void createRpmCharacterGroupWithExistingId() throws Exception {
        // Create the RpmCharacterGroup with an existing ID
        rpmCharacterGroup.setId(1L);
        RpmCharacterGroupDTO rpmCharacterGroupDTO = rpmCharacterGroupMapper.toDto(rpmCharacterGroup);

        int databaseSizeBeforeCreate = rpmCharacterGroupRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRpmCharacterGroupMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmCharacterGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmCharacterGroup in the database
        List<RpmCharacterGroup> rpmCharacterGroupList = rpmCharacterGroupRepository.findAll();
        assertThat(rpmCharacterGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = rpmCharacterGroupRepository.findAll().size();
        // set the field null
        rpmCharacterGroup.setName(null);

        // Create the RpmCharacterGroup, which fails.
        RpmCharacterGroupDTO rpmCharacterGroupDTO = rpmCharacterGroupMapper.toDto(rpmCharacterGroup);

        restRpmCharacterGroupMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmCharacterGroupDTO))
            )
            .andExpect(status().isBadRequest());

        List<RpmCharacterGroup> rpmCharacterGroupList = rpmCharacterGroupRepository.findAll();
        assertThat(rpmCharacterGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriorityIsRequired() throws Exception {
        int databaseSizeBeforeTest = rpmCharacterGroupRepository.findAll().size();
        // set the field null
        rpmCharacterGroup.setPriority(null);

        // Create the RpmCharacterGroup, which fails.
        RpmCharacterGroupDTO rpmCharacterGroupDTO = rpmCharacterGroupMapper.toDto(rpmCharacterGroup);

        restRpmCharacterGroupMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmCharacterGroupDTO))
            )
            .andExpect(status().isBadRequest());

        List<RpmCharacterGroup> rpmCharacterGroupList = rpmCharacterGroupRepository.findAll();
        assertThat(rpmCharacterGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRpmCharacterGroups() throws Exception {
        // Initialize the database
        rpmCharacterGroupRepository.saveAndFlush(rpmCharacterGroup);

        // Get all the rpmCharacterGroupList
        restRpmCharacterGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rpmCharacterGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)));
    }

    @Test
    @Transactional
    void getRpmCharacterGroup() throws Exception {
        // Initialize the database
        rpmCharacterGroupRepository.saveAndFlush(rpmCharacterGroup);

        // Get the rpmCharacterGroup
        restRpmCharacterGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, rpmCharacterGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rpmCharacterGroup.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY));
    }

    @Test
    @Transactional
    void getNonExistingRpmCharacterGroup() throws Exception {
        // Get the rpmCharacterGroup
        restRpmCharacterGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRpmCharacterGroup() throws Exception {
        // Initialize the database
        rpmCharacterGroupRepository.saveAndFlush(rpmCharacterGroup);

        int databaseSizeBeforeUpdate = rpmCharacterGroupRepository.findAll().size();

        // Update the rpmCharacterGroup
        RpmCharacterGroup updatedRpmCharacterGroup = rpmCharacterGroupRepository.findById(rpmCharacterGroup.getId()).get();
        // Disconnect from session so that the updates on updatedRpmCharacterGroup are not directly saved in db
        em.detach(updatedRpmCharacterGroup);
        updatedRpmCharacterGroup.name(UPDATED_NAME).priority(UPDATED_PRIORITY);
        RpmCharacterGroupDTO rpmCharacterGroupDTO = rpmCharacterGroupMapper.toDto(updatedRpmCharacterGroup);

        restRpmCharacterGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rpmCharacterGroupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmCharacterGroupDTO))
            )
            .andExpect(status().isOk());

        // Validate the RpmCharacterGroup in the database
        List<RpmCharacterGroup> rpmCharacterGroupList = rpmCharacterGroupRepository.findAll();
        assertThat(rpmCharacterGroupList).hasSize(databaseSizeBeforeUpdate);
        RpmCharacterGroup testRpmCharacterGroup = rpmCharacterGroupList.get(rpmCharacterGroupList.size() - 1);
        assertThat(testRpmCharacterGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRpmCharacterGroup.getPriority()).isEqualTo(UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void putNonExistingRpmCharacterGroup() throws Exception {
        int databaseSizeBeforeUpdate = rpmCharacterGroupRepository.findAll().size();
        rpmCharacterGroup.setId(count.incrementAndGet());

        // Create the RpmCharacterGroup
        RpmCharacterGroupDTO rpmCharacterGroupDTO = rpmCharacterGroupMapper.toDto(rpmCharacterGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRpmCharacterGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rpmCharacterGroupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmCharacterGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmCharacterGroup in the database
        List<RpmCharacterGroup> rpmCharacterGroupList = rpmCharacterGroupRepository.findAll();
        assertThat(rpmCharacterGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRpmCharacterGroup() throws Exception {
        int databaseSizeBeforeUpdate = rpmCharacterGroupRepository.findAll().size();
        rpmCharacterGroup.setId(count.incrementAndGet());

        // Create the RpmCharacterGroup
        RpmCharacterGroupDTO rpmCharacterGroupDTO = rpmCharacterGroupMapper.toDto(rpmCharacterGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmCharacterGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmCharacterGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmCharacterGroup in the database
        List<RpmCharacterGroup> rpmCharacterGroupList = rpmCharacterGroupRepository.findAll();
        assertThat(rpmCharacterGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRpmCharacterGroup() throws Exception {
        int databaseSizeBeforeUpdate = rpmCharacterGroupRepository.findAll().size();
        rpmCharacterGroup.setId(count.incrementAndGet());

        // Create the RpmCharacterGroup
        RpmCharacterGroupDTO rpmCharacterGroupDTO = rpmCharacterGroupMapper.toDto(rpmCharacterGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmCharacterGroupMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmCharacterGroupDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RpmCharacterGroup in the database
        List<RpmCharacterGroup> rpmCharacterGroupList = rpmCharacterGroupRepository.findAll();
        assertThat(rpmCharacterGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRpmCharacterGroupWithPatch() throws Exception {
        // Initialize the database
        rpmCharacterGroupRepository.saveAndFlush(rpmCharacterGroup);

        int databaseSizeBeforeUpdate = rpmCharacterGroupRepository.findAll().size();

        // Update the rpmCharacterGroup using partial update
        RpmCharacterGroup partialUpdatedRpmCharacterGroup = new RpmCharacterGroup();
        partialUpdatedRpmCharacterGroup.setId(rpmCharacterGroup.getId());

        partialUpdatedRpmCharacterGroup.priority(UPDATED_PRIORITY);

        restRpmCharacterGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRpmCharacterGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRpmCharacterGroup))
            )
            .andExpect(status().isOk());

        // Validate the RpmCharacterGroup in the database
        List<RpmCharacterGroup> rpmCharacterGroupList = rpmCharacterGroupRepository.findAll();
        assertThat(rpmCharacterGroupList).hasSize(databaseSizeBeforeUpdate);
        RpmCharacterGroup testRpmCharacterGroup = rpmCharacterGroupList.get(rpmCharacterGroupList.size() - 1);
        assertThat(testRpmCharacterGroup.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRpmCharacterGroup.getPriority()).isEqualTo(UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void fullUpdateRpmCharacterGroupWithPatch() throws Exception {
        // Initialize the database
        rpmCharacterGroupRepository.saveAndFlush(rpmCharacterGroup);

        int databaseSizeBeforeUpdate = rpmCharacterGroupRepository.findAll().size();

        // Update the rpmCharacterGroup using partial update
        RpmCharacterGroup partialUpdatedRpmCharacterGroup = new RpmCharacterGroup();
        partialUpdatedRpmCharacterGroup.setId(rpmCharacterGroup.getId());

        partialUpdatedRpmCharacterGroup.name(UPDATED_NAME).priority(UPDATED_PRIORITY);

        restRpmCharacterGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRpmCharacterGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRpmCharacterGroup))
            )
            .andExpect(status().isOk());

        // Validate the RpmCharacterGroup in the database
        List<RpmCharacterGroup> rpmCharacterGroupList = rpmCharacterGroupRepository.findAll();
        assertThat(rpmCharacterGroupList).hasSize(databaseSizeBeforeUpdate);
        RpmCharacterGroup testRpmCharacterGroup = rpmCharacterGroupList.get(rpmCharacterGroupList.size() - 1);
        assertThat(testRpmCharacterGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRpmCharacterGroup.getPriority()).isEqualTo(UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void patchNonExistingRpmCharacterGroup() throws Exception {
        int databaseSizeBeforeUpdate = rpmCharacterGroupRepository.findAll().size();
        rpmCharacterGroup.setId(count.incrementAndGet());

        // Create the RpmCharacterGroup
        RpmCharacterGroupDTO rpmCharacterGroupDTO = rpmCharacterGroupMapper.toDto(rpmCharacterGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRpmCharacterGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rpmCharacterGroupDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rpmCharacterGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmCharacterGroup in the database
        List<RpmCharacterGroup> rpmCharacterGroupList = rpmCharacterGroupRepository.findAll();
        assertThat(rpmCharacterGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRpmCharacterGroup() throws Exception {
        int databaseSizeBeforeUpdate = rpmCharacterGroupRepository.findAll().size();
        rpmCharacterGroup.setId(count.incrementAndGet());

        // Create the RpmCharacterGroup
        RpmCharacterGroupDTO rpmCharacterGroupDTO = rpmCharacterGroupMapper.toDto(rpmCharacterGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmCharacterGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rpmCharacterGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmCharacterGroup in the database
        List<RpmCharacterGroup> rpmCharacterGroupList = rpmCharacterGroupRepository.findAll();
        assertThat(rpmCharacterGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRpmCharacterGroup() throws Exception {
        int databaseSizeBeforeUpdate = rpmCharacterGroupRepository.findAll().size();
        rpmCharacterGroup.setId(count.incrementAndGet());

        // Create the RpmCharacterGroup
        RpmCharacterGroupDTO rpmCharacterGroupDTO = rpmCharacterGroupMapper.toDto(rpmCharacterGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmCharacterGroupMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rpmCharacterGroupDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RpmCharacterGroup in the database
        List<RpmCharacterGroup> rpmCharacterGroupList = rpmCharacterGroupRepository.findAll();
        assertThat(rpmCharacterGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRpmCharacterGroup() throws Exception {
        // Initialize the database
        rpmCharacterGroupRepository.saveAndFlush(rpmCharacterGroup);

        int databaseSizeBeforeDelete = rpmCharacterGroupRepository.findAll().size();

        // Delete the rpmCharacterGroup
        restRpmCharacterGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, rpmCharacterGroup.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RpmCharacterGroup> rpmCharacterGroupList = rpmCharacterGroupRepository.findAll();
        assertThat(rpmCharacterGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
