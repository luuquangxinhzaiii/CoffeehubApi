package com.ali.coffeehub.web.rest;

import com.ali.coffeehub.repository.EntityTagsRepository;
import com.ali.coffeehub.service.EntityTagsService;
import com.ali.coffeehub.service.dto.EntityTagsDTO;
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
 * REST controller for managing {@link com.ali.coffeehub.domain.EntityTagsEntity}.
 */
@RestController
@RequestMapping("/api/entity-tags")
public class EntityTagsResource {

    private static final Logger LOG = LoggerFactory.getLogger(EntityTagsResource.class);

    private static final String ENTITY_NAME = "entityTags";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EntityTagsService entityTagsService;

    private final EntityTagsRepository entityTagsRepository;

    public EntityTagsResource(EntityTagsService entityTagsService, EntityTagsRepository entityTagsRepository) {
        this.entityTagsService = entityTagsService;
        this.entityTagsRepository = entityTagsRepository;
    }

    /**
     * {@code POST  /entity-tags} : Create a new entityTags.
     *
     * @param entityTagsDTO the entityTagsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new entityTagsDTO, or with status {@code 400 (Bad Request)} if the entityTags has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EntityTagsDTO> createEntityTags(@Valid @RequestBody EntityTagsDTO entityTagsDTO) throws URISyntaxException {
        LOG.debug("REST request to save EntityTags : {}", entityTagsDTO);
        if (entityTagsDTO.getId() != null) {
            throw new BadRequestAlertException("A new entityTags cannot already have an ID", ENTITY_NAME, "idexists");
        }
        entityTagsDTO = entityTagsService.save(entityTagsDTO);
        return ResponseEntity.created(new URI("/api/entity-tags/" + entityTagsDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, entityTagsDTO.getId().toString()))
            .body(entityTagsDTO);
    }

    /**
     * {@code PUT  /entity-tags/:id} : Updates an existing entityTags.
     *
     * @param id the id of the entityTagsDTO to save.
     * @param entityTagsDTO the entityTagsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated entityTagsDTO,
     * or with status {@code 400 (Bad Request)} if the entityTagsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the entityTagsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EntityTagsDTO> updateEntityTags(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EntityTagsDTO entityTagsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update EntityTags : {}, {}", id, entityTagsDTO);
        if (entityTagsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, entityTagsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!entityTagsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        entityTagsDTO = entityTagsService.update(entityTagsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, entityTagsDTO.getId().toString()))
            .body(entityTagsDTO);
    }

    /**
     * {@code PATCH  /entity-tags/:id} : Partial updates given fields of an existing entityTags, field will ignore if it is null
     *
     * @param id the id of the entityTagsDTO to save.
     * @param entityTagsDTO the entityTagsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated entityTagsDTO,
     * or with status {@code 400 (Bad Request)} if the entityTagsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the entityTagsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the entityTagsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EntityTagsDTO> partialUpdateEntityTags(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EntityTagsDTO entityTagsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EntityTags partially : {}, {}", id, entityTagsDTO);
        if (entityTagsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, entityTagsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!entityTagsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EntityTagsDTO> result = entityTagsService.partialUpdate(entityTagsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, entityTagsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /entity-tags} : get all the entityTags.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of entityTags in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EntityTagsDTO>> getAllEntityTags(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of EntityTags");
        Page<EntityTagsDTO> page = entityTagsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /entity-tags/:id} : get the "id" entityTags.
     *
     * @param id the id of the entityTagsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the entityTagsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EntityTagsDTO> getEntityTags(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EntityTags : {}", id);
        Optional<EntityTagsDTO> entityTagsDTO = entityTagsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(entityTagsDTO);
    }

    /**
     * {@code DELETE  /entity-tags/:id} : delete the "id" entityTags.
     *
     * @param id the id of the entityTagsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntityTags(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EntityTags : {}", id);
        entityTagsService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
