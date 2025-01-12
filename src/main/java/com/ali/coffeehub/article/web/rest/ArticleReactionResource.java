package com.ali.coffeehub.article.web.rest;

import com.ali.coffeehub.article.repository.ArticleReactionRepository;
import com.ali.coffeehub.article.service.ArticleReactionService;
import com.ali.coffeehub.article.service.dto.ArticleReactionDTO;
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
 * REST controller for managing {@link com.ali.coffeehub.article.domain.ArticleReactionEntity}.
 */
@RestController
@RequestMapping("/api/article-reactions")
public class ArticleReactionResource {

    private static final Logger LOG = LoggerFactory.getLogger(ArticleReactionResource.class);

    private static final String ENTITY_NAME = "articleReaction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArticleReactionService articleReactionService;

    private final ArticleReactionRepository articleReactionRepository;

    public ArticleReactionResource(ArticleReactionService articleReactionService, ArticleReactionRepository articleReactionRepository) {
        this.articleReactionService = articleReactionService;
        this.articleReactionRepository = articleReactionRepository;
    }

    /**
     * {@code POST  /article-reactions} : Create a new articleReaction.
     *
     * @param articleReactionDTO the articleReactionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new articleReactionDTO, or with status {@code 400 (Bad Request)} if the articleReaction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ArticleReactionDTO> createArticleReaction(@Valid @RequestBody ArticleReactionDTO articleReactionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ArticleReaction : {}", articleReactionDTO);
        if (articleReactionDTO.getId() != null) {
            throw new BadRequestAlertException("A new articleReaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        articleReactionDTO = articleReactionService.save(articleReactionDTO);
        return ResponseEntity.created(new URI("/api/article-reactions/" + articleReactionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, articleReactionDTO.getId().toString()))
            .body(articleReactionDTO);
    }

    /**
     * {@code PUT  /article-reactions/:id} : Updates an existing articleReaction.
     *
     * @param id the id of the articleReactionDTO to save.
     * @param articleReactionDTO the articleReactionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated articleReactionDTO,
     * or with status {@code 400 (Bad Request)} if the articleReactionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the articleReactionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ArticleReactionDTO> updateArticleReaction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ArticleReactionDTO articleReactionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ArticleReaction : {}, {}", id, articleReactionDTO);
        if (articleReactionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, articleReactionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!articleReactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        articleReactionDTO = articleReactionService.update(articleReactionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, articleReactionDTO.getId().toString()))
            .body(articleReactionDTO);
    }

    /**
     * {@code PATCH  /article-reactions/:id} : Partial updates given fields of an existing articleReaction, field will ignore if it is null
     *
     * @param id the id of the articleReactionDTO to save.
     * @param articleReactionDTO the articleReactionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated articleReactionDTO,
     * or with status {@code 400 (Bad Request)} if the articleReactionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the articleReactionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the articleReactionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ArticleReactionDTO> partialUpdateArticleReaction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ArticleReactionDTO articleReactionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ArticleReaction partially : {}, {}", id, articleReactionDTO);
        if (articleReactionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, articleReactionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!articleReactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ArticleReactionDTO> result = articleReactionService.partialUpdate(articleReactionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, articleReactionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /article-reactions} : get all the articleReactions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of articleReactions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ArticleReactionDTO>> getAllArticleReactions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of ArticleReactions");
        Page<ArticleReactionDTO> page = articleReactionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /article-reactions/:id} : get the "id" articleReaction.
     *
     * @param id the id of the articleReactionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the articleReactionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ArticleReactionDTO> getArticleReaction(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ArticleReaction : {}", id);
        Optional<ArticleReactionDTO> articleReactionDTO = articleReactionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(articleReactionDTO);
    }

    /**
     * {@code DELETE  /article-reactions/:id} : delete the "id" articleReaction.
     *
     * @param id the id of the articleReactionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticleReaction(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ArticleReaction : {}", id);
        articleReactionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
