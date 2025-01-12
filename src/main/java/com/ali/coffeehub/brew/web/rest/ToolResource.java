package com.ali.coffeehub.brew.web.rest;

import com.ali.coffeehub.brew.repository.ToolRepository;
import com.ali.coffeehub.brew.service.ToolService;
import com.ali.coffeehub.brew.service.dto.ToolDTO;
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
 * REST controller for managing {@link com.ali.coffeehub.brew.domain.ToolEntity}.
 */
@RestController
@RequestMapping("/api/tools")
public class ToolResource {

    private static final Logger LOG = LoggerFactory.getLogger(ToolResource.class);

    private static final String ENTITY_NAME = "tool";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ToolService toolService;

    private final ToolRepository toolRepository;

    public ToolResource(ToolService toolService, ToolRepository toolRepository) {
        this.toolService = toolService;
        this.toolRepository = toolRepository;
    }

    /**
     * {@code POST  /tools} : Create a new tool.
     *
     * @param toolDTO the toolDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new toolDTO, or with status {@code 400 (Bad Request)} if the tool has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ToolDTO> createTool(@Valid @RequestBody ToolDTO toolDTO) throws URISyntaxException {
        LOG.debug("REST request to save Tool : {}", toolDTO);
        if (toolDTO.getId() != null) {
            throw new BadRequestAlertException("A new tool cannot already have an ID", ENTITY_NAME, "idexists");
        }
        toolDTO = toolService.save(toolDTO);
        return ResponseEntity.created(new URI("/api/tools/" + toolDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, toolDTO.getId().toString()))
            .body(toolDTO);
    }

    /**
     * {@code PUT  /tools/:id} : Updates an existing tool.
     *
     * @param id the id of the toolDTO to save.
     * @param toolDTO the toolDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated toolDTO,
     * or with status {@code 400 (Bad Request)} if the toolDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the toolDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ToolDTO> updateTool(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ToolDTO toolDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Tool : {}, {}", id, toolDTO);
        if (toolDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, toolDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!toolRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        toolDTO = toolService.update(toolDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, toolDTO.getId().toString()))
            .body(toolDTO);
    }

    /**
     * {@code PATCH  /tools/:id} : Partial updates given fields of an existing tool, field will ignore if it is null
     *
     * @param id the id of the toolDTO to save.
     * @param toolDTO the toolDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated toolDTO,
     * or with status {@code 400 (Bad Request)} if the toolDTO is not valid,
     * or with status {@code 404 (Not Found)} if the toolDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the toolDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ToolDTO> partialUpdateTool(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ToolDTO toolDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Tool partially : {}, {}", id, toolDTO);
        if (toolDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, toolDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!toolRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ToolDTO> result = toolService.partialUpdate(toolDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, toolDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tools} : get all the tools.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tools in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ToolDTO>> getAllTools(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Tools");
        Page<ToolDTO> page = toolService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tools/:id} : get the "id" tool.
     *
     * @param id the id of the toolDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the toolDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ToolDTO> getTool(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Tool : {}", id);
        Optional<ToolDTO> toolDTO = toolService.findOne(id);
        return ResponseUtil.wrapOrNotFound(toolDTO);
    }

    /**
     * {@code DELETE  /tools/:id} : delete the "id" tool.
     *
     * @param id the id of the toolDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTool(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Tool : {}", id);
        toolService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
