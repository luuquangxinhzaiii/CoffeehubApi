package com.ali.coffeehub.article.service;

import com.ali.coffeehub.article.domain.ArticlesEntity;
import com.ali.coffeehub.article.repository.ArticlesRepository;
import com.ali.coffeehub.article.service.dto.ArticlesDTO;
import com.ali.coffeehub.article.service.mapper.ArticlesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ali.coffeehub.article.domain.ArticlesEntity}.
 */
@Service
@Transactional
public class ArticlesService {

    private static final Logger LOG = LoggerFactory.getLogger(ArticlesService.class);

    private final ArticlesRepository articlesRepository;

    private final ArticlesMapper articlesMapper;

    public ArticlesService(ArticlesRepository articlesRepository, ArticlesMapper articlesMapper) {
        this.articlesRepository = articlesRepository;
        this.articlesMapper = articlesMapper;
    }

    /**
     * Save a articles.
     *
     * @param articlesDTO the entity to save.
     * @return the persisted entity.
     */
    public ArticlesDTO save(ArticlesDTO articlesDTO) {
        LOG.debug("Request to save Articles : {}", articlesDTO);
        ArticlesEntity articlesEntity = articlesMapper.toEntity(articlesDTO);
        articlesEntity = articlesRepository.save(articlesEntity);
        return articlesMapper.toDto(articlesEntity);
    }

    /**
     * Update a articles.
     *
     * @param articlesDTO the entity to save.
     * @return the persisted entity.
     */
    public ArticlesDTO update(ArticlesDTO articlesDTO) {
        LOG.debug("Request to update Articles : {}", articlesDTO);
        ArticlesEntity articlesEntity = articlesMapper.toEntity(articlesDTO);
        articlesEntity = articlesRepository.save(articlesEntity);
        return articlesMapper.toDto(articlesEntity);
    }

    /**
     * Partially update a articles.
     *
     * @param articlesDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ArticlesDTO> partialUpdate(ArticlesDTO articlesDTO) {
        LOG.debug("Request to partially update Articles : {}", articlesDTO);

        return articlesRepository
            .findById(articlesDTO.getId())
            .map(existingArticles -> {
                articlesMapper.partialUpdate(existingArticles, articlesDTO);

                return existingArticles;
            })
            .map(articlesRepository::save)
            .map(articlesMapper::toDto);
    }

    /**
     * Get all the articles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ArticlesDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Articles");
        return articlesRepository.findAll(pageable).map(articlesMapper::toDto);
    }

    /**
     * Get one articles by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ArticlesDTO> findOne(Long id) {
        LOG.debug("Request to get Articles : {}", id);
        return articlesRepository.findById(id).map(articlesMapper::toDto);
    }

    /**
     * Delete the articles by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Articles : {}", id);
        articlesRepository.deleteById(id);
    }
}
