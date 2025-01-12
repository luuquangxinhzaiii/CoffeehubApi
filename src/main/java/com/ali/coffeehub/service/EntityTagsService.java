package com.ali.coffeehub.service;

import com.ali.coffeehub.domain.EntityTagsEntity;
import com.ali.coffeehub.repository.EntityTagsRepository;
import com.ali.coffeehub.service.dto.EntityTagsDTO;
import com.ali.coffeehub.service.mapper.EntityTagsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ali.coffeehub.domain.EntityTagsEntity}.
 */
@Service
@Transactional
public class EntityTagsService {

    private static final Logger LOG = LoggerFactory.getLogger(EntityTagsService.class);

    private final EntityTagsRepository entityTagsRepository;

    private final EntityTagsMapper entityTagsMapper;

    public EntityTagsService(EntityTagsRepository entityTagsRepository, EntityTagsMapper entityTagsMapper) {
        this.entityTagsRepository = entityTagsRepository;
        this.entityTagsMapper = entityTagsMapper;
    }

    /**
     * Save a entityTags.
     *
     * @param entityTagsDTO the entity to save.
     * @return the persisted entity.
     */
    public EntityTagsDTO save(EntityTagsDTO entityTagsDTO) {
        LOG.debug("Request to save EntityTags : {}", entityTagsDTO);
        EntityTagsEntity entityTagsEntity = entityTagsMapper.toEntity(entityTagsDTO);
        entityTagsEntity = entityTagsRepository.save(entityTagsEntity);
        return entityTagsMapper.toDto(entityTagsEntity);
    }

    /**
     * Update a entityTags.
     *
     * @param entityTagsDTO the entity to save.
     * @return the persisted entity.
     */
    public EntityTagsDTO update(EntityTagsDTO entityTagsDTO) {
        LOG.debug("Request to update EntityTags : {}", entityTagsDTO);
        EntityTagsEntity entityTagsEntity = entityTagsMapper.toEntity(entityTagsDTO);
        entityTagsEntity = entityTagsRepository.save(entityTagsEntity);
        return entityTagsMapper.toDto(entityTagsEntity);
    }

    /**
     * Partially update a entityTags.
     *
     * @param entityTagsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EntityTagsDTO> partialUpdate(EntityTagsDTO entityTagsDTO) {
        LOG.debug("Request to partially update EntityTags : {}", entityTagsDTO);

        return entityTagsRepository
            .findById(entityTagsDTO.getId())
            .map(existingEntityTags -> {
                entityTagsMapper.partialUpdate(existingEntityTags, entityTagsDTO);

                return existingEntityTags;
            })
            .map(entityTagsRepository::save)
            .map(entityTagsMapper::toDto);
    }

    /**
     * Get all the entityTags.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EntityTagsDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all EntityTags");
        return entityTagsRepository.findAll(pageable).map(entityTagsMapper::toDto);
    }

    /**
     * Get one entityTags by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EntityTagsDTO> findOne(Long id) {
        LOG.debug("Request to get EntityTags : {}", id);
        return entityTagsRepository.findById(id).map(entityTagsMapper::toDto);
    }

    /**
     * Delete the entityTags by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete EntityTags : {}", id);
        entityTagsRepository.deleteById(id);
    }
}
