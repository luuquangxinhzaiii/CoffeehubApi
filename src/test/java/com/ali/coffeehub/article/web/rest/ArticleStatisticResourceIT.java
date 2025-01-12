package com.ali.coffeehub.article.web.rest;

import static com.ali.coffeehub.article.domain.ArticleStatisticEntityAsserts.*;
import static com.ali.coffeehub.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ali.coffeehub.IntegrationTest;
import com.ali.coffeehub.article.domain.ArticleStatisticEntity;
import com.ali.coffeehub.article.repository.ArticleStatisticRepository;
import com.ali.coffeehub.article.service.dto.ArticleStatisticDTO;
import com.ali.coffeehub.article.service.mapper.ArticleStatisticMapper;
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
 * Integration tests for the {@link ArticleStatisticResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ArticleStatisticResourceIT {

    private static final Long DEFAULT_ATICLE_ID = 1L;
    private static final Long UPDATED_ATICLE_ID = 2L;

    private static final Integer DEFAULT_VIEW_COUNT = 1;
    private static final Integer UPDATED_VIEW_COUNT = 2;

    private static final Integer DEFAULT_LIKE_COUNT = 1;
    private static final Integer UPDATED_LIKE_COUNT = 2;

    private static final Integer DEFAULT_COMMENT_COUNT = 1;
    private static final Integer UPDATED_COMMENT_COUNT = 2;

    private static final Integer DEFAULT_AVG_TIME_SPENT = 1;
    private static final Integer UPDATED_AVG_TIME_SPENT = 2;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/article-statistics";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ArticleStatisticRepository articleStatisticRepository;

    @Autowired
    private ArticleStatisticMapper articleStatisticMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArticleStatisticMockMvc;

    private ArticleStatisticEntity articleStatisticEntity;

    private ArticleStatisticEntity insertedArticleStatisticEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArticleStatisticEntity createEntity() {
        return new ArticleStatisticEntity()
            .aticleId(DEFAULT_ATICLE_ID)
            .viewCount(DEFAULT_VIEW_COUNT)
            .likeCount(DEFAULT_LIKE_COUNT)
            .commentCount(DEFAULT_COMMENT_COUNT)
            .avgTimeSpent(DEFAULT_AVG_TIME_SPENT)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArticleStatisticEntity createUpdatedEntity() {
        return new ArticleStatisticEntity()
            .aticleId(UPDATED_ATICLE_ID)
            .viewCount(UPDATED_VIEW_COUNT)
            .likeCount(UPDATED_LIKE_COUNT)
            .commentCount(UPDATED_COMMENT_COUNT)
            .avgTimeSpent(UPDATED_AVG_TIME_SPENT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
    }

    @BeforeEach
    public void initTest() {
        articleStatisticEntity = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedArticleStatisticEntity != null) {
            articleStatisticRepository.delete(insertedArticleStatisticEntity);
            insertedArticleStatisticEntity = null;
        }
    }

    @Test
    @Transactional
    void createArticleStatistic() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ArticleStatistic
        ArticleStatisticDTO articleStatisticDTO = articleStatisticMapper.toDto(articleStatisticEntity);
        var returnedArticleStatisticDTO = om.readValue(
            restArticleStatisticMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(articleStatisticDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ArticleStatisticDTO.class
        );

        // Validate the ArticleStatistic in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedArticleStatisticEntity = articleStatisticMapper.toEntity(returnedArticleStatisticDTO);
        assertArticleStatisticEntityUpdatableFieldsEquals(
            returnedArticleStatisticEntity,
            getPersistedArticleStatisticEntity(returnedArticleStatisticEntity)
        );

        insertedArticleStatisticEntity = returnedArticleStatisticEntity;
    }

    @Test
    @Transactional
    void createArticleStatisticWithExistingId() throws Exception {
        // Create the ArticleStatistic with an existing ID
        articleStatisticEntity.setId(1L);
        ArticleStatisticDTO articleStatisticDTO = articleStatisticMapper.toDto(articleStatisticEntity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restArticleStatisticMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(articleStatisticDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ArticleStatistic in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAticleIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        articleStatisticEntity.setAticleId(null);

        // Create the ArticleStatistic, which fails.
        ArticleStatisticDTO articleStatisticDTO = articleStatisticMapper.toDto(articleStatisticEntity);

        restArticleStatisticMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(articleStatisticDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        articleStatisticEntity.setCreatedAt(null);

        // Create the ArticleStatistic, which fails.
        ArticleStatisticDTO articleStatisticDTO = articleStatisticMapper.toDto(articleStatisticEntity);

        restArticleStatisticMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(articleStatisticDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllArticleStatistics() throws Exception {
        // Initialize the database
        insertedArticleStatisticEntity = articleStatisticRepository.saveAndFlush(articleStatisticEntity);

        // Get all the articleStatisticList
        restArticleStatisticMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(articleStatisticEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].aticleId").value(hasItem(DEFAULT_ATICLE_ID.intValue())))
            .andExpect(jsonPath("$.[*].viewCount").value(hasItem(DEFAULT_VIEW_COUNT)))
            .andExpect(jsonPath("$.[*].likeCount").value(hasItem(DEFAULT_LIKE_COUNT)))
            .andExpect(jsonPath("$.[*].commentCount").value(hasItem(DEFAULT_COMMENT_COUNT)))
            .andExpect(jsonPath("$.[*].avgTimeSpent").value(hasItem(DEFAULT_AVG_TIME_SPENT)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getArticleStatistic() throws Exception {
        // Initialize the database
        insertedArticleStatisticEntity = articleStatisticRepository.saveAndFlush(articleStatisticEntity);

        // Get the articleStatistic
        restArticleStatisticMockMvc
            .perform(get(ENTITY_API_URL_ID, articleStatisticEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(articleStatisticEntity.getId().intValue()))
            .andExpect(jsonPath("$.aticleId").value(DEFAULT_ATICLE_ID.intValue()))
            .andExpect(jsonPath("$.viewCount").value(DEFAULT_VIEW_COUNT))
            .andExpect(jsonPath("$.likeCount").value(DEFAULT_LIKE_COUNT))
            .andExpect(jsonPath("$.commentCount").value(DEFAULT_COMMENT_COUNT))
            .andExpect(jsonPath("$.avgTimeSpent").value(DEFAULT_AVG_TIME_SPENT))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingArticleStatistic() throws Exception {
        // Get the articleStatistic
        restArticleStatisticMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingArticleStatistic() throws Exception {
        // Initialize the database
        insertedArticleStatisticEntity = articleStatisticRepository.saveAndFlush(articleStatisticEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the articleStatistic
        ArticleStatisticEntity updatedArticleStatisticEntity = articleStatisticRepository
            .findById(articleStatisticEntity.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedArticleStatisticEntity are not directly saved in db
        em.detach(updatedArticleStatisticEntity);
        updatedArticleStatisticEntity
            .aticleId(UPDATED_ATICLE_ID)
            .viewCount(UPDATED_VIEW_COUNT)
            .likeCount(UPDATED_LIKE_COUNT)
            .commentCount(UPDATED_COMMENT_COUNT)
            .avgTimeSpent(UPDATED_AVG_TIME_SPENT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        ArticleStatisticDTO articleStatisticDTO = articleStatisticMapper.toDto(updatedArticleStatisticEntity);

        restArticleStatisticMockMvc
            .perform(
                put(ENTITY_API_URL_ID, articleStatisticDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(articleStatisticDTO))
            )
            .andExpect(status().isOk());

        // Validate the ArticleStatistic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedArticleStatisticEntityToMatchAllProperties(updatedArticleStatisticEntity);
    }

    @Test
    @Transactional
    void putNonExistingArticleStatistic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        articleStatisticEntity.setId(longCount.incrementAndGet());

        // Create the ArticleStatistic
        ArticleStatisticDTO articleStatisticDTO = articleStatisticMapper.toDto(articleStatisticEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArticleStatisticMockMvc
            .perform(
                put(ENTITY_API_URL_ID, articleStatisticDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(articleStatisticDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArticleStatistic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchArticleStatistic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        articleStatisticEntity.setId(longCount.incrementAndGet());

        // Create the ArticleStatistic
        ArticleStatisticDTO articleStatisticDTO = articleStatisticMapper.toDto(articleStatisticEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleStatisticMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(articleStatisticDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArticleStatistic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamArticleStatistic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        articleStatisticEntity.setId(longCount.incrementAndGet());

        // Create the ArticleStatistic
        ArticleStatisticDTO articleStatisticDTO = articleStatisticMapper.toDto(articleStatisticEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleStatisticMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(articleStatisticDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArticleStatistic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateArticleStatisticWithPatch() throws Exception {
        // Initialize the database
        insertedArticleStatisticEntity = articleStatisticRepository.saveAndFlush(articleStatisticEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the articleStatistic using partial update
        ArticleStatisticEntity partialUpdatedArticleStatisticEntity = new ArticleStatisticEntity();
        partialUpdatedArticleStatisticEntity.setId(articleStatisticEntity.getId());

        partialUpdatedArticleStatisticEntity
            .aticleId(UPDATED_ATICLE_ID)
            .viewCount(UPDATED_VIEW_COUNT)
            .likeCount(UPDATED_LIKE_COUNT)
            .avgTimeSpent(UPDATED_AVG_TIME_SPENT)
            .createdAt(UPDATED_CREATED_AT);

        restArticleStatisticMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArticleStatisticEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedArticleStatisticEntity))
            )
            .andExpect(status().isOk());

        // Validate the ArticleStatistic in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertArticleStatisticEntityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedArticleStatisticEntity, articleStatisticEntity),
            getPersistedArticleStatisticEntity(articleStatisticEntity)
        );
    }

    @Test
    @Transactional
    void fullUpdateArticleStatisticWithPatch() throws Exception {
        // Initialize the database
        insertedArticleStatisticEntity = articleStatisticRepository.saveAndFlush(articleStatisticEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the articleStatistic using partial update
        ArticleStatisticEntity partialUpdatedArticleStatisticEntity = new ArticleStatisticEntity();
        partialUpdatedArticleStatisticEntity.setId(articleStatisticEntity.getId());

        partialUpdatedArticleStatisticEntity
            .aticleId(UPDATED_ATICLE_ID)
            .viewCount(UPDATED_VIEW_COUNT)
            .likeCount(UPDATED_LIKE_COUNT)
            .commentCount(UPDATED_COMMENT_COUNT)
            .avgTimeSpent(UPDATED_AVG_TIME_SPENT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restArticleStatisticMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArticleStatisticEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedArticleStatisticEntity))
            )
            .andExpect(status().isOk());

        // Validate the ArticleStatistic in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertArticleStatisticEntityUpdatableFieldsEquals(
            partialUpdatedArticleStatisticEntity,
            getPersistedArticleStatisticEntity(partialUpdatedArticleStatisticEntity)
        );
    }

    @Test
    @Transactional
    void patchNonExistingArticleStatistic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        articleStatisticEntity.setId(longCount.incrementAndGet());

        // Create the ArticleStatistic
        ArticleStatisticDTO articleStatisticDTO = articleStatisticMapper.toDto(articleStatisticEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArticleStatisticMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, articleStatisticDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(articleStatisticDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArticleStatistic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchArticleStatistic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        articleStatisticEntity.setId(longCount.incrementAndGet());

        // Create the ArticleStatistic
        ArticleStatisticDTO articleStatisticDTO = articleStatisticMapper.toDto(articleStatisticEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleStatisticMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(articleStatisticDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArticleStatistic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamArticleStatistic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        articleStatisticEntity.setId(longCount.incrementAndGet());

        // Create the ArticleStatistic
        ArticleStatisticDTO articleStatisticDTO = articleStatisticMapper.toDto(articleStatisticEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleStatisticMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(articleStatisticDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArticleStatistic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteArticleStatistic() throws Exception {
        // Initialize the database
        insertedArticleStatisticEntity = articleStatisticRepository.saveAndFlush(articleStatisticEntity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the articleStatistic
        restArticleStatisticMockMvc
            .perform(delete(ENTITY_API_URL_ID, articleStatisticEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return articleStatisticRepository.count();
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

    protected ArticleStatisticEntity getPersistedArticleStatisticEntity(ArticleStatisticEntity articleStatistic) {
        return articleStatisticRepository.findById(articleStatistic.getId()).orElseThrow();
    }

    protected void assertPersistedArticleStatisticEntityToMatchAllProperties(ArticleStatisticEntity expectedArticleStatisticEntity) {
        assertArticleStatisticEntityAllPropertiesEquals(
            expectedArticleStatisticEntity,
            getPersistedArticleStatisticEntity(expectedArticleStatisticEntity)
        );
    }

    protected void assertPersistedArticleStatisticEntityToMatchUpdatableProperties(ArticleStatisticEntity expectedArticleStatisticEntity) {
        assertArticleStatisticEntityAllUpdatablePropertiesEquals(
            expectedArticleStatisticEntity,
            getPersistedArticleStatisticEntity(expectedArticleStatisticEntity)
        );
    }
}
