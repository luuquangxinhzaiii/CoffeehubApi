package com.ali.coffeehub.brew.web.rest;

import static com.ali.coffeehub.brew.domain.TimelineEntityAsserts.*;
import static com.ali.coffeehub.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ali.coffeehub.IntegrationTest;
import com.ali.coffeehub.brew.domain.TimelineEntity;
import com.ali.coffeehub.brew.repository.TimelineRepository;
import com.ali.coffeehub.brew.service.dto.TimelineDTO;
import com.ali.coffeehub.brew.service.mapper.TimelineMapper;
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
 * Integration tests for the {@link TimelineResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TimelineResourceIT {

    private static final Long DEFAULT_CONFIG_ID = 1L;
    private static final Long UPDATED_CONFIG_ID = 2L;

    private static final Long DEFAULT_CONFIG_VALUE_ID = 1L;
    private static final Long UPDATED_CONFIG_VALUE_ID = 2L;

    private static final Long DEFAULT_RECIPE_ID = 1L;
    private static final Long UPDATED_RECIPE_ID = 2L;

    private static final Integer DEFAULT_START_TIME = 1;
    private static final Integer UPDATED_START_TIME = 2;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_RATIO = 1;
    private static final Integer UPDATED_RATIO = 2;

    private static final Boolean DEFAULT_IS_FIXED = false;
    private static final Boolean UPDATED_IS_FIXED = true;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/timelines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TimelineRepository timelineRepository;

    @Autowired
    private TimelineMapper timelineMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTimelineMockMvc;

    private TimelineEntity timelineEntity;

    private TimelineEntity insertedTimelineEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TimelineEntity createEntity() {
        return new TimelineEntity()
            .configId(DEFAULT_CONFIG_ID)
            .configValueId(DEFAULT_CONFIG_VALUE_ID)
            .recipeId(DEFAULT_RECIPE_ID)
            .startTime(DEFAULT_START_TIME)
            .createdAt(DEFAULT_CREATED_AT)
            .ratio(DEFAULT_RATIO)
            .isFixed(DEFAULT_IS_FIXED)
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
    public static TimelineEntity createUpdatedEntity() {
        return new TimelineEntity()
            .configId(UPDATED_CONFIG_ID)
            .configValueId(UPDATED_CONFIG_VALUE_ID)
            .recipeId(UPDATED_RECIPE_ID)
            .startTime(UPDATED_START_TIME)
            .createdAt(UPDATED_CREATED_AT)
            .ratio(UPDATED_RATIO)
            .isFixed(UPDATED_IS_FIXED)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);
    }

    @BeforeEach
    public void initTest() {
        timelineEntity = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedTimelineEntity != null) {
            timelineRepository.delete(insertedTimelineEntity);
            insertedTimelineEntity = null;
        }
    }

    @Test
    @Transactional
    void createTimeline() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Timeline
        TimelineDTO timelineDTO = timelineMapper.toDto(timelineEntity);
        var returnedTimelineDTO = om.readValue(
            restTimelineMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timelineDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TimelineDTO.class
        );

        // Validate the Timeline in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTimelineEntity = timelineMapper.toEntity(returnedTimelineDTO);
        assertTimelineEntityUpdatableFieldsEquals(returnedTimelineEntity, getPersistedTimelineEntity(returnedTimelineEntity));

        insertedTimelineEntity = returnedTimelineEntity;
    }

    @Test
    @Transactional
    void createTimelineWithExistingId() throws Exception {
        // Create the Timeline with an existing ID
        timelineEntity.setId(1L);
        TimelineDTO timelineDTO = timelineMapper.toDto(timelineEntity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTimelineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timelineDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Timeline in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkConfigIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        timelineEntity.setConfigId(null);

        // Create the Timeline, which fails.
        TimelineDTO timelineDTO = timelineMapper.toDto(timelineEntity);

        restTimelineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timelineDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkConfigValueIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        timelineEntity.setConfigValueId(null);

        // Create the Timeline, which fails.
        TimelineDTO timelineDTO = timelineMapper.toDto(timelineEntity);

        restTimelineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timelineDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRecipeIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        timelineEntity.setRecipeId(null);

        // Create the Timeline, which fails.
        TimelineDTO timelineDTO = timelineMapper.toDto(timelineEntity);

        restTimelineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timelineDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        timelineEntity.setStartTime(null);

        // Create the Timeline, which fails.
        TimelineDTO timelineDTO = timelineMapper.toDto(timelineEntity);

        restTimelineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timelineDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        timelineEntity.setCreatedAt(null);

        // Create the Timeline, which fails.
        TimelineDTO timelineDTO = timelineMapper.toDto(timelineEntity);

        restTimelineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timelineDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTimelines() throws Exception {
        // Initialize the database
        insertedTimelineEntity = timelineRepository.saveAndFlush(timelineEntity);

        // Get all the timelineList
        restTimelineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(timelineEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].configId").value(hasItem(DEFAULT_CONFIG_ID.intValue())))
            .andExpect(jsonPath("$.[*].configValueId").value(hasItem(DEFAULT_CONFIG_VALUE_ID.intValue())))
            .andExpect(jsonPath("$.[*].recipeId").value(hasItem(DEFAULT_RECIPE_ID.intValue())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].ratio").value(hasItem(DEFAULT_RATIO)))
            .andExpect(jsonPath("$.[*].isFixed").value(hasItem(DEFAULT_IS_FIXED)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getTimeline() throws Exception {
        // Initialize the database
        insertedTimelineEntity = timelineRepository.saveAndFlush(timelineEntity);

        // Get the timeline
        restTimelineMockMvc
            .perform(get(ENTITY_API_URL_ID, timelineEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(timelineEntity.getId().intValue()))
            .andExpect(jsonPath("$.configId").value(DEFAULT_CONFIG_ID.intValue()))
            .andExpect(jsonPath("$.configValueId").value(DEFAULT_CONFIG_VALUE_ID.intValue()))
            .andExpect(jsonPath("$.recipeId").value(DEFAULT_RECIPE_ID.intValue()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.ratio").value(DEFAULT_RATIO))
            .andExpect(jsonPath("$.isFixed").value(DEFAULT_IS_FIXED))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingTimeline() throws Exception {
        // Get the timeline
        restTimelineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTimeline() throws Exception {
        // Initialize the database
        insertedTimelineEntity = timelineRepository.saveAndFlush(timelineEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the timeline
        TimelineEntity updatedTimelineEntity = timelineRepository.findById(timelineEntity.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTimelineEntity are not directly saved in db
        em.detach(updatedTimelineEntity);
        updatedTimelineEntity
            .configId(UPDATED_CONFIG_ID)
            .configValueId(UPDATED_CONFIG_VALUE_ID)
            .recipeId(UPDATED_RECIPE_ID)
            .startTime(UPDATED_START_TIME)
            .createdAt(UPDATED_CREATED_AT)
            .ratio(UPDATED_RATIO)
            .isFixed(UPDATED_IS_FIXED)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);
        TimelineDTO timelineDTO = timelineMapper.toDto(updatedTimelineEntity);

        restTimelineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, timelineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(timelineDTO))
            )
            .andExpect(status().isOk());

        // Validate the Timeline in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTimelineEntityToMatchAllProperties(updatedTimelineEntity);
    }

    @Test
    @Transactional
    void putNonExistingTimeline() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timelineEntity.setId(longCount.incrementAndGet());

        // Create the Timeline
        TimelineDTO timelineDTO = timelineMapper.toDto(timelineEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTimelineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, timelineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(timelineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Timeline in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTimeline() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timelineEntity.setId(longCount.incrementAndGet());

        // Create the Timeline
        TimelineDTO timelineDTO = timelineMapper.toDto(timelineEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimelineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(timelineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Timeline in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTimeline() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timelineEntity.setId(longCount.incrementAndGet());

        // Create the Timeline
        TimelineDTO timelineDTO = timelineMapper.toDto(timelineEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimelineMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timelineDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Timeline in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTimelineWithPatch() throws Exception {
        // Initialize the database
        insertedTimelineEntity = timelineRepository.saveAndFlush(timelineEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the timeline using partial update
        TimelineEntity partialUpdatedTimelineEntity = new TimelineEntity();
        partialUpdatedTimelineEntity.setId(timelineEntity.getId());

        partialUpdatedTimelineEntity.configId(UPDATED_CONFIG_ID).updatedBy(UPDATED_UPDATED_BY);

        restTimelineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTimelineEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTimelineEntity))
            )
            .andExpect(status().isOk());

        // Validate the Timeline in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTimelineEntityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTimelineEntity, timelineEntity),
            getPersistedTimelineEntity(timelineEntity)
        );
    }

    @Test
    @Transactional
    void fullUpdateTimelineWithPatch() throws Exception {
        // Initialize the database
        insertedTimelineEntity = timelineRepository.saveAndFlush(timelineEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the timeline using partial update
        TimelineEntity partialUpdatedTimelineEntity = new TimelineEntity();
        partialUpdatedTimelineEntity.setId(timelineEntity.getId());

        partialUpdatedTimelineEntity
            .configId(UPDATED_CONFIG_ID)
            .configValueId(UPDATED_CONFIG_VALUE_ID)
            .recipeId(UPDATED_RECIPE_ID)
            .startTime(UPDATED_START_TIME)
            .createdAt(UPDATED_CREATED_AT)
            .ratio(UPDATED_RATIO)
            .isFixed(UPDATED_IS_FIXED)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);

        restTimelineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTimelineEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTimelineEntity))
            )
            .andExpect(status().isOk());

        // Validate the Timeline in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTimelineEntityUpdatableFieldsEquals(partialUpdatedTimelineEntity, getPersistedTimelineEntity(partialUpdatedTimelineEntity));
    }

    @Test
    @Transactional
    void patchNonExistingTimeline() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timelineEntity.setId(longCount.incrementAndGet());

        // Create the Timeline
        TimelineDTO timelineDTO = timelineMapper.toDto(timelineEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTimelineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, timelineDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(timelineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Timeline in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTimeline() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timelineEntity.setId(longCount.incrementAndGet());

        // Create the Timeline
        TimelineDTO timelineDTO = timelineMapper.toDto(timelineEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimelineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(timelineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Timeline in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTimeline() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timelineEntity.setId(longCount.incrementAndGet());

        // Create the Timeline
        TimelineDTO timelineDTO = timelineMapper.toDto(timelineEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimelineMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(timelineDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Timeline in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTimeline() throws Exception {
        // Initialize the database
        insertedTimelineEntity = timelineRepository.saveAndFlush(timelineEntity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the timeline
        restTimelineMockMvc
            .perform(delete(ENTITY_API_URL_ID, timelineEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return timelineRepository.count();
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

    protected TimelineEntity getPersistedTimelineEntity(TimelineEntity timeline) {
        return timelineRepository.findById(timeline.getId()).orElseThrow();
    }

    protected void assertPersistedTimelineEntityToMatchAllProperties(TimelineEntity expectedTimelineEntity) {
        assertTimelineEntityAllPropertiesEquals(expectedTimelineEntity, getPersistedTimelineEntity(expectedTimelineEntity));
    }

    protected void assertPersistedTimelineEntityToMatchUpdatableProperties(TimelineEntity expectedTimelineEntity) {
        assertTimelineEntityAllUpdatablePropertiesEquals(expectedTimelineEntity, getPersistedTimelineEntity(expectedTimelineEntity));
    }
}
