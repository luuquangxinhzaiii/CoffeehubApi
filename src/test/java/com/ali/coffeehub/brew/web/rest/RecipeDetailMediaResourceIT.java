package com.ali.coffeehub.brew.web.rest;

import static com.ali.coffeehub.brew.domain.RecipeDetailMediaEntityAsserts.*;
import static com.ali.coffeehub.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ali.coffeehub.IntegrationTest;
import com.ali.coffeehub.brew.domain.RecipeDetailMediaEntity;
import com.ali.coffeehub.brew.repository.RecipeDetailMediaRepository;
import com.ali.coffeehub.brew.service.dto.RecipeDetailMediaDTO;
import com.ali.coffeehub.brew.service.mapper.RecipeDetailMediaMapper;
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
 * Integration tests for the {@link RecipeDetailMediaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RecipeDetailMediaResourceIT {

    private static final Long DEFAULT_MEDIA_ID = 1L;
    private static final Long UPDATED_MEDIA_ID = 2L;

    private static final Long DEFAULT_RECIPE_DETAIL_ID = 1L;
    private static final Long UPDATED_RECIPE_DETAIL_ID = 2L;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/recipe-detail-medias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RecipeDetailMediaRepository recipeDetailMediaRepository;

    @Autowired
    private RecipeDetailMediaMapper recipeDetailMediaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRecipeDetailMediaMockMvc;

    private RecipeDetailMediaEntity recipeDetailMediaEntity;

    private RecipeDetailMediaEntity insertedRecipeDetailMediaEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecipeDetailMediaEntity createEntity() {
        return new RecipeDetailMediaEntity()
            .mediaId(DEFAULT_MEDIA_ID)
            .recipeDetailId(DEFAULT_RECIPE_DETAIL_ID)
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
    public static RecipeDetailMediaEntity createUpdatedEntity() {
        return new RecipeDetailMediaEntity()
            .mediaId(UPDATED_MEDIA_ID)
            .recipeDetailId(UPDATED_RECIPE_DETAIL_ID)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);
    }

    @BeforeEach
    public void initTest() {
        recipeDetailMediaEntity = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedRecipeDetailMediaEntity != null) {
            recipeDetailMediaRepository.delete(insertedRecipeDetailMediaEntity);
            insertedRecipeDetailMediaEntity = null;
        }
    }

    @Test
    @Transactional
    void createRecipeDetailMedia() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the RecipeDetailMedia
        RecipeDetailMediaDTO recipeDetailMediaDTO = recipeDetailMediaMapper.toDto(recipeDetailMediaEntity);
        var returnedRecipeDetailMediaDTO = om.readValue(
            restRecipeDetailMediaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recipeDetailMediaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RecipeDetailMediaDTO.class
        );

        // Validate the RecipeDetailMedia in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRecipeDetailMediaEntity = recipeDetailMediaMapper.toEntity(returnedRecipeDetailMediaDTO);
        assertRecipeDetailMediaEntityUpdatableFieldsEquals(
            returnedRecipeDetailMediaEntity,
            getPersistedRecipeDetailMediaEntity(returnedRecipeDetailMediaEntity)
        );

        insertedRecipeDetailMediaEntity = returnedRecipeDetailMediaEntity;
    }

    @Test
    @Transactional
    void createRecipeDetailMediaWithExistingId() throws Exception {
        // Create the RecipeDetailMedia with an existing ID
        recipeDetailMediaEntity.setId(1L);
        RecipeDetailMediaDTO recipeDetailMediaDTO = recipeDetailMediaMapper.toDto(recipeDetailMediaEntity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecipeDetailMediaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recipeDetailMediaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RecipeDetailMedia in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMediaIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recipeDetailMediaEntity.setMediaId(null);

        // Create the RecipeDetailMedia, which fails.
        RecipeDetailMediaDTO recipeDetailMediaDTO = recipeDetailMediaMapper.toDto(recipeDetailMediaEntity);

        restRecipeDetailMediaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recipeDetailMediaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRecipeDetailIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recipeDetailMediaEntity.setRecipeDetailId(null);

        // Create the RecipeDetailMedia, which fails.
        RecipeDetailMediaDTO recipeDetailMediaDTO = recipeDetailMediaMapper.toDto(recipeDetailMediaEntity);

        restRecipeDetailMediaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recipeDetailMediaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recipeDetailMediaEntity.setCreatedAt(null);

        // Create the RecipeDetailMedia, which fails.
        RecipeDetailMediaDTO recipeDetailMediaDTO = recipeDetailMediaMapper.toDto(recipeDetailMediaEntity);

        restRecipeDetailMediaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recipeDetailMediaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRecipeDetailMedias() throws Exception {
        // Initialize the database
        insertedRecipeDetailMediaEntity = recipeDetailMediaRepository.saveAndFlush(recipeDetailMediaEntity);

        // Get all the recipeDetailMediaList
        restRecipeDetailMediaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recipeDetailMediaEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].mediaId").value(hasItem(DEFAULT_MEDIA_ID.intValue())))
            .andExpect(jsonPath("$.[*].recipeDetailId").value(hasItem(DEFAULT_RECIPE_DETAIL_ID.intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getRecipeDetailMedia() throws Exception {
        // Initialize the database
        insertedRecipeDetailMediaEntity = recipeDetailMediaRepository.saveAndFlush(recipeDetailMediaEntity);

        // Get the recipeDetailMedia
        restRecipeDetailMediaMockMvc
            .perform(get(ENTITY_API_URL_ID, recipeDetailMediaEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(recipeDetailMediaEntity.getId().intValue()))
            .andExpect(jsonPath("$.mediaId").value(DEFAULT_MEDIA_ID.intValue()))
            .andExpect(jsonPath("$.recipeDetailId").value(DEFAULT_RECIPE_DETAIL_ID.intValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingRecipeDetailMedia() throws Exception {
        // Get the recipeDetailMedia
        restRecipeDetailMediaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRecipeDetailMedia() throws Exception {
        // Initialize the database
        insertedRecipeDetailMediaEntity = recipeDetailMediaRepository.saveAndFlush(recipeDetailMediaEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recipeDetailMedia
        RecipeDetailMediaEntity updatedRecipeDetailMediaEntity = recipeDetailMediaRepository
            .findById(recipeDetailMediaEntity.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedRecipeDetailMediaEntity are not directly saved in db
        em.detach(updatedRecipeDetailMediaEntity);
        updatedRecipeDetailMediaEntity
            .mediaId(UPDATED_MEDIA_ID)
            .recipeDetailId(UPDATED_RECIPE_DETAIL_ID)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);
        RecipeDetailMediaDTO recipeDetailMediaDTO = recipeDetailMediaMapper.toDto(updatedRecipeDetailMediaEntity);

        restRecipeDetailMediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recipeDetailMediaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recipeDetailMediaDTO))
            )
            .andExpect(status().isOk());

        // Validate the RecipeDetailMedia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRecipeDetailMediaEntityToMatchAllProperties(updatedRecipeDetailMediaEntity);
    }

    @Test
    @Transactional
    void putNonExistingRecipeDetailMedia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recipeDetailMediaEntity.setId(longCount.incrementAndGet());

        // Create the RecipeDetailMedia
        RecipeDetailMediaDTO recipeDetailMediaDTO = recipeDetailMediaMapper.toDto(recipeDetailMediaEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecipeDetailMediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recipeDetailMediaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recipeDetailMediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecipeDetailMedia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRecipeDetailMedia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recipeDetailMediaEntity.setId(longCount.incrementAndGet());

        // Create the RecipeDetailMedia
        RecipeDetailMediaDTO recipeDetailMediaDTO = recipeDetailMediaMapper.toDto(recipeDetailMediaEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipeDetailMediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recipeDetailMediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecipeDetailMedia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRecipeDetailMedia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recipeDetailMediaEntity.setId(longCount.incrementAndGet());

        // Create the RecipeDetailMedia
        RecipeDetailMediaDTO recipeDetailMediaDTO = recipeDetailMediaMapper.toDto(recipeDetailMediaEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipeDetailMediaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recipeDetailMediaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RecipeDetailMedia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRecipeDetailMediaWithPatch() throws Exception {
        // Initialize the database
        insertedRecipeDetailMediaEntity = recipeDetailMediaRepository.saveAndFlush(recipeDetailMediaEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recipeDetailMedia using partial update
        RecipeDetailMediaEntity partialUpdatedRecipeDetailMediaEntity = new RecipeDetailMediaEntity();
        partialUpdatedRecipeDetailMediaEntity.setId(recipeDetailMediaEntity.getId());

        partialUpdatedRecipeDetailMediaEntity
            .recipeDetailId(UPDATED_RECIPE_DETAIL_ID)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);

        restRecipeDetailMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecipeDetailMediaEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRecipeDetailMediaEntity))
            )
            .andExpect(status().isOk());

        // Validate the RecipeDetailMedia in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRecipeDetailMediaEntityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRecipeDetailMediaEntity, recipeDetailMediaEntity),
            getPersistedRecipeDetailMediaEntity(recipeDetailMediaEntity)
        );
    }

    @Test
    @Transactional
    void fullUpdateRecipeDetailMediaWithPatch() throws Exception {
        // Initialize the database
        insertedRecipeDetailMediaEntity = recipeDetailMediaRepository.saveAndFlush(recipeDetailMediaEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recipeDetailMedia using partial update
        RecipeDetailMediaEntity partialUpdatedRecipeDetailMediaEntity = new RecipeDetailMediaEntity();
        partialUpdatedRecipeDetailMediaEntity.setId(recipeDetailMediaEntity.getId());

        partialUpdatedRecipeDetailMediaEntity
            .mediaId(UPDATED_MEDIA_ID)
            .recipeDetailId(UPDATED_RECIPE_DETAIL_ID)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);

        restRecipeDetailMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecipeDetailMediaEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRecipeDetailMediaEntity))
            )
            .andExpect(status().isOk());

        // Validate the RecipeDetailMedia in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRecipeDetailMediaEntityUpdatableFieldsEquals(
            partialUpdatedRecipeDetailMediaEntity,
            getPersistedRecipeDetailMediaEntity(partialUpdatedRecipeDetailMediaEntity)
        );
    }

    @Test
    @Transactional
    void patchNonExistingRecipeDetailMedia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recipeDetailMediaEntity.setId(longCount.incrementAndGet());

        // Create the RecipeDetailMedia
        RecipeDetailMediaDTO recipeDetailMediaDTO = recipeDetailMediaMapper.toDto(recipeDetailMediaEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecipeDetailMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, recipeDetailMediaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(recipeDetailMediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecipeDetailMedia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRecipeDetailMedia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recipeDetailMediaEntity.setId(longCount.incrementAndGet());

        // Create the RecipeDetailMedia
        RecipeDetailMediaDTO recipeDetailMediaDTO = recipeDetailMediaMapper.toDto(recipeDetailMediaEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipeDetailMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(recipeDetailMediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecipeDetailMedia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRecipeDetailMedia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recipeDetailMediaEntity.setId(longCount.incrementAndGet());

        // Create the RecipeDetailMedia
        RecipeDetailMediaDTO recipeDetailMediaDTO = recipeDetailMediaMapper.toDto(recipeDetailMediaEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipeDetailMediaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(recipeDetailMediaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RecipeDetailMedia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRecipeDetailMedia() throws Exception {
        // Initialize the database
        insertedRecipeDetailMediaEntity = recipeDetailMediaRepository.saveAndFlush(recipeDetailMediaEntity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the recipeDetailMedia
        restRecipeDetailMediaMockMvc
            .perform(delete(ENTITY_API_URL_ID, recipeDetailMediaEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return recipeDetailMediaRepository.count();
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

    protected RecipeDetailMediaEntity getPersistedRecipeDetailMediaEntity(RecipeDetailMediaEntity recipeDetailMedia) {
        return recipeDetailMediaRepository.findById(recipeDetailMedia.getId()).orElseThrow();
    }

    protected void assertPersistedRecipeDetailMediaEntityToMatchAllProperties(RecipeDetailMediaEntity expectedRecipeDetailMediaEntity) {
        assertRecipeDetailMediaEntityAllPropertiesEquals(
            expectedRecipeDetailMediaEntity,
            getPersistedRecipeDetailMediaEntity(expectedRecipeDetailMediaEntity)
        );
    }

    protected void assertPersistedRecipeDetailMediaEntityToMatchUpdatableProperties(
        RecipeDetailMediaEntity expectedRecipeDetailMediaEntity
    ) {
        assertRecipeDetailMediaEntityAllUpdatablePropertiesEquals(
            expectedRecipeDetailMediaEntity,
            getPersistedRecipeDetailMediaEntity(expectedRecipeDetailMediaEntity)
        );
    }
}
