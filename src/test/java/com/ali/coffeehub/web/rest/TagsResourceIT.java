package com.ali.coffeehub.web.rest;

import static com.ali.coffeehub.domain.TagsEntityAsserts.*;
import static com.ali.coffeehub.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ali.coffeehub.IntegrationTest;
import com.ali.coffeehub.domain.TagsEntity;
import com.ali.coffeehub.repository.TagsRepository;
import com.ali.coffeehub.service.dto.TagsDTO;
import com.ali.coffeehub.service.mapper.TagsMapper;
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
 * Integration tests for the {@link TagsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TagsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SLUG = "AAAAAAAAAA";
    private static final String UPDATED_SLUG = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ICON_URL = "AAAAAAAAAA";
    private static final String UPDATED_ICON_URL = "BBBBBBBBBB";

    private static final Long DEFAULT_PARENT_ID = 1L;
    private static final Long UPDATED_PARENT_ID = 2L;

    private static final Integer DEFAULT_DISPLAY_ORDER = 1;
    private static final Integer UPDATED_DISPLAY_ORDER = 2;

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

    private static final String ENTITY_API_URL = "/api/tags";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TagsRepository tagsRepository;

    @Autowired
    private TagsMapper tagsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTagsMockMvc;

    private TagsEntity tagsEntity;

    private TagsEntity insertedTagsEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TagsEntity createEntity() {
        return new TagsEntity()
            .name(DEFAULT_NAME)
            .slug(DEFAULT_SLUG)
            .description(DEFAULT_DESCRIPTION)
            .iconUrl(DEFAULT_ICON_URL)
            .parentId(DEFAULT_PARENT_ID)
            .displayOrder(DEFAULT_DISPLAY_ORDER)
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
    public static TagsEntity createUpdatedEntity() {
        return new TagsEntity()
            .name(UPDATED_NAME)
            .slug(UPDATED_SLUG)
            .description(UPDATED_DESCRIPTION)
            .iconUrl(UPDATED_ICON_URL)
            .parentId(UPDATED_PARENT_ID)
            .displayOrder(UPDATED_DISPLAY_ORDER)
            .deleted(UPDATED_DELETED)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);
    }

    @BeforeEach
    public void initTest() {
        tagsEntity = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedTagsEntity != null) {
            tagsRepository.delete(insertedTagsEntity);
            insertedTagsEntity = null;
        }
    }

    @Test
    @Transactional
    void createTags() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Tags
        TagsDTO tagsDTO = tagsMapper.toDto(tagsEntity);
        var returnedTagsDTO = om.readValue(
            restTagsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tagsDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TagsDTO.class
        );

        // Validate the Tags in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTagsEntity = tagsMapper.toEntity(returnedTagsDTO);
        assertTagsEntityUpdatableFieldsEquals(returnedTagsEntity, getPersistedTagsEntity(returnedTagsEntity));

        insertedTagsEntity = returnedTagsEntity;
    }

    @Test
    @Transactional
    void createTagsWithExistingId() throws Exception {
        // Create the Tags with an existing ID
        tagsEntity.setId(1L);
        TagsDTO tagsDTO = tagsMapper.toDto(tagsEntity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTagsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tagsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tags in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tagsEntity.setName(null);

        // Create the Tags, which fails.
        TagsDTO tagsDTO = tagsMapper.toDto(tagsEntity);

        restTagsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tagsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSlugIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tagsEntity.setSlug(null);

        // Create the Tags, which fails.
        TagsDTO tagsDTO = tagsMapper.toDto(tagsEntity);

        restTagsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tagsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tagsEntity.setCreatedAt(null);

        // Create the Tags, which fails.
        TagsDTO tagsDTO = tagsMapper.toDto(tagsEntity);

        restTagsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tagsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTags() throws Exception {
        // Initialize the database
        insertedTagsEntity = tagsRepository.saveAndFlush(tagsEntity);

        // Get all the tagsList
        restTagsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tagsEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].iconUrl").value(hasItem(DEFAULT_ICON_URL)))
            .andExpect(jsonPath("$.[*].parentId").value(hasItem(DEFAULT_PARENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].displayOrder").value(hasItem(DEFAULT_DISPLAY_ORDER)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getTags() throws Exception {
        // Initialize the database
        insertedTagsEntity = tagsRepository.saveAndFlush(tagsEntity);

        // Get the tags
        restTagsMockMvc
            .perform(get(ENTITY_API_URL_ID, tagsEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tagsEntity.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.slug").value(DEFAULT_SLUG))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.iconUrl").value(DEFAULT_ICON_URL))
            .andExpect(jsonPath("$.parentId").value(DEFAULT_PARENT_ID.intValue()))
            .andExpect(jsonPath("$.displayOrder").value(DEFAULT_DISPLAY_ORDER))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingTags() throws Exception {
        // Get the tags
        restTagsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTags() throws Exception {
        // Initialize the database
        insertedTagsEntity = tagsRepository.saveAndFlush(tagsEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tags
        TagsEntity updatedTagsEntity = tagsRepository.findById(tagsEntity.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTagsEntity are not directly saved in db
        em.detach(updatedTagsEntity);
        updatedTagsEntity
            .name(UPDATED_NAME)
            .slug(UPDATED_SLUG)
            .description(UPDATED_DESCRIPTION)
            .iconUrl(UPDATED_ICON_URL)
            .parentId(UPDATED_PARENT_ID)
            .displayOrder(UPDATED_DISPLAY_ORDER)
            .deleted(UPDATED_DELETED)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);
        TagsDTO tagsDTO = tagsMapper.toDto(updatedTagsEntity);

        restTagsMockMvc
            .perform(put(ENTITY_API_URL_ID, tagsDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tagsDTO)))
            .andExpect(status().isOk());

        // Validate the Tags in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTagsEntityToMatchAllProperties(updatedTagsEntity);
    }

    @Test
    @Transactional
    void putNonExistingTags() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tagsEntity.setId(longCount.incrementAndGet());

        // Create the Tags
        TagsDTO tagsDTO = tagsMapper.toDto(tagsEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTagsMockMvc
            .perform(put(ENTITY_API_URL_ID, tagsDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tagsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tags in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTags() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tagsEntity.setId(longCount.incrementAndGet());

        // Create the Tags
        TagsDTO tagsDTO = tagsMapper.toDto(tagsEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tagsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tags in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTags() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tagsEntity.setId(longCount.incrementAndGet());

        // Create the Tags
        TagsDTO tagsDTO = tagsMapper.toDto(tagsEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tagsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tags in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTagsWithPatch() throws Exception {
        // Initialize the database
        insertedTagsEntity = tagsRepository.saveAndFlush(tagsEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tags using partial update
        TagsEntity partialUpdatedTagsEntity = new TagsEntity();
        partialUpdatedTagsEntity.setId(tagsEntity.getId());

        partialUpdatedTagsEntity
            .slug(UPDATED_SLUG)
            .parentId(UPDATED_PARENT_ID)
            .displayOrder(UPDATED_DISPLAY_ORDER)
            .createdBy(UPDATED_CREATED_BY);

        restTagsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTagsEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTagsEntity))
            )
            .andExpect(status().isOk());

        // Validate the Tags in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTagsEntityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTagsEntity, tagsEntity),
            getPersistedTagsEntity(tagsEntity)
        );
    }

    @Test
    @Transactional
    void fullUpdateTagsWithPatch() throws Exception {
        // Initialize the database
        insertedTagsEntity = tagsRepository.saveAndFlush(tagsEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tags using partial update
        TagsEntity partialUpdatedTagsEntity = new TagsEntity();
        partialUpdatedTagsEntity.setId(tagsEntity.getId());

        partialUpdatedTagsEntity
            .name(UPDATED_NAME)
            .slug(UPDATED_SLUG)
            .description(UPDATED_DESCRIPTION)
            .iconUrl(UPDATED_ICON_URL)
            .parentId(UPDATED_PARENT_ID)
            .displayOrder(UPDATED_DISPLAY_ORDER)
            .deleted(UPDATED_DELETED)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);

        restTagsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTagsEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTagsEntity))
            )
            .andExpect(status().isOk());

        // Validate the Tags in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTagsEntityUpdatableFieldsEquals(partialUpdatedTagsEntity, getPersistedTagsEntity(partialUpdatedTagsEntity));
    }

    @Test
    @Transactional
    void patchNonExistingTags() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tagsEntity.setId(longCount.incrementAndGet());

        // Create the Tags
        TagsDTO tagsDTO = tagsMapper.toDto(tagsEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTagsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tagsDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tagsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tags in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTags() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tagsEntity.setId(longCount.incrementAndGet());

        // Create the Tags
        TagsDTO tagsDTO = tagsMapper.toDto(tagsEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tagsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tags in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTags() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tagsEntity.setId(longCount.incrementAndGet());

        // Create the Tags
        TagsDTO tagsDTO = tagsMapper.toDto(tagsEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tagsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tags in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTags() throws Exception {
        // Initialize the database
        insertedTagsEntity = tagsRepository.saveAndFlush(tagsEntity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tags
        restTagsMockMvc
            .perform(delete(ENTITY_API_URL_ID, tagsEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tagsRepository.count();
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

    protected TagsEntity getPersistedTagsEntity(TagsEntity tags) {
        return tagsRepository.findById(tags.getId()).orElseThrow();
    }

    protected void assertPersistedTagsEntityToMatchAllProperties(TagsEntity expectedTagsEntity) {
        assertTagsEntityAllPropertiesEquals(expectedTagsEntity, getPersistedTagsEntity(expectedTagsEntity));
    }

    protected void assertPersistedTagsEntityToMatchUpdatableProperties(TagsEntity expectedTagsEntity) {
        assertTagsEntityAllUpdatablePropertiesEquals(expectedTagsEntity, getPersistedTagsEntity(expectedTagsEntity));
    }
}
