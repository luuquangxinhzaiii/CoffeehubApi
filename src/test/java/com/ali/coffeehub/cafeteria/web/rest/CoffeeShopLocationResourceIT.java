package com.ali.coffeehub.cafeteria.web.rest;

import static com.ali.coffeehub.cafeteria.domain.CoffeeShopLocationEntityAsserts.*;
import static com.ali.coffeehub.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ali.coffeehub.IntegrationTest;
import com.ali.coffeehub.cafeteria.domain.CoffeeShopLocationEntity;
import com.ali.coffeehub.cafeteria.repository.CoffeeShopLocationRepository;
import com.ali.coffeehub.cafeteria.service.dto.CoffeeShopLocationDTO;
import com.ali.coffeehub.cafeteria.service.mapper.CoffeeShopLocationMapper;
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
 * Integration tests for the {@link CoffeeShopLocationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CoffeeShopLocationResourceIT {

    private static final Long DEFAULT_COFFEE_SHOP_ID = 1L;
    private static final Long UPDATED_COFFEE_SHOP_ID = 2L;

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_MAIN_BRANCH = false;
    private static final Boolean UPDATED_IS_MAIN_BRANCH = true;

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

    private static final String ENTITY_API_URL = "/api/coffee-shop-locations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CoffeeShopLocationRepository coffeeShopLocationRepository;

    @Autowired
    private CoffeeShopLocationMapper coffeeShopLocationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCoffeeShopLocationMockMvc;

    private CoffeeShopLocationEntity coffeeShopLocationEntity;

    private CoffeeShopLocationEntity insertedCoffeeShopLocationEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CoffeeShopLocationEntity createEntity() {
        return new CoffeeShopLocationEntity()
            .coffeeShopId(DEFAULT_COFFEE_SHOP_ID)
            .address(DEFAULT_ADDRESS)
            .isMainBranch(DEFAULT_IS_MAIN_BRANCH)
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
    public static CoffeeShopLocationEntity createUpdatedEntity() {
        return new CoffeeShopLocationEntity()
            .coffeeShopId(UPDATED_COFFEE_SHOP_ID)
            .address(UPDATED_ADDRESS)
            .isMainBranch(UPDATED_IS_MAIN_BRANCH)
            .deleted(UPDATED_DELETED)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);
    }

    @BeforeEach
    public void initTest() {
        coffeeShopLocationEntity = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCoffeeShopLocationEntity != null) {
            coffeeShopLocationRepository.delete(insertedCoffeeShopLocationEntity);
            insertedCoffeeShopLocationEntity = null;
        }
    }

    @Test
    @Transactional
    void createCoffeeShopLocation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CoffeeShopLocation
        CoffeeShopLocationDTO coffeeShopLocationDTO = coffeeShopLocationMapper.toDto(coffeeShopLocationEntity);
        var returnedCoffeeShopLocationDTO = om.readValue(
            restCoffeeShopLocationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coffeeShopLocationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CoffeeShopLocationDTO.class
        );

        // Validate the CoffeeShopLocation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCoffeeShopLocationEntity = coffeeShopLocationMapper.toEntity(returnedCoffeeShopLocationDTO);
        assertCoffeeShopLocationEntityUpdatableFieldsEquals(
            returnedCoffeeShopLocationEntity,
            getPersistedCoffeeShopLocationEntity(returnedCoffeeShopLocationEntity)
        );

        insertedCoffeeShopLocationEntity = returnedCoffeeShopLocationEntity;
    }

    @Test
    @Transactional
    void createCoffeeShopLocationWithExistingId() throws Exception {
        // Create the CoffeeShopLocation with an existing ID
        coffeeShopLocationEntity.setId(1L);
        CoffeeShopLocationDTO coffeeShopLocationDTO = coffeeShopLocationMapper.toDto(coffeeShopLocationEntity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCoffeeShopLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coffeeShopLocationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CoffeeShopLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCoffeeShopIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        coffeeShopLocationEntity.setCoffeeShopId(null);

        // Create the CoffeeShopLocation, which fails.
        CoffeeShopLocationDTO coffeeShopLocationDTO = coffeeShopLocationMapper.toDto(coffeeShopLocationEntity);

        restCoffeeShopLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coffeeShopLocationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        coffeeShopLocationEntity.setAddress(null);

        // Create the CoffeeShopLocation, which fails.
        CoffeeShopLocationDTO coffeeShopLocationDTO = coffeeShopLocationMapper.toDto(coffeeShopLocationEntity);

        restCoffeeShopLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coffeeShopLocationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        coffeeShopLocationEntity.setCreatedAt(null);

        // Create the CoffeeShopLocation, which fails.
        CoffeeShopLocationDTO coffeeShopLocationDTO = coffeeShopLocationMapper.toDto(coffeeShopLocationEntity);

        restCoffeeShopLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coffeeShopLocationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCoffeeShopLocations() throws Exception {
        // Initialize the database
        insertedCoffeeShopLocationEntity = coffeeShopLocationRepository.saveAndFlush(coffeeShopLocationEntity);

        // Get all the coffeeShopLocationList
        restCoffeeShopLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coffeeShopLocationEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].coffeeShopId").value(hasItem(DEFAULT_COFFEE_SHOP_ID.intValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].isMainBranch").value(hasItem(DEFAULT_IS_MAIN_BRANCH)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getCoffeeShopLocation() throws Exception {
        // Initialize the database
        insertedCoffeeShopLocationEntity = coffeeShopLocationRepository.saveAndFlush(coffeeShopLocationEntity);

        // Get the coffeeShopLocation
        restCoffeeShopLocationMockMvc
            .perform(get(ENTITY_API_URL_ID, coffeeShopLocationEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(coffeeShopLocationEntity.getId().intValue()))
            .andExpect(jsonPath("$.coffeeShopId").value(DEFAULT_COFFEE_SHOP_ID.intValue()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.isMainBranch").value(DEFAULT_IS_MAIN_BRANCH))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingCoffeeShopLocation() throws Exception {
        // Get the coffeeShopLocation
        restCoffeeShopLocationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCoffeeShopLocation() throws Exception {
        // Initialize the database
        insertedCoffeeShopLocationEntity = coffeeShopLocationRepository.saveAndFlush(coffeeShopLocationEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the coffeeShopLocation
        CoffeeShopLocationEntity updatedCoffeeShopLocationEntity = coffeeShopLocationRepository
            .findById(coffeeShopLocationEntity.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedCoffeeShopLocationEntity are not directly saved in db
        em.detach(updatedCoffeeShopLocationEntity);
        updatedCoffeeShopLocationEntity
            .coffeeShopId(UPDATED_COFFEE_SHOP_ID)
            .address(UPDATED_ADDRESS)
            .isMainBranch(UPDATED_IS_MAIN_BRANCH)
            .deleted(UPDATED_DELETED)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);
        CoffeeShopLocationDTO coffeeShopLocationDTO = coffeeShopLocationMapper.toDto(updatedCoffeeShopLocationEntity);

        restCoffeeShopLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, coffeeShopLocationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(coffeeShopLocationDTO))
            )
            .andExpect(status().isOk());

        // Validate the CoffeeShopLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCoffeeShopLocationEntityToMatchAllProperties(updatedCoffeeShopLocationEntity);
    }

    @Test
    @Transactional
    void putNonExistingCoffeeShopLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coffeeShopLocationEntity.setId(longCount.incrementAndGet());

        // Create the CoffeeShopLocation
        CoffeeShopLocationDTO coffeeShopLocationDTO = coffeeShopLocationMapper.toDto(coffeeShopLocationEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoffeeShopLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, coffeeShopLocationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(coffeeShopLocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoffeeShopLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCoffeeShopLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coffeeShopLocationEntity.setId(longCount.incrementAndGet());

        // Create the CoffeeShopLocation
        CoffeeShopLocationDTO coffeeShopLocationDTO = coffeeShopLocationMapper.toDto(coffeeShopLocationEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoffeeShopLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(coffeeShopLocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoffeeShopLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCoffeeShopLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coffeeShopLocationEntity.setId(longCount.incrementAndGet());

        // Create the CoffeeShopLocation
        CoffeeShopLocationDTO coffeeShopLocationDTO = coffeeShopLocationMapper.toDto(coffeeShopLocationEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoffeeShopLocationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coffeeShopLocationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CoffeeShopLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCoffeeShopLocationWithPatch() throws Exception {
        // Initialize the database
        insertedCoffeeShopLocationEntity = coffeeShopLocationRepository.saveAndFlush(coffeeShopLocationEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the coffeeShopLocation using partial update
        CoffeeShopLocationEntity partialUpdatedCoffeeShopLocationEntity = new CoffeeShopLocationEntity();
        partialUpdatedCoffeeShopLocationEntity.setId(coffeeShopLocationEntity.getId());

        partialUpdatedCoffeeShopLocationEntity.address(UPDATED_ADDRESS).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restCoffeeShopLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCoffeeShopLocationEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCoffeeShopLocationEntity))
            )
            .andExpect(status().isOk());

        // Validate the CoffeeShopLocation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCoffeeShopLocationEntityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCoffeeShopLocationEntity, coffeeShopLocationEntity),
            getPersistedCoffeeShopLocationEntity(coffeeShopLocationEntity)
        );
    }

    @Test
    @Transactional
    void fullUpdateCoffeeShopLocationWithPatch() throws Exception {
        // Initialize the database
        insertedCoffeeShopLocationEntity = coffeeShopLocationRepository.saveAndFlush(coffeeShopLocationEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the coffeeShopLocation using partial update
        CoffeeShopLocationEntity partialUpdatedCoffeeShopLocationEntity = new CoffeeShopLocationEntity();
        partialUpdatedCoffeeShopLocationEntity.setId(coffeeShopLocationEntity.getId());

        partialUpdatedCoffeeShopLocationEntity
            .coffeeShopId(UPDATED_COFFEE_SHOP_ID)
            .address(UPDATED_ADDRESS)
            .isMainBranch(UPDATED_IS_MAIN_BRANCH)
            .deleted(UPDATED_DELETED)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);

        restCoffeeShopLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCoffeeShopLocationEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCoffeeShopLocationEntity))
            )
            .andExpect(status().isOk());

        // Validate the CoffeeShopLocation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCoffeeShopLocationEntityUpdatableFieldsEquals(
            partialUpdatedCoffeeShopLocationEntity,
            getPersistedCoffeeShopLocationEntity(partialUpdatedCoffeeShopLocationEntity)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCoffeeShopLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coffeeShopLocationEntity.setId(longCount.incrementAndGet());

        // Create the CoffeeShopLocation
        CoffeeShopLocationDTO coffeeShopLocationDTO = coffeeShopLocationMapper.toDto(coffeeShopLocationEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoffeeShopLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, coffeeShopLocationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(coffeeShopLocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoffeeShopLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCoffeeShopLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coffeeShopLocationEntity.setId(longCount.incrementAndGet());

        // Create the CoffeeShopLocation
        CoffeeShopLocationDTO coffeeShopLocationDTO = coffeeShopLocationMapper.toDto(coffeeShopLocationEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoffeeShopLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(coffeeShopLocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoffeeShopLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCoffeeShopLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coffeeShopLocationEntity.setId(longCount.incrementAndGet());

        // Create the CoffeeShopLocation
        CoffeeShopLocationDTO coffeeShopLocationDTO = coffeeShopLocationMapper.toDto(coffeeShopLocationEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoffeeShopLocationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(coffeeShopLocationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CoffeeShopLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCoffeeShopLocation() throws Exception {
        // Initialize the database
        insertedCoffeeShopLocationEntity = coffeeShopLocationRepository.saveAndFlush(coffeeShopLocationEntity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the coffeeShopLocation
        restCoffeeShopLocationMockMvc
            .perform(delete(ENTITY_API_URL_ID, coffeeShopLocationEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return coffeeShopLocationRepository.count();
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

    protected CoffeeShopLocationEntity getPersistedCoffeeShopLocationEntity(CoffeeShopLocationEntity coffeeShopLocation) {
        return coffeeShopLocationRepository.findById(coffeeShopLocation.getId()).orElseThrow();
    }

    protected void assertPersistedCoffeeShopLocationEntityToMatchAllProperties(CoffeeShopLocationEntity expectedCoffeeShopLocationEntity) {
        assertCoffeeShopLocationEntityAllPropertiesEquals(
            expectedCoffeeShopLocationEntity,
            getPersistedCoffeeShopLocationEntity(expectedCoffeeShopLocationEntity)
        );
    }

    protected void assertPersistedCoffeeShopLocationEntityToMatchUpdatableProperties(
        CoffeeShopLocationEntity expectedCoffeeShopLocationEntity
    ) {
        assertCoffeeShopLocationEntityAllUpdatablePropertiesEquals(
            expectedCoffeeShopLocationEntity,
            getPersistedCoffeeShopLocationEntity(expectedCoffeeShopLocationEntity)
        );
    }
}
