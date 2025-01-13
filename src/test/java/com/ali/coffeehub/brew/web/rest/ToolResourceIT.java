package com.ali.coffeehub.brew.web.rest;

import static com.ali.coffeehub.brew.domain.ToolEntityAsserts.*;
import static com.ali.coffeehub.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ali.coffeehub.IntegrationTest;
import com.ali.coffeehub.brew.domain.ToolEntity;
import com.ali.coffeehub.brew.repository.ToolRepository;
import com.ali.coffeehub.brew.service.dto.ToolDTO;
import com.ali.coffeehub.brew.service.mapper.ToolMapper;
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
 * Integration tests for the {@link ToolResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ToolResourceIT {

    private static final Long DEFAULT_BREW_ID = 1L;
    private static final Long UPDATED_BREW_ID = 2L;

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

    private static final String ENTITY_API_URL = "/api/tools";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ToolRepository toolRepository;

    @Autowired
    private ToolMapper toolMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restToolMockMvc;

    private ToolEntity toolEntity;

    private ToolEntity insertedToolEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ToolEntity createEntity() {
        return new ToolEntity()
            .brewId(DEFAULT_BREW_ID)
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
    public static ToolEntity createUpdatedEntity() {
        return new ToolEntity()
            .brewId(UPDATED_BREW_ID)
            .detail(UPDATED_DETAIL)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);
    }

    @BeforeEach
    public void initTest() {
        toolEntity = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedToolEntity != null) {
            toolRepository.delete(insertedToolEntity);
            insertedToolEntity = null;
        }
    }

    @Test
    @Transactional
    void createTool() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Tool
        ToolDTO toolDTO = toolMapper.toDto(toolEntity);
        var returnedToolDTO = om.readValue(
            restToolMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(toolDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ToolDTO.class
        );

        // Validate the Tool in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedToolEntity = toolMapper.toEntity(returnedToolDTO);
        assertToolEntityUpdatableFieldsEquals(returnedToolEntity, getPersistedToolEntity(returnedToolEntity));

        insertedToolEntity = returnedToolEntity;
    }

    @Test
    @Transactional
    void createToolWithExistingId() throws Exception {
        // Create the Tool with an existing ID
        toolEntity.setId(1L);
        ToolDTO toolDTO = toolMapper.toDto(toolEntity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restToolMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(toolDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tool in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBrewIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        toolEntity.setBrewId(null);

        // Create the Tool, which fails.
        ToolDTO toolDTO = toolMapper.toDto(toolEntity);

        restToolMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(toolDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        toolEntity.setCreatedAt(null);

        // Create the Tool, which fails.
        ToolDTO toolDTO = toolMapper.toDto(toolEntity);

        restToolMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(toolDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTools() throws Exception {
        // Initialize the database
        insertedToolEntity = toolRepository.saveAndFlush(toolEntity);

        // Get all the toolList
        restToolMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(toolEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].brewId").value(hasItem(DEFAULT_BREW_ID.intValue())))
            .andExpect(jsonPath("$.[*].detail").value(hasItem(DEFAULT_DETAIL)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getTool() throws Exception {
        // Initialize the database
        insertedToolEntity = toolRepository.saveAndFlush(toolEntity);

        // Get the tool
        restToolMockMvc
            .perform(get(ENTITY_API_URL_ID, toolEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(toolEntity.getId().intValue()))
            .andExpect(jsonPath("$.brewId").value(DEFAULT_BREW_ID.intValue()))
            .andExpect(jsonPath("$.detail").value(DEFAULT_DETAIL))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingTool() throws Exception {
        // Get the tool
        restToolMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTool() throws Exception {
        // Initialize the database
        insertedToolEntity = toolRepository.saveAndFlush(toolEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tool
        ToolEntity updatedToolEntity = toolRepository.findById(toolEntity.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedToolEntity are not directly saved in db
        em.detach(updatedToolEntity);
        updatedToolEntity
            .brewId(UPDATED_BREW_ID)
            .detail(UPDATED_DETAIL)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);
        ToolDTO toolDTO = toolMapper.toDto(updatedToolEntity);

        restToolMockMvc
            .perform(put(ENTITY_API_URL_ID, toolDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(toolDTO)))
            .andExpect(status().isOk());

        // Validate the Tool in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedToolEntityToMatchAllProperties(updatedToolEntity);
    }

    @Test
    @Transactional
    void putNonExistingTool() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        toolEntity.setId(longCount.incrementAndGet());

        // Create the Tool
        ToolDTO toolDTO = toolMapper.toDto(toolEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restToolMockMvc
            .perform(put(ENTITY_API_URL_ID, toolDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(toolDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tool in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTool() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        toolEntity.setId(longCount.incrementAndGet());

        // Create the Tool
        ToolDTO toolDTO = toolMapper.toDto(toolEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restToolMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(toolDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tool in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTool() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        toolEntity.setId(longCount.incrementAndGet());

        // Create the Tool
        ToolDTO toolDTO = toolMapper.toDto(toolEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restToolMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(toolDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tool in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateToolWithPatch() throws Exception {
        // Initialize the database
        insertedToolEntity = toolRepository.saveAndFlush(toolEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tool using partial update
        ToolEntity partialUpdatedToolEntity = new ToolEntity();
        partialUpdatedToolEntity.setId(toolEntity.getId());

        partialUpdatedToolEntity
            .brewId(UPDATED_BREW_ID)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);

        restToolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedToolEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedToolEntity))
            )
            .andExpect(status().isOk());

        // Validate the Tool in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertToolEntityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedToolEntity, toolEntity),
            getPersistedToolEntity(toolEntity)
        );
    }

    @Test
    @Transactional
    void fullUpdateToolWithPatch() throws Exception {
        // Initialize the database
        insertedToolEntity = toolRepository.saveAndFlush(toolEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tool using partial update
        ToolEntity partialUpdatedToolEntity = new ToolEntity();
        partialUpdatedToolEntity.setId(toolEntity.getId());

        partialUpdatedToolEntity
            .brewId(UPDATED_BREW_ID)
            .detail(UPDATED_DETAIL)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);

        restToolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedToolEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedToolEntity))
            )
            .andExpect(status().isOk());

        // Validate the Tool in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertToolEntityUpdatableFieldsEquals(partialUpdatedToolEntity, getPersistedToolEntity(partialUpdatedToolEntity));
    }

    @Test
    @Transactional
    void patchNonExistingTool() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        toolEntity.setId(longCount.incrementAndGet());

        // Create the Tool
        ToolDTO toolDTO = toolMapper.toDto(toolEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restToolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, toolDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(toolDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tool in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTool() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        toolEntity.setId(longCount.incrementAndGet());

        // Create the Tool
        ToolDTO toolDTO = toolMapper.toDto(toolEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restToolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(toolDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tool in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTool() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        toolEntity.setId(longCount.incrementAndGet());

        // Create the Tool
        ToolDTO toolDTO = toolMapper.toDto(toolEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restToolMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(toolDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tool in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTool() throws Exception {
        // Initialize the database
        insertedToolEntity = toolRepository.saveAndFlush(toolEntity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tool
        restToolMockMvc
            .perform(delete(ENTITY_API_URL_ID, toolEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return toolRepository.count();
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

    protected ToolEntity getPersistedToolEntity(ToolEntity tool) {
        return toolRepository.findById(tool.getId()).orElseThrow();
    }

    protected void assertPersistedToolEntityToMatchAllProperties(ToolEntity expectedToolEntity) {
        assertToolEntityAllPropertiesEquals(expectedToolEntity, getPersistedToolEntity(expectedToolEntity));
    }

    protected void assertPersistedToolEntityToMatchUpdatableProperties(ToolEntity expectedToolEntity) {
        assertToolEntityAllUpdatablePropertiesEquals(expectedToolEntity, getPersistedToolEntity(expectedToolEntity));
    }
}
