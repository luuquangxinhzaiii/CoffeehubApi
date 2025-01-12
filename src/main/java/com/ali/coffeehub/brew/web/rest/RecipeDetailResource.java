package com.ali.coffeehub.brew.web.rest;

import com.ali.coffeehub.brew.repository.RecipeDetailRepository;
import com.ali.coffeehub.brew.service.RecipeDetailService;
import com.ali.coffeehub.brew.service.dto.RecipeDetailDTO;
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
 * REST controller for managing {@link com.ali.coffeehub.brew.domain.RecipeDetailEntity}.
 */
@RestController
@RequestMapping("/api/recipe-details")
public class RecipeDetailResource {

    private static final Logger LOG = LoggerFactory.getLogger(RecipeDetailResource.class);

    private static final String ENTITY_NAME = "recipeDetail";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RecipeDetailService recipeDetailService;

    private final RecipeDetailRepository recipeDetailRepository;

    public RecipeDetailResource(RecipeDetailService recipeDetailService, RecipeDetailRepository recipeDetailRepository) {
        this.recipeDetailService = recipeDetailService;
        this.recipeDetailRepository = recipeDetailRepository;
    }

    /**
     * {@code POST  /recipe-details} : Create a new recipeDetail.
     *
     * @param recipeDetailDTO the recipeDetailDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new recipeDetailDTO, or with status {@code 400 (Bad Request)} if the recipeDetail has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RecipeDetailDTO> createRecipeDetail(@Valid @RequestBody RecipeDetailDTO recipeDetailDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save RecipeDetail : {}", recipeDetailDTO);
        if (recipeDetailDTO.getId() != null) {
            throw new BadRequestAlertException("A new recipeDetail cannot already have an ID", ENTITY_NAME, "idexists");
        }
        recipeDetailDTO = recipeDetailService.save(recipeDetailDTO);
        return ResponseEntity.created(new URI("/api/recipe-details/" + recipeDetailDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, recipeDetailDTO.getId().toString()))
            .body(recipeDetailDTO);
    }

    /**
     * {@code PUT  /recipe-details/:id} : Updates an existing recipeDetail.
     *
     * @param id the id of the recipeDetailDTO to save.
     * @param recipeDetailDTO the recipeDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recipeDetailDTO,
     * or with status {@code 400 (Bad Request)} if the recipeDetailDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the recipeDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RecipeDetailDTO> updateRecipeDetail(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RecipeDetailDTO recipeDetailDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update RecipeDetail : {}, {}", id, recipeDetailDTO);
        if (recipeDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recipeDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recipeDetailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        recipeDetailDTO = recipeDetailService.update(recipeDetailDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recipeDetailDTO.getId().toString()))
            .body(recipeDetailDTO);
    }

    /**
     * {@code PATCH  /recipe-details/:id} : Partial updates given fields of an existing recipeDetail, field will ignore if it is null
     *
     * @param id the id of the recipeDetailDTO to save.
     * @param recipeDetailDTO the recipeDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recipeDetailDTO,
     * or with status {@code 400 (Bad Request)} if the recipeDetailDTO is not valid,
     * or with status {@code 404 (Not Found)} if the recipeDetailDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the recipeDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RecipeDetailDTO> partialUpdateRecipeDetail(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RecipeDetailDTO recipeDetailDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update RecipeDetail partially : {}, {}", id, recipeDetailDTO);
        if (recipeDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recipeDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recipeDetailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RecipeDetailDTO> result = recipeDetailService.partialUpdate(recipeDetailDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recipeDetailDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /recipe-details} : get all the recipeDetails.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of recipeDetails in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RecipeDetailDTO>> getAllRecipeDetails(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of RecipeDetails");
        Page<RecipeDetailDTO> page = recipeDetailService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /recipe-details/:id} : get the "id" recipeDetail.
     *
     * @param id the id of the recipeDetailDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recipeDetailDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RecipeDetailDTO> getRecipeDetail(@PathVariable("id") Long id) {
        LOG.debug("REST request to get RecipeDetail : {}", id);
        Optional<RecipeDetailDTO> recipeDetailDTO = recipeDetailService.findOne(id);
        return ResponseUtil.wrapOrNotFound(recipeDetailDTO);
    }

    /**
     * {@code DELETE  /recipe-details/:id} : delete the "id" recipeDetail.
     *
     * @param id the id of the recipeDetailDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipeDetail(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete RecipeDetail : {}", id);
        recipeDetailService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
