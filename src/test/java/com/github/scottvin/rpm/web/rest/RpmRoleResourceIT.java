package com.github.scottvin.rpm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.github.scottvin.rpm.IntegrationTest;
import com.github.scottvin.rpm.domain.RpmRole;
import com.github.scottvin.rpm.repository.RpmRoleRepository;
import com.github.scottvin.rpm.service.dto.RpmRoleDTO;
import com.github.scottvin.rpm.service.mapper.RpmRoleMapper;
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
 * Integration tests for the {@link RpmRoleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RpmRoleResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/rpm-roles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RpmRoleRepository rpmRoleRepository;

    @Autowired
    private RpmRoleMapper rpmRoleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRpmRoleMockMvc;

    private RpmRole rpmRole;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RpmRole createEntity(EntityManager em) {
        RpmRole rpmRole = new RpmRole().name(DEFAULT_NAME);
        return rpmRole;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RpmRole createUpdatedEntity(EntityManager em) {
        RpmRole rpmRole = new RpmRole().name(UPDATED_NAME);
        return rpmRole;
    }

    @BeforeEach
    public void initTest() {
        rpmRole = createEntity(em);
    }

    @Test
    @Transactional
    void createRpmRole() throws Exception {
        int databaseSizeBeforeCreate = rpmRoleRepository.findAll().size();
        // Create the RpmRole
        RpmRoleDTO rpmRoleDTO = rpmRoleMapper.toDto(rpmRole);
        restRpmRoleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmRoleDTO)))
            .andExpect(status().isCreated());

        // Validate the RpmRole in the database
        List<RpmRole> rpmRoleList = rpmRoleRepository.findAll();
        assertThat(rpmRoleList).hasSize(databaseSizeBeforeCreate + 1);
        RpmRole testRpmRole = rpmRoleList.get(rpmRoleList.size() - 1);
        assertThat(testRpmRole.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createRpmRoleWithExistingId() throws Exception {
        // Create the RpmRole with an existing ID
        rpmRole.setId(1L);
        RpmRoleDTO rpmRoleDTO = rpmRoleMapper.toDto(rpmRole);

        int databaseSizeBeforeCreate = rpmRoleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRpmRoleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmRoleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RpmRole in the database
        List<RpmRole> rpmRoleList = rpmRoleRepository.findAll();
        assertThat(rpmRoleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = rpmRoleRepository.findAll().size();
        // set the field null
        rpmRole.setName(null);

        // Create the RpmRole, which fails.
        RpmRoleDTO rpmRoleDTO = rpmRoleMapper.toDto(rpmRole);

        restRpmRoleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmRoleDTO)))
            .andExpect(status().isBadRequest());

        List<RpmRole> rpmRoleList = rpmRoleRepository.findAll();
        assertThat(rpmRoleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRpmRoles() throws Exception {
        // Initialize the database
        rpmRoleRepository.saveAndFlush(rpmRole);

        // Get all the rpmRoleList
        restRpmRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rpmRole.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getRpmRole() throws Exception {
        // Initialize the database
        rpmRoleRepository.saveAndFlush(rpmRole);

        // Get the rpmRole
        restRpmRoleMockMvc
            .perform(get(ENTITY_API_URL_ID, rpmRole.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rpmRole.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingRpmRole() throws Exception {
        // Get the rpmRole
        restRpmRoleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRpmRole() throws Exception {
        // Initialize the database
        rpmRoleRepository.saveAndFlush(rpmRole);

        int databaseSizeBeforeUpdate = rpmRoleRepository.findAll().size();

        // Update the rpmRole
        RpmRole updatedRpmRole = rpmRoleRepository.findById(rpmRole.getId()).get();
        // Disconnect from session so that the updates on updatedRpmRole are not directly saved in db
        em.detach(updatedRpmRole);
        updatedRpmRole.name(UPDATED_NAME);
        RpmRoleDTO rpmRoleDTO = rpmRoleMapper.toDto(updatedRpmRole);

        restRpmRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rpmRoleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmRoleDTO))
            )
            .andExpect(status().isOk());

        // Validate the RpmRole in the database
        List<RpmRole> rpmRoleList = rpmRoleRepository.findAll();
        assertThat(rpmRoleList).hasSize(databaseSizeBeforeUpdate);
        RpmRole testRpmRole = rpmRoleList.get(rpmRoleList.size() - 1);
        assertThat(testRpmRole.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingRpmRole() throws Exception {
        int databaseSizeBeforeUpdate = rpmRoleRepository.findAll().size();
        rpmRole.setId(count.incrementAndGet());

        // Create the RpmRole
        RpmRoleDTO rpmRoleDTO = rpmRoleMapper.toDto(rpmRole);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRpmRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rpmRoleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmRoleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmRole in the database
        List<RpmRole> rpmRoleList = rpmRoleRepository.findAll();
        assertThat(rpmRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRpmRole() throws Exception {
        int databaseSizeBeforeUpdate = rpmRoleRepository.findAll().size();
        rpmRole.setId(count.incrementAndGet());

        // Create the RpmRole
        RpmRoleDTO rpmRoleDTO = rpmRoleMapper.toDto(rpmRole);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmRoleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmRole in the database
        List<RpmRole> rpmRoleList = rpmRoleRepository.findAll();
        assertThat(rpmRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRpmRole() throws Exception {
        int databaseSizeBeforeUpdate = rpmRoleRepository.findAll().size();
        rpmRole.setId(count.incrementAndGet());

        // Create the RpmRole
        RpmRoleDTO rpmRoleDTO = rpmRoleMapper.toDto(rpmRole);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmRoleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmRoleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RpmRole in the database
        List<RpmRole> rpmRoleList = rpmRoleRepository.findAll();
        assertThat(rpmRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRpmRoleWithPatch() throws Exception {
        // Initialize the database
        rpmRoleRepository.saveAndFlush(rpmRole);

        int databaseSizeBeforeUpdate = rpmRoleRepository.findAll().size();

        // Update the rpmRole using partial update
        RpmRole partialUpdatedRpmRole = new RpmRole();
        partialUpdatedRpmRole.setId(rpmRole.getId());

        restRpmRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRpmRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRpmRole))
            )
            .andExpect(status().isOk());

        // Validate the RpmRole in the database
        List<RpmRole> rpmRoleList = rpmRoleRepository.findAll();
        assertThat(rpmRoleList).hasSize(databaseSizeBeforeUpdate);
        RpmRole testRpmRole = rpmRoleList.get(rpmRoleList.size() - 1);
        assertThat(testRpmRole.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateRpmRoleWithPatch() throws Exception {
        // Initialize the database
        rpmRoleRepository.saveAndFlush(rpmRole);

        int databaseSizeBeforeUpdate = rpmRoleRepository.findAll().size();

        // Update the rpmRole using partial update
        RpmRole partialUpdatedRpmRole = new RpmRole();
        partialUpdatedRpmRole.setId(rpmRole.getId());

        partialUpdatedRpmRole.name(UPDATED_NAME);

        restRpmRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRpmRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRpmRole))
            )
            .andExpect(status().isOk());

        // Validate the RpmRole in the database
        List<RpmRole> rpmRoleList = rpmRoleRepository.findAll();
        assertThat(rpmRoleList).hasSize(databaseSizeBeforeUpdate);
        RpmRole testRpmRole = rpmRoleList.get(rpmRoleList.size() - 1);
        assertThat(testRpmRole.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingRpmRole() throws Exception {
        int databaseSizeBeforeUpdate = rpmRoleRepository.findAll().size();
        rpmRole.setId(count.incrementAndGet());

        // Create the RpmRole
        RpmRoleDTO rpmRoleDTO = rpmRoleMapper.toDto(rpmRole);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRpmRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rpmRoleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rpmRoleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmRole in the database
        List<RpmRole> rpmRoleList = rpmRoleRepository.findAll();
        assertThat(rpmRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRpmRole() throws Exception {
        int databaseSizeBeforeUpdate = rpmRoleRepository.findAll().size();
        rpmRole.setId(count.incrementAndGet());

        // Create the RpmRole
        RpmRoleDTO rpmRoleDTO = rpmRoleMapper.toDto(rpmRole);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rpmRoleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmRole in the database
        List<RpmRole> rpmRoleList = rpmRoleRepository.findAll();
        assertThat(rpmRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRpmRole() throws Exception {
        int databaseSizeBeforeUpdate = rpmRoleRepository.findAll().size();
        rpmRole.setId(count.incrementAndGet());

        // Create the RpmRole
        RpmRoleDTO rpmRoleDTO = rpmRoleMapper.toDto(rpmRole);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmRoleMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rpmRoleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RpmRole in the database
        List<RpmRole> rpmRoleList = rpmRoleRepository.findAll();
        assertThat(rpmRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRpmRole() throws Exception {
        // Initialize the database
        rpmRoleRepository.saveAndFlush(rpmRole);

        int databaseSizeBeforeDelete = rpmRoleRepository.findAll().size();

        // Delete the rpmRole
        restRpmRoleMockMvc
            .perform(delete(ENTITY_API_URL_ID, rpmRole.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RpmRole> rpmRoleList = rpmRoleRepository.findAll();
        assertThat(rpmRoleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
