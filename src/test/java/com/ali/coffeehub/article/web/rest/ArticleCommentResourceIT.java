package com.ali.coffeehub.article.web.rest;

import static com.ali.coffeehub.article.domain.ArticleCommentEntityAsserts.*;
import static com.ali.coffeehub.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ali.coffeehub.IntegrationTest;
import com.ali.coffeehub.article.domain.ArticleCommentEntity;
import com.ali.coffeehub.article.repository.ArticleCommentRepository;
import com.ali.coffeehub.article.service.dto.ArticleCommentDTO;
import com.ali.coffeehub.article.service.mapper.ArticleCommentMapper;
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
 * Integration tests for the {@link ArticleCommentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ArticleCommentResourceIT {

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Long DEFAULT_ARTICLE_ID = 1L;
    private static final Long UPDATED_ARTICLE_ID = 2L;

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final Long DEFAULT_PARENT_ID = 1L;
    private static final Long UPDATED_PARENT_ID = 2L;

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/article-comments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ArticleCommentRepository articleCommentRepository;

    @Autowired
    private ArticleCommentMapper articleCommentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArticleCommentMockMvc;

    private ArticleCommentEntity articleCommentEntity;

    private ArticleCommentEntity insertedArticleCommentEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArticleCommentEntity createEntity() {
        return new ArticleCommentEntity()
            .content(DEFAULT_CONTENT)
            .articleId(DEFAULT_ARTICLE_ID)
            .userId(DEFAULT_USER_ID)
            .parentId(DEFAULT_PARENT_ID)
            .status(DEFAULT_STATUS)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArticleCommentEntity createUpdatedEntity() {
        return new ArticleCommentEntity()
            .content(UPDATED_CONTENT)
            .articleId(UPDATED_ARTICLE_ID)
            .userId(UPDATED_USER_ID)
            .parentId(UPDATED_PARENT_ID)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
    }

    @BeforeEach
    public void initTest() {
        articleCommentEntity = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedArticleCommentEntity != null) {
            articleCommentRepository.delete(insertedArticleCommentEntity);
            insertedArticleCommentEntity = null;
        }
    }

    @Test
    @Transactional
    void createArticleComment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ArticleComment
        ArticleCommentDTO articleCommentDTO = articleCommentMapper.toDto(articleCommentEntity);
        var returnedArticleCommentDTO = om.readValue(
            restArticleCommentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(articleCommentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ArticleCommentDTO.class
        );

        // Validate the ArticleComment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedArticleCommentEntity = articleCommentMapper.toEntity(returnedArticleCommentDTO);
        assertArticleCommentEntityUpdatableFieldsEquals(
            returnedArticleCommentEntity,
            getPersistedArticleCommentEntity(returnedArticleCommentEntity)
        );

        insertedArticleCommentEntity = returnedArticleCommentEntity;
    }

    @Test
    @Transactional
    void createArticleCommentWithExistingId() throws Exception {
        // Create the ArticleComment with an existing ID
        articleCommentEntity.setId(1L);
        ArticleCommentDTO articleCommentDTO = articleCommentMapper.toDto(articleCommentEntity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restArticleCommentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(articleCommentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ArticleComment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkArticleIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        articleCommentEntity.setArticleId(null);

        // Create the ArticleComment, which fails.
        ArticleCommentDTO articleCommentDTO = articleCommentMapper.toDto(articleCommentEntity);

        restArticleCommentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(articleCommentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllArticleComments() throws Exception {
        // Initialize the database
        insertedArticleCommentEntity = articleCommentRepository.saveAndFlush(articleCommentEntity);

        // Get all the articleCommentList
        restArticleCommentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(articleCommentEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].articleId").value(hasItem(DEFAULT_ARTICLE_ID.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].parentId").value(hasItem(DEFAULT_PARENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getArticleComment() throws Exception {
        // Initialize the database
        insertedArticleCommentEntity = articleCommentRepository.saveAndFlush(articleCommentEntity);

        // Get the articleComment
        restArticleCommentMockMvc
            .perform(get(ENTITY_API_URL_ID, articleCommentEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(articleCommentEntity.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.articleId").value(DEFAULT_ARTICLE_ID.intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.parentId").value(DEFAULT_PARENT_ID.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingArticleComment() throws Exception {
        // Get the articleComment
        restArticleCommentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingArticleComment() throws Exception {
        // Initialize the database
        insertedArticleCommentEntity = articleCommentRepository.saveAndFlush(articleCommentEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the articleComment
        ArticleCommentEntity updatedArticleCommentEntity = articleCommentRepository.findById(articleCommentEntity.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedArticleCommentEntity are not directly saved in db
        em.detach(updatedArticleCommentEntity);
        updatedArticleCommentEntity
            .content(UPDATED_CONTENT)
            .articleId(UPDATED_ARTICLE_ID)
            .userId(UPDATED_USER_ID)
            .parentId(UPDATED_PARENT_ID)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        ArticleCommentDTO articleCommentDTO = articleCommentMapper.toDto(updatedArticleCommentEntity);

        restArticleCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, articleCommentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(articleCommentDTO))
            )
            .andExpect(status().isOk());

        // Validate the ArticleComment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedArticleCommentEntityToMatchAllProperties(updatedArticleCommentEntity);
    }

    @Test
    @Transactional
    void putNonExistingArticleComment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        articleCommentEntity.setId(longCount.incrementAndGet());

        // Create the ArticleComment
        ArticleCommentDTO articleCommentDTO = articleCommentMapper.toDto(articleCommentEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArticleCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, articleCommentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(articleCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArticleComment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchArticleComment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        articleCommentEntity.setId(longCount.incrementAndGet());

        // Create the ArticleComment
        ArticleCommentDTO articleCommentDTO = articleCommentMapper.toDto(articleCommentEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(articleCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArticleComment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamArticleComment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        articleCommentEntity.setId(longCount.incrementAndGet());

        // Create the ArticleComment
        ArticleCommentDTO articleCommentDTO = articleCommentMapper.toDto(articleCommentEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleCommentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(articleCommentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArticleComment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateArticleCommentWithPatch() throws Exception {
        // Initialize the database
        insertedArticleCommentEntity = articleCommentRepository.saveAndFlush(articleCommentEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the articleComment using partial update
        ArticleCommentEntity partialUpdatedArticleCommentEntity = new ArticleCommentEntity();
        partialUpdatedArticleCommentEntity.setId(articleCommentEntity.getId());

        partialUpdatedArticleCommentEntity.status(UPDATED_STATUS).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restArticleCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArticleCommentEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedArticleCommentEntity))
            )
            .andExpect(status().isOk());

        // Validate the ArticleComment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertArticleCommentEntityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedArticleCommentEntity, articleCommentEntity),
            getPersistedArticleCommentEntity(articleCommentEntity)
        );
    }

    @Test
    @Transactional
    void fullUpdateArticleCommentWithPatch() throws Exception {
        // Initialize the database
        insertedArticleCommentEntity = articleCommentRepository.saveAndFlush(articleCommentEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the articleComment using partial update
        ArticleCommentEntity partialUpdatedArticleCommentEntity = new ArticleCommentEntity();
        partialUpdatedArticleCommentEntity.setId(articleCommentEntity.getId());

        partialUpdatedArticleCommentEntity
            .content(UPDATED_CONTENT)
            .articleId(UPDATED_ARTICLE_ID)
            .userId(UPDATED_USER_ID)
            .parentId(UPDATED_PARENT_ID)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restArticleCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArticleCommentEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedArticleCommentEntity))
            )
            .andExpect(status().isOk());

        // Validate the ArticleComment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertArticleCommentEntityUpdatableFieldsEquals(
            partialUpdatedArticleCommentEntity,
            getPersistedArticleCommentEntity(partialUpdatedArticleCommentEntity)
        );
    }

    @Test
    @Transactional
    void patchNonExistingArticleComment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        articleCommentEntity.setId(longCount.incrementAndGet());

        // Create the ArticleComment
        ArticleCommentDTO articleCommentDTO = articleCommentMapper.toDto(articleCommentEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArticleCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, articleCommentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(articleCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArticleComment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchArticleComment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        articleCommentEntity.setId(longCount.incrementAndGet());

        // Create the ArticleComment
        ArticleCommentDTO articleCommentDTO = articleCommentMapper.toDto(articleCommentEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(articleCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArticleComment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamArticleComment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        articleCommentEntity.setId(longCount.incrementAndGet());

        // Create the ArticleComment
        ArticleCommentDTO articleCommentDTO = articleCommentMapper.toDto(articleCommentEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleCommentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(articleCommentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArticleComment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteArticleComment() throws Exception {
        // Initialize the database
        insertedArticleCommentEntity = articleCommentRepository.saveAndFlush(articleCommentEntity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the articleComment
        restArticleCommentMockMvc
            .perform(delete(ENTITY_API_URL_ID, articleCommentEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return articleCommentRepository.count();
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

    protected ArticleCommentEntity getPersistedArticleCommentEntity(ArticleCommentEntity articleComment) {
        return articleCommentRepository.findById(articleComment.getId()).orElseThrow();
    }

    protected void assertPersistedArticleCommentEntityToMatchAllProperties(ArticleCommentEntity expectedArticleCommentEntity) {
        assertArticleCommentEntityAllPropertiesEquals(
            expectedArticleCommentEntity,
            getPersistedArticleCommentEntity(expectedArticleCommentEntity)
        );
    }

    protected void assertPersistedArticleCommentEntityToMatchUpdatableProperties(ArticleCommentEntity expectedArticleCommentEntity) {
        assertArticleCommentEntityAllUpdatablePropertiesEquals(
            expectedArticleCommentEntity,
            getPersistedArticleCommentEntity(expectedArticleCommentEntity)
        );
    }
}
