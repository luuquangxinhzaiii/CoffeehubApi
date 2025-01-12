package com.ali.coffeehub.article.web.rest;

import static com.ali.coffeehub.article.domain.ArticlesEntityAsserts.*;
import static com.ali.coffeehub.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ali.coffeehub.IntegrationTest;
import com.ali.coffeehub.article.domain.ArticlesEntity;
import com.ali.coffeehub.article.repository.ArticlesRepository;
import com.ali.coffeehub.article.service.dto.ArticlesDTO;
import com.ali.coffeehub.article.service.mapper.ArticlesMapper;
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
 * Integration tests for the {@link ArticlesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ArticlesResourceIT {

    private static final Long DEFAULT_CATEGORY_ID = 1L;
    private static final Long UPDATED_CATEGORY_ID = 2L;

    private static final Long DEFAULT_BODY_ID = 1L;
    private static final Long UPDATED_BODY_ID = 2L;

    private static final Long DEFAULT_AUTHOR_ID = 1L;
    private static final Long UPDATED_AUTHOR_ID = 2L;

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SLUG = "AAAAAAAAAA";
    private static final String UPDATED_SLUG = "BBBBBBBBBB";

    private static final String DEFAULT_SUB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_SUB_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_THUMBNAIL_URL = "AAAAAAAAAA";
    private static final String UPDATED_THUMBNAIL_URL = "BBBBBBBBBB";

    private static final Integer DEFAULT_READING_TIME = 1;
    private static final Integer UPDATED_READING_TIME = 2;

    private static final Boolean DEFAULT_IS_PINNED = false;
    private static final Boolean UPDATED_IS_PINNED = true;

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

    private static final String ENTITY_API_URL = "/api/articles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ArticlesRepository articlesRepository;

    @Autowired
    private ArticlesMapper articlesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArticlesMockMvc;

    private ArticlesEntity articlesEntity;

    private ArticlesEntity insertedArticlesEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArticlesEntity createEntity() {
        return new ArticlesEntity()
            .categoryID(DEFAULT_CATEGORY_ID)
            .bodyId(DEFAULT_BODY_ID)
            .authorId(DEFAULT_AUTHOR_ID)
            .title(DEFAULT_TITLE)
            .slug(DEFAULT_SLUG)
            .subTitle(DEFAULT_SUB_TITLE)
            .thumbnailUrl(DEFAULT_THUMBNAIL_URL)
            .readingTime(DEFAULT_READING_TIME)
            .isPinned(DEFAULT_IS_PINNED)
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
    public static ArticlesEntity createUpdatedEntity() {
        return new ArticlesEntity()
            .categoryID(UPDATED_CATEGORY_ID)
            .bodyId(UPDATED_BODY_ID)
            .authorId(UPDATED_AUTHOR_ID)
            .title(UPDATED_TITLE)
            .slug(UPDATED_SLUG)
            .subTitle(UPDATED_SUB_TITLE)
            .thumbnailUrl(UPDATED_THUMBNAIL_URL)
            .readingTime(UPDATED_READING_TIME)
            .isPinned(UPDATED_IS_PINNED)
            .deleted(UPDATED_DELETED)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);
    }

    @BeforeEach
    public void initTest() {
        articlesEntity = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedArticlesEntity != null) {
            articlesRepository.delete(insertedArticlesEntity);
            insertedArticlesEntity = null;
        }
    }

    @Test
    @Transactional
    void createArticles() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Articles
        ArticlesDTO articlesDTO = articlesMapper.toDto(articlesEntity);
        var returnedArticlesDTO = om.readValue(
            restArticlesMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(articlesDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ArticlesDTO.class
        );

        // Validate the Articles in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedArticlesEntity = articlesMapper.toEntity(returnedArticlesDTO);
        assertArticlesEntityUpdatableFieldsEquals(returnedArticlesEntity, getPersistedArticlesEntity(returnedArticlesEntity));

        insertedArticlesEntity = returnedArticlesEntity;
    }

    @Test
    @Transactional
    void createArticlesWithExistingId() throws Exception {
        // Create the Articles with an existing ID
        articlesEntity.setId(1L);
        ArticlesDTO articlesDTO = articlesMapper.toDto(articlesEntity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restArticlesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(articlesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Articles in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCategoryIDIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        articlesEntity.setCategoryID(null);

        // Create the Articles, which fails.
        ArticlesDTO articlesDTO = articlesMapper.toDto(articlesEntity);

        restArticlesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(articlesDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBodyIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        articlesEntity.setBodyId(null);

        // Create the Articles, which fails.
        ArticlesDTO articlesDTO = articlesMapper.toDto(articlesEntity);

        restArticlesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(articlesDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAuthorIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        articlesEntity.setAuthorId(null);

        // Create the Articles, which fails.
        ArticlesDTO articlesDTO = articlesMapper.toDto(articlesEntity);

        restArticlesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(articlesDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        articlesEntity.setTitle(null);

        // Create the Articles, which fails.
        ArticlesDTO articlesDTO = articlesMapper.toDto(articlesEntity);

        restArticlesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(articlesDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        articlesEntity.setCreatedAt(null);

        // Create the Articles, which fails.
        ArticlesDTO articlesDTO = articlesMapper.toDto(articlesEntity);

        restArticlesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(articlesDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllArticles() throws Exception {
        // Initialize the database
        insertedArticlesEntity = articlesRepository.saveAndFlush(articlesEntity);

        // Get all the articlesList
        restArticlesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(articlesEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryID").value(hasItem(DEFAULT_CATEGORY_ID.intValue())))
            .andExpect(jsonPath("$.[*].bodyId").value(hasItem(DEFAULT_BODY_ID.intValue())))
            .andExpect(jsonPath("$.[*].authorId").value(hasItem(DEFAULT_AUTHOR_ID.intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG)))
            .andExpect(jsonPath("$.[*].subTitle").value(hasItem(DEFAULT_SUB_TITLE)))
            .andExpect(jsonPath("$.[*].thumbnailUrl").value(hasItem(DEFAULT_THUMBNAIL_URL)))
            .andExpect(jsonPath("$.[*].readingTime").value(hasItem(DEFAULT_READING_TIME)))
            .andExpect(jsonPath("$.[*].isPinned").value(hasItem(DEFAULT_IS_PINNED)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getArticles() throws Exception {
        // Initialize the database
        insertedArticlesEntity = articlesRepository.saveAndFlush(articlesEntity);

        // Get the articles
        restArticlesMockMvc
            .perform(get(ENTITY_API_URL_ID, articlesEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(articlesEntity.getId().intValue()))
            .andExpect(jsonPath("$.categoryID").value(DEFAULT_CATEGORY_ID.intValue()))
            .andExpect(jsonPath("$.bodyId").value(DEFAULT_BODY_ID.intValue()))
            .andExpect(jsonPath("$.authorId").value(DEFAULT_AUTHOR_ID.intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.slug").value(DEFAULT_SLUG))
            .andExpect(jsonPath("$.subTitle").value(DEFAULT_SUB_TITLE))
            .andExpect(jsonPath("$.thumbnailUrl").value(DEFAULT_THUMBNAIL_URL))
            .andExpect(jsonPath("$.readingTime").value(DEFAULT_READING_TIME))
            .andExpect(jsonPath("$.isPinned").value(DEFAULT_IS_PINNED))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingArticles() throws Exception {
        // Get the articles
        restArticlesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingArticles() throws Exception {
        // Initialize the database
        insertedArticlesEntity = articlesRepository.saveAndFlush(articlesEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the articles
        ArticlesEntity updatedArticlesEntity = articlesRepository.findById(articlesEntity.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedArticlesEntity are not directly saved in db
        em.detach(updatedArticlesEntity);
        updatedArticlesEntity
            .categoryID(UPDATED_CATEGORY_ID)
            .bodyId(UPDATED_BODY_ID)
            .authorId(UPDATED_AUTHOR_ID)
            .title(UPDATED_TITLE)
            .slug(UPDATED_SLUG)
            .subTitle(UPDATED_SUB_TITLE)
            .thumbnailUrl(UPDATED_THUMBNAIL_URL)
            .readingTime(UPDATED_READING_TIME)
            .isPinned(UPDATED_IS_PINNED)
            .deleted(UPDATED_DELETED)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);
        ArticlesDTO articlesDTO = articlesMapper.toDto(updatedArticlesEntity);

        restArticlesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, articlesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(articlesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Articles in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedArticlesEntityToMatchAllProperties(updatedArticlesEntity);
    }

    @Test
    @Transactional
    void putNonExistingArticles() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        articlesEntity.setId(longCount.incrementAndGet());

        // Create the Articles
        ArticlesDTO articlesDTO = articlesMapper.toDto(articlesEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArticlesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, articlesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(articlesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Articles in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchArticles() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        articlesEntity.setId(longCount.incrementAndGet());

        // Create the Articles
        ArticlesDTO articlesDTO = articlesMapper.toDto(articlesEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticlesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(articlesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Articles in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamArticles() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        articlesEntity.setId(longCount.incrementAndGet());

        // Create the Articles
        ArticlesDTO articlesDTO = articlesMapper.toDto(articlesEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticlesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(articlesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Articles in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateArticlesWithPatch() throws Exception {
        // Initialize the database
        insertedArticlesEntity = articlesRepository.saveAndFlush(articlesEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the articles using partial update
        ArticlesEntity partialUpdatedArticlesEntity = new ArticlesEntity();
        partialUpdatedArticlesEntity.setId(articlesEntity.getId());

        partialUpdatedArticlesEntity
            .categoryID(UPDATED_CATEGORY_ID)
            .bodyId(UPDATED_BODY_ID)
            .title(UPDATED_TITLE)
            .thumbnailUrl(UPDATED_THUMBNAIL_URL)
            .readingTime(UPDATED_READING_TIME)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);

        restArticlesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArticlesEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedArticlesEntity))
            )
            .andExpect(status().isOk());

        // Validate the Articles in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertArticlesEntityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedArticlesEntity, articlesEntity),
            getPersistedArticlesEntity(articlesEntity)
        );
    }

    @Test
    @Transactional
    void fullUpdateArticlesWithPatch() throws Exception {
        // Initialize the database
        insertedArticlesEntity = articlesRepository.saveAndFlush(articlesEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the articles using partial update
        ArticlesEntity partialUpdatedArticlesEntity = new ArticlesEntity();
        partialUpdatedArticlesEntity.setId(articlesEntity.getId());

        partialUpdatedArticlesEntity
            .categoryID(UPDATED_CATEGORY_ID)
            .bodyId(UPDATED_BODY_ID)
            .authorId(UPDATED_AUTHOR_ID)
            .title(UPDATED_TITLE)
            .slug(UPDATED_SLUG)
            .subTitle(UPDATED_SUB_TITLE)
            .thumbnailUrl(UPDATED_THUMBNAIL_URL)
            .readingTime(UPDATED_READING_TIME)
            .isPinned(UPDATED_IS_PINNED)
            .deleted(UPDATED_DELETED)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);

        restArticlesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArticlesEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedArticlesEntity))
            )
            .andExpect(status().isOk());

        // Validate the Articles in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertArticlesEntityUpdatableFieldsEquals(partialUpdatedArticlesEntity, getPersistedArticlesEntity(partialUpdatedArticlesEntity));
    }

    @Test
    @Transactional
    void patchNonExistingArticles() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        articlesEntity.setId(longCount.incrementAndGet());

        // Create the Articles
        ArticlesDTO articlesDTO = articlesMapper.toDto(articlesEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArticlesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, articlesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(articlesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Articles in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchArticles() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        articlesEntity.setId(longCount.incrementAndGet());

        // Create the Articles
        ArticlesDTO articlesDTO = articlesMapper.toDto(articlesEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticlesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(articlesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Articles in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamArticles() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        articlesEntity.setId(longCount.incrementAndGet());

        // Create the Articles
        ArticlesDTO articlesDTO = articlesMapper.toDto(articlesEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticlesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(articlesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Articles in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteArticles() throws Exception {
        // Initialize the database
        insertedArticlesEntity = articlesRepository.saveAndFlush(articlesEntity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the articles
        restArticlesMockMvc
            .perform(delete(ENTITY_API_URL_ID, articlesEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return articlesRepository.count();
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

    protected ArticlesEntity getPersistedArticlesEntity(ArticlesEntity articles) {
        return articlesRepository.findById(articles.getId()).orElseThrow();
    }

    protected void assertPersistedArticlesEntityToMatchAllProperties(ArticlesEntity expectedArticlesEntity) {
        assertArticlesEntityAllPropertiesEquals(expectedArticlesEntity, getPersistedArticlesEntity(expectedArticlesEntity));
    }

    protected void assertPersistedArticlesEntityToMatchUpdatableProperties(ArticlesEntity expectedArticlesEntity) {
        assertArticlesEntityAllUpdatablePropertiesEquals(expectedArticlesEntity, getPersistedArticlesEntity(expectedArticlesEntity));
    }
}
