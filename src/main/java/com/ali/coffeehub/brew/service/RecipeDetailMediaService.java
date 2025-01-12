package com.ali.coffeehub.brew.service;

import com.ali.coffeehub.brew.domain.RecipeDetailMediaEntity;
import com.ali.coffeehub.brew.repository.RecipeDetailMediaRepository;
import com.ali.coffeehub.brew.service.dto.RecipeDetailMediaDTO;
import com.ali.coffeehub.brew.service.mapper.RecipeDetailMediaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ali.coffeehub.brew.domain.RecipeDetailMediaEntity}.
 */
@Service
@Transactional
public class RecipeDetailMediaService {

    private static final Logger LOG = LoggerFactory.getLogger(RecipeDetailMediaService.class);

    private final RecipeDetailMediaRepository recipeDetailMediaRepository;

    private final RecipeDetailMediaMapper recipeDetailMediaMapper;

    public RecipeDetailMediaService(
        RecipeDetailMediaRepository recipeDetailMediaRepository,
        RecipeDetailMediaMapper recipeDetailMediaMapper
    ) {
        this.recipeDetailMediaRepository = recipeDetailMediaRepository;
        this.recipeDetailMediaMapper = recipeDetailMediaMapper;
    }

    /**
     * Save a recipeDetailMedia.
     *
     * @param recipeDetailMediaDTO the entity to save.
     * @return the persisted entity.
     */
    public RecipeDetailMediaDTO save(RecipeDetailMediaDTO recipeDetailMediaDTO) {
        LOG.debug("Request to save RecipeDetailMedia : {}", recipeDetailMediaDTO);
        RecipeDetailMediaEntity recipeDetailMediaEntity = recipeDetailMediaMapper.toEntity(recipeDetailMediaDTO);
        recipeDetailMediaEntity = recipeDetailMediaRepository.save(recipeDetailMediaEntity);
        return recipeDetailMediaMapper.toDto(recipeDetailMediaEntity);
    }

    /**
     * Update a recipeDetailMedia.
     *
     * @param recipeDetailMediaDTO the entity to save.
     * @return the persisted entity.
     */
    public RecipeDetailMediaDTO update(RecipeDetailMediaDTO recipeDetailMediaDTO) {
        LOG.debug("Request to update RecipeDetailMedia : {}", recipeDetailMediaDTO);
        RecipeDetailMediaEntity recipeDetailMediaEntity = recipeDetailMediaMapper.toEntity(recipeDetailMediaDTO);
        recipeDetailMediaEntity = recipeDetailMediaRepository.save(recipeDetailMediaEntity);
        return recipeDetailMediaMapper.toDto(recipeDetailMediaEntity);
    }

    /**
     * Partially update a recipeDetailMedia.
     *
     * @param recipeDetailMediaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RecipeDetailMediaDTO> partialUpdate(RecipeDetailMediaDTO recipeDetailMediaDTO) {
        LOG.debug("Request to partially update RecipeDetailMedia : {}", recipeDetailMediaDTO);

        return recipeDetailMediaRepository
            .findById(recipeDetailMediaDTO.getId())
            .map(existingRecipeDetailMedia -> {
                recipeDetailMediaMapper.partialUpdate(existingRecipeDetailMedia, recipeDetailMediaDTO);

                return existingRecipeDetailMedia;
            })
            .map(recipeDetailMediaRepository::save)
            .map(recipeDetailMediaMapper::toDto);
    }

    /**
     * Get all the recipeDetailMedias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RecipeDetailMediaDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all RecipeDetailMedias");
        return recipeDetailMediaRepository.findAll(pageable).map(recipeDetailMediaMapper::toDto);
    }

    /**
     * Get one recipeDetailMedia by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RecipeDetailMediaDTO> findOne(Long id) {
        LOG.debug("Request to get RecipeDetailMedia : {}", id);
        return recipeDetailMediaRepository.findById(id).map(recipeDetailMediaMapper::toDto);
    }

    /**
     * Delete the recipeDetailMedia by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete RecipeDetailMedia : {}", id);
        recipeDetailMediaRepository.deleteById(id);
    }
}
