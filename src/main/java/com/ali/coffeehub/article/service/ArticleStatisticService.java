package com.ali.coffeehub.article.service;

import com.ali.coffeehub.article.domain.ArticleStatisticEntity;
import com.ali.coffeehub.article.repository.ArticleStatisticRepository;
import com.ali.coffeehub.article.service.dto.ArticleStatisticDTO;
import com.ali.coffeehub.article.service.mapper.ArticleStatisticMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ali.coffeehub.article.domain.ArticleStatisticEntity}.
 */
@Service
@Transactional
public class ArticleStatisticService {

    private static final Logger LOG = LoggerFactory.getLogger(ArticleStatisticService.class);

    private final ArticleStatisticRepository articleStatisticRepository;

    private final ArticleStatisticMapper articleStatisticMapper;

    public ArticleStatisticService(ArticleStatisticRepository articleStatisticRepository, ArticleStatisticMapper articleStatisticMapper) {
        this.articleStatisticRepository = articleStatisticRepository;
        this.articleStatisticMapper = articleStatisticMapper;
    }

    /**
     * Save a articleStatistic.
     *
     * @param articleStatisticDTO the entity to save.
     * @return the persisted entity.
     */
    public ArticleStatisticDTO save(ArticleStatisticDTO articleStatisticDTO) {
        LOG.debug("Request to save ArticleStatistic : {}", articleStatisticDTO);
        ArticleStatisticEntity articleStatisticEntity = articleStatisticMapper.toEntity(articleStatisticDTO);
        articleStatisticEntity = articleStatisticRepository.save(articleStatisticEntity);
        return articleStatisticMapper.toDto(articleStatisticEntity);
    }

    /**
     * Update a articleStatistic.
     *
     * @param articleStatisticDTO the entity to save.
     * @return the persisted entity.
     */
    public ArticleStatisticDTO update(ArticleStatisticDTO articleStatisticDTO) {
        LOG.debug("Request to update ArticleStatistic : {}", articleStatisticDTO);
        ArticleStatisticEntity articleStatisticEntity = articleStatisticMapper.toEntity(articleStatisticDTO);
        articleStatisticEntity = articleStatisticRepository.save(articleStatisticEntity);
        return articleStatisticMapper.toDto(articleStatisticEntity);
    }

    /**
     * Partially update a articleStatistic.
     *
     * @param articleStatisticDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ArticleStatisticDTO> partialUpdate(ArticleStatisticDTO articleStatisticDTO) {
        LOG.debug("Request to partially update ArticleStatistic : {}", articleStatisticDTO);

        return articleStatisticRepository
            .findById(articleStatisticDTO.getId())
            .map(existingArticleStatistic -> {
                articleStatisticMapper.partialUpdate(existingArticleStatistic, articleStatisticDTO);

                return existingArticleStatistic;
            })
            .map(articleStatisticRepository::save)
            .map(articleStatisticMapper::toDto);
    }

    /**
     * Get all the articleStatistics.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ArticleStatisticDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ArticleStatistics");
        return articleStatisticRepository.findAll(pageable).map(articleStatisticMapper::toDto);
    }

    /**
     * Get one articleStatistic by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ArticleStatisticDTO> findOne(Long id) {
        LOG.debug("Request to get ArticleStatistic : {}", id);
        return articleStatisticRepository.findById(id).map(articleStatisticMapper::toDto);
    }

    /**
     * Delete the articleStatistic by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ArticleStatistic : {}", id);
        articleStatisticRepository.deleteById(id);
    }
}
