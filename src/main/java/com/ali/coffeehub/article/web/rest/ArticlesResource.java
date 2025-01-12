package com.ali.coffeehub.article.web.rest;

import com.ali.coffeehub.article.repository.ArticlesRepository;
import com.ali.coffeehub.article.service.ArticlesService;
import com.ali.coffeehub.article.service.dto.ArticlesDTO;
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
 * REST controller for managing {@link com.ali.coffeehub.article.domain.ArticlesEntity}.
 */
@RestController
@RequestMapping("/api/articles")
public class ArticlesResource {

    private static final Logger LOG = LoggerFactory.getLogger(ArticlesResource.class);

    private static final String ENTITY_NAME = "articles";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArticlesService articlesService;

    private final ArticlesRepository articlesRepository;

    public ArticlesResource(ArticlesService articlesService, ArticlesRepository articlesRepository) {
        this.articlesService = articlesService;
        this.articlesRepository = articlesRepository;
    }

    /**
     * {@code POST  /articles} : Create a new articles.
     *
     * @param articlesDTO the articlesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new articlesDTO, or with status {@code 400 (Bad Request)} if the articles has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ArticlesDTO> createArticles(@Valid @RequestBody ArticlesDTO articlesDTO) throws URISyntaxException {
        LOG.debug("REST request to save Articles : {}", articlesDTO);
        if (articlesDTO.getId() != null) {
            throw new BadRequestAlertException("A new articles cannot already have an ID", ENTITY_NAME, "idexists");
        }
        articlesDTO = articlesService.save(articlesDTO);
        return ResponseEntity.created(new URI("/api/articles/" + articlesDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, articlesDTO.getId().toString()))
            .body(articlesDTO);
    }

    /**
     * {@code PUT  /articles/:id} : Updates an existing articles.
     *
     * @param id the id of the articlesDTO to save.
     * @param articlesDTO the articlesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated articlesDTO,
     * or with status {@code 400 (Bad Request)} if the articlesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the articlesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ArticlesDTO> updateArticles(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ArticlesDTO articlesDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Articles : {}, {}", id, articlesDTO);
        if (articlesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, articlesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!articlesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        articlesDTO = articlesService.update(articlesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, articlesDTO.getId().toString()))
            .body(articlesDTO);
    }

    /**
     * {@code PATCH  /articles/:id} : Partial updates given fields of an existing articles, field will ignore if it is null
     *
     * @param id the id of the articlesDTO to save.
     * @param articlesDTO the articlesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated articlesDTO,
     * or with status {@code 400 (Bad Request)} if the articlesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the articlesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the articlesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ArticlesDTO> partialUpdateArticles(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ArticlesDTO articlesDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Articles partially : {}, {}", id, articlesDTO);
        if (articlesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, articlesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!articlesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ArticlesDTO> result = articlesService.partialUpdate(articlesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, articlesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /articles} : get all the articles.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of articles in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ArticlesDTO>> getAllArticles(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Articles");
        Page<ArticlesDTO> page = articlesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /articles/:id} : get the "id" articles.
     *
     * @param id the id of the articlesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the articlesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ArticlesDTO> getArticles(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Articles : {}", id);
        Optional<ArticlesDTO> articlesDTO = articlesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(articlesDTO);
    }

    /**
     * {@code DELETE  /articles/:id} : delete the "id" articles.
     *
     * @param id the id of the articlesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticles(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Articles : {}", id);
        articlesService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
