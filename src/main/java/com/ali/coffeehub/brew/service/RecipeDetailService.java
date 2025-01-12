package com.ali.coffeehub.brew.service;

import com.ali.coffeehub.brew.domain.RecipeDetailEntity;
import com.ali.coffeehub.brew.repository.RecipeDetailRepository;
import com.ali.coffeehub.brew.service.dto.RecipeDetailDTO;
import com.ali.coffeehub.brew.service.mapper.RecipeDetailMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ali.coffeehub.brew.domain.RecipeDetailEntity}.
 */
@Service
@Transactional
public class RecipeDetailService {

    private static final Logger LOG = LoggerFactory.getLogger(RecipeDetailService.class);

    private final RecipeDetailRepository recipeDetailRepository;

    private final RecipeDetailMapper recipeDetailMapper;

    public RecipeDetailService(RecipeDetailRepository recipeDetailRepository, RecipeDetailMapper recipeDetailMapper) {
        this.recipeDetailRepository = recipeDetailRepository;
        this.recipeDetailMapper = recipeDetailMapper;
    }

    /**
     * Save a recipeDetail.
     *
     * @param recipeDetailDTO the entity to save.
     * @return the persisted entity.
     */
    public RecipeDetailDTO save(RecipeDetailDTO recipeDetailDTO) {
        LOG.debug("Request to save RecipeDetail : {}", recipeDetailDTO);
        RecipeDetailEntity recipeDetailEntity = recipeDetailMapper.toEntity(recipeDetailDTO);
        recipeDetailEntity = recipeDetailRepository.save(recipeDetailEntity);
        return recipeDetailMapper.toDto(recipeDetailEntity);
    }

    /**
     * Update a recipeDetail.
     *
     * @param recipeDetailDTO the entity to save.
     * @return the persisted entity.
     */
    public RecipeDetailDTO update(RecipeDetailDTO recipeDetailDTO) {
        LOG.debug("Request to update RecipeDetail : {}", recipeDetailDTO);
        RecipeDetailEntity recipeDetailEntity = recipeDetailMapper.toEntity(recipeDetailDTO);
        recipeDetailEntity = recipeDetailRepository.save(recipeDetailEntity);
        return recipeDetailMapper.toDto(recipeDetailEntity);
    }

    /**
     * Partially update a recipeDetail.
     *
     * @param recipeDetailDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RecipeDetailDTO> partialUpdate(RecipeDetailDTO recipeDetailDTO) {
        LOG.debug("Request to partially update RecipeDetail : {}", recipeDetailDTO);

        return recipeDetailRepository
            .findById(recipeDetailDTO.getId())
            .map(existingRecipeDetail -> {
                recipeDetailMapper.partialUpdate(existingRecipeDetail, recipeDetailDTO);

                return existingRecipeDetail;
            })
            .map(recipeDetailRepository::save)
            .map(recipeDetailMapper::toDto);
    }

    /**
     * Get all the recipeDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RecipeDetailDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all RecipeDetails");
        return recipeDetailRepository.findAll(pageable).map(recipeDetailMapper::toDto);
    }

    /**
     * Get one recipeDetail by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RecipeDetailDTO> findOne(Long id) {
        LOG.debug("Request to get RecipeDetail : {}", id);
        return recipeDetailRepository.findById(id).map(recipeDetailMapper::toDto);
    }

    /**
     * Delete the recipeDetail by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete RecipeDetail : {}", id);
        recipeDetailRepository.deleteById(id);
    }
}
