package com.ali.coffeehub.roaster.web.rest;

import static com.ali.coffeehub.roaster.domain.RoasterSocialLinkEntityAsserts.*;
import static com.ali.coffeehub.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ali.coffeehub.IntegrationTest;
import com.ali.coffeehub.roaster.domain.RoasterSocialLinkEntity;
import com.ali.coffeehub.roaster.repository.RoasterSocialLinkRepository;
import com.ali.coffeehub.roaster.service.dto.RoasterSocialLinkDTO;
import com.ali.coffeehub.roaster.service.mapper.RoasterSocialLinkMapper;
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
 * Integration tests for the {@link RoasterSocialLinkResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RoasterSocialLinkResourceIT {

    private static final Long DEFAULT_ROASTER_ID = 1L;
    private static final Long UPDATED_ROASTER_ID = 2L;

    private static final Long DEFAULT_PLATFORM = 1L;
    private static final Long UPDATED_PLATFORM = 2L;

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final String ENTITY_API_URL = "/api/roaster-social-links";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RoasterSocialLinkRepository roasterSocialLinkRepository;

    @Autowired
    private RoasterSocialLinkMapper roasterSocialLinkMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoasterSocialLinkMockMvc;

    private RoasterSocialLinkEntity roasterSocialLinkEntity;

    private RoasterSocialLinkEntity insertedRoasterSocialLinkEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoasterSocialLinkEntity createEntity() {
        return new RoasterSocialLinkEntity()
            .roasterId(DEFAULT_ROASTER_ID)
            .platform(DEFAULT_PLATFORM)
            .url(DEFAULT_URL)
            .deleted(DEFAULT_DELETED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoasterSocialLinkEntity createUpdatedEntity() {
        return new RoasterSocialLinkEntity()
            .roasterId(UPDATED_ROASTER_ID)
            .platform(UPDATED_PLATFORM)
            .url(UPDATED_URL)
            .deleted(UPDATED_DELETED);
    }

    @BeforeEach
    public void initTest() {
        roasterSocialLinkEntity = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedRoasterSocialLinkEntity != null) {
            roasterSocialLinkRepository.delete(insertedRoasterSocialLinkEntity);
            insertedRoasterSocialLinkEntity = null;
        }
    }

    @Test
    @Transactional
    void createRoasterSocialLink() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the RoasterSocialLink
        RoasterSocialLinkDTO roasterSocialLinkDTO = roasterSocialLinkMapper.toDto(roasterSocialLinkEntity);
        var returnedRoasterSocialLinkDTO = om.readValue(
            restRoasterSocialLinkMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roasterSocialLinkDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RoasterSocialLinkDTO.class
        );

        // Validate the RoasterSocialLink in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRoasterSocialLinkEntity = roasterSocialLinkMapper.toEntity(returnedRoasterSocialLinkDTO);
        assertRoasterSocialLinkEntityUpdatableFieldsEquals(
            returnedRoasterSocialLinkEntity,
            getPersistedRoasterSocialLinkEntity(returnedRoasterSocialLinkEntity)
        );

        insertedRoasterSocialLinkEntity = returnedRoasterSocialLinkEntity;
    }

    @Test
    @Transactional
    void createRoasterSocialLinkWithExistingId() throws Exception {
        // Create the RoasterSocialLink with an existing ID
        roasterSocialLinkEntity.setId(1L);
        RoasterSocialLinkDTO roasterSocialLinkDTO = roasterSocialLinkMapper.toDto(roasterSocialLinkEntity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoasterSocialLinkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roasterSocialLinkDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RoasterSocialLink in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRoasterIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        roasterSocialLinkEntity.setRoasterId(null);

        // Create the RoasterSocialLink, which fails.
        RoasterSocialLinkDTO roasterSocialLinkDTO = roasterSocialLinkMapper.toDto(roasterSocialLinkEntity);

        restRoasterSocialLinkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roasterSocialLinkDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPlatformIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        roasterSocialLinkEntity.setPlatform(null);

        // Create the RoasterSocialLink, which fails.
        RoasterSocialLinkDTO roasterSocialLinkDTO = roasterSocialLinkMapper.toDto(roasterSocialLinkEntity);

        restRoasterSocialLinkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roasterSocialLinkDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRoasterSocialLinks() throws Exception {
        // Initialize the database
        insertedRoasterSocialLinkEntity = roasterSocialLinkRepository.saveAndFlush(roasterSocialLinkEntity);

        // Get all the roasterSocialLinkList
        restRoasterSocialLinkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roasterSocialLinkEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].roasterId").value(hasItem(DEFAULT_ROASTER_ID.intValue())))
            .andExpect(jsonPath("$.[*].platform").value(hasItem(DEFAULT_PLATFORM.intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)));
    }

    @Test
    @Transactional
    void getRoasterSocialLink() throws Exception {
        // Initialize the database
        insertedRoasterSocialLinkEntity = roasterSocialLinkRepository.saveAndFlush(roasterSocialLinkEntity);

        // Get the roasterSocialLink
        restRoasterSocialLinkMockMvc
            .perform(get(ENTITY_API_URL_ID, roasterSocialLinkEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(roasterSocialLinkEntity.getId().intValue()))
            .andExpect(jsonPath("$.roasterId").value(DEFAULT_ROASTER_ID.intValue()))
            .andExpect(jsonPath("$.platform").value(DEFAULT_PLATFORM.intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED));
    }

    @Test
    @Transactional
    void getNonExistingRoasterSocialLink() throws Exception {
        // Get the roasterSocialLink
        restRoasterSocialLinkMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRoasterSocialLink() throws Exception {
        // Initialize the database
        insertedRoasterSocialLinkEntity = roasterSocialLinkRepository.saveAndFlush(roasterSocialLinkEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the roasterSocialLink
        RoasterSocialLinkEntity updatedRoasterSocialLinkEntity = roasterSocialLinkRepository
            .findById(roasterSocialLinkEntity.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedRoasterSocialLinkEntity are not directly saved in db
        em.detach(updatedRoasterSocialLinkEntity);
        updatedRoasterSocialLinkEntity.roasterId(UPDATED_ROASTER_ID).platform(UPDATED_PLATFORM).url(UPDATED_URL).deleted(UPDATED_DELETED);
        RoasterSocialLinkDTO roasterSocialLinkDTO = roasterSocialLinkMapper.toDto(updatedRoasterSocialLinkEntity);

        restRoasterSocialLinkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roasterSocialLinkDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(roasterSocialLinkDTO))
            )
            .andExpect(status().isOk());

        // Validate the RoasterSocialLink in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRoasterSocialLinkEntityToMatchAllProperties(updatedRoasterSocialLinkEntity);
    }

    @Test
    @Transactional
    void putNonExistingRoasterSocialLink() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roasterSocialLinkEntity.setId(longCount.incrementAndGet());

        // Create the RoasterSocialLink
        RoasterSocialLinkDTO roasterSocialLinkDTO = roasterSocialLinkMapper.toDto(roasterSocialLinkEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoasterSocialLinkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roasterSocialLinkDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(roasterSocialLinkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoasterSocialLink in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRoasterSocialLink() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roasterSocialLinkEntity.setId(longCount.incrementAndGet());

        // Create the RoasterSocialLink
        RoasterSocialLinkDTO roasterSocialLinkDTO = roasterSocialLinkMapper.toDto(roasterSocialLinkEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoasterSocialLinkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(roasterSocialLinkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoasterSocialLink in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRoasterSocialLink() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roasterSocialLinkEntity.setId(longCount.incrementAndGet());

        // Create the RoasterSocialLink
        RoasterSocialLinkDTO roasterSocialLinkDTO = roasterSocialLinkMapper.toDto(roasterSocialLinkEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoasterSocialLinkMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roasterSocialLinkDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RoasterSocialLink in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRoasterSocialLinkWithPatch() throws Exception {
        // Initialize the database
        insertedRoasterSocialLinkEntity = roasterSocialLinkRepository.saveAndFlush(roasterSocialLinkEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the roasterSocialLink using partial update
        RoasterSocialLinkEntity partialUpdatedRoasterSocialLinkEntity = new RoasterSocialLinkEntity();
        partialUpdatedRoasterSocialLinkEntity.setId(roasterSocialLinkEntity.getId());

        partialUpdatedRoasterSocialLinkEntity
            .roasterId(UPDATED_ROASTER_ID)
            .platform(UPDATED_PLATFORM)
            .url(UPDATED_URL)
            .deleted(UPDATED_DELETED);

        restRoasterSocialLinkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoasterSocialLinkEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRoasterSocialLinkEntity))
            )
            .andExpect(status().isOk());

        // Validate the RoasterSocialLink in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRoasterSocialLinkEntityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRoasterSocialLinkEntity, roasterSocialLinkEntity),
            getPersistedRoasterSocialLinkEntity(roasterSocialLinkEntity)
        );
    }

    @Test
    @Transactional
    void fullUpdateRoasterSocialLinkWithPatch() throws Exception {
        // Initialize the database
        insertedRoasterSocialLinkEntity = roasterSocialLinkRepository.saveAndFlush(roasterSocialLinkEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the roasterSocialLink using partial update
        RoasterSocialLinkEntity partialUpdatedRoasterSocialLinkEntity = new RoasterSocialLinkEntity();
        partialUpdatedRoasterSocialLinkEntity.setId(roasterSocialLinkEntity.getId());

        partialUpdatedRoasterSocialLinkEntity
            .roasterId(UPDATED_ROASTER_ID)
            .platform(UPDATED_PLATFORM)
            .url(UPDATED_URL)
            .deleted(UPDATED_DELETED);

        restRoasterSocialLinkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoasterSocialLinkEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRoasterSocialLinkEntity))
            )
            .andExpect(status().isOk());

        // Validate the RoasterSocialLink in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRoasterSocialLinkEntityUpdatableFieldsEquals(
            partialUpdatedRoasterSocialLinkEntity,
            getPersistedRoasterSocialLinkEntity(partialUpdatedRoasterSocialLinkEntity)
        );
    }

    @Test
    @Transactional
    void patchNonExistingRoasterSocialLink() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roasterSocialLinkEntity.setId(longCount.incrementAndGet());

        // Create the RoasterSocialLink
        RoasterSocialLinkDTO roasterSocialLinkDTO = roasterSocialLinkMapper.toDto(roasterSocialLinkEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoasterSocialLinkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, roasterSocialLinkDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(roasterSocialLinkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoasterSocialLink in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRoasterSocialLink() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roasterSocialLinkEntity.setId(longCount.incrementAndGet());

        // Create the RoasterSocialLink
        RoasterSocialLinkDTO roasterSocialLinkDTO = roasterSocialLinkMapper.toDto(roasterSocialLinkEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoasterSocialLinkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(roasterSocialLinkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoasterSocialLink in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRoasterSocialLink() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roasterSocialLinkEntity.setId(longCount.incrementAndGet());

        // Create the RoasterSocialLink
        RoasterSocialLinkDTO roasterSocialLinkDTO = roasterSocialLinkMapper.toDto(roasterSocialLinkEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoasterSocialLinkMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(roasterSocialLinkDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RoasterSocialLink in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRoasterSocialLink() throws Exception {
        // Initialize the database
        insertedRoasterSocialLinkEntity = roasterSocialLinkRepository.saveAndFlush(roasterSocialLinkEntity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the roasterSocialLink
        restRoasterSocialLinkMockMvc
            .perform(delete(ENTITY_API_URL_ID, roasterSocialLinkEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return roasterSocialLinkRepository.count();
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

    protected RoasterSocialLinkEntity getPersistedRoasterSocialLinkEntity(RoasterSocialLinkEntity roasterSocialLink) {
        return roasterSocialLinkRepository.findById(roasterSocialLink.getId()).orElseThrow();
    }

    protected void assertPersistedRoasterSocialLinkEntityToMatchAllProperties(RoasterSocialLinkEntity expectedRoasterSocialLinkEntity) {
        assertRoasterSocialLinkEntityAllPropertiesEquals(
            expectedRoasterSocialLinkEntity,
            getPersistedRoasterSocialLinkEntity(expectedRoasterSocialLinkEntity)
        );
    }

    protected void assertPersistedRoasterSocialLinkEntityToMatchUpdatableProperties(
        RoasterSocialLinkEntity expectedRoasterSocialLinkEntity
    ) {
        assertRoasterSocialLinkEntityAllUpdatablePropertiesEquals(
            expectedRoasterSocialLinkEntity,
            getPersistedRoasterSocialLinkEntity(expectedRoasterSocialLinkEntity)
        );
    }
}
