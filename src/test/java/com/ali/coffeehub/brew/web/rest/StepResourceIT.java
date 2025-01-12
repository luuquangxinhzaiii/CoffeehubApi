package com.ali.coffeehub.brew.web.rest;

import static com.ali.coffeehub.brew.domain.StepEntityAsserts.*;
import static com.ali.coffeehub.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ali.coffeehub.IntegrationTest;
import com.ali.coffeehub.brew.domain.StepEntity;
import com.ali.coffeehub.brew.repository.StepRepository;
import com.ali.coffeehub.brew.service.dto.StepDTO;
import com.ali.coffeehub.brew.service.mapper.StepMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link StepResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StepResourceIT {

    private static final Long DEFAULT_BREW_ID = 1L;
    private static final Long UPDATED_BREW_ID = 2L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DETAIL = "AAAAAAAAAA";
    private static final String UPDATED_DETAIL = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/steps";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StepRepository stepRepository;

    @Autowired
    private StepMapper stepMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStepMockMvc;

    private StepEntity stepEntity;

    private StepEntity insertedStepEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StepEntity createEntity() {
        return new StepEntity()
            .brewId(DEFAULT_BREW_ID)
            .name(DEFAULT_NAME)
            .detail(DEFAULT_DETAIL)
            .createdAt(DEFAULT_CREATED_AT)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedAt(DEFAULT_UPDATED_AT)
            .updatedBy(DEFAULT_UPDATED_BY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StepEntity createUpdatedEntity() {
        return new StepEntity()
            .brewId(UPDATED_BREW_ID)
            .name(UPDATED_NAME)
            .detail(UPDATED_DETAIL)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);
    }

    @BeforeEach
    public void initTest() {
        stepEntity = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedStepEntity != null) {
            stepRepository.delete(insertedStepEntity);
            insertedStepEntity = null;
        }
    }

    @Test
    @Transactional
    void createStep() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Step
        StepDTO stepDTO = stepMapper.toDto(stepEntity);
        var returnedStepDTO = om.readValue(
            restStepMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stepDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            StepDTO.class
        );

        // Validate the Step in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedStepEntity = stepMapper.toEntity(returnedStepDTO);
        assertStepEntityUpdatableFieldsEquals(returnedStepEntity, getPersistedStepEntity(returnedStepEntity));

        insertedStepEntity = returnedStepEntity;
    }

    @Test
    @Transactional
    void createStepWithExistingId() throws Exception {
        // Create the Step with an existing ID
        stepEntity.setId(1L);
        StepDTO stepDTO = stepMapper.toDto(stepEntity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStepMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stepDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Step in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBrewIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        stepEntity.setBrewId(null);

        // Create the Step, which fails.
        StepDTO stepDTO = stepMapper.toDto(stepEntity);

        restStepMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stepDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        stepEntity.setName(null);

        // Create the Step, which fails.
        StepDTO stepDTO = stepMapper.toDto(stepEntity);

        restStepMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stepDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDetailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        stepEntity.setDetail(null);

        // Create the Step, which fails.
        StepDTO stepDTO = stepMapper.toDto(stepEntity);

        restStepMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stepDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        stepEntity.setCreatedAt(null);

        // Create the Step, which fails.
        StepDTO stepDTO = stepMapper.toDto(stepEntity);

        restStepMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stepDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSteps() throws Exception {
        // Initialize the database
        insertedStepEntity = stepRepository.saveAndFlush(stepEntity);

        // Get all the stepList
        restStepMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stepEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].brewId").value(hasItem(DEFAULT_BREW_ID.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].detail").value(hasItem(DEFAULT_DETAIL)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getStep() throws Exception {
        // Initialize the database
        insertedStepEntity = stepRepository.saveAndFlush(stepEntity);

        // Get the step
        restStepMockMvc
            .perform(get(ENTITY_API_URL_ID, stepEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stepEntity.getId().intValue()))
            .andExpect(jsonPath("$.brewId").value(DEFAULT_BREW_ID.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.detail").value(DEFAULT_DETAIL))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingStep() throws Exception {
        // Get the step
        restStepMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStep() throws Exception {
        // Initialize the database
        insertedStepEntity = stepRepository.saveAndFlush(stepEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the step
        StepEntity updatedStepEntity = stepRepository.findById(stepEntity.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStepEntity are not directly saved in db
        em.detach(updatedStepEntity);
        updatedStepEntity
            .brewId(UPDATED_BREW_ID)
            .name(UPDATED_NAME)
            .detail(UPDATED_DETAIL)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);
        StepDTO stepDTO = stepMapper.toDto(updatedStepEntity);

        restStepMockMvc
            .perform(put(ENTITY_API_URL_ID, stepDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stepDTO)))
            .andExpect(status().isOk());

        // Validate the Step in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStepEntityToMatchAllProperties(updatedStepEntity);
    }

    @Test
    @Transactional
    void putNonExistingStep() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stepEntity.setId(longCount.incrementAndGet());

        // Create the Step
        StepDTO stepDTO = stepMapper.toDto(stepEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStepMockMvc
            .perform(put(ENTITY_API_URL_ID, stepDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stepDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Step in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStep() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stepEntity.setId(longCount.incrementAndGet());

        // Create the Step
        StepDTO stepDTO = stepMapper.toDto(stepEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStepMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(stepDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Step in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStep() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stepEntity.setId(longCount.incrementAndGet());

        // Create the Step
        StepDTO stepDTO = stepMapper.toDto(stepEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStepMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stepDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Step in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStepWithPatch() throws Exception {
        // Initialize the database
        insertedStepEntity = stepRepository.saveAndFlush(stepEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the step using partial update
        StepEntity partialUpdatedStepEntity = new StepEntity();
        partialUpdatedStepEntity.setId(stepEntity.getId());

        partialUpdatedStepEntity.name(UPDATED_NAME).createdBy(UPDATED_CREATED_BY).updatedBy(UPDATED_UPDATED_BY);

        restStepMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStepEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStepEntity))
            )
            .andExpect(status().isOk());

        // Validate the Step in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStepEntityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedStepEntity, stepEntity),
            getPersistedStepEntity(stepEntity)
        );
    }

    @Test
    @Transactional
    void fullUpdateStepWithPatch() throws Exception {
        // Initialize the database
        insertedStepEntity = stepRepository.saveAndFlush(stepEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the step using partial update
        StepEntity partialUpdatedStepEntity = new StepEntity();
        partialUpdatedStepEntity.setId(stepEntity.getId());

        partialUpdatedStepEntity
            .brewId(UPDATED_BREW_ID)
            .name(UPDATED_NAME)
            .detail(UPDATED_DETAIL)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);

        restStepMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStepEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStepEntity))
            )
            .andExpect(status().isOk());

        // Validate the Step in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStepEntityUpdatableFieldsEquals(partialUpdatedStepEntity, getPersistedStepEntity(partialUpdatedStepEntity));
    }

    @Test
    @Transactional
    void patchNonExistingStep() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stepEntity.setId(longCount.incrementAndGet());

        // Create the Step
        StepDTO stepDTO = stepMapper.toDto(stepEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStepMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, stepDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(stepDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Step in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStep() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stepEntity.setId(longCount.incrementAndGet());

        // Create the Step
        StepDTO stepDTO = stepMapper.toDto(stepEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStepMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(stepDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Step in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStep() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stepEntity.setId(longCount.incrementAndGet());

        // Create the Step
        StepDTO stepDTO = stepMapper.toDto(stepEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStepMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(stepDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Step in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStep() throws Exception {
        // Initialize the database
        insertedStepEntity = stepRepository.saveAndFlush(stepEntity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the step
        restStepMockMvc
            .perform(delete(ENTITY_API_URL_ID, stepEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return stepRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected StepEntity getPersistedStepEntity(StepEntity step) {
        return stepRepository.findById(step.getId()).orElseThrow();
    }

    protected void assertPersistedStepEntityToMatchAllProperties(StepEntity expectedStepEntity) {
        assertStepEntityAllPropertiesEquals(expectedStepEntity, getPersistedStepEntity(expectedStepEntity));
    }

    protected void assertPersistedStepEntityToMatchUpdatableProperties(StepEntity expectedStepEntity) {
        assertStepEntityAllUpdatablePropertiesEquals(expectedStepEntity, getPersistedStepEntity(expectedStepEntity));
    }
}
