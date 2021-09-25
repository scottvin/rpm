package com.github.scottvin.rpm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.github.scottvin.rpm.IntegrationTest;
import com.github.scottvin.rpm.domain.RpmCharacter;
import com.github.scottvin.rpm.domain.RpmResult;
import com.github.scottvin.rpm.repository.RpmCharacterRepository;
import com.github.scottvin.rpm.service.criteria.RpmCharacterCriteria;
import com.github.scottvin.rpm.service.dto.RpmCharacterDTO;
import com.github.scottvin.rpm.service.mapper.RpmCharacterMapper;
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
 * Integration tests for the {@link RpmCharacterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RpmCharacterResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRIORITY = 1;
    private static final Integer UPDATED_PRIORITY = 2;
    private static final Integer SMALLER_PRIORITY = 1 - 1;

    private static final String ENTITY_API_URL = "/api/rpm-characters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RpmCharacterRepository rpmCharacterRepository;

    @Autowired
    private RpmCharacterMapper rpmCharacterMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRpmCharacterMockMvc;

    private RpmCharacter rpmCharacter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RpmCharacter createEntity(EntityManager em) {
        RpmCharacter rpmCharacter = new RpmCharacter().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).priority(DEFAULT_PRIORITY);
        // Add required entity
        RpmResult rpmResult;
        if (TestUtil.findAll(em, RpmResult.class).isEmpty()) {
            rpmResult = RpmResultResourceIT.createEntity(em);
            em.persist(rpmResult);
            em.flush();
        } else {
            rpmResult = TestUtil.findAll(em, RpmResult.class).get(0);
        }
        rpmCharacter.setResult(rpmResult);
        return rpmCharacter;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RpmCharacter createUpdatedEntity(EntityManager em) {
        RpmCharacter rpmCharacter = new RpmCharacter().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).priority(UPDATED_PRIORITY);
        // Add required entity
        RpmResult rpmResult;
        if (TestUtil.findAll(em, RpmResult.class).isEmpty()) {
            rpmResult = RpmResultResourceIT.createUpdatedEntity(em);
            em.persist(rpmResult);
            em.flush();
        } else {
            rpmResult = TestUtil.findAll(em, RpmResult.class).get(0);
        }
        rpmCharacter.setResult(rpmResult);
        return rpmCharacter;
    }

    @BeforeEach
    public void initTest() {
        rpmCharacter = createEntity(em);
    }

    @Test
    @Transactional
    void createRpmCharacter() throws Exception {
        int databaseSizeBeforeCreate = rpmCharacterRepository.findAll().size();
        // Create the RpmCharacter
        RpmCharacterDTO rpmCharacterDTO = rpmCharacterMapper.toDto(rpmCharacter);
        restRpmCharacterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmCharacterDTO))
            )
            .andExpect(status().isCreated());

        // Validate the RpmCharacter in the database
        List<RpmCharacter> rpmCharacterList = rpmCharacterRepository.findAll();
        assertThat(rpmCharacterList).hasSize(databaseSizeBeforeCreate + 1);
        RpmCharacter testRpmCharacter = rpmCharacterList.get(rpmCharacterList.size() - 1);
        assertThat(testRpmCharacter.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRpmCharacter.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRpmCharacter.getPriority()).isEqualTo(DEFAULT_PRIORITY);
    }

    @Test
    @Transactional
    void createRpmCharacterWithExistingId() throws Exception {
        // Create the RpmCharacter with an existing ID
        rpmCharacter.setId(1L);
        RpmCharacterDTO rpmCharacterDTO = rpmCharacterMapper.toDto(rpmCharacter);

        int databaseSizeBeforeCreate = rpmCharacterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRpmCharacterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmCharacterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmCharacter in the database
        List<RpmCharacter> rpmCharacterList = rpmCharacterRepository.findAll();
        assertThat(rpmCharacterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = rpmCharacterRepository.findAll().size();
        // set the field null
        rpmCharacter.setName(null);

        // Create the RpmCharacter, which fails.
        RpmCharacterDTO rpmCharacterDTO = rpmCharacterMapper.toDto(rpmCharacter);

        restRpmCharacterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmCharacterDTO))
            )
            .andExpect(status().isBadRequest());

        List<RpmCharacter> rpmCharacterList = rpmCharacterRepository.findAll();
        assertThat(rpmCharacterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = rpmCharacterRepository.findAll().size();
        // set the field null
        rpmCharacter.setDescription(null);

        // Create the RpmCharacter, which fails.
        RpmCharacterDTO rpmCharacterDTO = rpmCharacterMapper.toDto(rpmCharacter);

        restRpmCharacterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmCharacterDTO))
            )
            .andExpect(status().isBadRequest());

        List<RpmCharacter> rpmCharacterList = rpmCharacterRepository.findAll();
        assertThat(rpmCharacterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriorityIsRequired() throws Exception {
        int databaseSizeBeforeTest = rpmCharacterRepository.findAll().size();
        // set the field null
        rpmCharacter.setPriority(null);

        // Create the RpmCharacter, which fails.
        RpmCharacterDTO rpmCharacterDTO = rpmCharacterMapper.toDto(rpmCharacter);

        restRpmCharacterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmCharacterDTO))
            )
            .andExpect(status().isBadRequest());

        List<RpmCharacter> rpmCharacterList = rpmCharacterRepository.findAll();
        assertThat(rpmCharacterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRpmCharacters() throws Exception {
        // Initialize the database
        rpmCharacterRepository.saveAndFlush(rpmCharacter);

        // Get all the rpmCharacterList
        restRpmCharacterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rpmCharacter.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)));
    }

    @Test
    @Transactional
    void getRpmCharacter() throws Exception {
        // Initialize the database
        rpmCharacterRepository.saveAndFlush(rpmCharacter);

        // Get the rpmCharacter
        restRpmCharacterMockMvc
            .perform(get(ENTITY_API_URL_ID, rpmCharacter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rpmCharacter.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY));
    }

    @Test
    @Transactional
    void getRpmCharactersByIdFiltering() throws Exception {
        // Initialize the database
        rpmCharacterRepository.saveAndFlush(rpmCharacter);

        Long id = rpmCharacter.getId();

        defaultRpmCharacterShouldBeFound("id.equals=" + id);
        defaultRpmCharacterShouldNotBeFound("id.notEquals=" + id);

        defaultRpmCharacterShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRpmCharacterShouldNotBeFound("id.greaterThan=" + id);

        defaultRpmCharacterShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRpmCharacterShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRpmCharactersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        rpmCharacterRepository.saveAndFlush(rpmCharacter);

        // Get all the rpmCharacterList where name equals to DEFAULT_NAME
        defaultRpmCharacterShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the rpmCharacterList where name equals to UPDATED_NAME
        defaultRpmCharacterShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRpmCharactersByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rpmCharacterRepository.saveAndFlush(rpmCharacter);

        // Get all the rpmCharacterList where name not equals to DEFAULT_NAME
        defaultRpmCharacterShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the rpmCharacterList where name not equals to UPDATED_NAME
        defaultRpmCharacterShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRpmCharactersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        rpmCharacterRepository.saveAndFlush(rpmCharacter);

        // Get all the rpmCharacterList where name in DEFAULT_NAME or UPDATED_NAME
        defaultRpmCharacterShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the rpmCharacterList where name equals to UPDATED_NAME
        defaultRpmCharacterShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRpmCharactersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        rpmCharacterRepository.saveAndFlush(rpmCharacter);

        // Get all the rpmCharacterList where name is not null
        defaultRpmCharacterShouldBeFound("name.specified=true");

        // Get all the rpmCharacterList where name is null
        defaultRpmCharacterShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllRpmCharactersByNameContainsSomething() throws Exception {
        // Initialize the database
        rpmCharacterRepository.saveAndFlush(rpmCharacter);

        // Get all the rpmCharacterList where name contains DEFAULT_NAME
        defaultRpmCharacterShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the rpmCharacterList where name contains UPDATED_NAME
        defaultRpmCharacterShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRpmCharactersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        rpmCharacterRepository.saveAndFlush(rpmCharacter);

        // Get all the rpmCharacterList where name does not contain DEFAULT_NAME
        defaultRpmCharacterShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the rpmCharacterList where name does not contain UPDATED_NAME
        defaultRpmCharacterShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRpmCharactersByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        rpmCharacterRepository.saveAndFlush(rpmCharacter);

        // Get all the rpmCharacterList where description equals to DEFAULT_DESCRIPTION
        defaultRpmCharacterShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the rpmCharacterList where description equals to UPDATED_DESCRIPTION
        defaultRpmCharacterShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRpmCharactersByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rpmCharacterRepository.saveAndFlush(rpmCharacter);

        // Get all the rpmCharacterList where description not equals to DEFAULT_DESCRIPTION
        defaultRpmCharacterShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the rpmCharacterList where description not equals to UPDATED_DESCRIPTION
        defaultRpmCharacterShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRpmCharactersByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        rpmCharacterRepository.saveAndFlush(rpmCharacter);

        // Get all the rpmCharacterList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultRpmCharacterShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the rpmCharacterList where description equals to UPDATED_DESCRIPTION
        defaultRpmCharacterShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRpmCharactersByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        rpmCharacterRepository.saveAndFlush(rpmCharacter);

        // Get all the rpmCharacterList where description is not null
        defaultRpmCharacterShouldBeFound("description.specified=true");

        // Get all the rpmCharacterList where description is null
        defaultRpmCharacterShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllRpmCharactersByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        rpmCharacterRepository.saveAndFlush(rpmCharacter);

        // Get all the rpmCharacterList where description contains DEFAULT_DESCRIPTION
        defaultRpmCharacterShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the rpmCharacterList where description contains UPDATED_DESCRIPTION
        defaultRpmCharacterShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRpmCharactersByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        rpmCharacterRepository.saveAndFlush(rpmCharacter);

        // Get all the rpmCharacterList where description does not contain DEFAULT_DESCRIPTION
        defaultRpmCharacterShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the rpmCharacterList where description does not contain UPDATED_DESCRIPTION
        defaultRpmCharacterShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRpmCharactersByPriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        rpmCharacterRepository.saveAndFlush(rpmCharacter);

        // Get all the rpmCharacterList where priority equals to DEFAULT_PRIORITY
        defaultRpmCharacterShouldBeFound("priority.equals=" + DEFAULT_PRIORITY);

        // Get all the rpmCharacterList where priority equals to UPDATED_PRIORITY
        defaultRpmCharacterShouldNotBeFound("priority.equals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllRpmCharactersByPriorityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rpmCharacterRepository.saveAndFlush(rpmCharacter);

        // Get all the rpmCharacterList where priority not equals to DEFAULT_PRIORITY
        defaultRpmCharacterShouldNotBeFound("priority.notEquals=" + DEFAULT_PRIORITY);

        // Get all the rpmCharacterList where priority not equals to UPDATED_PRIORITY
        defaultRpmCharacterShouldBeFound("priority.notEquals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllRpmCharactersByPriorityIsInShouldWork() throws Exception {
        // Initialize the database
        rpmCharacterRepository.saveAndFlush(rpmCharacter);

        // Get all the rpmCharacterList where priority in DEFAULT_PRIORITY or UPDATED_PRIORITY
        defaultRpmCharacterShouldBeFound("priority.in=" + DEFAULT_PRIORITY + "," + UPDATED_PRIORITY);

        // Get all the rpmCharacterList where priority equals to UPDATED_PRIORITY
        defaultRpmCharacterShouldNotBeFound("priority.in=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllRpmCharactersByPriorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        rpmCharacterRepository.saveAndFlush(rpmCharacter);

        // Get all the rpmCharacterList where priority is not null
        defaultRpmCharacterShouldBeFound("priority.specified=true");

        // Get all the rpmCharacterList where priority is null
        defaultRpmCharacterShouldNotBeFound("priority.specified=false");
    }

    @Test
    @Transactional
    void getAllRpmCharactersByPriorityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rpmCharacterRepository.saveAndFlush(rpmCharacter);

        // Get all the rpmCharacterList where priority is greater than or equal to DEFAULT_PRIORITY
        defaultRpmCharacterShouldBeFound("priority.greaterThanOrEqual=" + DEFAULT_PRIORITY);

        // Get all the rpmCharacterList where priority is greater than or equal to UPDATED_PRIORITY
        defaultRpmCharacterShouldNotBeFound("priority.greaterThanOrEqual=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllRpmCharactersByPriorityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rpmCharacterRepository.saveAndFlush(rpmCharacter);

        // Get all the rpmCharacterList where priority is less than or equal to DEFAULT_PRIORITY
        defaultRpmCharacterShouldBeFound("priority.lessThanOrEqual=" + DEFAULT_PRIORITY);

        // Get all the rpmCharacterList where priority is less than or equal to SMALLER_PRIORITY
        defaultRpmCharacterShouldNotBeFound("priority.lessThanOrEqual=" + SMALLER_PRIORITY);
    }

    @Test
    @Transactional
    void getAllRpmCharactersByPriorityIsLessThanSomething() throws Exception {
        // Initialize the database
        rpmCharacterRepository.saveAndFlush(rpmCharacter);

        // Get all the rpmCharacterList where priority is less than DEFAULT_PRIORITY
        defaultRpmCharacterShouldNotBeFound("priority.lessThan=" + DEFAULT_PRIORITY);

        // Get all the rpmCharacterList where priority is less than UPDATED_PRIORITY
        defaultRpmCharacterShouldBeFound("priority.lessThan=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllRpmCharactersByPriorityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rpmCharacterRepository.saveAndFlush(rpmCharacter);

        // Get all the rpmCharacterList where priority is greater than DEFAULT_PRIORITY
        defaultRpmCharacterShouldNotBeFound("priority.greaterThan=" + DEFAULT_PRIORITY);

        // Get all the rpmCharacterList where priority is greater than SMALLER_PRIORITY
        defaultRpmCharacterShouldBeFound("priority.greaterThan=" + SMALLER_PRIORITY);
    }

    @Test
    @Transactional
    void getAllRpmCharactersByResultIsEqualToSomething() throws Exception {
        // Initialize the database
        rpmCharacterRepository.saveAndFlush(rpmCharacter);
        RpmResult result;
        if (TestUtil.findAll(em, RpmResult.class).isEmpty()) {
            result = RpmResultResourceIT.createEntity(em);
            em.persist(result);
            em.flush();
        } else {
            result = TestUtil.findAll(em, RpmResult.class).get(0);
        }
        em.persist(result);
        em.flush();
        rpmCharacter.setResult(result);
        rpmCharacterRepository.saveAndFlush(rpmCharacter);
        Long resultId = result.getId();

        // Get all the rpmCharacterList where result equals to resultId
        defaultRpmCharacterShouldBeFound("resultId.equals=" + resultId);

        // Get all the rpmCharacterList where result equals to (resultId + 1)
        defaultRpmCharacterShouldNotBeFound("resultId.equals=" + (resultId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRpmCharacterShouldBeFound(String filter) throws Exception {
        restRpmCharacterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rpmCharacter.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)));

        // Check, that the count call also returns 1
        restRpmCharacterMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRpmCharacterShouldNotBeFound(String filter) throws Exception {
        restRpmCharacterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRpmCharacterMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRpmCharacter() throws Exception {
        // Get the rpmCharacter
        restRpmCharacterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRpmCharacter() throws Exception {
        // Initialize the database
        rpmCharacterRepository.saveAndFlush(rpmCharacter);

        int databaseSizeBeforeUpdate = rpmCharacterRepository.findAll().size();

        // Update the rpmCharacter
        RpmCharacter updatedRpmCharacter = rpmCharacterRepository.findById(rpmCharacter.getId()).get();
        // Disconnect from session so that the updates on updatedRpmCharacter are not directly saved in db
        em.detach(updatedRpmCharacter);
        updatedRpmCharacter.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).priority(UPDATED_PRIORITY);
        RpmCharacterDTO rpmCharacterDTO = rpmCharacterMapper.toDto(updatedRpmCharacter);

        restRpmCharacterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rpmCharacterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmCharacterDTO))
            )
            .andExpect(status().isOk());

        // Validate the RpmCharacter in the database
        List<RpmCharacter> rpmCharacterList = rpmCharacterRepository.findAll();
        assertThat(rpmCharacterList).hasSize(databaseSizeBeforeUpdate);
        RpmCharacter testRpmCharacter = rpmCharacterList.get(rpmCharacterList.size() - 1);
        assertThat(testRpmCharacter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRpmCharacter.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRpmCharacter.getPriority()).isEqualTo(UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void putNonExistingRpmCharacter() throws Exception {
        int databaseSizeBeforeUpdate = rpmCharacterRepository.findAll().size();
        rpmCharacter.setId(count.incrementAndGet());

        // Create the RpmCharacter
        RpmCharacterDTO rpmCharacterDTO = rpmCharacterMapper.toDto(rpmCharacter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRpmCharacterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rpmCharacterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmCharacterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmCharacter in the database
        List<RpmCharacter> rpmCharacterList = rpmCharacterRepository.findAll();
        assertThat(rpmCharacterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRpmCharacter() throws Exception {
        int databaseSizeBeforeUpdate = rpmCharacterRepository.findAll().size();
        rpmCharacter.setId(count.incrementAndGet());

        // Create the RpmCharacter
        RpmCharacterDTO rpmCharacterDTO = rpmCharacterMapper.toDto(rpmCharacter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmCharacterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rpmCharacterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmCharacter in the database
        List<RpmCharacter> rpmCharacterList = rpmCharacterRepository.findAll();
        assertThat(rpmCharacterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRpmCharacter() throws Exception {
        int databaseSizeBeforeUpdate = rpmCharacterRepository.findAll().size();
        rpmCharacter.setId(count.incrementAndGet());

        // Create the RpmCharacter
        RpmCharacterDTO rpmCharacterDTO = rpmCharacterMapper.toDto(rpmCharacter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmCharacterMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rpmCharacterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RpmCharacter in the database
        List<RpmCharacter> rpmCharacterList = rpmCharacterRepository.findAll();
        assertThat(rpmCharacterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRpmCharacterWithPatch() throws Exception {
        // Initialize the database
        rpmCharacterRepository.saveAndFlush(rpmCharacter);

        int databaseSizeBeforeUpdate = rpmCharacterRepository.findAll().size();

        // Update the rpmCharacter using partial update
        RpmCharacter partialUpdatedRpmCharacter = new RpmCharacter();
        partialUpdatedRpmCharacter.setId(rpmCharacter.getId());

        partialUpdatedRpmCharacter.name(UPDATED_NAME);

        restRpmCharacterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRpmCharacter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRpmCharacter))
            )
            .andExpect(status().isOk());

        // Validate the RpmCharacter in the database
        List<RpmCharacter> rpmCharacterList = rpmCharacterRepository.findAll();
        assertThat(rpmCharacterList).hasSize(databaseSizeBeforeUpdate);
        RpmCharacter testRpmCharacter = rpmCharacterList.get(rpmCharacterList.size() - 1);
        assertThat(testRpmCharacter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRpmCharacter.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRpmCharacter.getPriority()).isEqualTo(DEFAULT_PRIORITY);
    }

    @Test
    @Transactional
    void fullUpdateRpmCharacterWithPatch() throws Exception {
        // Initialize the database
        rpmCharacterRepository.saveAndFlush(rpmCharacter);

        int databaseSizeBeforeUpdate = rpmCharacterRepository.findAll().size();

        // Update the rpmCharacter using partial update
        RpmCharacter partialUpdatedRpmCharacter = new RpmCharacter();
        partialUpdatedRpmCharacter.setId(rpmCharacter.getId());

        partialUpdatedRpmCharacter.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).priority(UPDATED_PRIORITY);

        restRpmCharacterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRpmCharacter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRpmCharacter))
            )
            .andExpect(status().isOk());

        // Validate the RpmCharacter in the database
        List<RpmCharacter> rpmCharacterList = rpmCharacterRepository.findAll();
        assertThat(rpmCharacterList).hasSize(databaseSizeBeforeUpdate);
        RpmCharacter testRpmCharacter = rpmCharacterList.get(rpmCharacterList.size() - 1);
        assertThat(testRpmCharacter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRpmCharacter.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRpmCharacter.getPriority()).isEqualTo(UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void patchNonExistingRpmCharacter() throws Exception {
        int databaseSizeBeforeUpdate = rpmCharacterRepository.findAll().size();
        rpmCharacter.setId(count.incrementAndGet());

        // Create the RpmCharacter
        RpmCharacterDTO rpmCharacterDTO = rpmCharacterMapper.toDto(rpmCharacter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRpmCharacterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rpmCharacterDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rpmCharacterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmCharacter in the database
        List<RpmCharacter> rpmCharacterList = rpmCharacterRepository.findAll();
        assertThat(rpmCharacterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRpmCharacter() throws Exception {
        int databaseSizeBeforeUpdate = rpmCharacterRepository.findAll().size();
        rpmCharacter.setId(count.incrementAndGet());

        // Create the RpmCharacter
        RpmCharacterDTO rpmCharacterDTO = rpmCharacterMapper.toDto(rpmCharacter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmCharacterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rpmCharacterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RpmCharacter in the database
        List<RpmCharacter> rpmCharacterList = rpmCharacterRepository.findAll();
        assertThat(rpmCharacterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRpmCharacter() throws Exception {
        int databaseSizeBeforeUpdate = rpmCharacterRepository.findAll().size();
        rpmCharacter.setId(count.incrementAndGet());

        // Create the RpmCharacter
        RpmCharacterDTO rpmCharacterDTO = rpmCharacterMapper.toDto(rpmCharacter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRpmCharacterMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rpmCharacterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RpmCharacter in the database
        List<RpmCharacter> rpmCharacterList = rpmCharacterRepository.findAll();
        assertThat(rpmCharacterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRpmCharacter() throws Exception {
        // Initialize the database
        rpmCharacterRepository.saveAndFlush(rpmCharacter);

        int databaseSizeBeforeDelete = rpmCharacterRepository.findAll().size();

        // Delete the rpmCharacter
        restRpmCharacterMockMvc
            .perform(delete(ENTITY_API_URL_ID, rpmCharacter.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RpmCharacter> rpmCharacterList = rpmCharacterRepository.findAll();
        assertThat(rpmCharacterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
