package com.ali.coffeehub.cafeteria.web.rest;

import static com.ali.coffeehub.cafeteria.domain.CoffeeShopReactionEntityAsserts.*;
import static com.ali.coffeehub.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ali.coffeehub.IntegrationTest;
import com.ali.coffeehub.cafeteria.domain.CoffeeShopReactionEntity;
import com.ali.coffeehub.cafeteria.repository.CoffeeShopReactionRepository;
import com.ali.coffeehub.cafeteria.service.dto.CoffeeShopReactionDTO;
import com.ali.coffeehub.cafeteria.service.mapper.CoffeeShopReactionMapper;
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
 * Integration tests for the {@link CoffeeShopReactionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CoffeeShopReactionResourceIT {

    private static final Long DEFAULT_COFFEE_SHOP_ID = 1L;
    private static final Long UPDATED_COFFEE_SHOP_ID = 2L;

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/coffee-shop-reactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CoffeeShopReactionRepository coffeeShopReactionRepository;

    @Autowired
    private CoffeeShopReactionMapper coffeeShopReactionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCoffeeShopReactionMockMvc;

    private CoffeeShopReactionEntity coffeeShopReactionEntity;

    private CoffeeShopReactionEntity insertedCoffeeShopReactionEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CoffeeShopReactionEntity createEntity() {
        return new CoffeeShopReactionEntity()
            .coffeeShopId(DEFAULT_COFFEE_SHOP_ID)
            .userId(DEFAULT_USER_ID)
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
    public static CoffeeShopReactionEntity createUpdatedEntity() {
        return new CoffeeShopReactionEntity()
            .coffeeShopId(UPDATED_COFFEE_SHOP_ID)
            .userId(UPDATED_USER_ID)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);
    }

    @BeforeEach
    public void initTest() {
        coffeeShopReactionEntity = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCoffeeShopReactionEntity != null) {
            coffeeShopReactionRepository.delete(insertedCoffeeShopReactionEntity);
            insertedCoffeeShopReactionEntity = null;
        }
    }

    @Test
    @Transactional
    void createCoffeeShopReaction() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CoffeeShopReaction
        CoffeeShopReactionDTO coffeeShopReactionDTO = coffeeShopReactionMapper.toDto(coffeeShopReactionEntity);
        var returnedCoffeeShopReactionDTO = om.readValue(
            restCoffeeShopReactionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coffeeShopReactionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CoffeeShopReactionDTO.class
        );

        // Validate the CoffeeShopReaction in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCoffeeShopReactionEntity = coffeeShopReactionMapper.toEntity(returnedCoffeeShopReactionDTO);
        assertCoffeeShopReactionEntityUpdatableFieldsEquals(
            returnedCoffeeShopReactionEntity,
            getPersistedCoffeeShopReactionEntity(returnedCoffeeShopReactionEntity)
        );

        insertedCoffeeShopReactionEntity = returnedCoffeeShopReactionEntity;
    }

    @Test
    @Transactional
    void createCoffeeShopReactionWithExistingId() throws Exception {
        // Create the CoffeeShopReaction with an existing ID
        coffeeShopReactionEntity.setId(1L);
        CoffeeShopReactionDTO coffeeShopReactionDTO = coffeeShopReactionMapper.toDto(coffeeShopReactionEntity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCoffeeShopReactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coffeeShopReactionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CoffeeShopReaction in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCoffeeShopIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        coffeeShopReactionEntity.setCoffeeShopId(null);

        // Create the CoffeeShopReaction, which fails.
        CoffeeShopReactionDTO coffeeShopReactionDTO = coffeeShopReactionMapper.toDto(coffeeShopReactionEntity);

        restCoffeeShopReactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coffeeShopReactionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUserIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        coffeeShopReactionEntity.setUserId(null);

        // Create the CoffeeShopReaction, which fails.
        CoffeeShopReactionDTO coffeeShopReactionDTO = coffeeShopReactionMapper.toDto(coffeeShopReactionEntity);

        restCoffeeShopReactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coffeeShopReactionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        coffeeShopReactionEntity.setCreatedAt(null);

        // Create the CoffeeShopReaction, which fails.
        CoffeeShopReactionDTO coffeeShopReactionDTO = coffeeShopReactionMapper.toDto(coffeeShopReactionEntity);

        restCoffeeShopReactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coffeeShopReactionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCoffeeShopReactions() throws Exception {
        // Initialize the database
        insertedCoffeeShopReactionEntity = coffeeShopReactionRepository.saveAndFlush(coffeeShopReactionEntity);

        // Get all the coffeeShopReactionList
        restCoffeeShopReactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coffeeShopReactionEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].coffeeShopId").value(hasItem(DEFAULT_COFFEE_SHOP_ID.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getCoffeeShopReaction() throws Exception {
        // Initialize the database
        insertedCoffeeShopReactionEntity = coffeeShopReactionRepository.saveAndFlush(coffeeShopReactionEntity);

        // Get the coffeeShopReaction
        restCoffeeShopReactionMockMvc
            .perform(get(ENTITY_API_URL_ID, coffeeShopReactionEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(coffeeShopReactionEntity.getId().intValue()))
            .andExpect(jsonPath("$.coffeeShopId").value(DEFAULT_COFFEE_SHOP_ID.intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingCoffeeShopReaction() throws Exception {
        // Get the coffeeShopReaction
        restCoffeeShopReactionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCoffeeShopReaction() throws Exception {
        // Initialize the database
        insertedCoffeeShopReactionEntity = coffeeShopReactionRepository.saveAndFlush(coffeeShopReactionEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the coffeeShopReaction
        CoffeeShopReactionEntity updatedCoffeeShopReactionEntity = coffeeShopReactionRepository
            .findById(coffeeShopReactionEntity.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedCoffeeShopReactionEntity are not directly saved in db
        em.detach(updatedCoffeeShopReactionEntity);
        updatedCoffeeShopReactionEntity
            .coffeeShopId(UPDATED_COFFEE_SHOP_ID)
            .userId(UPDATED_USER_ID)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);
        CoffeeShopReactionDTO coffeeShopReactionDTO = coffeeShopReactionMapper.toDto(updatedCoffeeShopReactionEntity);

        restCoffeeShopReactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, coffeeShopReactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(coffeeShopReactionDTO))
            )
            .andExpect(status().isOk());

        // Validate the CoffeeShopReaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCoffeeShopReactionEntityToMatchAllProperties(updatedCoffeeShopReactionEntity);
    }

    @Test
    @Transactional
    void putNonExistingCoffeeShopReaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coffeeShopReactionEntity.setId(longCount.incrementAndGet());

        // Create the CoffeeShopReaction
        CoffeeShopReactionDTO coffeeShopReactionDTO = coffeeShopReactionMapper.toDto(coffeeShopReactionEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoffeeShopReactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, coffeeShopReactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(coffeeShopReactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoffeeShopReaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCoffeeShopReaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coffeeShopReactionEntity.setId(longCount.incrementAndGet());

        // Create the CoffeeShopReaction
        CoffeeShopReactionDTO coffeeShopReactionDTO = coffeeShopReactionMapper.toDto(coffeeShopReactionEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoffeeShopReactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(coffeeShopReactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoffeeShopReaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCoffeeShopReaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coffeeShopReactionEntity.setId(longCount.incrementAndGet());

        // Create the CoffeeShopReaction
        CoffeeShopReactionDTO coffeeShopReactionDTO = coffeeShopReactionMapper.toDto(coffeeShopReactionEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoffeeShopReactionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coffeeShopReactionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CoffeeShopReaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCoffeeShopReactionWithPatch() throws Exception {
        // Initialize the database
        insertedCoffeeShopReactionEntity = coffeeShopReactionRepository.saveAndFlush(coffeeShopReactionEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the coffeeShopReaction using partial update
        CoffeeShopReactionEntity partialUpdatedCoffeeShopReactionEntity = new CoffeeShopReactionEntity();
        partialUpdatedCoffeeShopReactionEntity.setId(coffeeShopReactionEntity.getId());

        partialUpdatedCoffeeShopReactionEntity
            .coffeeShopId(UPDATED_COFFEE_SHOP_ID)
            .userId(UPDATED_USER_ID)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);

        restCoffeeShopReactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCoffeeShopReactionEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCoffeeShopReactionEntity))
            )
            .andExpect(status().isOk());

        // Validate the CoffeeShopReaction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCoffeeShopReactionEntityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCoffeeShopReactionEntity, coffeeShopReactionEntity),
            getPersistedCoffeeShopReactionEntity(coffeeShopReactionEntity)
        );
    }

    @Test
    @Transactional
    void fullUpdateCoffeeShopReactionWithPatch() throws Exception {
        // Initialize the database
        insertedCoffeeShopReactionEntity = coffeeShopReactionRepository.saveAndFlush(coffeeShopReactionEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the coffeeShopReaction using partial update
        CoffeeShopReactionEntity partialUpdatedCoffeeShopReactionEntity = new CoffeeShopReactionEntity();
        partialUpdatedCoffeeShopReactionEntity.setId(coffeeShopReactionEntity.getId());

        partialUpdatedCoffeeShopReactionEntity
            .coffeeShopId(UPDATED_COFFEE_SHOP_ID)
            .userId(UPDATED_USER_ID)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);

        restCoffeeShopReactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCoffeeShopReactionEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCoffeeShopReactionEntity))
            )
            .andExpect(status().isOk());

        // Validate the CoffeeShopReaction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCoffeeShopReactionEntityUpdatableFieldsEquals(
            partialUpdatedCoffeeShopReactionEntity,
            getPersistedCoffeeShopReactionEntity(partialUpdatedCoffeeShopReactionEntity)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCoffeeShopReaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coffeeShopReactionEntity.setId(longCount.incrementAndGet());

        // Create the CoffeeShopReaction
        CoffeeShopReactionDTO coffeeShopReactionDTO = coffeeShopReactionMapper.toDto(coffeeShopReactionEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoffeeShopReactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, coffeeShopReactionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(coffeeShopReactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoffeeShopReaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCoffeeShopReaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coffeeShopReactionEntity.setId(longCount.incrementAndGet());

        // Create the CoffeeShopReaction
        CoffeeShopReactionDTO coffeeShopReactionDTO = coffeeShopReactionMapper.toDto(coffeeShopReactionEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoffeeShopReactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(coffeeShopReactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoffeeShopReaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCoffeeShopReaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coffeeShopReactionEntity.setId(longCount.incrementAndGet());

        // Create the CoffeeShopReaction
        CoffeeShopReactionDTO coffeeShopReactionDTO = coffeeShopReactionMapper.toDto(coffeeShopReactionEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoffeeShopReactionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(coffeeShopReactionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CoffeeShopReaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCoffeeShopReaction() throws Exception {
        // Initialize the database
        insertedCoffeeShopReactionEntity = coffeeShopReactionRepository.saveAndFlush(coffeeShopReactionEntity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the coffeeShopReaction
        restCoffeeShopReactionMockMvc
            .perform(delete(ENTITY_API_URL_ID, coffeeShopReactionEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return coffeeShopReactionRepository.count();
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

    protected CoffeeShopReactionEntity getPersistedCoffeeShopReactionEntity(CoffeeShopReactionEntity coffeeShopReaction) {
        return coffeeShopReactionRepository.findById(coffeeShopReaction.getId()).orElseThrow();
    }

    protected void assertPersistedCoffeeShopReactionEntityToMatchAllProperties(CoffeeShopReactionEntity expectedCoffeeShopReactionEntity) {
        assertCoffeeShopReactionEntityAllPropertiesEquals(
            expectedCoffeeShopReactionEntity,
            getPersistedCoffeeShopReactionEntity(expectedCoffeeShopReactionEntity)
        );
    }

    protected void assertPersistedCoffeeShopReactionEntityToMatchUpdatableProperties(
        CoffeeShopReactionEntity expectedCoffeeShopReactionEntity
    ) {
        assertCoffeeShopReactionEntityAllUpdatablePropertiesEquals(
            expectedCoffeeShopReactionEntity,
            getPersistedCoffeeShopReactionEntity(expectedCoffeeShopReactionEntity)
        );
    }
}
