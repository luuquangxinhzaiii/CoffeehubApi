package com.ali.coffeehub.brew.web.rest;

import static com.ali.coffeehub.brew.domain.BrewEntityAsserts.*;
import static com.ali.coffeehub.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ali.coffeehub.IntegrationTest;
import com.ali.coffeehub.brew.domain.BrewEntity;
import com.ali.coffeehub.brew.repository.BrewRepository;
import com.ali.coffeehub.brew.service.dto.BrewDTO;
import com.ali.coffeehub.brew.service.mapper.BrewMapper;
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
 * Integration tests for the {@link BrewResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BrewResourceIT {

    private static final Long DEFAULT_CATEGORY_ID = 1L;
    private static final Long UPDATED_CATEGORY_ID = 2L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_LEVEL = 1;
    private static final Integer UPDATED_LEVEL = 2;

    private static final String DEFAULT_SERVING = "AAAAAAAAAA";
    private static final String UPDATED_SERVING = "BBBBBBBBBB";

    private static final String DEFAULT_ICON_URI = "AAAAAAAAAA";
    private static final String UPDATED_ICON_URI = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_URI = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URI = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final Boolean DEFAULT_IS_PINNED = false;
    private static final Boolean UPDATED_IS_PINNED = true;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/brews";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BrewRepository brewRepository;

    @Autowired
    private BrewMapper brewMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBrewMockMvc;

    private BrewEntity brewEntity;

    private BrewEntity insertedBrewEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BrewEntity createEntity() {
        return new BrewEntity()
            .categoryId(DEFAULT_CATEGORY_ID)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .level(DEFAULT_LEVEL)
            .serving(DEFAULT_SERVING)
            .iconUri(DEFAULT_ICON_URI)
            .imageUri(DEFAULT_IMAGE_URI)
            .deleted(DEFAULT_DELETED)
            .isPinned(DEFAULT_IS_PINNED)
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
    public static BrewEntity createUpdatedEntity() {
        return new BrewEntity()
            .categoryId(UPDATED_CATEGORY_ID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .level(UPDATED_LEVEL)
            .serving(UPDATED_SERVING)
            .iconUri(UPDATED_ICON_URI)
            .imageUri(UPDATED_IMAGE_URI)
            .deleted(UPDATED_DELETED)
            .isPinned(UPDATED_IS_PINNED)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);
    }

    @BeforeEach
    public void initTest() {
        brewEntity = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedBrewEntity != null) {
            brewRepository.delete(insertedBrewEntity);
            insertedBrewEntity = null;
        }
    }

    @Test
    @Transactional
    void createBrew() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Brew
        BrewDTO brewDTO = brewMapper.toDto(brewEntity);
        var returnedBrewDTO = om.readValue(
            restBrewMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(brewDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BrewDTO.class
        );

        // Validate the Brew in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBrewEntity = brewMapper.toEntity(returnedBrewDTO);
        assertBrewEntityUpdatableFieldsEquals(returnedBrewEntity, getPersistedBrewEntity(returnedBrewEntity));

        insertedBrewEntity = returnedBrewEntity;
    }

    @Test
    @Transactional
    void createBrewWithExistingId() throws Exception {
        // Create the Brew with an existing ID
        brewEntity.setId(1L);
        BrewDTO brewDTO = brewMapper.toDto(brewEntity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBrewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(brewDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Brew in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCategoryIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        brewEntity.setCategoryId(null);

        // Create the Brew, which fails.
        BrewDTO brewDTO = brewMapper.toDto(brewEntity);

        restBrewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(brewDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        brewEntity.setName(null);

        // Create the Brew, which fails.
        BrewDTO brewDTO = brewMapper.toDto(brewEntity);

        restBrewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(brewDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        brewEntity.setCreatedAt(null);

        // Create the Brew, which fails.
        BrewDTO brewDTO = brewMapper.toDto(brewEntity);

        restBrewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(brewDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBrews() throws Exception {
        // Initialize the database
        insertedBrewEntity = brewRepository.saveAndFlush(brewEntity);

        // Get all the brewList
        restBrewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(brewEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryId").value(hasItem(DEFAULT_CATEGORY_ID.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
            .andExpect(jsonPath("$.[*].serving").value(hasItem(DEFAULT_SERVING)))
            .andExpect(jsonPath("$.[*].iconUri").value(hasItem(DEFAULT_ICON_URI)))
            .andExpect(jsonPath("$.[*].imageUri").value(hasItem(DEFAULT_IMAGE_URI)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)))
            .andExpect(jsonPath("$.[*].isPinned").value(hasItem(DEFAULT_IS_PINNED)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getBrew() throws Exception {
        // Initialize the database
        insertedBrewEntity = brewRepository.saveAndFlush(brewEntity);

        // Get the brew
        restBrewMockMvc
            .perform(get(ENTITY_API_URL_ID, brewEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(brewEntity.getId().intValue()))
            .andExpect(jsonPath("$.categoryId").value(DEFAULT_CATEGORY_ID.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL))
            .andExpect(jsonPath("$.serving").value(DEFAULT_SERVING))
            .andExpect(jsonPath("$.iconUri").value(DEFAULT_ICON_URI))
            .andExpect(jsonPath("$.imageUri").value(DEFAULT_IMAGE_URI))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED))
            .andExpect(jsonPath("$.isPinned").value(DEFAULT_IS_PINNED))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingBrew() throws Exception {
        // Get the brew
        restBrewMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBrew() throws Exception {
        // Initialize the database
        insertedBrewEntity = brewRepository.saveAndFlush(brewEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the brew
        BrewEntity updatedBrewEntity = brewRepository.findById(brewEntity.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBrewEntity are not directly saved in db
        em.detach(updatedBrewEntity);
        updatedBrewEntity
            .categoryId(UPDATED_CATEGORY_ID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .level(UPDATED_LEVEL)
            .serving(UPDATED_SERVING)
            .iconUri(UPDATED_ICON_URI)
            .imageUri(UPDATED_IMAGE_URI)
            .deleted(UPDATED_DELETED)
            .isPinned(UPDATED_IS_PINNED)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);
        BrewDTO brewDTO = brewMapper.toDto(updatedBrewEntity);

        restBrewMockMvc
            .perform(put(ENTITY_API_URL_ID, brewDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(brewDTO)))
            .andExpect(status().isOk());

        // Validate the Brew in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBrewEntityToMatchAllProperties(updatedBrewEntity);
    }

    @Test
    @Transactional
    void putNonExistingBrew() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        brewEntity.setId(longCount.incrementAndGet());

        // Create the Brew
        BrewDTO brewDTO = brewMapper.toDto(brewEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBrewMockMvc
            .perform(put(ENTITY_API_URL_ID, brewDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(brewDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Brew in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBrew() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        brewEntity.setId(longCount.incrementAndGet());

        // Create the Brew
        BrewDTO brewDTO = brewMapper.toDto(brewEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBrewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(brewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Brew in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBrew() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        brewEntity.setId(longCount.incrementAndGet());

        // Create the Brew
        BrewDTO brewDTO = brewMapper.toDto(brewEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBrewMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(brewDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Brew in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBrewWithPatch() throws Exception {
        // Initialize the database
        insertedBrewEntity = brewRepository.saveAndFlush(brewEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the brew using partial update
        BrewEntity partialUpdatedBrewEntity = new BrewEntity();
        partialUpdatedBrewEntity.setId(brewEntity.getId());

        partialUpdatedBrewEntity
            .name(UPDATED_NAME)
            .level(UPDATED_LEVEL)
            .serving(UPDATED_SERVING)
            .deleted(UPDATED_DELETED)
            .isPinned(UPDATED_IS_PINNED)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);

        restBrewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBrewEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBrewEntity))
            )
            .andExpect(status().isOk());

        // Validate the Brew in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBrewEntityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBrewEntity, brewEntity),
            getPersistedBrewEntity(brewEntity)
        );
    }

    @Test
    @Transactional
    void fullUpdateBrewWithPatch() throws Exception {
        // Initialize the database
        insertedBrewEntity = brewRepository.saveAndFlush(brewEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the brew using partial update
        BrewEntity partialUpdatedBrewEntity = new BrewEntity();
        partialUpdatedBrewEntity.setId(brewEntity.getId());

        partialUpdatedBrewEntity
            .categoryId(UPDATED_CATEGORY_ID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .level(UPDATED_LEVEL)
            .serving(UPDATED_SERVING)
            .iconUri(UPDATED_ICON_URI)
            .imageUri(UPDATED_IMAGE_URI)
            .deleted(UPDATED_DELETED)
            .isPinned(UPDATED_IS_PINNED)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);

        restBrewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBrewEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBrewEntity))
            )
            .andExpect(status().isOk());

        // Validate the Brew in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBrewEntityUpdatableFieldsEquals(partialUpdatedBrewEntity, getPersistedBrewEntity(partialUpdatedBrewEntity));
    }

    @Test
    @Transactional
    void patchNonExistingBrew() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        brewEntity.setId(longCount.incrementAndGet());

        // Create the Brew
        BrewDTO brewDTO = brewMapper.toDto(brewEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBrewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, brewDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(brewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Brew in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBrew() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        brewEntity.setId(longCount.incrementAndGet());

        // Create the Brew
        BrewDTO brewDTO = brewMapper.toDto(brewEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBrewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(brewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Brew in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBrew() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        brewEntity.setId(longCount.incrementAndGet());

        // Create the Brew
        BrewDTO brewDTO = brewMapper.toDto(brewEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBrewMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(brewDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Brew in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBrew() throws Exception {
        // Initialize the database
        insertedBrewEntity = brewRepository.saveAndFlush(brewEntity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the brew
        restBrewMockMvc
            .perform(delete(ENTITY_API_URL_ID, brewEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return brewRepository.count();
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

    protected BrewEntity getPersistedBrewEntity(BrewEntity brew) {
        return brewRepository.findById(brew.getId()).orElseThrow();
    }

    protected void assertPersistedBrewEntityToMatchAllProperties(BrewEntity expectedBrewEntity) {
        assertBrewEntityAllPropertiesEquals(expectedBrewEntity, getPersistedBrewEntity(expectedBrewEntity));
    }

    protected void assertPersistedBrewEntityToMatchUpdatableProperties(BrewEntity expectedBrewEntity) {
        assertBrewEntityAllUpdatablePropertiesEquals(expectedBrewEntity, getPersistedBrewEntity(expectedBrewEntity));
    }
}
