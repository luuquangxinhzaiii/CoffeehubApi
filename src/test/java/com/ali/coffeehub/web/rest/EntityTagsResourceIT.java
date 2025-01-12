package com.ali.coffeehub.web.rest;

import static com.ali.coffeehub.domain.EntityTagsEntityAsserts.*;
import static com.ali.coffeehub.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ali.coffeehub.IntegrationTest;
import com.ali.coffeehub.domain.EntityTagsEntity;
import com.ali.coffeehub.repository.EntityTagsRepository;
import com.ali.coffeehub.service.dto.EntityTagsDTO;
import com.ali.coffeehub.service.mapper.EntityTagsMapper;
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
 * Integration tests for the {@link EntityTagsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EntityTagsResourceIT {

    private static final Long DEFAULT_TAG_ID = 1L;
    private static final Long UPDATED_TAG_ID = 2L;

    private static final Long DEFAULT_ENTITY_TYPE = 1L;
    private static final Long UPDATED_ENTITY_TYPE = 2L;

    private static final Long DEFAULT_ENTITY_ID = 1L;
    private static final Long UPDATED_ENTITY_ID = 2L;

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

    private static final String ENTITY_API_URL = "/api/entity-tags";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EntityTagsRepository entityTagsRepository;

    @Autowired
    private EntityTagsMapper entityTagsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEntityTagsMockMvc;

    private EntityTagsEntity entityTagsEntity;

    private EntityTagsEntity insertedEntityTagsEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EntityTagsEntity createEntity() {
        return new EntityTagsEntity()
            .tagId(DEFAULT_TAG_ID)
            .entityType(DEFAULT_ENTITY_TYPE)
            .entityId(DEFAULT_ENTITY_ID)
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
    public static EntityTagsEntity createUpdatedEntity() {
        return new EntityTagsEntity()
            .tagId(UPDATED_TAG_ID)
            .entityType(UPDATED_ENTITY_TYPE)
            .entityId(UPDATED_ENTITY_ID)
            .deleted(UPDATED_DELETED)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);
    }

    @BeforeEach
    public void initTest() {
        entityTagsEntity = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedEntityTagsEntity != null) {
            entityTagsRepository.delete(insertedEntityTagsEntity);
            insertedEntityTagsEntity = null;
        }
    }

    @Test
    @Transactional
    void createEntityTags() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EntityTags
        EntityTagsDTO entityTagsDTO = entityTagsMapper.toDto(entityTagsEntity);
        var returnedEntityTagsDTO = om.readValue(
            restEntityTagsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(entityTagsDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EntityTagsDTO.class
        );

        // Validate the EntityTags in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEntityTagsEntity = entityTagsMapper.toEntity(returnedEntityTagsDTO);
        assertEntityTagsEntityUpdatableFieldsEquals(returnedEntityTagsEntity, getPersistedEntityTagsEntity(returnedEntityTagsEntity));

        insertedEntityTagsEntity = returnedEntityTagsEntity;
    }

    @Test
    @Transactional
    void createEntityTagsWithExistingId() throws Exception {
        // Create the EntityTags with an existing ID
        entityTagsEntity.setId(1L);
        EntityTagsDTO entityTagsDTO = entityTagsMapper.toDto(entityTagsEntity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEntityTagsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(entityTagsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EntityTags in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTagIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        entityTagsEntity.setTagId(null);

        // Create the EntityTags, which fails.
        EntityTagsDTO entityTagsDTO = entityTagsMapper.toDto(entityTagsEntity);

        restEntityTagsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(entityTagsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEntityTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        entityTagsEntity.setEntityType(null);

        // Create the EntityTags, which fails.
        EntityTagsDTO entityTagsDTO = entityTagsMapper.toDto(entityTagsEntity);

        restEntityTagsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(entityTagsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEntityIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        entityTagsEntity.setEntityId(null);

        // Create the EntityTags, which fails.
        EntityTagsDTO entityTagsDTO = entityTagsMapper.toDto(entityTagsEntity);

        restEntityTagsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(entityTagsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        entityTagsEntity.setCreatedAt(null);

        // Create the EntityTags, which fails.
        EntityTagsDTO entityTagsDTO = entityTagsMapper.toDto(entityTagsEntity);

        restEntityTagsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(entityTagsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEntityTags() throws Exception {
        // Initialize the database
        insertedEntityTagsEntity = entityTagsRepository.saveAndFlush(entityTagsEntity);

        // Get all the entityTagsList
        restEntityTagsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entityTagsEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].tagId").value(hasItem(DEFAULT_TAG_ID.intValue())))
            .andExpect(jsonPath("$.[*].entityType").value(hasItem(DEFAULT_ENTITY_TYPE.intValue())))
            .andExpect(jsonPath("$.[*].entityId").value(hasItem(DEFAULT_ENTITY_ID.intValue())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getEntityTags() throws Exception {
        // Initialize the database
        insertedEntityTagsEntity = entityTagsRepository.saveAndFlush(entityTagsEntity);

        // Get the entityTags
        restEntityTagsMockMvc
            .perform(get(ENTITY_API_URL_ID, entityTagsEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(entityTagsEntity.getId().intValue()))
            .andExpect(jsonPath("$.tagId").value(DEFAULT_TAG_ID.intValue()))
            .andExpect(jsonPath("$.entityType").value(DEFAULT_ENTITY_TYPE.intValue()))
            .andExpect(jsonPath("$.entityId").value(DEFAULT_ENTITY_ID.intValue()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingEntityTags() throws Exception {
        // Get the entityTags
        restEntityTagsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEntityTags() throws Exception {
        // Initialize the database
        insertedEntityTagsEntity = entityTagsRepository.saveAndFlush(entityTagsEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the entityTags
        EntityTagsEntity updatedEntityTagsEntity = entityTagsRepository.findById(entityTagsEntity.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEntityTagsEntity are not directly saved in db
        em.detach(updatedEntityTagsEntity);
        updatedEntityTagsEntity
            .tagId(UPDATED_TAG_ID)
            .entityType(UPDATED_ENTITY_TYPE)
            .entityId(UPDATED_ENTITY_ID)
            .deleted(UPDATED_DELETED)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);
        EntityTagsDTO entityTagsDTO = entityTagsMapper.toDto(updatedEntityTagsEntity);

        restEntityTagsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, entityTagsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(entityTagsDTO))
            )
            .andExpect(status().isOk());

        // Validate the EntityTags in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEntityTagsEntityToMatchAllProperties(updatedEntityTagsEntity);
    }

    @Test
    @Transactional
    void putNonExistingEntityTags() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityTagsEntity.setId(longCount.incrementAndGet());

        // Create the EntityTags
        EntityTagsDTO entityTagsDTO = entityTagsMapper.toDto(entityTagsEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEntityTagsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, entityTagsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(entityTagsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EntityTags in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEntityTags() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityTagsEntity.setId(longCount.incrementAndGet());

        // Create the EntityTags
        EntityTagsDTO entityTagsDTO = entityTagsMapper.toDto(entityTagsEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntityTagsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(entityTagsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EntityTags in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEntityTags() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityTagsEntity.setId(longCount.incrementAndGet());

        // Create the EntityTags
        EntityTagsDTO entityTagsDTO = entityTagsMapper.toDto(entityTagsEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntityTagsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(entityTagsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EntityTags in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEntityTagsWithPatch() throws Exception {
        // Initialize the database
        insertedEntityTagsEntity = entityTagsRepository.saveAndFlush(entityTagsEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the entityTags using partial update
        EntityTagsEntity partialUpdatedEntityTagsEntity = new EntityTagsEntity();
        partialUpdatedEntityTagsEntity.setId(entityTagsEntity.getId());

        partialUpdatedEntityTagsEntity
            .tagId(UPDATED_TAG_ID)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);

        restEntityTagsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEntityTagsEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEntityTagsEntity))
            )
            .andExpect(status().isOk());

        // Validate the EntityTags in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEntityTagsEntityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEntityTagsEntity, entityTagsEntity),
            getPersistedEntityTagsEntity(entityTagsEntity)
        );
    }

    @Test
    @Transactional
    void fullUpdateEntityTagsWithPatch() throws Exception {
        // Initialize the database
        insertedEntityTagsEntity = entityTagsRepository.saveAndFlush(entityTagsEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the entityTags using partial update
        EntityTagsEntity partialUpdatedEntityTagsEntity = new EntityTagsEntity();
        partialUpdatedEntityTagsEntity.setId(entityTagsEntity.getId());

        partialUpdatedEntityTagsEntity
            .tagId(UPDATED_TAG_ID)
            .entityType(UPDATED_ENTITY_TYPE)
            .entityId(UPDATED_ENTITY_ID)
            .deleted(UPDATED_DELETED)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);

        restEntityTagsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEntityTagsEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEntityTagsEntity))
            )
            .andExpect(status().isOk());

        // Validate the EntityTags in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEntityTagsEntityUpdatableFieldsEquals(
            partialUpdatedEntityTagsEntity,
            getPersistedEntityTagsEntity(partialUpdatedEntityTagsEntity)
        );
    }

    @Test
    @Transactional
    void patchNonExistingEntityTags() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityTagsEntity.setId(longCount.incrementAndGet());

        // Create the EntityTags
        EntityTagsDTO entityTagsDTO = entityTagsMapper.toDto(entityTagsEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEntityTagsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, entityTagsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(entityTagsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EntityTags in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEntityTags() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityTagsEntity.setId(longCount.incrementAndGet());

        // Create the EntityTags
        EntityTagsDTO entityTagsDTO = entityTagsMapper.toDto(entityTagsEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntityTagsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(entityTagsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EntityTags in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEntityTags() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entityTagsEntity.setId(longCount.incrementAndGet());

        // Create the EntityTags
        EntityTagsDTO entityTagsDTO = entityTagsMapper.toDto(entityTagsEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntityTagsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(entityTagsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EntityTags in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEntityTags() throws Exception {
        // Initialize the database
        insertedEntityTagsEntity = entityTagsRepository.saveAndFlush(entityTagsEntity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the entityTags
        restEntityTagsMockMvc
            .perform(delete(ENTITY_API_URL_ID, entityTagsEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return entityTagsRepository.count();
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

    protected EntityTagsEntity getPersistedEntityTagsEntity(EntityTagsEntity entityTags) {
        return entityTagsRepository.findById(entityTags.getId()).orElseThrow();
    }

    protected void assertPersistedEntityTagsEntityToMatchAllProperties(EntityTagsEntity expectedEntityTagsEntity) {
        assertEntityTagsEntityAllPropertiesEquals(expectedEntityTagsEntity, getPersistedEntityTagsEntity(expectedEntityTagsEntity));
    }

    protected void assertPersistedEntityTagsEntityToMatchUpdatableProperties(EntityTagsEntity expectedEntityTagsEntity) {
        assertEntityTagsEntityAllUpdatablePropertiesEquals(
            expectedEntityTagsEntity,
            getPersistedEntityTagsEntity(expectedEntityTagsEntity)
        );
    }
}
