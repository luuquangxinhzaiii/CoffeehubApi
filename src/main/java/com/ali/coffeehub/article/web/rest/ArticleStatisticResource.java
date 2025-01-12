package com.ali.coffeehub.article.web.rest;

import com.ali.coffeehub.article.repository.ArticleStatisticRepository;
import com.ali.coffeehub.article.service.ArticleStatisticService;
import com.ali.coffeehub.article.service.dto.ArticleStatisticDTO;
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
 * REST controller for managing {@link com.ali.coffeehub.article.domain.ArticleStatisticEntity}.
 */
@RestController
@RequestMapping("/api/article-statistics")
public class ArticleStatisticResource {

    private static final Logger LOG = LoggerFactory.getLogger(ArticleStatisticResource.class);

    private static final String ENTITY_NAME = "articleStatistic";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArticleStatisticService articleStatisticService;

    private final ArticleStatisticRepository articleStatisticRepository;

    public ArticleStatisticResource(
        ArticleStatisticService articleStatisticService,
        ArticleStatisticRepository articleStatisticRepository
    ) {
        this.articleStatisticService = articleStatisticService;
        this.articleStatisticRepository = articleStatisticRepository;
    }

    /**
     * {@code POST  /article-statistics} : Create a new articleStatistic.
     *
     * @param articleStatisticDTO the articleStatisticDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new articleStatisticDTO, or with status {@code 400 (Bad Request)} if the articleStatistic has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ArticleStatisticDTO> createArticleStatistic(@Valid @RequestBody ArticleStatisticDTO articleStatisticDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ArticleStatistic : {}", articleStatisticDTO);
        if (articleStatisticDTO.getId() != null) {
            throw new BadRequestAlertException("A new articleStatistic cannot already have an ID", ENTITY_NAME, "idexists");
        }
        articleStatisticDTO = articleStatisticService.save(articleStatisticDTO);
        return ResponseEntity.created(new URI("/api/article-statistics/" + articleStatisticDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, articleStatisticDTO.getId().toString()))
            .body(articleStatisticDTO);
    }

    /**
     * {@code PUT  /article-statistics/:id} : Updates an existing articleStatistic.
     *
     * @param id the id of the articleStatisticDTO to save.
     * @param articleStatisticDTO the articleStatisticDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated articleStatisticDTO,
     * or with status {@code 400 (Bad Request)} if the articleStatisticDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the articleStatisticDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ArticleStatisticDTO> updateArticleStatistic(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ArticleStatisticDTO articleStatisticDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ArticleStatistic : {}, {}", id, articleStatisticDTO);
        if (articleStatisticDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, articleStatisticDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!articleStatisticRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        articleStatisticDTO = articleStatisticService.update(articleStatisticDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, articleStatisticDTO.getId().toString()))
            .body(articleStatisticDTO);
    }

    /**
     * {@code PATCH  /article-statistics/:id} : Partial updates given fields of an existing articleStatistic, field will ignore if it is null
     *
     * @param id the id of the articleStatisticDTO to save.
     * @param articleStatisticDTO the articleStatisticDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated articleStatisticDTO,
     * or with status {@code 400 (Bad Request)} if the articleStatisticDTO is not valid,
     * or with status {@code 404 (Not Found)} if the articleStatisticDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the articleStatisticDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ArticleStatisticDTO> partialUpdateArticleStatistic(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ArticleStatisticDTO articleStatisticDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ArticleStatistic partially : {}, {}", id, articleStatisticDTO);
        if (articleStatisticDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, articleStatisticDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!articleStatisticRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ArticleStatisticDTO> result = articleStatisticService.partialUpdate(articleStatisticDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, articleStatisticDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /article-statistics} : get all the articleStatistics.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of articleStatistics in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ArticleStatisticDTO>> getAllArticleStatistics(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of ArticleStatistics");
        Page<ArticleStatisticDTO> page = articleStatisticService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /article-statistics/:id} : get the "id" articleStatistic.
     *
     * @param id the id of the articleStatisticDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the articleStatisticDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ArticleStatisticDTO> getArticleStatistic(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ArticleStatistic : {}", id);
        Optional<ArticleStatisticDTO> articleStatisticDTO = articleStatisticService.findOne(id);
        return ResponseUtil.wrapOrNotFound(articleStatisticDTO);
    }

    /**
     * {@code DELETE  /article-statistics/:id} : delete the "id" articleStatistic.
     *
     * @param id the id of the articleStatisticDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticleStatistic(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ArticleStatistic : {}", id);
        articleStatisticService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
