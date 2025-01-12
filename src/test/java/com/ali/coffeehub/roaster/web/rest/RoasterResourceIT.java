package com.ali.coffeehub.roaster.web.rest;

import static com.ali.coffeehub.roaster.domain.RoasterEntityAsserts.*;
import static com.ali.coffeehub.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ali.coffeehub.IntegrationTest;
import com.ali.coffeehub.roaster.domain.RoasterEntity;
import com.ali.coffeehub.roaster.repository.RoasterRepository;
import com.ali.coffeehub.roaster.service.dto.RoasterDTO;
import com.ali.coffeehub.roaster.service.mapper.RoasterMapper;
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
 * Integration tests for the {@link RoasterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RoasterResourceIT {

    private static final Long DEFAULT_CATEGORY_ID = 1L;
    private static final Long UPDATED_CATEGORY_ID = 2L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/roasters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RoasterRepository roasterRepository;

    @Autowired
    private RoasterMapper roasterMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoasterMockMvc;

    private RoasterEntity roasterEntity;

    private RoasterEntity insertedRoasterEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoasterEntity createEntity() {
        return new RoasterEntity()
            .categoryId(DEFAULT_CATEGORY_ID)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .deleted(DEFAULT_DELETED)
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
    public static RoasterEntity createUpdatedEntity() {
        return new RoasterEntity()
            .categoryId(UPDATED_CATEGORY_ID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .deleted(UPDATED_DELETED)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);
    }

    @BeforeEach
    public void initTest() {
        roasterEntity = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedRoasterEntity != null) {
            roasterRepository.delete(insertedRoasterEntity);
            insertedRoasterEntity = null;
        }
    }

    @Test
    @Transactional
    void createRoaster() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Roaster
        RoasterDTO roasterDTO = roasterMapper.toDto(roasterEntity);
        var returnedRoasterDTO = om.readValue(
            restRoasterMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roasterDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RoasterDTO.class
        );

        // Validate the Roaster in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRoasterEntity = roasterMapper.toEntity(returnedRoasterDTO);
        assertRoasterEntityUpdatableFieldsEquals(returnedRoasterEntity, getPersistedRoasterEntity(returnedRoasterEntity));

        insertedRoasterEntity = returnedRoasterEntity;
    }

    @Test
    @Transactional
    void createRoasterWithExistingId() throws Exception {
        // Create the Roaster with an existing ID
        roasterEntity.setId(1L);
        RoasterDTO roasterDTO = roasterMapper.toDto(roasterEntity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoasterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roasterDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Roaster in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        roasterEntity.setCreatedAt(null);

        // Create the Roaster, which fails.
        RoasterDTO roasterDTO = roasterMapper.toDto(roasterEntity);

        restRoasterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roasterDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRoasters() throws Exception {
        // Initialize the database
        insertedRoasterEntity = roasterRepository.saveAndFlush(roasterEntity);

        // Get all the roasterList
        restRoasterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roasterEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryId").value(hasItem(DEFAULT_CATEGORY_ID.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getRoaster() throws Exception {
        // Initialize the database
        insertedRoasterEntity = roasterRepository.saveAndFlush(roasterEntity);

        // Get the roaster
        restRoasterMockMvc
            .perform(get(ENTITY_API_URL_ID, roasterEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(roasterEntity.getId().intValue()))
            .andExpect(jsonPath("$.categoryId").value(DEFAULT_CATEGORY_ID.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingRoaster() throws Exception {
        // Get the roaster
        restRoasterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRoaster() throws Exception {
        // Initialize the database
        insertedRoasterEntity = roasterRepository.saveAndFlush(roasterEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the roaster
        RoasterEntity updatedRoasterEntity = roasterRepository.findById(roasterEntity.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRoasterEntity are not directly saved in db
        em.detach(updatedRoasterEntity);
        updatedRoasterEntity
            .categoryId(UPDATED_CATEGORY_ID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .deleted(UPDATED_DELETED)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);
        RoasterDTO roasterDTO = roasterMapper.toDto(updatedRoasterEntity);

        restRoasterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roasterDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roasterDTO))
            )
            .andExpect(status().isOk());

        // Validate the Roaster in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRoasterEntityToMatchAllProperties(updatedRoasterEntity);
    }

    @Test
    @Transactional
    void putNonExistingRoaster() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roasterEntity.setId(longCount.incrementAndGet());

        // Create the Roaster
        RoasterDTO roasterDTO = roasterMapper.toDto(roasterEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoasterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roasterDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roasterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Roaster in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRoaster() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roasterEntity.setId(longCount.incrementAndGet());

        // Create the Roaster
        RoasterDTO roasterDTO = roasterMapper.toDto(roasterEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoasterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(roasterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Roaster in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRoaster() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roasterEntity.setId(longCount.incrementAndGet());

        // Create the Roaster
        RoasterDTO roasterDTO = roasterMapper.toDto(roasterEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoasterMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roasterDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Roaster in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRoasterWithPatch() throws Exception {
        // Initialize the database
        insertedRoasterEntity = roasterRepository.saveAndFlush(roasterEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the roaster using partial update
        RoasterEntity partialUpdatedRoasterEntity = new RoasterEntity();
        partialUpdatedRoasterEntity.setId(roasterEntity.getId());

        partialUpdatedRoasterEntity.description(UPDATED_DESCRIPTION).updatedAt(UPDATED_UPDATED_AT).updatedBy(UPDATED_UPDATED_BY);

        restRoasterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoasterEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRoasterEntity))
            )
            .andExpect(status().isOk());

        // Validate the Roaster in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRoasterEntityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRoasterEntity, roasterEntity),
            getPersistedRoasterEntity(roasterEntity)
        );
    }

    @Test
    @Transactional
    void fullUpdateRoasterWithPatch() throws Exception {
        // Initialize the database
        insertedRoasterEntity = roasterRepository.saveAndFlush(roasterEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the roaster using partial update
        RoasterEntity partialUpdatedRoasterEntity = new RoasterEntity();
        partialUpdatedRoasterEntity.setId(roasterEntity.getId());

        partialUpdatedRoasterEntity
            .categoryId(UPDATED_CATEGORY_ID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .deleted(UPDATED_DELETED)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);

        restRoasterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoasterEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRoasterEntity))
            )
            .andExpect(status().isOk());

        // Validate the Roaster in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRoasterEntityUpdatableFieldsEquals(partialUpdatedRoasterEntity, getPersistedRoasterEntity(partialUpdatedRoasterEntity));
    }

    @Test
    @Transactional
    void patchNonExistingRoaster() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roasterEntity.setId(longCount.incrementAndGet());

        // Create the Roaster
        RoasterDTO roasterDTO = roasterMapper.toDto(roasterEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoasterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, roasterDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(roasterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Roaster in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRoaster() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roasterEntity.setId(longCount.incrementAndGet());

        // Create the Roaster
        RoasterDTO roasterDTO = roasterMapper.toDto(roasterEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoasterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(roasterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Roaster in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRoaster() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roasterEntity.setId(longCount.incrementAndGet());

        // Create the Roaster
        RoasterDTO roasterDTO = roasterMapper.toDto(roasterEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoasterMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(roasterDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Roaster in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRoaster() throws Exception {
        // Initialize the database
        insertedRoasterEntity = roasterRepository.saveAndFlush(roasterEntity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the roaster
        restRoasterMockMvc
            .perform(delete(ENTITY_API_URL_ID, roasterEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return roasterRepository.count();
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

    protected RoasterEntity getPersistedRoasterEntity(RoasterEntity roaster) {
        return roasterRepository.findById(roaster.getId()).orElseThrow();
    }

    protected void assertPersistedRoasterEntityToMatchAllProperties(RoasterEntity expectedRoasterEntity) {
        assertRoasterEntityAllPropertiesEquals(expectedRoasterEntity, getPersistedRoasterEntity(expectedRoasterEntity));
    }

    protected void assertPersistedRoasterEntityToMatchUpdatableProperties(RoasterEntity expectedRoasterEntity) {
        assertRoasterEntityAllUpdatablePropertiesEquals(expectedRoasterEntity, getPersistedRoasterEntity(expectedRoasterEntity));
    }
}
