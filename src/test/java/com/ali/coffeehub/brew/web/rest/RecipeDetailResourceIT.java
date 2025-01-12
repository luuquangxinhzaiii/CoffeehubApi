package com.ali.coffeehub.brew.web.rest;

import static com.ali.coffeehub.brew.domain.RecipeDetailEntityAsserts.*;
import static com.ali.coffeehub.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ali.coffeehub.IntegrationTest;
import com.ali.coffeehub.brew.domain.RecipeDetailEntity;
import com.ali.coffeehub.brew.repository.RecipeDetailRepository;
import com.ali.coffeehub.brew.service.dto.RecipeDetailDTO;
import com.ali.coffeehub.brew.service.mapper.RecipeDetailMapper;
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
 * Integration tests for the {@link RecipeDetailResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RecipeDetailResourceIT {

    private static final Long DEFAULT_RECIPE_ID = 1L;
    private static final Long UPDATED_RECIPE_ID = 2L;

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

    private static final String ENTITY_API_URL = "/api/recipe-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RecipeDetailRepository recipeDetailRepository;

    @Autowired
    private RecipeDetailMapper recipeDetailMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRecipeDetailMockMvc;

    private RecipeDetailEntity recipeDetailEntity;

    private RecipeDetailEntity insertedRecipeDetailEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecipeDetailEntity createEntity() {
        return new RecipeDetailEntity()
            .recipeId(DEFAULT_RECIPE_ID)
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
    public static RecipeDetailEntity createUpdatedEntity() {
        return new RecipeDetailEntity()
            .recipeId(UPDATED_RECIPE_ID)
            .detail(UPDATED_DETAIL)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);
    }

    @BeforeEach
    public void initTest() {
        recipeDetailEntity = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedRecipeDetailEntity != null) {
            recipeDetailRepository.delete(insertedRecipeDetailEntity);
            insertedRecipeDetailEntity = null;
        }
    }

    @Test
    @Transactional
    void createRecipeDetail() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the RecipeDetail
        RecipeDetailDTO recipeDetailDTO = recipeDetailMapper.toDto(recipeDetailEntity);
        var returnedRecipeDetailDTO = om.readValue(
            restRecipeDetailMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recipeDetailDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RecipeDetailDTO.class
        );

        // Validate the RecipeDetail in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRecipeDetailEntity = recipeDetailMapper.toEntity(returnedRecipeDetailDTO);
        assertRecipeDetailEntityUpdatableFieldsEquals(
            returnedRecipeDetailEntity,
            getPersistedRecipeDetailEntity(returnedRecipeDetailEntity)
        );

        insertedRecipeDetailEntity = returnedRecipeDetailEntity;
    }

    @Test
    @Transactional
    void createRecipeDetailWithExistingId() throws Exception {
        // Create the RecipeDetail with an existing ID
        recipeDetailEntity.setId(1L);
        RecipeDetailDTO recipeDetailDTO = recipeDetailMapper.toDto(recipeDetailEntity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecipeDetailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recipeDetailDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RecipeDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRecipeIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recipeDetailEntity.setRecipeId(null);

        // Create the RecipeDetail, which fails.
        RecipeDetailDTO recipeDetailDTO = recipeDetailMapper.toDto(recipeDetailEntity);

        restRecipeDetailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recipeDetailDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recipeDetailEntity.setCreatedAt(null);

        // Create the RecipeDetail, which fails.
        RecipeDetailDTO recipeDetailDTO = recipeDetailMapper.toDto(recipeDetailEntity);

        restRecipeDetailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recipeDetailDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRecipeDetails() throws Exception {
        // Initialize the database
        insertedRecipeDetailEntity = recipeDetailRepository.saveAndFlush(recipeDetailEntity);

        // Get all the recipeDetailList
        restRecipeDetailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recipeDetailEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].recipeId").value(hasItem(DEFAULT_RECIPE_ID.intValue())))
            .andExpect(jsonPath("$.[*].detail").value(hasItem(DEFAULT_DETAIL)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getRecipeDetail() throws Exception {
        // Initialize the database
        insertedRecipeDetailEntity = recipeDetailRepository.saveAndFlush(recipeDetailEntity);

        // Get the recipeDetail
        restRecipeDetailMockMvc
            .perform(get(ENTITY_API_URL_ID, recipeDetailEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(recipeDetailEntity.getId().intValue()))
            .andExpect(jsonPath("$.recipeId").value(DEFAULT_RECIPE_ID.intValue()))
            .andExpect(jsonPath("$.detail").value(DEFAULT_DETAIL))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingRecipeDetail() throws Exception {
        // Get the recipeDetail
        restRecipeDetailMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRecipeDetail() throws Exception {
        // Initialize the database
        insertedRecipeDetailEntity = recipeDetailRepository.saveAndFlush(recipeDetailEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recipeDetail
        RecipeDetailEntity updatedRecipeDetailEntity = recipeDetailRepository.findById(recipeDetailEntity.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRecipeDetailEntity are not directly saved in db
        em.detach(updatedRecipeDetailEntity);
        updatedRecipeDetailEntity
            .recipeId(UPDATED_RECIPE_ID)
            .detail(UPDATED_DETAIL)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);
        RecipeDetailDTO recipeDetailDTO = recipeDetailMapper.toDto(updatedRecipeDetailEntity);

        restRecipeDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recipeDetailDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recipeDetailDTO))
            )
            .andExpect(status().isOk());

        // Validate the RecipeDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRecipeDetailEntityToMatchAllProperties(updatedRecipeDetailEntity);
    }

    @Test
    @Transactional
    void putNonExistingRecipeDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recipeDetailEntity.setId(longCount.incrementAndGet());

        // Create the RecipeDetail
        RecipeDetailDTO recipeDetailDTO = recipeDetailMapper.toDto(recipeDetailEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecipeDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recipeDetailDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recipeDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecipeDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRecipeDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recipeDetailEntity.setId(longCount.incrementAndGet());

        // Create the RecipeDetail
        RecipeDetailDTO recipeDetailDTO = recipeDetailMapper.toDto(recipeDetailEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipeDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recipeDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecipeDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRecipeDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recipeDetailEntity.setId(longCount.incrementAndGet());

        // Create the RecipeDetail
        RecipeDetailDTO recipeDetailDTO = recipeDetailMapper.toDto(recipeDetailEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipeDetailMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recipeDetailDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RecipeDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRecipeDetailWithPatch() throws Exception {
        // Initialize the database
        insertedRecipeDetailEntity = recipeDetailRepository.saveAndFlush(recipeDetailEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recipeDetail using partial update
        RecipeDetailEntity partialUpdatedRecipeDetailEntity = new RecipeDetailEntity();
        partialUpdatedRecipeDetailEntity.setId(recipeDetailEntity.getId());

        partialUpdatedRecipeDetailEntity
            .recipeId(UPDATED_RECIPE_ID)
            .detail(UPDATED_DETAIL)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);

        restRecipeDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecipeDetailEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRecipeDetailEntity))
            )
            .andExpect(status().isOk());

        // Validate the RecipeDetail in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRecipeDetailEntityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRecipeDetailEntity, recipeDetailEntity),
            getPersistedRecipeDetailEntity(recipeDetailEntity)
        );
    }

    @Test
    @Transactional
    void fullUpdateRecipeDetailWithPatch() throws Exception {
        // Initialize the database
        insertedRecipeDetailEntity = recipeDetailRepository.saveAndFlush(recipeDetailEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recipeDetail using partial update
        RecipeDetailEntity partialUpdatedRecipeDetailEntity = new RecipeDetailEntity();
        partialUpdatedRecipeDetailEntity.setId(recipeDetailEntity.getId());

        partialUpdatedRecipeDetailEntity
            .recipeId(UPDATED_RECIPE_ID)
            .detail(UPDATED_DETAIL)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);

        restRecipeDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecipeDetailEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRecipeDetailEntity))
            )
            .andExpect(status().isOk());

        // Validate the RecipeDetail in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRecipeDetailEntityUpdatableFieldsEquals(
            partialUpdatedRecipeDetailEntity,
            getPersistedRecipeDetailEntity(partialUpdatedRecipeDetailEntity)
        );
    }

    @Test
    @Transactional
    void patchNonExistingRecipeDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recipeDetailEntity.setId(longCount.incrementAndGet());

        // Create the RecipeDetail
        RecipeDetailDTO recipeDetailDTO = recipeDetailMapper.toDto(recipeDetailEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecipeDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, recipeDetailDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(recipeDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecipeDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRecipeDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recipeDetailEntity.setId(longCount.incrementAndGet());

        // Create the RecipeDetail
        RecipeDetailDTO recipeDetailDTO = recipeDetailMapper.toDto(recipeDetailEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipeDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(recipeDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecipeDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRecipeDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recipeDetailEntity.setId(longCount.incrementAndGet());

        // Create the RecipeDetail
        RecipeDetailDTO recipeDetailDTO = recipeDetailMapper.toDto(recipeDetailEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipeDetailMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(recipeDetailDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RecipeDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRecipeDetail() throws Exception {
        // Initialize the database
        insertedRecipeDetailEntity = recipeDetailRepository.saveAndFlush(recipeDetailEntity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the recipeDetail
        restRecipeDetailMockMvc
            .perform(delete(ENTITY_API_URL_ID, recipeDetailEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return recipeDetailRepository.count();
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

    protected RecipeDetailEntity getPersistedRecipeDetailEntity(RecipeDetailEntity recipeDetail) {
        return recipeDetailRepository.findById(recipeDetail.getId()).orElseThrow();
    }

    protected void assertPersistedRecipeDetailEntityToMatchAllProperties(RecipeDetailEntity expectedRecipeDetailEntity) {
        assertRecipeDetailEntityAllPropertiesEquals(expectedRecipeDetailEntity, getPersistedRecipeDetailEntity(expectedRecipeDetailEntity));
    }

    protected void assertPersistedRecipeDetailEntityToMatchUpdatableProperties(RecipeDetailEntity expectedRecipeDetailEntity) {
        assertRecipeDetailEntityAllUpdatablePropertiesEquals(
            expectedRecipeDetailEntity,
            getPersistedRecipeDetailEntity(expectedRecipeDetailEntity)
        );
    }
}
