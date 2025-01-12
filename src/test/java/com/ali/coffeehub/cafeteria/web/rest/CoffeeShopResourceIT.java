package com.ali.coffeehub.cafeteria.web.rest;

import static com.ali.coffeehub.cafeteria.domain.CoffeeShopEntityAsserts.*;
import static com.ali.coffeehub.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ali.coffeehub.IntegrationTest;
import com.ali.coffeehub.cafeteria.domain.CoffeeShopEntity;
import com.ali.coffeehub.cafeteria.repository.CoffeeShopRepository;
import com.ali.coffeehub.cafeteria.service.dto.CoffeeShopDTO;
import com.ali.coffeehub.cafeteria.service.mapper.CoffeeShopMapper;
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
 * Integration tests for the {@link CoffeeShopResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CoffeeShopResourceIT {

    private static final Long DEFAULT_CATEGORY_ID = 1L;
    private static final Long UPDATED_CATEGORY_ID = 2L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOGO_URI = "AAAAAAAAAA";
    private static final String UPDATED_LOGO_URI = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final Instant DEFAULT_AVAILABLE_HOURS_START = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_AVAILABLE_HOURS_START = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_AVAILABLE_HOURS_END = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_AVAILABLE_HOURS_END = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/coffee-shops";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CoffeeShopRepository coffeeShopRepository;

    @Autowired
    private CoffeeShopMapper coffeeShopMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCoffeeShopMockMvc;

    private CoffeeShopEntity coffeeShopEntity;

    private CoffeeShopEntity insertedCoffeeShopEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CoffeeShopEntity createEntity() {
        return new CoffeeShopEntity()
            .categoryId(DEFAULT_CATEGORY_ID)
            .name(DEFAULT_NAME)
            .logoUri(DEFAULT_LOGO_URI)
            .deleted(DEFAULT_DELETED)
            .phone(DEFAULT_PHONE)
            .availableHoursStart(DEFAULT_AVAILABLE_HOURS_START)
            .availableHoursEnd(DEFAULT_AVAILABLE_HOURS_END)
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
    public static CoffeeShopEntity createUpdatedEntity() {
        return new CoffeeShopEntity()
            .categoryId(UPDATED_CATEGORY_ID)
            .name(UPDATED_NAME)
            .logoUri(UPDATED_LOGO_URI)
            .deleted(UPDATED_DELETED)
            .phone(UPDATED_PHONE)
            .availableHoursStart(UPDATED_AVAILABLE_HOURS_START)
            .availableHoursEnd(UPDATED_AVAILABLE_HOURS_END)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);
    }

    @BeforeEach
    public void initTest() {
        coffeeShopEntity = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCoffeeShopEntity != null) {
            coffeeShopRepository.delete(insertedCoffeeShopEntity);
            insertedCoffeeShopEntity = null;
        }
    }

    @Test
    @Transactional
    void createCoffeeShop() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CoffeeShop
        CoffeeShopDTO coffeeShopDTO = coffeeShopMapper.toDto(coffeeShopEntity);
        var returnedCoffeeShopDTO = om.readValue(
            restCoffeeShopMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coffeeShopDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CoffeeShopDTO.class
        );

        // Validate the CoffeeShop in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCoffeeShopEntity = coffeeShopMapper.toEntity(returnedCoffeeShopDTO);
        assertCoffeeShopEntityUpdatableFieldsEquals(returnedCoffeeShopEntity, getPersistedCoffeeShopEntity(returnedCoffeeShopEntity));

        insertedCoffeeShopEntity = returnedCoffeeShopEntity;
    }

    @Test
    @Transactional
    void createCoffeeShopWithExistingId() throws Exception {
        // Create the CoffeeShop with an existing ID
        coffeeShopEntity.setId(1L);
        CoffeeShopDTO coffeeShopDTO = coffeeShopMapper.toDto(coffeeShopEntity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCoffeeShopMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coffeeShopDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CoffeeShop in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCategoryIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        coffeeShopEntity.setCategoryId(null);

        // Create the CoffeeShop, which fails.
        CoffeeShopDTO coffeeShopDTO = coffeeShopMapper.toDto(coffeeShopEntity);

        restCoffeeShopMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coffeeShopDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        coffeeShopEntity.setName(null);

        // Create the CoffeeShop, which fails.
        CoffeeShopDTO coffeeShopDTO = coffeeShopMapper.toDto(coffeeShopEntity);

        restCoffeeShopMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coffeeShopDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLogoUriIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        coffeeShopEntity.setLogoUri(null);

        // Create the CoffeeShop, which fails.
        CoffeeShopDTO coffeeShopDTO = coffeeShopMapper.toDto(coffeeShopEntity);

        restCoffeeShopMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coffeeShopDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        coffeeShopEntity.setCreatedAt(null);

        // Create the CoffeeShop, which fails.
        CoffeeShopDTO coffeeShopDTO = coffeeShopMapper.toDto(coffeeShopEntity);

        restCoffeeShopMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coffeeShopDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCoffeeShops() throws Exception {
        // Initialize the database
        insertedCoffeeShopEntity = coffeeShopRepository.saveAndFlush(coffeeShopEntity);

        // Get all the coffeeShopList
        restCoffeeShopMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coffeeShopEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryId").value(hasItem(DEFAULT_CATEGORY_ID.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].logoUri").value(hasItem(DEFAULT_LOGO_URI)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].availableHoursStart").value(hasItem(DEFAULT_AVAILABLE_HOURS_START.toString())))
            .andExpect(jsonPath("$.[*].availableHoursEnd").value(hasItem(DEFAULT_AVAILABLE_HOURS_END.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getCoffeeShop() throws Exception {
        // Initialize the database
        insertedCoffeeShopEntity = coffeeShopRepository.saveAndFlush(coffeeShopEntity);

        // Get the coffeeShop
        restCoffeeShopMockMvc
            .perform(get(ENTITY_API_URL_ID, coffeeShopEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(coffeeShopEntity.getId().intValue()))
            .andExpect(jsonPath("$.categoryId").value(DEFAULT_CATEGORY_ID.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.logoUri").value(DEFAULT_LOGO_URI))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.availableHoursStart").value(DEFAULT_AVAILABLE_HOURS_START.toString()))
            .andExpect(jsonPath("$.availableHoursEnd").value(DEFAULT_AVAILABLE_HOURS_END.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingCoffeeShop() throws Exception {
        // Get the coffeeShop
        restCoffeeShopMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCoffeeShop() throws Exception {
        // Initialize the database
        insertedCoffeeShopEntity = coffeeShopRepository.saveAndFlush(coffeeShopEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the coffeeShop
        CoffeeShopEntity updatedCoffeeShopEntity = coffeeShopRepository.findById(coffeeShopEntity.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCoffeeShopEntity are not directly saved in db
        em.detach(updatedCoffeeShopEntity);
        updatedCoffeeShopEntity
            .categoryId(UPDATED_CATEGORY_ID)
            .name(UPDATED_NAME)
            .logoUri(UPDATED_LOGO_URI)
            .deleted(UPDATED_DELETED)
            .phone(UPDATED_PHONE)
            .availableHoursStart(UPDATED_AVAILABLE_HOURS_START)
            .availableHoursEnd(UPDATED_AVAILABLE_HOURS_END)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);
        CoffeeShopDTO coffeeShopDTO = coffeeShopMapper.toDto(updatedCoffeeShopEntity);

        restCoffeeShopMockMvc
            .perform(
                put(ENTITY_API_URL_ID, coffeeShopDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(coffeeShopDTO))
            )
            .andExpect(status().isOk());

        // Validate the CoffeeShop in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCoffeeShopEntityToMatchAllProperties(updatedCoffeeShopEntity);
    }

    @Test
    @Transactional
    void putNonExistingCoffeeShop() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coffeeShopEntity.setId(longCount.incrementAndGet());

        // Create the CoffeeShop
        CoffeeShopDTO coffeeShopDTO = coffeeShopMapper.toDto(coffeeShopEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoffeeShopMockMvc
            .perform(
                put(ENTITY_API_URL_ID, coffeeShopDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(coffeeShopDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoffeeShop in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCoffeeShop() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coffeeShopEntity.setId(longCount.incrementAndGet());

        // Create the CoffeeShop
        CoffeeShopDTO coffeeShopDTO = coffeeShopMapper.toDto(coffeeShopEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoffeeShopMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(coffeeShopDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoffeeShop in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCoffeeShop() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coffeeShopEntity.setId(longCount.incrementAndGet());

        // Create the CoffeeShop
        CoffeeShopDTO coffeeShopDTO = coffeeShopMapper.toDto(coffeeShopEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoffeeShopMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coffeeShopDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CoffeeShop in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCoffeeShopWithPatch() throws Exception {
        // Initialize the database
        insertedCoffeeShopEntity = coffeeShopRepository.saveAndFlush(coffeeShopEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the coffeeShop using partial update
        CoffeeShopEntity partialUpdatedCoffeeShopEntity = new CoffeeShopEntity();
        partialUpdatedCoffeeShopEntity.setId(coffeeShopEntity.getId());

        partialUpdatedCoffeeShopEntity
            .categoryId(UPDATED_CATEGORY_ID)
            .deleted(UPDATED_DELETED)
            .availableHoursStart(UPDATED_AVAILABLE_HOURS_START)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restCoffeeShopMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCoffeeShopEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCoffeeShopEntity))
            )
            .andExpect(status().isOk());

        // Validate the CoffeeShop in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCoffeeShopEntityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCoffeeShopEntity, coffeeShopEntity),
            getPersistedCoffeeShopEntity(coffeeShopEntity)
        );
    }

    @Test
    @Transactional
    void fullUpdateCoffeeShopWithPatch() throws Exception {
        // Initialize the database
        insertedCoffeeShopEntity = coffeeShopRepository.saveAndFlush(coffeeShopEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the coffeeShop using partial update
        CoffeeShopEntity partialUpdatedCoffeeShopEntity = new CoffeeShopEntity();
        partialUpdatedCoffeeShopEntity.setId(coffeeShopEntity.getId());

        partialUpdatedCoffeeShopEntity
            .categoryId(UPDATED_CATEGORY_ID)
            .name(UPDATED_NAME)
            .logoUri(UPDATED_LOGO_URI)
            .deleted(UPDATED_DELETED)
            .phone(UPDATED_PHONE)
            .availableHoursStart(UPDATED_AVAILABLE_HOURS_START)
            .availableHoursEnd(UPDATED_AVAILABLE_HOURS_END)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);

        restCoffeeShopMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCoffeeShopEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCoffeeShopEntity))
            )
            .andExpect(status().isOk());

        // Validate the CoffeeShop in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCoffeeShopEntityUpdatableFieldsEquals(
            partialUpdatedCoffeeShopEntity,
            getPersistedCoffeeShopEntity(partialUpdatedCoffeeShopEntity)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCoffeeShop() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coffeeShopEntity.setId(longCount.incrementAndGet());

        // Create the CoffeeShop
        CoffeeShopDTO coffeeShopDTO = coffeeShopMapper.toDto(coffeeShopEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoffeeShopMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, coffeeShopDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(coffeeShopDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoffeeShop in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCoffeeShop() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coffeeShopEntity.setId(longCount.incrementAndGet());

        // Create the CoffeeShop
        CoffeeShopDTO coffeeShopDTO = coffeeShopMapper.toDto(coffeeShopEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoffeeShopMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(coffeeShopDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoffeeShop in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCoffeeShop() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coffeeShopEntity.setId(longCount.incrementAndGet());

        // Create the CoffeeShop
        CoffeeShopDTO coffeeShopDTO = coffeeShopMapper.toDto(coffeeShopEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoffeeShopMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(coffeeShopDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CoffeeShop in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCoffeeShop() throws Exception {
        // Initialize the database
        insertedCoffeeShopEntity = coffeeShopRepository.saveAndFlush(coffeeShopEntity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the coffeeShop
        restCoffeeShopMockMvc
            .perform(delete(ENTITY_API_URL_ID, coffeeShopEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return coffeeShopRepository.count();
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

    protected CoffeeShopEntity getPersistedCoffeeShopEntity(CoffeeShopEntity coffeeShop) {
        return coffeeShopRepository.findById(coffeeShop.getId()).orElseThrow();
    }

    protected void assertPersistedCoffeeShopEntityToMatchAllProperties(CoffeeShopEntity expectedCoffeeShopEntity) {
        assertCoffeeShopEntityAllPropertiesEquals(expectedCoffeeShopEntity, getPersistedCoffeeShopEntity(expectedCoffeeShopEntity));
    }

    protected void assertPersistedCoffeeShopEntityToMatchUpdatableProperties(CoffeeShopEntity expectedCoffeeShopEntity) {
        assertCoffeeShopEntityAllUpdatablePropertiesEquals(
            expectedCoffeeShopEntity,
            getPersistedCoffeeShopEntity(expectedCoffeeShopEntity)
        );
    }
}
