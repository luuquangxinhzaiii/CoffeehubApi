package com.ali.coffeehub.brew.web.rest;

import static com.ali.coffeehub.brew.domain.RecipeEntityAsserts.*;
import static com.ali.coffeehub.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ali.coffeehub.IntegrationTest;
import com.ali.coffeehub.brew.domain.RecipeEntity;
import com.ali.coffeehub.brew.repository.RecipeRepository;
import com.ali.coffeehub.brew.service.dto.RecipeDTO;
import com.ali.coffeehub.brew.service.mapper.RecipeMapper;
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
 * Integration tests for the {@link RecipeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RecipeResourceIT {

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

    private static final String ENTITY_API_URL = "/api/recipes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeMapper recipeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRecipeMockMvc;

    private RecipeEntity recipeEntity;

    private RecipeEntity insertedRecipeEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecipeEntity createEntity() {
        return new RecipeEntity()
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
    public static RecipeEntity createUpdatedEntity() {
        return new RecipeEntity()
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
        recipeEntity = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedRecipeEntity != null) {
            recipeRepository.delete(insertedRecipeEntity);
            insertedRecipeEntity = null;
        }
    }

    @Test
    @Transactional
    void createRecipe() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Recipe
        RecipeDTO recipeDTO = recipeMapper.toDto(recipeEntity);
        var returnedRecipeDTO = om.readValue(
            restRecipeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recipeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RecipeDTO.class
        );

        // Validate the Recipe in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRecipeEntity = recipeMapper.toEntity(returnedRecipeDTO);
        assertRecipeEntityUpdatableFieldsEquals(returnedRecipeEntity, getPersistedRecipeEntity(returnedRecipeEntity));

        insertedRecipeEntity = returnedRecipeEntity;
    }

    @Test
    @Transactional
    void createRecipeWithExistingId() throws Exception {
        // Create the Recipe with an existing ID
        recipeEntity.setId(1L);
        RecipeDTO recipeDTO = recipeMapper.toDto(recipeEntity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecipeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recipeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Recipe in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBrewIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recipeEntity.setBrewId(null);

        // Create the Recipe, which fails.
        RecipeDTO recipeDTO = recipeMapper.toDto(recipeEntity);

        restRecipeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recipeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recipeEntity.setCreatedAt(null);

        // Create the Recipe, which fails.
        RecipeDTO recipeDTO = recipeMapper.toDto(recipeEntity);

        restRecipeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recipeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRecipes() throws Exception {
        // Initialize the database
        insertedRecipeEntity = recipeRepository.saveAndFlush(recipeEntity);

        // Get all the recipeList
        restRecipeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recipeEntity.getId().intValue())))
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
    void getRecipe() throws Exception {
        // Initialize the database
        insertedRecipeEntity = recipeRepository.saveAndFlush(recipeEntity);

        // Get the recipe
        restRecipeMockMvc
            .perform(get(ENTITY_API_URL_ID, recipeEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(recipeEntity.getId().intValue()))
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
    void getNonExistingRecipe() throws Exception {
        // Get the recipe
        restRecipeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRecipe() throws Exception {
        // Initialize the database
        insertedRecipeEntity = recipeRepository.saveAndFlush(recipeEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recipe
        RecipeEntity updatedRecipeEntity = recipeRepository.findById(recipeEntity.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRecipeEntity are not directly saved in db
        em.detach(updatedRecipeEntity);
        updatedRecipeEntity
            .brewId(UPDATED_BREW_ID)
            .name(UPDATED_NAME)
            .detail(UPDATED_DETAIL)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);
        RecipeDTO recipeDTO = recipeMapper.toDto(updatedRecipeEntity);

        restRecipeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recipeDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recipeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Recipe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRecipeEntityToMatchAllProperties(updatedRecipeEntity);
    }

    @Test
    @Transactional
    void putNonExistingRecipe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recipeEntity.setId(longCount.incrementAndGet());

        // Create the Recipe
        RecipeDTO recipeDTO = recipeMapper.toDto(recipeEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecipeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recipeDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recipeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recipe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRecipe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recipeEntity.setId(longCount.incrementAndGet());

        // Create the Recipe
        RecipeDTO recipeDTO = recipeMapper.toDto(recipeEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recipeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recipe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRecipe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recipeEntity.setId(longCount.incrementAndGet());

        // Create the Recipe
        RecipeDTO recipeDTO = recipeMapper.toDto(recipeEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recipeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Recipe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRecipeWithPatch() throws Exception {
        // Initialize the database
        insertedRecipeEntity = recipeRepository.saveAndFlush(recipeEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recipe using partial update
        RecipeEntity partialUpdatedRecipeEntity = new RecipeEntity();
        partialUpdatedRecipeEntity.setId(recipeEntity.getId());

        partialUpdatedRecipeEntity
            .brewId(UPDATED_BREW_ID)
            .name(UPDATED_NAME)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);

        restRecipeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecipeEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRecipeEntity))
            )
            .andExpect(status().isOk());

        // Validate the Recipe in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRecipeEntityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRecipeEntity, recipeEntity),
            getPersistedRecipeEntity(recipeEntity)
        );
    }

    @Test
    @Transactional
    void fullUpdateRecipeWithPatch() throws Exception {
        // Initialize the database
        insertedRecipeEntity = recipeRepository.saveAndFlush(recipeEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recipe using partial update
        RecipeEntity partialUpdatedRecipeEntity = new RecipeEntity();
        partialUpdatedRecipeEntity.setId(recipeEntity.getId());

        partialUpdatedRecipeEntity
            .brewId(UPDATED_BREW_ID)
            .name(UPDATED_NAME)
            .detail(UPDATED_DETAIL)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);

        restRecipeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecipeEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRecipeEntity))
            )
            .andExpect(status().isOk());

        // Validate the Recipe in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRecipeEntityUpdatableFieldsEquals(partialUpdatedRecipeEntity, getPersistedRecipeEntity(partialUpdatedRecipeEntity));
    }

    @Test
    @Transactional
    void patchNonExistingRecipe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recipeEntity.setId(longCount.incrementAndGet());

        // Create the Recipe
        RecipeDTO recipeDTO = recipeMapper.toDto(recipeEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecipeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, recipeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(recipeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recipe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRecipe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recipeEntity.setId(longCount.incrementAndGet());

        // Create the Recipe
        RecipeDTO recipeDTO = recipeMapper.toDto(recipeEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(recipeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recipe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRecipe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recipeEntity.setId(longCount.incrementAndGet());

        // Create the Recipe
        RecipeDTO recipeDTO = recipeMapper.toDto(recipeEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(recipeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Recipe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRecipe() throws Exception {
        // Initialize the database
        insertedRecipeEntity = recipeRepository.saveAndFlush(recipeEntity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the recipe
        restRecipeMockMvc
            .perform(delete(ENTITY_API_URL_ID, recipeEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return recipeRepository.count();
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

    protected RecipeEntity getPersistedRecipeEntity(RecipeEntity recipe) {
        return recipeRepository.findById(recipe.getId()).orElseThrow();
    }

    protected void assertPersistedRecipeEntityToMatchAllProperties(RecipeEntity expectedRecipeEntity) {
        assertRecipeEntityAllPropertiesEquals(expectedRecipeEntity, getPersistedRecipeEntity(expectedRecipeEntity));
    }

    protected void assertPersistedRecipeEntityToMatchUpdatableProperties(RecipeEntity expectedRecipeEntity) {
        assertRecipeEntityAllUpdatablePropertiesEquals(expectedRecipeEntity, getPersistedRecipeEntity(expectedRecipeEntity));
    }
}
