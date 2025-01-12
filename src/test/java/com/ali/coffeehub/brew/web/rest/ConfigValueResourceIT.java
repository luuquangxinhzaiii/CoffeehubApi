package com.ali.coffeehub.brew.web.rest;

import static com.ali.coffeehub.brew.domain.ConfigValueEntityAsserts.*;
import static com.ali.coffeehub.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ali.coffeehub.IntegrationTest;
import com.ali.coffeehub.brew.domain.ConfigValueEntity;
import com.ali.coffeehub.brew.repository.ConfigValueRepository;
import com.ali.coffeehub.brew.service.dto.ConfigValueDTO;
import com.ali.coffeehub.brew.service.mapper.ConfigValueMapper;
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
 * Integration tests for the {@link ConfigValueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConfigValueResourceIT {

    private static final Long DEFAULT_CONFIG_ID = 1L;
    private static final Long UPDATED_CONFIG_ID = 2L;

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/config-values";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ConfigValueRepository configValueRepository;

    @Autowired
    private ConfigValueMapper configValueMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConfigValueMockMvc;

    private ConfigValueEntity configValueEntity;

    private ConfigValueEntity insertedConfigValueEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigValueEntity createEntity() {
        return new ConfigValueEntity()
            .configId(DEFAULT_CONFIG_ID)
            .value(DEFAULT_VALUE)
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
    public static ConfigValueEntity createUpdatedEntity() {
        return new ConfigValueEntity()
            .configId(UPDATED_CONFIG_ID)
            .value(UPDATED_VALUE)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);
    }

    @BeforeEach
    public void initTest() {
        configValueEntity = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedConfigValueEntity != null) {
            configValueRepository.delete(insertedConfigValueEntity);
            insertedConfigValueEntity = null;
        }
    }

    @Test
    @Transactional
    void createConfigValue() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ConfigValue
        ConfigValueDTO configValueDTO = configValueMapper.toDto(configValueEntity);
        var returnedConfigValueDTO = om.readValue(
            restConfigValueMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(configValueDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ConfigValueDTO.class
        );

        // Validate the ConfigValue in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedConfigValueEntity = configValueMapper.toEntity(returnedConfigValueDTO);
        assertConfigValueEntityUpdatableFieldsEquals(returnedConfigValueEntity, getPersistedConfigValueEntity(returnedConfigValueEntity));

        insertedConfigValueEntity = returnedConfigValueEntity;
    }

    @Test
    @Transactional
    void createConfigValueWithExistingId() throws Exception {
        // Create the ConfigValue with an existing ID
        configValueEntity.setId(1L);
        ConfigValueDTO configValueDTO = configValueMapper.toDto(configValueEntity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigValueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(configValueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigValue in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkConfigIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        configValueEntity.setConfigId(null);

        // Create the ConfigValue, which fails.
        ConfigValueDTO configValueDTO = configValueMapper.toDto(configValueEntity);

        restConfigValueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(configValueDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        configValueEntity.setValue(null);

        // Create the ConfigValue, which fails.
        ConfigValueDTO configValueDTO = configValueMapper.toDto(configValueEntity);

        restConfigValueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(configValueDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        configValueEntity.setCreatedAt(null);

        // Create the ConfigValue, which fails.
        ConfigValueDTO configValueDTO = configValueMapper.toDto(configValueEntity);

        restConfigValueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(configValueDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllConfigValues() throws Exception {
        // Initialize the database
        insertedConfigValueEntity = configValueRepository.saveAndFlush(configValueEntity);

        // Get all the configValueList
        restConfigValueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configValueEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].configId").value(hasItem(DEFAULT_CONFIG_ID.intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getConfigValue() throws Exception {
        // Initialize the database
        insertedConfigValueEntity = configValueRepository.saveAndFlush(configValueEntity);

        // Get the configValue
        restConfigValueMockMvc
            .perform(get(ENTITY_API_URL_ID, configValueEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(configValueEntity.getId().intValue()))
            .andExpect(jsonPath("$.configId").value(DEFAULT_CONFIG_ID.intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingConfigValue() throws Exception {
        // Get the configValue
        restConfigValueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingConfigValue() throws Exception {
        // Initialize the database
        insertedConfigValueEntity = configValueRepository.saveAndFlush(configValueEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the configValue
        ConfigValueEntity updatedConfigValueEntity = configValueRepository.findById(configValueEntity.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedConfigValueEntity are not directly saved in db
        em.detach(updatedConfigValueEntity);
        updatedConfigValueEntity
            .configId(UPDATED_CONFIG_ID)
            .value(UPDATED_VALUE)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);
        ConfigValueDTO configValueDTO = configValueMapper.toDto(updatedConfigValueEntity);

        restConfigValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, configValueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(configValueDTO))
            )
            .andExpect(status().isOk());

        // Validate the ConfigValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedConfigValueEntityToMatchAllProperties(updatedConfigValueEntity);
    }

    @Test
    @Transactional
    void putNonExistingConfigValue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        configValueEntity.setId(longCount.incrementAndGet());

        // Create the ConfigValue
        ConfigValueDTO configValueDTO = configValueMapper.toDto(configValueEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, configValueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(configValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfigValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConfigValue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        configValueEntity.setId(longCount.incrementAndGet());

        // Create the ConfigValue
        ConfigValueDTO configValueDTO = configValueMapper.toDto(configValueEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(configValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfigValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConfigValue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        configValueEntity.setId(longCount.incrementAndGet());

        // Create the ConfigValue
        ConfigValueDTO configValueDTO = configValueMapper.toDto(configValueEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigValueMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(configValueDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConfigValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConfigValueWithPatch() throws Exception {
        // Initialize the database
        insertedConfigValueEntity = configValueRepository.saveAndFlush(configValueEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the configValue using partial update
        ConfigValueEntity partialUpdatedConfigValueEntity = new ConfigValueEntity();
        partialUpdatedConfigValueEntity.setId(configValueEntity.getId());

        partialUpdatedConfigValueEntity.value(UPDATED_VALUE).createdAt(UPDATED_CREATED_AT);

        restConfigValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConfigValueEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConfigValueEntity))
            )
            .andExpect(status().isOk());

        // Validate the ConfigValue in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConfigValueEntityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedConfigValueEntity, configValueEntity),
            getPersistedConfigValueEntity(configValueEntity)
        );
    }

    @Test
    @Transactional
    void fullUpdateConfigValueWithPatch() throws Exception {
        // Initialize the database
        insertedConfigValueEntity = configValueRepository.saveAndFlush(configValueEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the configValue using partial update
        ConfigValueEntity partialUpdatedConfigValueEntity = new ConfigValueEntity();
        partialUpdatedConfigValueEntity.setId(configValueEntity.getId());

        partialUpdatedConfigValueEntity
            .configId(UPDATED_CONFIG_ID)
            .value(UPDATED_VALUE)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);

        restConfigValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConfigValueEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConfigValueEntity))
            )
            .andExpect(status().isOk());

        // Validate the ConfigValue in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConfigValueEntityUpdatableFieldsEquals(
            partialUpdatedConfigValueEntity,
            getPersistedConfigValueEntity(partialUpdatedConfigValueEntity)
        );
    }

    @Test
    @Transactional
    void patchNonExistingConfigValue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        configValueEntity.setId(longCount.incrementAndGet());

        // Create the ConfigValue
        ConfigValueDTO configValueDTO = configValueMapper.toDto(configValueEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, configValueDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(configValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfigValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConfigValue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        configValueEntity.setId(longCount.incrementAndGet());

        // Create the ConfigValue
        ConfigValueDTO configValueDTO = configValueMapper.toDto(configValueEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(configValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfigValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConfigValue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        configValueEntity.setId(longCount.incrementAndGet());

        // Create the ConfigValue
        ConfigValueDTO configValueDTO = configValueMapper.toDto(configValueEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigValueMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(configValueDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConfigValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConfigValue() throws Exception {
        // Initialize the database
        insertedConfigValueEntity = configValueRepository.saveAndFlush(configValueEntity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the configValue
        restConfigValueMockMvc
            .perform(delete(ENTITY_API_URL_ID, configValueEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return configValueRepository.count();
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

    protected ConfigValueEntity getPersistedConfigValueEntity(ConfigValueEntity configValue) {
        return configValueRepository.findById(configValue.getId()).orElseThrow();
    }

    protected void assertPersistedConfigValueEntityToMatchAllProperties(ConfigValueEntity expectedConfigValueEntity) {
        assertConfigValueEntityAllPropertiesEquals(expectedConfigValueEntity, getPersistedConfigValueEntity(expectedConfigValueEntity));
    }

    protected void assertPersistedConfigValueEntityToMatchUpdatableProperties(ConfigValueEntity expectedConfigValueEntity) {
        assertConfigValueEntityAllUpdatablePropertiesEquals(
            expectedConfigValueEntity,
            getPersistedConfigValueEntity(expectedConfigValueEntity)
        );
    }
}
