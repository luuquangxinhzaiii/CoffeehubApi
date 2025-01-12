package com.ali.coffeehub.article.web.rest;

import com.ali.coffeehub.article.repository.ArticleCommentRepository;
import com.ali.coffeehub.article.service.ArticleCommentService;
import com.ali.coffeehub.article.service.dto.ArticleCommentDTO;
import com.ali.coffeehub.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ali.coffeehub.article.domain.ArticleCommentEntity}.
 */
@RestController
@RequestMapping("/api/article-comments")
public class ArticleCommentResource {

    private static final Logger LOG = LoggerFactory.getLogger(ArticleCommentResource.class);

    private static final String ENTITY_NAME = "articleComment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArticleCommentService articleCommentService;

    private final ArticleCommentRepository articleCommentRepository;

    public ArticleCommentResource(ArticleCommentService articleCommentService, ArticleCommentRepository articleCommentRepository) {
        this.articleCommentService = articleCommentService;
        this.articleCommentRepository = articleCommentRepository;
    }

    /**
     * {@code POST  /article-comments} : Create a new articleComment.
     *
     * @param articleCommentDTO the articleCommentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new articleCommentDTO, or with status {@code 400 (Bad Request)} if the articleComment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ArticleCommentDTO> createArticleComment(@Valid @RequestBody ArticleCommentDTO articleCommentDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ArticleComment : {}", articleCommentDTO);
        if (articleCommentDTO.getId() != null) {
            throw new BadRequestAlertException("A new articleComment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        articleCommentDTO = articleCommentService.save(articleCommentDTO);
        return ResponseEntity.created(new URI("/api/article-comments/" + articleCommentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, articleCommentDTO.getId().toString()))
            .body(articleCommentDTO);
    }

    /**
     * {@code PUT  /article-comments/:id} : Updates an existing articleComment.
     *
     * @param id the id of the articleCommentDTO to save.
     * @param articleCommentDTO the articleCommentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated articleCommentDTO,
     * or with status {@code 400 (Bad Request)} if the articleCommentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the articleCommentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ArticleCommentDTO> updateArticleComment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ArticleCommentDTO articleCommentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ArticleComment : {}, {}", id, articleCommentDTO);
        if (articleCommentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, articleCommentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!articleCommentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        articleCommentDTO = articleCommentService.update(articleCommentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, articleCommentDTO.getId().toString()))
            .body(articleCommentDTO);
    }

    /**
     * {@code PATCH  /article-comments/:id} : Partial updates given fields of an existing articleComment, field will ignore if it is null
     *
     * @param id the id of the articleCommentDTO to save.
     * @param articleCommentDTO the articleCommentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated articleCommentDTO,
     * or with status {@code 400 (Bad Request)} if the articleCommentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the articleCommentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the articleCommentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ArticleCommentDTO> partialUpdateArticleComment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ArticleCommentDTO articleCommentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ArticleComment partially : {}, {}", id, articleCommentDTO);
        if (articleCommentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, articleCommentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!articleCommentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ArticleCommentDTO> result = articleCommentService.partialUpdate(articleCommentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, articleCommentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /article-comments} : get all the articleComments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of articleComments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ArticleCommentDTO>> getAllArticleComments(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of ArticleComments");
        Page<ArticleCommentDTO> page = articleCommentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /article-comments/:id} : get the "id" articleComment.
     *
     * @param id the id of the articleCommentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the articleCommentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ArticleCommentDTO> getArticleComment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ArticleComment : {}", id);
        Optional<ArticleCommentDTO> articleCommentDTO = articleCommentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(articleCommentDTO);
    }

    /**
     * {@code DELETE  /article-comments/:id} : delete the "id" articleComment.
     *
     * @param id the id of the articleCommentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticleComment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ArticleComment : {}", id);
        articleCommentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
