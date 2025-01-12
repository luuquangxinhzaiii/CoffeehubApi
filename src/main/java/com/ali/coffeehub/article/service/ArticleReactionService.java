package com.ali.coffeehub.article.service;

import com.ali.coffeehub.article.domain.ArticleReactionEntity;
import com.ali.coffeehub.article.repository.ArticleReactionRepository;
import com.ali.coffeehub.article.service.dto.ArticleReactionDTO;
import com.ali.coffeehub.article.service.mapper.ArticleReactionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ali.coffeehub.article.domain.ArticleReactionEntity}.
 */
@Service
@Transactional
public class ArticleReactionService {

    private static final Logger LOG = LoggerFactory.getLogger(ArticleReactionService.class);

    private final ArticleReactionRepository articleReactionRepository;

    private final ArticleReactionMapper articleReactionMapper;

    public ArticleReactionService(ArticleReactionRepository articleReactionRepository, ArticleReactionMapper articleReactionMapper) {
        this.articleReactionRepository = articleReactionRepository;
        this.articleReactionMapper = articleReactionMapper;
    }

    /**
     * Save a articleReaction.
     *
     * @param articleReactionDTO the entity to save.
     * @return the persisted entity.
     */
    public ArticleReactionDTO save(ArticleReactionDTO articleReactionDTO) {
        LOG.debug("Request to save ArticleReaction : {}", articleReactionDTO);
        ArticleReactionEntity articleReactionEntity = articleReactionMapper.toEntity(articleReactionDTO);
        articleReactionEntity = articleReactionRepository.save(articleReactionEntity);
        return articleReactionMapper.toDto(articleReactionEntity);
    }

    /**
     * Update a articleReaction.
     *
     * @param articleReactionDTO the entity to save.
     * @return the persisted entity.
     */
    public ArticleReactionDTO update(ArticleReactionDTO articleReactionDTO) {
        LOG.debug("Request to update ArticleReaction : {}", articleReactionDTO);
        ArticleReactionEntity articleReactionEntity = articleReactionMapper.toEntity(articleReactionDTO);
        articleReactionEntity = articleReactionRepository.save(articleReactionEntity);
        return articleReactionMapper.toDto(articleReactionEntity);
    }

    /**
     * Partially update a articleReaction.
     *
     * @param articleReactionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ArticleReactionDTO> partialUpdate(ArticleReactionDTO articleReactionDTO) {
        LOG.debug("Request to partially update ArticleReaction : {}", articleReactionDTO);

        return articleReactionRepository
            .findById(articleReactionDTO.getId())
            .map(existingArticleReaction -> {
                articleReactionMapper.partialUpdate(existingArticleReaction, articleReactionDTO);

                return existingArticleReaction;
            })
            .map(articleReactionRepository::save)
            .map(articleReactionMapper::toDto);
    }

    /**
     * Get all the articleReactions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ArticleReactionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ArticleReactions");
        return articleReactionRepository.findAll(pageable).map(articleReactionMapper::toDto);
    }

    /**
     * Get one articleReaction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ArticleReactionDTO> findOne(Long id) {
        LOG.debug("Request to get ArticleReaction : {}", id);
        return articleReactionRepository.findById(id).map(articleReactionMapper::toDto);
    }

    /**
     * Delete the articleReaction by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ArticleReaction : {}", id);
        articleReactionRepository.deleteById(id);
    }
}
