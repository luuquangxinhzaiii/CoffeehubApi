package com.ali.coffeehub.article.web.rest;

import com.ali.coffeehub.article.repository.ArticleBodyRepository;
import com.ali.coffeehub.article.service.ArticleBodyService;
import com.ali.coffeehub.article.service.dto.ArticleBodyDTO;
import com.ali.coffeehub.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.ali.coffeehub.article.domain.ArticleBodyEntity}.
 */
@RestController
@RequestMapping("/api/article-bodies")
public class ArticleBodyResource {

    private static final Logger LOG = LoggerFactory.getLogger(ArticleBodyResource.class);

    private static final String ENTITY_NAME = "articleBody";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArticleBodyService articleBodyService;

    private final ArticleBodyRepository articleBodyRepository;

    public ArticleBodyResource(ArticleBodyService articleBodyService, ArticleBodyRepository articleBodyRepository) {
        this.articleBodyService = articleBodyService;
        this.articleBodyRepository = articleBodyRepository;
    }

    /**
     * {@code POST  /article-bodies} : Create a new articleBody.
     *
     * @param articleBodyDTO the articleBodyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new articleBodyDTO, or with status {@code 400 (Bad Request)} if the articleBody has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ArticleBodyDTO> createArticleBody(@RequestBody ArticleBodyDTO articleBodyDTO) throws URISyntaxException {
        LOG.debug("REST request to save ArticleBody : {}", articleBodyDTO);
        if (articleBodyDTO.getId() != null) {
            throw new BadRequestAlertException("A new articleBody cannot already have an ID", ENTITY_NAME, "idexists");
        }
        articleBodyDTO = articleBodyService.save(articleBodyDTO);
        return ResponseEntity.created(new URI("/api/article-bodies/" + articleBodyDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, articleBodyDTO.getId().toString()))
            .body(articleBodyDTO);
    }

    /**
     * {@code PUT  /article-bodies/:id} : Updates an existing articleBody.
     *
     * @param id the id of the articleBodyDTO to save.
     * @param articleBodyDTO the articleBodyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated articleBodyDTO,
     * or with status {@code 400 (Bad Request)} if the articleBodyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the articleBodyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ArticleBodyDTO> updateArticleBody(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ArticleBodyDTO articleBodyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ArticleBody : {}, {}", id, articleBodyDTO);
        if (articleBodyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, articleBodyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!articleBodyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        articleBodyDTO = articleBodyService.update(articleBodyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, articleBodyDTO.getId().toString()))
            .body(articleBodyDTO);
    }

    /**
     * {@code PATCH  /article-bodies/:id} : Partial updates given fields of an existing articleBody, field will ignore if it is null
     *
     * @param id the id of the articleBodyDTO to save.
     * @param articleBodyDTO the articleBodyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated articleBodyDTO,
     * or with status {@code 400 (Bad Request)} if the articleBodyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the articleBodyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the articleBodyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ArticleBodyDTO> partialUpdateArticleBody(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ArticleBodyDTO articleBodyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ArticleBody partially : {}, {}", id, articleBodyDTO);
        if (articleBodyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, articleBodyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!articleBodyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ArticleBodyDTO> result = articleBodyService.partialUpdate(articleBodyDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, articleBodyDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /article-bodies} : get all the articleBodies.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of articleBodies in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ArticleBodyDTO>> getAllArticleBodies(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of ArticleBodies");
        Page<ArticleBodyDTO> page = articleBodyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /article-bodies/:id} : get the "id" articleBody.
     *
     * @param id the id of the articleBodyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the articleBodyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ArticleBodyDTO> getArticleBody(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ArticleBody : {}", id);
        Optional<ArticleBodyDTO> articleBodyDTO = articleBodyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(articleBodyDTO);
    }

    /**
     * {@code DELETE  /article-bodies/:id} : delete the "id" articleBody.
     *
     * @param id the id of the articleBodyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticleBody(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ArticleBody : {}", id);
        articleBodyService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
