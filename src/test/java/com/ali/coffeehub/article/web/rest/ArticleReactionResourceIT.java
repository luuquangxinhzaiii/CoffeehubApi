package com.ali.coffeehub.article.web.rest;

import static com.ali.coffeehub.article.domain.ArticleReactionEntityAsserts.*;
import static com.ali.coffeehub.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ali.coffeehub.IntegrationTest;
import com.ali.coffeehub.article.domain.ArticleReactionEntity;
import com.ali.coffeehub.article.repository.ArticleReactionRepository;
import com.ali.coffeehub.article.service.dto.ArticleReactionDTO;
import com.ali.coffeehub.article.service.mapper.ArticleReactionMapper;
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
 * Integration tests for the {@link ArticleReactionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ArticleReactionResourceIT {

    private static final Long DEFAULT_ATICLE_ID = 1L;
    private static final Long UPDATED_ATICLE_ID = 2L;

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/article-reactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ArticleReactionRepository articleReactionRepository;

    @Autowired
    private ArticleReactionMapper articleReactionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArticleReactionMockMvc;

    private ArticleReactionEntity articleReactionEntity;

    private ArticleReactionEntity insertedArticleReactionEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArticleReactionEntity createEntity() {
        return new ArticleReactionEntity().aticleId(DEFAULT_ATICLE_ID).userId(DEFAULT_USER_ID).createdAt(DEFAULT_CREATED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArticleReactionEntity createUpdatedEntity() {
        return new ArticleReactionEntity().aticleId(UPDATED_ATICLE_ID).userId(UPDATED_USER_ID).createdAt(UPDATED_CREATED_AT);
    }

    @BeforeEach
    public void initTest() {
        articleReactionEntity = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedArticleReactionEntity != null) {
            articleReactionRepository.delete(insertedArticleReactionEntity);
            insertedArticleReactionEntity = null;
        }
    }

    @Test
    @Transactional
    void createArticleReaction() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ArticleReaction
        ArticleReactionDTO articleReactionDTO = articleReactionMapper.toDto(articleReactionEntity);
        var returnedArticleReactionDTO = om.readValue(
            restArticleReactionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(articleReactionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ArticleReactionDTO.class
        );

        // Validate the ArticleReaction in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedArticleReactionEntity = articleReactionMapper.toEntity(returnedArticleReactionDTO);
        assertArticleReactionEntityUpdatableFieldsEquals(
            returnedArticleReactionEntity,
            getPersistedArticleReactionEntity(returnedArticleReactionEntity)
        );

        insertedArticleReactionEntity = returnedArticleReactionEntity;
    }

    @Test
    @Transactional
    void createArticleReactionWithExistingId() throws Exception {
        // Create the ArticleReaction with an existing ID
        articleReactionEntity.setId(1L);
        ArticleReactionDTO articleReactionDTO = articleReactionMapper.toDto(articleReactionEntity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restArticleReactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(articleReactionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ArticleReaction in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAticleIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        articleReactionEntity.setAticleId(null);

        // Create the ArticleReaction, which fails.
        ArticleReactionDTO articleReactionDTO = articleReactionMapper.toDto(articleReactionEntity);

        restArticleReactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(articleReactionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUserIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        articleReactionEntity.setUserId(null);

        // Create the ArticleReaction, which fails.
        ArticleReactionDTO articleReactionDTO = articleReactionMapper.toDto(articleReactionEntity);

        restArticleReactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(articleReactionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        articleReactionEntity.setCreatedAt(null);

        // Create the ArticleReaction, which fails.
        ArticleReactionDTO articleReactionDTO = articleReactionMapper.toDto(articleReactionEntity);

        restArticleReactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(articleReactionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllArticleReactions() throws Exception {
        // Initialize the database
        insertedArticleReactionEntity = articleReactionRepository.saveAndFlush(articleReactionEntity);

        // Get all the articleReactionList
        restArticleReactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(articleReactionEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].aticleId").value(hasItem(DEFAULT_ATICLE_ID.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @Test
    @Transactional
    void getArticleReaction() throws Exception {
        // Initialize the database
        insertedArticleReactionEntity = articleReactionRepository.saveAndFlush(articleReactionEntity);

        // Get the articleReaction
        restArticleReactionMockMvc
            .perform(get(ENTITY_API_URL_ID, articleReactionEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(articleReactionEntity.getId().intValue()))
            .andExpect(jsonPath("$.aticleId").value(DEFAULT_ATICLE_ID.intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingArticleReaction() throws Exception {
        // Get the articleReaction
        restArticleReactionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingArticleReaction() throws Exception {
        // Initialize the database
        insertedArticleReactionEntity = articleReactionRepository.saveAndFlush(articleReactionEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the articleReaction
        ArticleReactionEntity updatedArticleReactionEntity = articleReactionRepository
            .findById(articleReactionEntity.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedArticleReactionEntity are not directly saved in db
        em.detach(updatedArticleReactionEntity);
        updatedArticleReactionEntity.aticleId(UPDATED_ATICLE_ID).userId(UPDATED_USER_ID).createdAt(UPDATED_CREATED_AT);
        ArticleReactionDTO articleReactionDTO = articleReactionMapper.toDto(updatedArticleReactionEntity);

        restArticleReactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, articleReactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(articleReactionDTO))
            )
            .andExpect(status().isOk());

        // Validate the ArticleReaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedArticleReactionEntityToMatchAllProperties(updatedArticleReactionEntity);
    }

    @Test
    @Transactional
    void putNonExistingArticleReaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        articleReactionEntity.setId(longCount.incrementAndGet());

        // Create the ArticleReaction
        ArticleReactionDTO articleReactionDTO = articleReactionMapper.toDto(articleReactionEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArticleReactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, articleReactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(articleReactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArticleReaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchArticleReaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        articleReactionEntity.setId(longCount.incrementAndGet());

        // Create the ArticleReaction
        ArticleReactionDTO articleReactionDTO = articleReactionMapper.toDto(articleReactionEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleReactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(articleReactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArticleReaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamArticleReaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        articleReactionEntity.setId(longCount.incrementAndGet());

        // Create the ArticleReaction
        ArticleReactionDTO articleReactionDTO = articleReactionMapper.toDto(articleReactionEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleReactionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(articleReactionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArticleReaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateArticleReactionWithPatch() throws Exception {
        // Initialize the database
        insertedArticleReactionEntity = articleReactionRepository.saveAndFlush(articleReactionEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the articleReaction using partial update
        ArticleReactionEntity partialUpdatedArticleReactionEntity = new ArticleReactionEntity();
        partialUpdatedArticleReactionEntity.setId(articleReactionEntity.getId());

        partialUpdatedArticleReactionEntity.userId(UPDATED_USER_ID).createdAt(UPDATED_CREATED_AT);

        restArticleReactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArticleReactionEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedArticleReactionEntity))
            )
            .andExpect(status().isOk());

        // Validate the ArticleReaction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertArticleReactionEntityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedArticleReactionEntity, articleReactionEntity),
            getPersistedArticleReactionEntity(articleReactionEntity)
        );
    }

    @Test
    @Transactional
    void fullUpdateArticleReactionWithPatch() throws Exception {
        // Initialize the database
        insertedArticleReactionEntity = articleReactionRepository.saveAndFlush(articleReactionEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the articleReaction using partial update
        ArticleReactionEntity partialUpdatedArticleReactionEntity = new ArticleReactionEntity();
        partialUpdatedArticleReactionEntity.setId(articleReactionEntity.getId());

        partialUpdatedArticleReactionEntity.aticleId(UPDATED_ATICLE_ID).userId(UPDATED_USER_ID).createdAt(UPDATED_CREATED_AT);

        restArticleReactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArticleReactionEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedArticleReactionEntity))
            )
            .andExpect(status().isOk());

        // Validate the ArticleReaction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertArticleReactionEntityUpdatableFieldsEquals(
            partialUpdatedArticleReactionEntity,
            getPersistedArticleReactionEntity(partialUpdatedArticleReactionEntity)
        );
    }

    @Test
    @Transactional
    void patchNonExistingArticleReaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        articleReactionEntity.setId(longCount.incrementAndGet());

        // Create the ArticleReaction
        ArticleReactionDTO articleReactionDTO = articleReactionMapper.toDto(articleReactionEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArticleReactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, articleReactionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(articleReactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArticleReaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchArticleReaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        articleReactionEntity.setId(longCount.incrementAndGet());

        // Create the ArticleReaction
        ArticleReactionDTO articleReactionDTO = articleReactionMapper.toDto(articleReactionEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleReactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(articleReactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArticleReaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamArticleReaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        articleReactionEntity.setId(longCount.incrementAndGet());

        // Create the ArticleReaction
        ArticleReactionDTO articleReactionDTO = articleReactionMapper.toDto(articleReactionEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleReactionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(articleReactionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArticleReaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteArticleReaction() throws Exception {
        // Initialize the database
        insertedArticleReactionEntity = articleReactionRepository.saveAndFlush(articleReactionEntity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the articleReaction
        restArticleReactionMockMvc
            .perform(delete(ENTITY_API_URL_ID, articleReactionEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return articleReactionRepository.count();
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

    protected ArticleReactionEntity getPersistedArticleReactionEntity(ArticleReactionEntity articleReaction) {
        return articleReactionRepository.findById(articleReaction.getId()).orElseThrow();
    }

    protected void assertPersistedArticleReactionEntityToMatchAllProperties(ArticleReactionEntity expectedArticleReactionEntity) {
        assertArticleReactionEntityAllPropertiesEquals(
            expectedArticleReactionEntity,
            getPersistedArticleReactionEntity(expectedArticleReactionEntity)
        );
    }

    protected void assertPersistedArticleReactionEntityToMatchUpdatableProperties(ArticleReactionEntity expectedArticleReactionEntity) {
        assertArticleReactionEntityAllUpdatablePropertiesEquals(
            expectedArticleReactionEntity,
            getPersistedArticleReactionEntity(expectedArticleReactionEntity)
        );
    }
}
