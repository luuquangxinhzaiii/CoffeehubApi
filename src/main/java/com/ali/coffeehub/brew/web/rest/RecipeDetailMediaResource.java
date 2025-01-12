package com.ali.coffeehub.brew.web.rest;

import com.ali.coffeehub.brew.repository.RecipeDetailMediaRepository;
import com.ali.coffeehub.brew.service.RecipeDetailMediaService;
import com.ali.coffeehub.brew.service.dto.RecipeDetailMediaDTO;
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
 * REST controller for managing {@link com.ali.coffeehub.brew.domain.RecipeDetailMediaEntity}.
 */
@RestController
@RequestMapping("/api/recipe-detail-medias")
public class RecipeDetailMediaResource {

    private static final Logger LOG = LoggerFactory.getLogger(RecipeDetailMediaResource.class);

    private static final String ENTITY_NAME = "recipeDetailMedia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RecipeDetailMediaService recipeDetailMediaService;

    private final RecipeDetailMediaRepository recipeDetailMediaRepository;

    public RecipeDetailMediaResource(
        RecipeDetailMediaService recipeDetailMediaService,
        RecipeDetailMediaRepository recipeDetailMediaRepository
    ) {
        this.recipeDetailMediaService = recipeDetailMediaService;
        this.recipeDetailMediaRepository = recipeDetailMediaRepository;
    }

    /**
     * {@code POST  /recipe-detail-medias} : Create a new recipeDetailMedia.
     *
     * @param recipeDetailMediaDTO the recipeDetailMediaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new recipeDetailMediaDTO, or with status {@code 400 (Bad Request)} if the recipeDetailMedia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RecipeDetailMediaDTO> createRecipeDetailMedia(@Valid @RequestBody RecipeDetailMediaDTO recipeDetailMediaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save RecipeDetailMedia : {}", recipeDetailMediaDTO);
        if (recipeDetailMediaDTO.getId() != null) {
            throw new BadRequestAlertException("A new recipeDetailMedia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        recipeDetailMediaDTO = recipeDetailMediaService.save(recipeDetailMediaDTO);
        return ResponseEntity.created(new URI("/api/recipe-detail-medias/" + recipeDetailMediaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, recipeDetailMediaDTO.getId().toString()))
            .body(recipeDetailMediaDTO);
    }

    /**
     * {@code PUT  /recipe-detail-medias/:id} : Updates an existing recipeDetailMedia.
     *
     * @param id the id of the recipeDetailMediaDTO to save.
     * @param recipeDetailMediaDTO the recipeDetailMediaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recipeDetailMediaDTO,
     * or with status {@code 400 (Bad Request)} if the recipeDetailMediaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the recipeDetailMediaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RecipeDetailMediaDTO> updateRecipeDetailMedia(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RecipeDetailMediaDTO recipeDetailMediaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update RecipeDetailMedia : {}, {}", id, recipeDetailMediaDTO);
        if (recipeDetailMediaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recipeDetailMediaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recipeDetailMediaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        recipeDetailMediaDTO = recipeDetailMediaService.update(recipeDetailMediaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recipeDetailMediaDTO.getId().toString()))
            .body(recipeDetailMediaDTO);
    }

    /**
     * {@code PATCH  /recipe-detail-medias/:id} : Partial updates given fields of an existing recipeDetailMedia, field will ignore if it is null
     *
     * @param id the id of the recipeDetailMediaDTO to save.
     * @param recipeDetailMediaDTO the recipeDetailMediaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recipeDetailMediaDTO,
     * or with status {@code 400 (Bad Request)} if the recipeDetailMediaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the recipeDetailMediaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the recipeDetailMediaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RecipeDetailMediaDTO> partialUpdateRecipeDetailMedia(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RecipeDetailMediaDTO recipeDetailMediaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update RecipeDetailMedia partially : {}, {}", id, recipeDetailMediaDTO);
        if (recipeDetailMediaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recipeDetailMediaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recipeDetailMediaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RecipeDetailMediaDTO> result = recipeDetailMediaService.partialUpdate(recipeDetailMediaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recipeDetailMediaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /recipe-detail-medias} : get all the recipeDetailMedias.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of recipeDetailMedias in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RecipeDetailMediaDTO>> getAllRecipeDetailMedias(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of RecipeDetailMedias");
        Page<RecipeDetailMediaDTO> page = recipeDetailMediaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /recipe-detail-medias/:id} : get the "id" recipeDetailMedia.
     *
     * @param id the id of the recipeDetailMediaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recipeDetailMediaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RecipeDetailMediaDTO> getRecipeDetailMedia(@PathVariable("id") Long id) {
        LOG.debug("REST request to get RecipeDetailMedia : {}", id);
        Optional<RecipeDetailMediaDTO> recipeDetailMediaDTO = recipeDetailMediaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(recipeDetailMediaDTO);
    }

    /**
     * {@code DELETE  /recipe-detail-medias/:id} : delete the "id" recipeDetailMedia.
     *
     * @param id the id of the recipeDetailMediaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipeDetailMedia(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete RecipeDetailMedia : {}", id);
        recipeDetailMediaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
