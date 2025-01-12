package com.ali.coffeehub.web.rest;

import static com.ali.coffeehub.domain.SystemConfigEntityAsserts.*;
import static com.ali.coffeehub.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ali.coffeehub.IntegrationTest;
import com.ali.coffeehub.domain.SystemConfigEntity;
import com.ali.coffeehub.repository.SystemConfigRepository;
import com.ali.coffeehub.service.dto.SystemConfigDTO;
import com.ali.coffeehub.service.mapper.SystemConfigMapper;
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
 * Integration tests for the {@link SystemConfigResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SystemConfigResourceIT {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_MODULE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MODULE_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/system-configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SystemConfigRepository systemConfigRepository;

    @Autowired
    private SystemConfigMapper systemConfigMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSystemConfigMockMvc;

    private SystemConfigEntity systemConfigEntity;

    private SystemConfigEntity insertedSystemConfigEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemConfigEntity createEntity() {
        return new SystemConfigEntity()
            .key(DEFAULT_KEY)
            .value(DEFAULT_VALUE)
            .description(DEFAULT_DESCRIPTION)
            .moduleName(DEFAULT_MODULE_NAME)
            .isActive(DEFAULT_IS_ACTIVE)
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
    public static SystemConfigEntity createUpdatedEntity() {
        return new SystemConfigEntity()
            .key(UPDATED_KEY)
            .value(UPDATED_VALUE)
            .description(UPDATED_DESCRIPTION)
            .moduleName(UPDATED_MODULE_NAME)
            .isActive(UPDATED_IS_ACTIVE)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);
    }

    @BeforeEach
    public void initTest() {
        systemConfigEntity = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSystemConfigEntity != null) {
            systemConfigRepository.delete(insertedSystemConfigEntity);
            insertedSystemConfigEntity = null;
        }
    }

    @Test
    @Transactional
    void createSystemConfig() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SystemConfig
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfigEntity);
        var returnedSystemConfigDTO = om.readValue(
            restSystemConfigMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemConfigDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SystemConfigDTO.class
        );

        // Validate the SystemConfig in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSystemConfigEntity = systemConfigMapper.toEntity(returnedSystemConfigDTO);
        assertSystemConfigEntityUpdatableFieldsEquals(
            returnedSystemConfigEntity,
            getPersistedSystemConfigEntity(returnedSystemConfigEntity)
        );

        insertedSystemConfigEntity = returnedSystemConfigEntity;
    }

    @Test
    @Transactional
    void createSystemConfigWithExistingId() throws Exception {
        // Create the SystemConfig with an existing ID
        systemConfigEntity.setId(1L);
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfigEntity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSystemConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SystemConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkKeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        systemConfigEntity.setKey(null);

        // Create the SystemConfig, which fails.
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfigEntity);

        restSystemConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemConfigDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        systemConfigEntity.setCreatedAt(null);

        // Create the SystemConfig, which fails.
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfigEntity);

        restSystemConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemConfigDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSystemConfigs() throws Exception {
        // Initialize the database
        insertedSystemConfigEntity = systemConfigRepository.saveAndFlush(systemConfigEntity);

        // Get all the systemConfigList
        restSystemConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemConfigEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].moduleName").value(hasItem(DEFAULT_MODULE_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getSystemConfig() throws Exception {
        // Initialize the database
        insertedSystemConfigEntity = systemConfigRepository.saveAndFlush(systemConfigEntity);

        // Get the systemConfig
        restSystemConfigMockMvc
            .perform(get(ENTITY_API_URL_ID, systemConfigEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(systemConfigEntity.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.moduleName").value(DEFAULT_MODULE_NAME))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingSystemConfig() throws Exception {
        // Get the systemConfig
        restSystemConfigMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSystemConfig() throws Exception {
        // Initialize the database
        insertedSystemConfigEntity = systemConfigRepository.saveAndFlush(systemConfigEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the systemConfig
        SystemConfigEntity updatedSystemConfigEntity = systemConfigRepository.findById(systemConfigEntity.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSystemConfigEntity are not directly saved in db
        em.detach(updatedSystemConfigEntity);
        updatedSystemConfigEntity
            .key(UPDATED_KEY)
            .value(UPDATED_VALUE)
            .description(UPDATED_DESCRIPTION)
            .moduleName(UPDATED_MODULE_NAME)
            .isActive(UPDATED_IS_ACTIVE)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(updatedSystemConfigEntity);

        restSystemConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, systemConfigDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(systemConfigDTO))
            )
            .andExpect(status().isOk());

        // Validate the SystemConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSystemConfigEntityToMatchAllProperties(updatedSystemConfigEntity);
    }

    @Test
    @Transactional
    void putNonExistingSystemConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemConfigEntity.setId(longCount.incrementAndGet());

        // Create the SystemConfig
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfigEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, systemConfigDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(systemConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSystemConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemConfigEntity.setId(longCount.incrementAndGet());

        // Create the SystemConfig
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfigEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(systemConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSystemConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemConfigEntity.setId(longCount.incrementAndGet());

        // Create the SystemConfig
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfigEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemConfigMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemConfigDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSystemConfigWithPatch() throws Exception {
        // Initialize the database
        insertedSystemConfigEntity = systemConfigRepository.saveAndFlush(systemConfigEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the systemConfig using partial update
        SystemConfigEntity partialUpdatedSystemConfigEntity = new SystemConfigEntity();
        partialUpdatedSystemConfigEntity.setId(systemConfigEntity.getId());

        partialUpdatedSystemConfigEntity
            .value(UPDATED_VALUE)
            .description(UPDATED_DESCRIPTION)
            .moduleName(UPDATED_MODULE_NAME)
            .isActive(UPDATED_IS_ACTIVE)
            .updatedBy(UPDATED_UPDATED_BY);

        restSystemConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemConfigEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSystemConfigEntity))
            )
            .andExpect(status().isOk());

        // Validate the SystemConfig in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSystemConfigEntityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSystemConfigEntity, systemConfigEntity),
            getPersistedSystemConfigEntity(systemConfigEntity)
        );
    }

    @Test
    @Transactional
    void fullUpdateSystemConfigWithPatch() throws Exception {
        // Initialize the database
        insertedSystemConfigEntity = systemConfigRepository.saveAndFlush(systemConfigEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the systemConfig using partial update
        SystemConfigEntity partialUpdatedSystemConfigEntity = new SystemConfigEntity();
        partialUpdatedSystemConfigEntity.setId(systemConfigEntity.getId());

        partialUpdatedSystemConfigEntity
            .key(UPDATED_KEY)
            .value(UPDATED_VALUE)
            .description(UPDATED_DESCRIPTION)
            .moduleName(UPDATED_MODULE_NAME)
            .isActive(UPDATED_IS_ACTIVE)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);

        restSystemConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemConfigEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSystemConfigEntity))
            )
            .andExpect(status().isOk());

        // Validate the SystemConfig in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSystemConfigEntityUpdatableFieldsEquals(
            partialUpdatedSystemConfigEntity,
            getPersistedSystemConfigEntity(partialUpdatedSystemConfigEntity)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSystemConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemConfigEntity.setId(longCount.incrementAndGet());

        // Create the SystemConfig
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfigEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, systemConfigDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(systemConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSystemConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemConfigEntity.setId(longCount.incrementAndGet());

        // Create the SystemConfig
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfigEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(systemConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSystemConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemConfigEntity.setId(longCount.incrementAndGet());

        // Create the SystemConfig
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfigEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemConfigMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(systemConfigDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSystemConfig() throws Exception {
        // Initialize the database
        insertedSystemConfigEntity = systemConfigRepository.saveAndFlush(systemConfigEntity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the systemConfig
        restSystemConfigMockMvc
            .perform(delete(ENTITY_API_URL_ID, systemConfigEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return systemConfigRepository.count();
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

    protected SystemConfigEntity getPersistedSystemConfigEntity(SystemConfigEntity systemConfig) {
        return systemConfigRepository.findById(systemConfig.getId()).orElseThrow();
    }

    protected void assertPersistedSystemConfigEntityToMatchAllProperties(SystemConfigEntity expectedSystemConfigEntity) {
        assertSystemConfigEntityAllPropertiesEquals(expectedSystemConfigEntity, getPersistedSystemConfigEntity(expectedSystemConfigEntity));
    }

    protected void assertPersistedSystemConfigEntityToMatchUpdatableProperties(SystemConfigEntity expectedSystemConfigEntity) {
        assertSystemConfigEntityAllUpdatablePropertiesEquals(
            expectedSystemConfigEntity,
            getPersistedSystemConfigEntity(expectedSystemConfigEntity)
        );
    }
}
