package com.ali.coffeehub.article.service;

import com.ali.coffeehub.article.domain.ArticleBodyEntity;
import com.ali.coffeehub.article.repository.ArticleBodyRepository;
import com.ali.coffeehub.article.service.dto.ArticleBodyDTO;
import com.ali.coffeehub.article.service.mapper.ArticleBodyMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ali.coffeehub.article.domain.ArticleBodyEntity}.
 */
@Service
@Transactional
public class ArticleBodyService {

    private static final Logger LOG = LoggerFactory.getLogger(ArticleBodyService.class);

    private final ArticleBodyRepository articleBodyRepository;

    private final ArticleBodyMapper articleBodyMapper;

    public ArticleBodyService(ArticleBodyRepository articleBodyRepository, ArticleBodyMapper articleBodyMapper) {
        this.articleBodyRepository = articleBodyRepository;
        this.articleBodyMapper = articleBodyMapper;
    }

    /**
     * Save a articleBody.
     *
     * @param articleBodyDTO the entity to save.
     * @return the persisted entity.
     */
    public ArticleBodyDTO save(ArticleBodyDTO articleBodyDTO) {
        LOG.debug("Request to save ArticleBody : {}", articleBodyDTO);
        ArticleBodyEntity articleBodyEntity = articleBodyMapper.toEntity(articleBodyDTO);
        articleBodyEntity = articleBodyRepository.save(articleBodyEntity);
        return articleBodyMapper.toDto(articleBodyEntity);
    }

    /**
     * Update a articleBody.
     *
     * @param articleBodyDTO the entity to save.
     * @return the persisted entity.
     */
    public ArticleBodyDTO update(ArticleBodyDTO articleBodyDTO) {
        LOG.debug("Request to update ArticleBody : {}", articleBodyDTO);
        ArticleBodyEntity articleBodyEntity = articleBodyMapper.toEntity(articleBodyDTO);
        articleBodyEntity = articleBodyRepository.save(articleBodyEntity);
        return articleBodyMapper.toDto(articleBodyEntity);
    }

    /**
     * Partially update a articleBody.
     *
     * @param articleBodyDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ArticleBodyDTO> partialUpdate(ArticleBodyDTO articleBodyDTO) {
        LOG.debug("Request to partially update ArticleBody : {}", articleBodyDTO);

        return articleBodyRepository
            .findById(articleBodyDTO.getId())
            .map(existingArticleBody -> {
                articleBodyMapper.partialUpdate(existingArticleBody, articleBodyDTO);

                return existingArticleBody;
            })
            .map(articleBodyRepository::save)
            .map(articleBodyMapper::toDto);
    }

    /**
     * Get all the articleBodies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ArticleBodyDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ArticleBodies");
        return articleBodyRepository.findAll(pageable).map(articleBodyMapper::toDto);
    }

    /**
     * Get one articleBody by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ArticleBodyDTO> findOne(Long id) {
        LOG.debug("Request to get ArticleBody : {}", id);
        return articleBodyRepository.findById(id).map(articleBodyMapper::toDto);
    }

    /**
     * Delete the articleBody by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ArticleBody : {}", id);
        articleBodyRepository.deleteById(id);
    }
}
