package com.ali.coffeehub.brew.web.rest;

import static com.ali.coffeehub.brew.domain.ConfigEntityAsserts.*;
import static com.ali.coffeehub.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ali.coffeehub.IntegrationTest;
import com.ali.coffeehub.brew.domain.ConfigEntity;
import com.ali.coffeehub.brew.repository.ConfigRepository;
import com.ali.coffeehub.brew.service.dto.ConfigDTO;
import com.ali.coffeehub.brew.service.mapper.ConfigMapper;
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
 * Integration tests for the {@link ConfigResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConfigResourceIT {

    private static final Long DEFAULT_RECIPE_ID = 1L;
    private static final Long UPDATED_RECIPE_ID = 2L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private ConfigMapper configMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConfigMockMvc;

    private ConfigEntity configEntity;

    private ConfigEntity insertedConfigEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigEntity createEntity() {
        return new ConfigEntity()
            .recipeId(DEFAULT_RECIPE_ID)
            .name(DEFAULT_NAME)
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
    public static ConfigEntity createUpdatedEntity() {
        return new ConfigEntity()
            .recipeId(UPDATED_RECIPE_ID)
            .name(UPDATED_NAME)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);
    }

    @BeforeEach
    public void initTest() {
        configEntity = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedConfigEntity != null) {
            configRepository.delete(insertedConfigEntity);
            insertedConfigEntity = null;
        }
    }

    @Test
    @Transactional
    void createConfig() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Config
        ConfigDTO configDTO = configMapper.toDto(configEntity);
        var returnedConfigDTO = om.readValue(
            restConfigMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(configDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ConfigDTO.class
        );

        // Validate the Config in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedConfigEntity = configMapper.toEntity(returnedConfigDTO);
        assertConfigEntityUpdatableFieldsEquals(returnedConfigEntity, getPersistedConfigEntity(returnedConfigEntity));

        insertedConfigEntity = returnedConfigEntity;
    }

    @Test
    @Transactional
    void createConfigWithExistingId() throws Exception {
        // Create the Config with an existing ID
        configEntity.setId(1L);
        ConfigDTO configDTO = configMapper.toDto(configEntity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(configDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Config in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRecipeIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        configEntity.setRecipeId(null);

        // Create the Config, which fails.
        ConfigDTO configDTO = configMapper.toDto(configEntity);

        restConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(configDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        configEntity.setName(null);

        // Create the Config, which fails.
        ConfigDTO configDTO = configMapper.toDto(configEntity);

        restConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(configDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        configEntity.setCreatedAt(null);

        // Create the Config, which fails.
        ConfigDTO configDTO = configMapper.toDto(configEntity);

        restConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(configDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllConfigs() throws Exception {
        // Initialize the database
        insertedConfigEntity = configRepository.saveAndFlush(configEntity);

        // Get all the configList
        restConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].recipeId").value(hasItem(DEFAULT_RECIPE_ID.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getConfig() throws Exception {
        // Initialize the database
        insertedConfigEntity = configRepository.saveAndFlush(configEntity);

        // Get the config
        restConfigMockMvc
            .perform(get(ENTITY_API_URL_ID, configEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(configEntity.getId().intValue()))
            .andExpect(jsonPath("$.recipeId").value(DEFAULT_RECIPE_ID.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingConfig() throws Exception {
        // Get the config
        restConfigMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingConfig() throws Exception {
        // Initialize the database
        insertedConfigEntity = configRepository.saveAndFlush(configEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the config
        ConfigEntity updatedConfigEntity = configRepository.findById(configEntity.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedConfigEntity are not directly saved in db
        em.detach(updatedConfigEntity);
        updatedConfigEntity
            .recipeId(UPDATED_RECIPE_ID)
            .name(UPDATED_NAME)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);
        ConfigDTO configDTO = configMapper.toDto(updatedConfigEntity);

        restConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, configDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(configDTO))
            )
            .andExpect(status().isOk());

        // Validate the Config in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedConfigEntityToMatchAllProperties(updatedConfigEntity);
    }

    @Test
    @Transactional
    void putNonExistingConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        configEntity.setId(longCount.incrementAndGet());

        // Create the Config
        ConfigDTO configDTO = configMapper.toDto(configEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, configDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(configDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Config in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        configEntity.setId(longCount.incrementAndGet());

        // Create the Config
        ConfigDTO configDTO = configMapper.toDto(configEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(configDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Config in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        configEntity.setId(longCount.incrementAndGet());

        // Create the Config
        ConfigDTO configDTO = configMapper.toDto(configEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(configDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Config in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConfigWithPatch() throws Exception {
        // Initialize the database
        insertedConfigEntity = configRepository.saveAndFlush(configEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the config using partial update
        ConfigEntity partialUpdatedConfigEntity = new ConfigEntity();
        partialUpdatedConfigEntity.setId(configEntity.getId());

        partialUpdatedConfigEntity.recipeId(UPDATED_RECIPE_ID).updatedAt(UPDATED_UPDATED_AT).updatedBy(UPDATED_UPDATED_BY);

        restConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConfigEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConfigEntity))
            )
            .andExpect(status().isOk());

        // Validate the Config in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConfigEntityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedConfigEntity, configEntity),
            getPersistedConfigEntity(configEntity)
        );
    }

    @Test
    @Transactional
    void fullUpdateConfigWithPatch() throws Exception {
        // Initialize the database
        insertedConfigEntity = configRepository.saveAndFlush(configEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the config using partial update
        ConfigEntity partialUpdatedConfigEntity = new ConfigEntity();
        partialUpdatedConfigEntity.setId(configEntity.getId());

        partialUpdatedConfigEntity
            .recipeId(UPDATED_RECIPE_ID)
            .name(UPDATED_NAME)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);

        restConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConfigEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConfigEntity))
            )
            .andExpect(status().isOk());

        // Validate the Config in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConfigEntityUpdatableFieldsEquals(partialUpdatedConfigEntity, getPersistedConfigEntity(partialUpdatedConfigEntity));
    }

    @Test
    @Transactional
    void patchNonExistingConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        configEntity.setId(longCount.incrementAndGet());

        // Create the Config
        ConfigDTO configDTO = configMapper.toDto(configEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, configDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(configDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Config in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        configEntity.setId(longCount.incrementAndGet());

        // Create the Config
        ConfigDTO configDTO = configMapper.toDto(configEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(configDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Config in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        configEntity.setId(longCount.incrementAndGet());

        // Create the Config
        ConfigDTO configDTO = configMapper.toDto(configEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(configDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Config in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConfig() throws Exception {
        // Initialize the database
        insertedConfigEntity = configRepository.saveAndFlush(configEntity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the config
        restConfigMockMvc
            .perform(delete(ENTITY_API_URL_ID, configEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return configRepository.count();
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

    protected ConfigEntity getPersistedConfigEntity(ConfigEntity config) {
        return configRepository.findById(config.getId()).orElseThrow();
    }

    protected void assertPersistedConfigEntityToMatchAllProperties(ConfigEntity expectedConfigEntity) {
        assertConfigEntityAllPropertiesEquals(expectedConfigEntity, getPersistedConfigEntity(expectedConfigEntity));
    }

    protected void assertPersistedConfigEntityToMatchUpdatableProperties(ConfigEntity expectedConfigEntity) {
        assertConfigEntityAllUpdatablePropertiesEquals(expectedConfigEntity, getPersistedConfigEntity(expectedConfigEntity));
    }
}
