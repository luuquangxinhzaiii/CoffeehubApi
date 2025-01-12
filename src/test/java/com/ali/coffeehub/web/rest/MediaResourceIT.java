package com.ali.coffeehub.web.rest;

import static com.ali.coffeehub.domain.MediaEntityAsserts.*;
import static com.ali.coffeehub.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ali.coffeehub.IntegrationTest;
import com.ali.coffeehub.domain.MediaEntity;
import com.ali.coffeehub.repository.MediaRepository;
import com.ali.coffeehub.service.dto.MediaDTO;
import com.ali.coffeehub.service.mapper.MediaMapper;
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
 * Integration tests for the {@link MediaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MediaResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ENTITY_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_ENTITY_ID = 1L;
    private static final Long UPDATED_ENTITY_ID = 2L;

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_FILE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_MIME_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_MIME_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_FILE_SIZE = 1L;
    private static final Long UPDATED_FILE_SIZE = 2L;

    private static final String DEFAULT_FILE_URI = "AAAAAAAAAA";
    private static final String UPDATED_FILE_URI = "BBBBBBBBBB";

    private static final String DEFAULT_THUMBNAIL_URI = "AAAAAAAAAA";
    private static final String UPDATED_THUMBNAIL_URI = "BBBBBBBBBB";

    private static final Integer DEFAULT_WIDTH = 1;
    private static final Integer UPDATED_WIDTH = 2;

    private static final Integer DEFAULT_HEIGHT = 1;
    private static final Integer UPDATED_HEIGHT = 2;

    private static final Integer DEFAULT_DURATION = 1;
    private static final Integer UPDATED_DURATION = 2;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/media";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private MediaMapper mediaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMediaMockMvc;

    private MediaEntity mediaEntity;

    private MediaEntity insertedMediaEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MediaEntity createEntity() {
        return new MediaEntity()
            .name(DEFAULT_NAME)
            .entityType(DEFAULT_ENTITY_TYPE)
            .entityId(DEFAULT_ENTITY_ID)
            .fileName(DEFAULT_FILE_NAME)
            .fileType(DEFAULT_FILE_TYPE)
            .mimeType(DEFAULT_MIME_TYPE)
            .fileSize(DEFAULT_FILE_SIZE)
            .fileUri(DEFAULT_FILE_URI)
            .thumbnailUri(DEFAULT_THUMBNAIL_URI)
            .width(DEFAULT_WIDTH)
            .height(DEFAULT_HEIGHT)
            .duration(DEFAULT_DURATION)
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
    public static MediaEntity createUpdatedEntity() {
        return new MediaEntity()
            .name(UPDATED_NAME)
            .entityType(UPDATED_ENTITY_TYPE)
            .entityId(UPDATED_ENTITY_ID)
            .fileName(UPDATED_FILE_NAME)
            .fileType(UPDATED_FILE_TYPE)
            .mimeType(UPDATED_MIME_TYPE)
            .fileSize(UPDATED_FILE_SIZE)
            .fileUri(UPDATED_FILE_URI)
            .thumbnailUri(UPDATED_THUMBNAIL_URI)
            .width(UPDATED_WIDTH)
            .height(UPDATED_HEIGHT)
            .duration(UPDATED_DURATION)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);
    }

    @BeforeEach
    public void initTest() {
        mediaEntity = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMediaEntity != null) {
            mediaRepository.delete(insertedMediaEntity);
            insertedMediaEntity = null;
        }
    }

    @Test
    @Transactional
    void createMedia() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Media
        MediaDTO mediaDTO = mediaMapper.toDto(mediaEntity);
        var returnedMediaDTO = om.readValue(
            restMediaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mediaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MediaDTO.class
        );

        // Validate the Media in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMediaEntity = mediaMapper.toEntity(returnedMediaDTO);
        assertMediaEntityUpdatableFieldsEquals(returnedMediaEntity, getPersistedMediaEntity(returnedMediaEntity));

        insertedMediaEntity = returnedMediaEntity;
    }

    @Test
    @Transactional
    void createMediaWithExistingId() throws Exception {
        // Create the Media with an existing ID
        mediaEntity.setId(1L);
        MediaDTO mediaDTO = mediaMapper.toDto(mediaEntity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMediaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mediaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Media in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        mediaEntity.setName(null);

        // Create the Media, which fails.
        MediaDTO mediaDTO = mediaMapper.toDto(mediaEntity);

        restMediaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mediaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEntityTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        mediaEntity.setEntityType(null);

        // Create the Media, which fails.
        MediaDTO mediaDTO = mediaMapper.toDto(mediaEntity);

        restMediaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mediaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEntityIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        mediaEntity.setEntityId(null);

        // Create the Media, which fails.
        MediaDTO mediaDTO = mediaMapper.toDto(mediaEntity);

        restMediaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mediaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFileNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        mediaEntity.setFileName(null);

        // Create the Media, which fails.
        MediaDTO mediaDTO = mediaMapper.toDto(mediaEntity);

        restMediaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mediaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFileUriIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        mediaEntity.setFileUri(null);

        // Create the Media, which fails.
        MediaDTO mediaDTO = mediaMapper.toDto(mediaEntity);

        restMediaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mediaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        mediaEntity.setCreatedAt(null);

        // Create the Media, which fails.
        MediaDTO mediaDTO = mediaMapper.toDto(mediaEntity);

        restMediaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mediaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMedia() throws Exception {
        // Initialize the database
        insertedMediaEntity = mediaRepository.saveAndFlush(mediaEntity);

        // Get all the mediaList
        restMediaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mediaEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].entityType").value(hasItem(DEFAULT_ENTITY_TYPE)))
            .andExpect(jsonPath("$.[*].entityId").value(hasItem(DEFAULT_ENTITY_ID.intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].fileType").value(hasItem(DEFAULT_FILE_TYPE)))
            .andExpect(jsonPath("$.[*].mimeType").value(hasItem(DEFAULT_MIME_TYPE)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].fileUri").value(hasItem(DEFAULT_FILE_URI)))
            .andExpect(jsonPath("$.[*].thumbnailUri").value(hasItem(DEFAULT_THUMBNAIL_URI)))
            .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH)))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT)))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getMedia() throws Exception {
        // Initialize the database
        insertedMediaEntity = mediaRepository.saveAndFlush(mediaEntity);

        // Get the media
        restMediaMockMvc
            .perform(get(ENTITY_API_URL_ID, mediaEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mediaEntity.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.entityType").value(DEFAULT_ENTITY_TYPE))
            .andExpect(jsonPath("$.entityId").value(DEFAULT_ENTITY_ID.intValue()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME))
            .andExpect(jsonPath("$.fileType").value(DEFAULT_FILE_TYPE))
            .andExpect(jsonPath("$.mimeType").value(DEFAULT_MIME_TYPE))
            .andExpect(jsonPath("$.fileSize").value(DEFAULT_FILE_SIZE.intValue()))
            .andExpect(jsonPath("$.fileUri").value(DEFAULT_FILE_URI))
            .andExpect(jsonPath("$.thumbnailUri").value(DEFAULT_THUMBNAIL_URI))
            .andExpect(jsonPath("$.width").value(DEFAULT_WIDTH))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingMedia() throws Exception {
        // Get the media
        restMediaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMedia() throws Exception {
        // Initialize the database
        insertedMediaEntity = mediaRepository.saveAndFlush(mediaEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the media
        MediaEntity updatedMediaEntity = mediaRepository.findById(mediaEntity.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMediaEntity are not directly saved in db
        em.detach(updatedMediaEntity);
        updatedMediaEntity
            .name(UPDATED_NAME)
            .entityType(UPDATED_ENTITY_TYPE)
            .entityId(UPDATED_ENTITY_ID)
            .fileName(UPDATED_FILE_NAME)
            .fileType(UPDATED_FILE_TYPE)
            .mimeType(UPDATED_MIME_TYPE)
            .fileSize(UPDATED_FILE_SIZE)
            .fileUri(UPDATED_FILE_URI)
            .thumbnailUri(UPDATED_THUMBNAIL_URI)
            .width(UPDATED_WIDTH)
            .height(UPDATED_HEIGHT)
            .duration(UPDATED_DURATION)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);
        MediaDTO mediaDTO = mediaMapper.toDto(updatedMediaEntity);

        restMediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mediaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mediaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Media in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMediaEntityToMatchAllProperties(updatedMediaEntity);
    }

    @Test
    @Transactional
    void putNonExistingMedia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mediaEntity.setId(longCount.incrementAndGet());

        // Create the Media
        MediaDTO mediaDTO = mediaMapper.toDto(mediaEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mediaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Media in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMedia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mediaEntity.setId(longCount.incrementAndGet());

        // Create the Media
        MediaDTO mediaDTO = mediaMapper.toDto(mediaEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(mediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Media in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMedia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mediaEntity.setId(longCount.incrementAndGet());

        // Create the Media
        MediaDTO mediaDTO = mediaMapper.toDto(mediaEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMediaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mediaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Media in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMediaWithPatch() throws Exception {
        // Initialize the database
        insertedMediaEntity = mediaRepository.saveAndFlush(mediaEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the media using partial update
        MediaEntity partialUpdatedMediaEntity = new MediaEntity();
        partialUpdatedMediaEntity.setId(mediaEntity.getId());

        partialUpdatedMediaEntity
            .name(UPDATED_NAME)
            .entityType(UPDATED_ENTITY_TYPE)
            .entityId(UPDATED_ENTITY_ID)
            .fileName(UPDATED_FILE_NAME)
            .fileType(UPDATED_FILE_TYPE)
            .fileSize(UPDATED_FILE_SIZE)
            .fileUri(UPDATED_FILE_URI)
            .duration(UPDATED_DURATION)
            .createdBy(UPDATED_CREATED_BY);

        restMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMediaEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMediaEntity))
            )
            .andExpect(status().isOk());

        // Validate the Media in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMediaEntityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMediaEntity, mediaEntity),
            getPersistedMediaEntity(mediaEntity)
        );
    }

    @Test
    @Transactional
    void fullUpdateMediaWithPatch() throws Exception {
        // Initialize the database
        insertedMediaEntity = mediaRepository.saveAndFlush(mediaEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the media using partial update
        MediaEntity partialUpdatedMediaEntity = new MediaEntity();
        partialUpdatedMediaEntity.setId(mediaEntity.getId());

        partialUpdatedMediaEntity
            .name(UPDATED_NAME)
            .entityType(UPDATED_ENTITY_TYPE)
            .entityId(UPDATED_ENTITY_ID)
            .fileName(UPDATED_FILE_NAME)
            .fileType(UPDATED_FILE_TYPE)
            .mimeType(UPDATED_MIME_TYPE)
            .fileSize(UPDATED_FILE_SIZE)
            .fileUri(UPDATED_FILE_URI)
            .thumbnailUri(UPDATED_THUMBNAIL_URI)
            .width(UPDATED_WIDTH)
            .height(UPDATED_HEIGHT)
            .duration(UPDATED_DURATION)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);

        restMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMediaEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMediaEntity))
            )
            .andExpect(status().isOk());

        // Validate the Media in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMediaEntityUpdatableFieldsEquals(partialUpdatedMediaEntity, getPersistedMediaEntity(partialUpdatedMediaEntity));
    }

    @Test
    @Transactional
    void patchNonExistingMedia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mediaEntity.setId(longCount.incrementAndGet());

        // Create the Media
        MediaDTO mediaDTO = mediaMapper.toDto(mediaEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mediaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(mediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Media in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMedia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mediaEntity.setId(longCount.incrementAndGet());

        // Create the Media
        MediaDTO mediaDTO = mediaMapper.toDto(mediaEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(mediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Media in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMedia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mediaEntity.setId(longCount.incrementAndGet());

        // Create the Media
        MediaDTO mediaDTO = mediaMapper.toDto(mediaEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMediaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(mediaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Media in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMedia() throws Exception {
        // Initialize the database
        insertedMediaEntity = mediaRepository.saveAndFlush(mediaEntity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the media
        restMediaMockMvc
            .perform(delete(ENTITY_API_URL_ID, mediaEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return mediaRepository.count();
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

    protected MediaEntity getPersistedMediaEntity(MediaEntity media) {
        return mediaRepository.findById(media.getId()).orElseThrow();
    }

    protected void assertPersistedMediaEntityToMatchAllProperties(MediaEntity expectedMediaEntity) {
        assertMediaEntityAllPropertiesEquals(expectedMediaEntity, getPersistedMediaEntity(expectedMediaEntity));
    }

    protected void assertPersistedMediaEntityToMatchUpdatableProperties(MediaEntity expectedMediaEntity) {
        assertMediaEntityAllUpdatablePropertiesEquals(expectedMediaEntity, getPersistedMediaEntity(expectedMediaEntity));
    }
}
