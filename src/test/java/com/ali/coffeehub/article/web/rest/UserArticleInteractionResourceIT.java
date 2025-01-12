package com.ali.coffeehub.article.web.rest;

import static com.ali.coffeehub.article.domain.UserArticleInteractionEntityAsserts.*;
import static com.ali.coffeehub.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ali.coffeehub.IntegrationTest;
import com.ali.coffeehub.article.domain.UserArticleInteractionEntity;
import com.ali.coffeehub.article.repository.UserArticleInteractionRepository;
import com.ali.coffeehub.article.service.dto.UserArticleInteractionDTO;
import com.ali.coffeehub.article.service.mapper.UserArticleInteractionMapper;
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
 * Integration tests for the {@link UserArticleInteractionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserArticleInteractionResourceIT {

    private static final Long DEFAULT_ATICLE_ID = 1L;
    private static final Long UPDATED_ATICLE_ID = 2L;

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final Integer DEFAULT_READ_PROGRESS = 1;
    private static final Integer UPDATED_READ_PROGRESS = 2;

    private static final Boolean DEFAULT_IS_BOOKMARKED = false;
    private static final Boolean UPDATED_IS_BOOKMARKED = true;

    private static final Instant DEFAULT_LAST_READ_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_READ_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/user-article-interactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserArticleInteractionRepository userArticleInteractionRepository;

    @Autowired
    private UserArticleInteractionMapper userArticleInteractionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserArticleInteractionMockMvc;

    private UserArticleInteractionEntity userArticleInteractionEntity;

    private UserArticleInteractionEntity insertedUserArticleInteractionEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserArticleInteractionEntity createEntity() {
        return new UserArticleInteractionEntity()
            .aticleId(DEFAULT_ATICLE_ID)
            .userId(DEFAULT_USER_ID)
            .readProgress(DEFAULT_READ_PROGRESS)
            .isBookmarked(DEFAULT_IS_BOOKMARKED)
            .lastReadAt(DEFAULT_LAST_READ_AT)
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
    public static UserArticleInteractionEntity createUpdatedEntity() {
        return new UserArticleInteractionEntity()
            .aticleId(UPDATED_ATICLE_ID)
            .userId(UPDATED_USER_ID)
            .readProgress(UPDATED_READ_PROGRESS)
            .isBookmarked(UPDATED_IS_BOOKMARKED)
            .lastReadAt(UPDATED_LAST_READ_AT)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);
    }

    @BeforeEach
    public void initTest() {
        userArticleInteractionEntity = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedUserArticleInteractionEntity != null) {
            userArticleInteractionRepository.delete(insertedUserArticleInteractionEntity);
            insertedUserArticleInteractionEntity = null;
        }
    }

    @Test
    @Transactional
    void createUserArticleInteraction() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserArticleInteraction
        UserArticleInteractionDTO userArticleInteractionDTO = userArticleInteractionMapper.toDto(userArticleInteractionEntity);
        var returnedUserArticleInteractionDTO = om.readValue(
            restUserArticleInteractionMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userArticleInteractionDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserArticleInteractionDTO.class
        );

        // Validate the UserArticleInteraction in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUserArticleInteractionEntity = userArticleInteractionMapper.toEntity(returnedUserArticleInteractionDTO);
        assertUserArticleInteractionEntityUpdatableFieldsEquals(
            returnedUserArticleInteractionEntity,
            getPersistedUserArticleInteractionEntity(returnedUserArticleInteractionEntity)
        );

        insertedUserArticleInteractionEntity = returnedUserArticleInteractionEntity;
    }

    @Test
    @Transactional
    void createUserArticleInteractionWithExistingId() throws Exception {
        // Create the UserArticleInteraction with an existing ID
        userArticleInteractionEntity.setId(1L);
        UserArticleInteractionDTO userArticleInteractionDTO = userArticleInteractionMapper.toDto(userArticleInteractionEntity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserArticleInteractionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userArticleInteractionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserArticleInteraction in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAticleIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userArticleInteractionEntity.setAticleId(null);

        // Create the UserArticleInteraction, which fails.
        UserArticleInteractionDTO userArticleInteractionDTO = userArticleInteractionMapper.toDto(userArticleInteractionEntity);

        restUserArticleInteractionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userArticleInteractionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUserIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userArticleInteractionEntity.setUserId(null);

        // Create the UserArticleInteraction, which fails.
        UserArticleInteractionDTO userArticleInteractionDTO = userArticleInteractionMapper.toDto(userArticleInteractionEntity);

        restUserArticleInteractionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userArticleInteractionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userArticleInteractionEntity.setCreatedAt(null);

        // Create the UserArticleInteraction, which fails.
        UserArticleInteractionDTO userArticleInteractionDTO = userArticleInteractionMapper.toDto(userArticleInteractionEntity);

        restUserArticleInteractionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userArticleInteractionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserArticleInteractions() throws Exception {
        // Initialize the database
        insertedUserArticleInteractionEntity = userArticleInteractionRepository.saveAndFlush(userArticleInteractionEntity);

        // Get all the userArticleInteractionList
        restUserArticleInteractionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userArticleInteractionEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].aticleId").value(hasItem(DEFAULT_ATICLE_ID.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].readProgress").value(hasItem(DEFAULT_READ_PROGRESS)))
            .andExpect(jsonPath("$.[*].isBookmarked").value(hasItem(DEFAULT_IS_BOOKMARKED)))
            .andExpect(jsonPath("$.[*].lastReadAt").value(hasItem(DEFAULT_LAST_READ_AT.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getUserArticleInteraction() throws Exception {
        // Initialize the database
        insertedUserArticleInteractionEntity = userArticleInteractionRepository.saveAndFlush(userArticleInteractionEntity);

        // Get the userArticleInteraction
        restUserArticleInteractionMockMvc
            .perform(get(ENTITY_API_URL_ID, userArticleInteractionEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userArticleInteractionEntity.getId().intValue()))
            .andExpect(jsonPath("$.aticleId").value(DEFAULT_ATICLE_ID.intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.readProgress").value(DEFAULT_READ_PROGRESS))
            .andExpect(jsonPath("$.isBookmarked").value(DEFAULT_IS_BOOKMARKED))
            .andExpect(jsonPath("$.lastReadAt").value(DEFAULT_LAST_READ_AT.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingUserArticleInteraction() throws Exception {
        // Get the userArticleInteraction
        restUserArticleInteractionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserArticleInteraction() throws Exception {
        // Initialize the database
        insertedUserArticleInteractionEntity = userArticleInteractionRepository.saveAndFlush(userArticleInteractionEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userArticleInteraction
        UserArticleInteractionEntity updatedUserArticleInteractionEntity = userArticleInteractionRepository
            .findById(userArticleInteractionEntity.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedUserArticleInteractionEntity are not directly saved in db
        em.detach(updatedUserArticleInteractionEntity);
        updatedUserArticleInteractionEntity
            .aticleId(UPDATED_ATICLE_ID)
            .userId(UPDATED_USER_ID)
            .readProgress(UPDATED_READ_PROGRESS)
            .isBookmarked(UPDATED_IS_BOOKMARKED)
            .lastReadAt(UPDATED_LAST_READ_AT)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);
        UserArticleInteractionDTO userArticleInteractionDTO = userArticleInteractionMapper.toDto(updatedUserArticleInteractionEntity);

        restUserArticleInteractionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userArticleInteractionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userArticleInteractionDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserArticleInteraction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserArticleInteractionEntityToMatchAllProperties(updatedUserArticleInteractionEntity);
    }

    @Test
    @Transactional
    void putNonExistingUserArticleInteraction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userArticleInteractionEntity.setId(longCount.incrementAndGet());

        // Create the UserArticleInteraction
        UserArticleInteractionDTO userArticleInteractionDTO = userArticleInteractionMapper.toDto(userArticleInteractionEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserArticleInteractionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userArticleInteractionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userArticleInteractionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserArticleInteraction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserArticleInteraction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userArticleInteractionEntity.setId(longCount.incrementAndGet());

        // Create the UserArticleInteraction
        UserArticleInteractionDTO userArticleInteractionDTO = userArticleInteractionMapper.toDto(userArticleInteractionEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserArticleInteractionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userArticleInteractionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserArticleInteraction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserArticleInteraction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userArticleInteractionEntity.setId(longCount.incrementAndGet());

        // Create the UserArticleInteraction
        UserArticleInteractionDTO userArticleInteractionDTO = userArticleInteractionMapper.toDto(userArticleInteractionEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserArticleInteractionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userArticleInteractionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserArticleInteraction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserArticleInteractionWithPatch() throws Exception {
        // Initialize the database
        insertedUserArticleInteractionEntity = userArticleInteractionRepository.saveAndFlush(userArticleInteractionEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userArticleInteraction using partial update
        UserArticleInteractionEntity partialUpdatedUserArticleInteractionEntity = new UserArticleInteractionEntity();
        partialUpdatedUserArticleInteractionEntity.setId(userArticleInteractionEntity.getId());

        partialUpdatedUserArticleInteractionEntity
            .userId(UPDATED_USER_ID)
            .isBookmarked(UPDATED_IS_BOOKMARKED)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);

        restUserArticleInteractionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserArticleInteractionEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserArticleInteractionEntity))
            )
            .andExpect(status().isOk());

        // Validate the UserArticleInteraction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserArticleInteractionEntityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserArticleInteractionEntity, userArticleInteractionEntity),
            getPersistedUserArticleInteractionEntity(userArticleInteractionEntity)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserArticleInteractionWithPatch() throws Exception {
        // Initialize the database
        insertedUserArticleInteractionEntity = userArticleInteractionRepository.saveAndFlush(userArticleInteractionEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userArticleInteraction using partial update
        UserArticleInteractionEntity partialUpdatedUserArticleInteractionEntity = new UserArticleInteractionEntity();
        partialUpdatedUserArticleInteractionEntity.setId(userArticleInteractionEntity.getId());

        partialUpdatedUserArticleInteractionEntity
            .aticleId(UPDATED_ATICLE_ID)
            .userId(UPDATED_USER_ID)
            .readProgress(UPDATED_READ_PROGRESS)
            .isBookmarked(UPDATED_IS_BOOKMARKED)
            .lastReadAt(UPDATED_LAST_READ_AT)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);

        restUserArticleInteractionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserArticleInteractionEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserArticleInteractionEntity))
            )
            .andExpect(status().isOk());

        // Validate the UserArticleInteraction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserArticleInteractionEntityUpdatableFieldsEquals(
            partialUpdatedUserArticleInteractionEntity,
            getPersistedUserArticleInteractionEntity(partialUpdatedUserArticleInteractionEntity)
        );
    }

    @Test
    @Transactional
    void patchNonExistingUserArticleInteraction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userArticleInteractionEntity.setId(longCount.incrementAndGet());

        // Create the UserArticleInteraction
        UserArticleInteractionDTO userArticleInteractionDTO = userArticleInteractionMapper.toDto(userArticleInteractionEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserArticleInteractionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userArticleInteractionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userArticleInteractionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserArticleInteraction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserArticleInteraction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userArticleInteractionEntity.setId(longCount.incrementAndGet());

        // Create the UserArticleInteraction
        UserArticleInteractionDTO userArticleInteractionDTO = userArticleInteractionMapper.toDto(userArticleInteractionEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserArticleInteractionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userArticleInteractionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserArticleInteraction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserArticleInteraction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userArticleInteractionEntity.setId(longCount.incrementAndGet());

        // Create the UserArticleInteraction
        UserArticleInteractionDTO userArticleInteractionDTO = userArticleInteractionMapper.toDto(userArticleInteractionEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserArticleInteractionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userArticleInteractionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserArticleInteraction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserArticleInteraction() throws Exception {
        // Initialize the database
        insertedUserArticleInteractionEntity = userArticleInteractionRepository.saveAndFlush(userArticleInteractionEntity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userArticleInteraction
        restUserArticleInteractionMockMvc
            .perform(delete(ENTITY_API_URL_ID, userArticleInteractionEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userArticleInteractionRepository.count();
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

    protected UserArticleInteractionEntity getPersistedUserArticleInteractionEntity(UserArticleInteractionEntity userArticleInteraction) {
        return userArticleInteractionRepository.findById(userArticleInteraction.getId()).orElseThrow();
    }

    protected void assertPersistedUserArticleInteractionEntityToMatchAllProperties(
        UserArticleInteractionEntity expectedUserArticleInteractionEntity
    ) {
        assertUserArticleInteractionEntityAllPropertiesEquals(
            expectedUserArticleInteractionEntity,
            getPersistedUserArticleInteractionEntity(expectedUserArticleInteractionEntity)
        );
    }

    protected void assertPersistedUserArticleInteractionEntityToMatchUpdatableProperties(
        UserArticleInteractionEntity expectedUserArticleInteractionEntity
    ) {
        assertUserArticleInteractionEntityAllUpdatablePropertiesEquals(
            expectedUserArticleInteractionEntity,
            getPersistedUserArticleInteractionEntity(expectedUserArticleInteractionEntity)
        );
    }
}
