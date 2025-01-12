package com.ali.coffeehub.brew.web.rest;

import com.ali.coffeehub.brew.repository.BrewRepository;
import com.ali.coffeehub.brew.service.BrewService;
import com.ali.coffeehub.brew.service.dto.BrewDTO;
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
 * REST controller for managing {@link com.ali.coffeehub.brew.domain.BrewEntity}.
 */
@RestController
@RequestMapping("/api/brews")
public class BrewResource {

    private static final Logger LOG = LoggerFactory.getLogger(BrewResource.class);

    private static final String ENTITY_NAME = "brew";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BrewService brewService;

    private final BrewRepository brewRepository;

    public BrewResource(BrewService brewService, BrewRepository brewRepository) {
        this.brewService = brewService;
        this.brewRepository = brewRepository;
    }

    /**
     * {@code POST  /brews} : Create a new brew.
     *
     * @param brewDTO the brewDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new brewDTO, or with status {@code 400 (Bad Request)} if the brew has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BrewDTO> createBrew(@Valid @RequestBody BrewDTO brewDTO) throws URISyntaxException {
        LOG.debug("REST request to save Brew : {}", brewDTO);
        if (brewDTO.getId() != null) {
            throw new BadRequestAlertException("A new brew cannot already have an ID", ENTITY_NAME, "idexists");
        }
        brewDTO = brewService.save(brewDTO);
        return ResponseEntity.created(new URI("/api/brews/" + brewDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, brewDTO.getId().toString()))
            .body(brewDTO);
    }

    /**
     * {@code PUT  /brews/:id} : Updates an existing brew.
     *
     * @param id the id of the brewDTO to save.
     * @param brewDTO the brewDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated brewDTO,
     * or with status {@code 400 (Bad Request)} if the brewDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the brewDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BrewDTO> updateBrew(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BrewDTO brewDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Brew : {}, {}", id, brewDTO);
        if (brewDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, brewDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!brewRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        brewDTO = brewService.update(brewDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, brewDTO.getId().toString()))
            .body(brewDTO);
    }

    /**
     * {@code PATCH  /brews/:id} : Partial updates given fields of an existing brew, field will ignore if it is null
     *
     * @param id the id of the brewDTO to save.
     * @param brewDTO the brewDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated brewDTO,
     * or with status {@code 400 (Bad Request)} if the brewDTO is not valid,
     * or with status {@code 404 (Not Found)} if the brewDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the brewDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BrewDTO> partialUpdateBrew(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BrewDTO brewDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Brew partially : {}, {}", id, brewDTO);
        if (brewDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, brewDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!brewRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BrewDTO> result = brewService.partialUpdate(brewDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, brewDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /brews} : get all the brews.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of brews in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BrewDTO>> getAllBrews(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Brews");
        Page<BrewDTO> page = brewService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /brews/:id} : get the "id" brew.
     *
     * @param id the id of the brewDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the brewDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BrewDTO> getBrew(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Brew : {}", id);
        Optional<BrewDTO> brewDTO = brewService.findOne(id);
        return ResponseUtil.wrapOrNotFound(brewDTO);
    }

    /**
     * {@code DELETE  /brews/:id} : delete the "id" brew.
     *
     * @param id the id of the brewDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrew(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Brew : {}", id);
        brewService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
