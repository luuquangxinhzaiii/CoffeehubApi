package com.ali.coffeehub.article.web.rest;

import static com.ali.coffeehub.article.domain.ArticleBodyEntityAsserts.*;
import static com.ali.coffeehub.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ali.coffeehub.IntegrationTest;
import com.ali.coffeehub.article.domain.ArticleBodyEntity;
import com.ali.coffeehub.article.repository.ArticleBodyRepository;
import com.ali.coffeehub.article.service.dto.ArticleBodyDTO;
import com.ali.coffeehub.article.service.mapper.ArticleBodyMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link ArticleBodyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ArticleBodyResourceIT {

    private static final String DEFAULT_BODY = "AAAAAAAAAA";
    private static final String UPDATED_BODY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/article-bodies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ArticleBodyRepository articleBodyRepository;

    @Autowired
    private ArticleBodyMapper articleBodyMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArticleBodyMockMvc;

    private ArticleBodyEntity articleBodyEntity;

    private ArticleBodyEntity insertedArticleBodyEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArticleBodyEntity createEntity() {
        return new ArticleBodyEntity().body(DEFAULT_BODY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArticleBodyEntity createUpdatedEntity() {
        return new ArticleBodyEntity().body(UPDATED_BODY);
    }

    @BeforeEach
    public void initTest() {
        articleBodyEntity = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedArticleBodyEntity != null) {
            articleBodyRepository.delete(insertedArticleBodyEntity);
            insertedArticleBodyEntity = null;
        }
    }

    @Test
    @Transactional
    void createArticleBody() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ArticleBody
        ArticleBodyDTO articleBodyDTO = articleBodyMapper.toDto(articleBodyEntity);
        var returnedArticleBodyDTO = om.readValue(
            restArticleBodyMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(articleBodyDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ArticleBodyDTO.class
        );

        // Validate the ArticleBody in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedArticleBodyEntity = articleBodyMapper.toEntity(returnedArticleBodyDTO);
        assertArticleBodyEntityUpdatableFieldsEquals(returnedArticleBodyEntity, getPersistedArticleBodyEntity(returnedArticleBodyEntity));

        insertedArticleBodyEntity = returnedArticleBodyEntity;
    }

    @Test
    @Transactional
    void createArticleBodyWithExistingId() throws Exception {
        // Create the ArticleBody with an existing ID
        articleBodyEntity.setId(1L);
        ArticleBodyDTO articleBodyDTO = articleBodyMapper.toDto(articleBodyEntity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restArticleBodyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(articleBodyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ArticleBody in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllArticleBodies() throws Exception {
        // Initialize the database
        insertedArticleBodyEntity = articleBodyRepository.saveAndFlush(articleBodyEntity);

        // Get all the articleBodyList
        restArticleBodyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(articleBodyEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY)));
    }

    @Test
    @Transactional
    void getArticleBody() throws Exception {
        // Initialize the database
        insertedArticleBodyEntity = articleBodyRepository.saveAndFlush(articleBodyEntity);

        // Get the articleBody
        restArticleBodyMockMvc
            .perform(get(ENTITY_API_URL_ID, articleBodyEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(articleBodyEntity.getId().intValue()))
            .andExpect(jsonPath("$.body").value(DEFAULT_BODY));
    }

    @Test
    @Transactional
    void getNonExistingArticleBody() throws Exception {
        // Get the articleBody
        restArticleBodyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingArticleBody() throws Exception {
        // Initialize the database
        insertedArticleBodyEntity = articleBodyRepository.saveAndFlush(articleBodyEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the articleBody
        ArticleBodyEntity updatedArticleBodyEntity = articleBodyRepository.findById(articleBodyEntity.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedArticleBodyEntity are not directly saved in db
        em.detach(updatedArticleBodyEntity);
        updatedArticleBodyEntity.body(UPDATED_BODY);
        ArticleBodyDTO articleBodyDTO = articleBodyMapper.toDto(updatedArticleBodyEntity);

        restArticleBodyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, articleBodyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(articleBodyDTO))
            )
            .andExpect(status().isOk());

        // Validate the ArticleBody in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedArticleBodyEntityToMatchAllProperties(updatedArticleBodyEntity);
    }

    @Test
    @Transactional
    void putNonExistingArticleBody() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        articleBodyEntity.setId(longCount.incrementAndGet());

        // Create the ArticleBody
        ArticleBodyDTO articleBodyDTO = articleBodyMapper.toDto(articleBodyEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArticleBodyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, articleBodyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(articleBodyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArticleBody in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchArticleBody() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        articleBodyEntity.setId(longCount.incrementAndGet());

        // Create the ArticleBody
        ArticleBodyDTO articleBodyDTO = articleBodyMapper.toDto(articleBodyEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleBodyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(articleBodyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArticleBody in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamArticleBody() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        articleBodyEntity.setId(longCount.incrementAndGet());

        // Create the ArticleBody
        ArticleBodyDTO articleBodyDTO = articleBodyMapper.toDto(articleBodyEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleBodyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(articleBodyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArticleBody in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateArticleBodyWithPatch() throws Exception {
        // Initialize the database
        insertedArticleBodyEntity = articleBodyRepository.saveAndFlush(articleBodyEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the articleBody using partial update
        ArticleBodyEntity partialUpdatedArticleBodyEntity = new ArticleBodyEntity();
        partialUpdatedArticleBodyEntity.setId(articleBodyEntity.getId());

        restArticleBodyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArticleBodyEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedArticleBodyEntity))
            )
            .andExpect(status().isOk());

        // Validate the ArticleBody in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertArticleBodyEntityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedArticleBodyEntity, articleBodyEntity),
            getPersistedArticleBodyEntity(articleBodyEntity)
        );
    }

    @Test
    @Transactional
    void fullUpdateArticleBodyWithPatch() throws Exception {
        // Initialize the database
        insertedArticleBodyEntity = articleBodyRepository.saveAndFlush(articleBodyEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the articleBody using partial update
        ArticleBodyEntity partialUpdatedArticleBodyEntity = new ArticleBodyEntity();
        partialUpdatedArticleBodyEntity.setId(articleBodyEntity.getId());

        partialUpdatedArticleBodyEntity.body(UPDATED_BODY);

        restArticleBodyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArticleBodyEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedArticleBodyEntity))
            )
            .andExpect(status().isOk());

        // Validate the ArticleBody in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertArticleBodyEntityUpdatableFieldsEquals(
            partialUpdatedArticleBodyEntity,
            getPersistedArticleBodyEntity(partialUpdatedArticleBodyEntity)
        );
    }

    @Test
    @Transactional
    void patchNonExistingArticleBody() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        articleBodyEntity.setId(longCount.incrementAndGet());

        // Create the ArticleBody
        ArticleBodyDTO articleBodyDTO = articleBodyMapper.toDto(articleBodyEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArticleBodyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, articleBodyDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(articleBodyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArticleBody in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchArticleBody() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        articleBodyEntity.setId(longCount.incrementAndGet());

        // Create the ArticleBody
        ArticleBodyDTO articleBodyDTO = articleBodyMapper.toDto(articleBodyEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleBodyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(articleBodyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArticleBody in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamArticleBody() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        articleBodyEntity.setId(longCount.incrementAndGet());

        // Create the ArticleBody
        ArticleBodyDTO articleBodyDTO = articleBodyMapper.toDto(articleBodyEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleBodyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(articleBodyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArticleBody in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteArticleBody() throws Exception {
        // Initialize the database
        insertedArticleBodyEntity = articleBodyRepository.saveAndFlush(articleBodyEntity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the articleBody
        restArticleBodyMockMvc
            .perform(delete(ENTITY_API_URL_ID, articleBodyEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return articleBodyRepository.count();
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

    protected ArticleBodyEntity getPersistedArticleBodyEntity(ArticleBodyEntity articleBody) {
        return articleBodyRepository.findById(articleBody.getId()).orElseThrow();
    }

    protected void assertPersistedArticleBodyEntityToMatchAllProperties(ArticleBodyEntity expectedArticleBodyEntity) {
        assertArticleBodyEntityAllPropertiesEquals(expectedArticleBodyEntity, getPersistedArticleBodyEntity(expectedArticleBodyEntity));
    }

    protected void assertPersistedArticleBodyEntityToMatchUpdatableProperties(ArticleBodyEntity expectedArticleBodyEntity) {
        assertArticleBodyEntityAllUpdatablePropertiesEquals(
            expectedArticleBodyEntity,
            getPersistedArticleBodyEntity(expectedArticleBodyEntity)
        );
    }
}
