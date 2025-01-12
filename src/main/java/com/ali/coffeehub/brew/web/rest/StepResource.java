package com.ali.coffeehub.brew.web.rest;

import com.ali.coffeehub.brew.repository.StepRepository;
import com.ali.coffeehub.brew.service.StepService;
import com.ali.coffeehub.brew.service.dto.StepDTO;
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
 * REST controller for managing {@link com.ali.coffeehub.brew.domain.StepEntity}.
 */
@RestController
@RequestMapping("/api/steps")
public class StepResource {

    private static final Logger LOG = LoggerFactory.getLogger(StepResource.class);

    private static final String ENTITY_NAME = "step";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StepService stepService;

    private final StepRepository stepRepository;

    public StepResource(StepService stepService, StepRepository stepRepository) {
        this.stepService = stepService;
        this.stepRepository = stepRepository;
    }

    /**
     * {@code POST  /steps} : Create a new step.
     *
     * @param stepDTO the stepDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new stepDTO, or with status {@code 400 (Bad Request)} if the step has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StepDTO> createStep(@Valid @RequestBody StepDTO stepDTO) throws URISyntaxException {
        LOG.debug("REST request to save Step : {}", stepDTO);
        if (stepDTO.getId() != null) {
            throw new BadRequestAlertException("A new step cannot already have an ID", ENTITY_NAME, "idexists");
        }
        stepDTO = stepService.save(stepDTO);
        return ResponseEntity.created(new URI("/api/steps/" + stepDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, stepDTO.getId().toString()))
            .body(stepDTO);
    }

    /**
     * {@code PUT  /steps/:id} : Updates an existing step.
     *
     * @param id the id of the stepDTO to save.
     * @param stepDTO the stepDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stepDTO,
     * or with status {@code 400 (Bad Request)} if the stepDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the stepDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StepDTO> updateStep(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StepDTO stepDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Step : {}, {}", id, stepDTO);
        if (stepDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stepDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stepRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        stepDTO = stepService.update(stepDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stepDTO.getId().toString()))
            .body(stepDTO);
    }

    /**
     * {@code PATCH  /steps/:id} : Partial updates given fields of an existing step, field will ignore if it is null
     *
     * @param id the id of the stepDTO to save.
     * @param stepDTO the stepDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stepDTO,
     * or with status {@code 400 (Bad Request)} if the stepDTO is not valid,
     * or with status {@code 404 (Not Found)} if the stepDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the stepDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StepDTO> partialUpdateStep(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StepDTO stepDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Step partially : {}, {}", id, stepDTO);
        if (stepDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stepDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stepRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StepDTO> result = stepService.partialUpdate(stepDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stepDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /steps} : get all the steps.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of steps in body.
     */
    @GetMapping("")
    public ResponseEntity<List<StepDTO>> getAllSteps(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Steps");
        Page<StepDTO> page = stepService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /steps/:id} : get the "id" step.
     *
     * @param id the id of the stepDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the stepDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StepDTO> getStep(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Step : {}", id);
        Optional<StepDTO> stepDTO = stepService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stepDTO);
    }

    /**
     * {@code DELETE  /steps/:id} : delete the "id" step.
     *
     * @param id the id of the stepDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStep(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Step : {}", id);
        stepService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
