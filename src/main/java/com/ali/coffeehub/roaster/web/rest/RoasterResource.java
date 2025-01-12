package com.ali.coffeehub.roaster.web.rest;

import com.ali.coffeehub.roaster.repository.RoasterRepository;
import com.ali.coffeehub.roaster.service.RoasterService;
import com.ali.coffeehub.roaster.service.dto.RoasterDTO;
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
 * REST controller for managing {@link com.ali.coffeehub.roaster.domain.RoasterEntity}.
 */
@RestController
@RequestMapping("/api/roasters")
public class RoasterResource {

    private static final Logger LOG = LoggerFactory.getLogger(RoasterResource.class);

    private static final String ENTITY_NAME = "roaster";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RoasterService roasterService;

    private final RoasterRepository roasterRepository;

    public RoasterResource(RoasterService roasterService, RoasterRepository roasterRepository) {
        this.roasterService = roasterService;
        this.roasterRepository = roasterRepository;
    }

    /**
     * {@code POST  /roasters} : Create a new roaster.
     *
     * @param roasterDTO the roasterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new roasterDTO, or with status {@code 400 (Bad Request)} if the roaster has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RoasterDTO> createRoaster(@Valid @RequestBody RoasterDTO roasterDTO) throws URISyntaxException {
        LOG.debug("REST request to save Roaster : {}", roasterDTO);
        if (roasterDTO.getId() != null) {
            throw new BadRequestAlertException("A new roaster cannot already have an ID", ENTITY_NAME, "idexists");
        }
        roasterDTO = roasterService.save(roasterDTO);
        return ResponseEntity.created(new URI("/api/roasters/" + roasterDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, roasterDTO.getId().toString()))
            .body(roasterDTO);
    }

    /**
     * {@code PUT  /roasters/:id} : Updates an existing roaster.
     *
     * @param id the id of the roasterDTO to save.
     * @param roasterDTO the roasterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roasterDTO,
     * or with status {@code 400 (Bad Request)} if the roasterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the roasterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RoasterDTO> updateRoaster(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RoasterDTO roasterDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Roaster : {}, {}", id, roasterDTO);
        if (roasterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roasterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roasterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        roasterDTO = roasterService.update(roasterDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roasterDTO.getId().toString()))
            .body(roasterDTO);
    }

    /**
     * {@code PATCH  /roasters/:id} : Partial updates given fields of an existing roaster, field will ignore if it is null
     *
     * @param id the id of the roasterDTO to save.
     * @param roasterDTO the roasterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roasterDTO,
     * or with status {@code 400 (Bad Request)} if the roasterDTO is not valid,
     * or with status {@code 404 (Not Found)} if the roasterDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the roasterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RoasterDTO> partialUpdateRoaster(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RoasterDTO roasterDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Roaster partially : {}, {}", id, roasterDTO);
        if (roasterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roasterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roasterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RoasterDTO> result = roasterService.partialUpdate(roasterDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roasterDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /roasters} : get all the roasters.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of roasters in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RoasterDTO>> getAllRoasters(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Roasters");
        Page<RoasterDTO> page = roasterService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /roasters/:id} : get the "id" roaster.
     *
     * @param id the id of the roasterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the roasterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoasterDTO> getRoaster(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Roaster : {}", id);
        Optional<RoasterDTO> roasterDTO = roasterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(roasterDTO);
    }

    /**
     * {@code DELETE  /roasters/:id} : delete the "id" roaster.
     *
     * @param id the id of the roasterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoaster(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Roaster : {}", id);
        roasterService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
