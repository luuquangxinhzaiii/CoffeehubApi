package com.ali.coffeehub.article.service;

import com.ali.coffeehub.article.domain.ArticleCommentEntity;
import com.ali.coffeehub.article.repository.ArticleCommentRepository;
import com.ali.coffeehub.article.service.dto.ArticleCommentDTO;
import com.ali.coffeehub.article.service.mapper.ArticleCommentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ali.coffeehub.article.domain.ArticleCommentEntity}.
 */
@Service
@Transactional
public class ArticleCommentService {

    private static final Logger LOG = LoggerFactory.getLogger(ArticleCommentService.class);

    private final ArticleCommentRepository articleCommentRepository;

    private final ArticleCommentMapper articleCommentMapper;

    public ArticleCommentService(ArticleCommentRepository articleCommentRepository, ArticleCommentMapper articleCommentMapper) {
        this.articleCommentRepository = articleCommentRepository;
        this.articleCommentMapper = articleCommentMapper;
    }

    /**
     * Save a articleComment.
     *
     * @param articleCommentDTO the entity to save.
     * @return the persisted entity.
     */
    public ArticleCommentDTO save(ArticleCommentDTO articleCommentDTO) {
        LOG.debug("Request to save ArticleComment : {}", articleCommentDTO);
        ArticleCommentEntity articleCommentEntity = articleCommentMapper.toEntity(articleCommentDTO);
        articleCommentEntity = articleCommentRepository.save(articleCommentEntity);
        return articleCommentMapper.toDto(articleCommentEntity);
    }

    /**
     * Update a articleComment.
     *
     * @param articleCommentDTO the entity to save.
     * @return the persisted entity.
     */
    public ArticleCommentDTO update(ArticleCommentDTO articleCommentDTO) {
        LOG.debug("Request to update ArticleComment : {}", articleCommentDTO);
        ArticleCommentEntity articleCommentEntity = articleCommentMapper.toEntity(articleCommentDTO);
        articleCommentEntity = articleCommentRepository.save(articleCommentEntity);
        return articleCommentMapper.toDto(articleCommentEntity);
    }

    /**
     * Partially update a articleComment.
     *
     * @param articleCommentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ArticleCommentDTO> partialUpdate(ArticleCommentDTO articleCommentDTO) {
        LOG.debug("Request to partially update ArticleComment : {}", articleCommentDTO);

        return articleCommentRepository
            .findById(articleCommentDTO.getId())
            .map(existingArticleComment -> {
                articleCommentMapper.partialUpdate(existingArticleComment, articleCommentDTO);

                return existingArticleComment;
            })
            .map(articleCommentRepository::save)
            .map(articleCommentMapper::toDto);
    }

    /**
     * Get all the articleComments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ArticleCommentDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ArticleComments");
        return articleCommentRepository.findAll(pageable).map(articleCommentMapper::toDto);
    }

    /**
     * Get one articleComment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ArticleCommentDTO> findOne(Long id) {
        LOG.debug("Request to get ArticleComment : {}", id);
        return articleCommentRepository.findById(id).map(articleCommentMapper::toDto);
    }

    /**
     * Delete the articleComment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ArticleComment : {}", id);
        articleCommentRepository.deleteById(id);
    }
}
