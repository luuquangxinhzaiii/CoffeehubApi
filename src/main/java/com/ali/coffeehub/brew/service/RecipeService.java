package com.ali.coffeehub.brew.service;

import com.ali.coffeehub.brew.domain.RecipeEntity;
import com.ali.coffeehub.brew.repository.RecipeRepository;
import com.ali.coffeehub.brew.service.dto.RecipeDTO;
import com.ali.coffeehub.brew.service.mapper.RecipeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ali.coffeehub.brew.domain.RecipeEntity}.
 */
@Service
@Transactional
public class RecipeService {

    private static final Logger LOG = LoggerFactory.getLogger(RecipeService.class);

    private final RecipeRepository recipeRepository;

    private final RecipeMapper recipeMapper;

    public RecipeService(RecipeRepository recipeRepository, RecipeMapper recipeMapper) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
    }

    /**
     * Save a recipe.
     *
     * @param recipeDTO the entity to save.
     * @return the persisted entity.
     */
    public RecipeDTO save(RecipeDTO recipeDTO) {
        LOG.debug("Request to save Recipe : {}", recipeDTO);
        RecipeEntity recipeEntity = recipeMapper.toEntity(recipeDTO);
        recipeEntity = recipeRepository.save(recipeEntity);
        return recipeMapper.toDto(recipeEntity);
    }

    /**
     * Update a recipe.
     *
     * @param recipeDTO the entity to save.
     * @return the persisted entity.
     */
    public RecipeDTO update(RecipeDTO recipeDTO) {
        LOG.debug("Request to update Recipe : {}", recipeDTO);
        RecipeEntity recipeEntity = recipeMapper.toEntity(recipeDTO);
        recipeEntity = recipeRepository.save(recipeEntity);
        return recipeMapper.toDto(recipeEntity);
    }

    /**
     * Partially update a recipe.
     *
     * @param recipeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RecipeDTO> partialUpdate(RecipeDTO recipeDTO) {
        LOG.debug("Request to partially update Recipe : {}", recipeDTO);

        return recipeRepository
            .findById(recipeDTO.getId())
            .map(existingRecipe -> {
                recipeMapper.partialUpdate(existingRecipe, recipeDTO);

                return existingRecipe;
            })
            .map(recipeRepository::save)
            .map(recipeMapper::toDto);
    }

    /**
     * Get all the recipes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RecipeDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Recipes");
        return recipeRepository.findAll(pageable).map(recipeMapper::toDto);
    }

    /**
     * Get one recipe by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RecipeDTO> findOne(Long id) {
        LOG.debug("Request to get Recipe : {}", id);
        return recipeRepository.findById(id).map(recipeMapper::toDto);
    }

    /**
     * Delete the recipe by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Recipe : {}", id);
        recipeRepository.deleteById(id);
    }
}
