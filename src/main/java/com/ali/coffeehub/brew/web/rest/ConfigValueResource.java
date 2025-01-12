package com.ali.coffeehub.brew.web.rest;

import com.ali.coffeehub.brew.repository.ConfigValueRepository;
import com.ali.coffeehub.brew.service.ConfigValueService;
import com.ali.coffeehub.brew.service.dto.ConfigValueDTO;
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
 * REST controller for managing {@link com.ali.coffeehub.brew.domain.ConfigValueEntity}.
 */
@RestController
@RequestMapping("/api/config-values")
public class ConfigValueResource {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigValueResource.class);

    private static final String ENTITY_NAME = "configValue";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfigValueService configValueService;

    private final ConfigValueRepository configValueRepository;

    public ConfigValueResource(ConfigValueService configValueService, ConfigValueRepository configValueRepository) {
        this.configValueService = configValueService;
        this.configValueRepository = configValueRepository;
    }

    /**
     * {@code POST  /config-values} : Create a new configValue.
     *
     * @param configValueDTO the configValueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new configValueDTO, or with status {@code 400 (Bad Request)} if the configValue has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ConfigValueDTO> createConfigValue(@Valid @RequestBody ConfigValueDTO configValueDTO) throws URISyntaxException {
        LOG.debug("REST request to save ConfigValue : {}", configValueDTO);
        if (configValueDTO.getId() != null) {
            throw new BadRequestAlertException("A new configValue cannot already have an ID", ENTITY_NAME, "idexists");
        }
        configValueDTO = configValueService.save(configValueDTO);
        return ResponseEntity.created(new URI("/api/config-values/" + configValueDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, configValueDTO.getId().toString()))
            .body(configValueDTO);
    }

    /**
     * {@code PUT  /config-values/:id} : Updates an existing configValue.
     *
     * @param id the id of the configValueDTO to save.
     * @param configValueDTO the configValueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configValueDTO,
     * or with status {@code 400 (Bad Request)} if the configValueDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the configValueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ConfigValueDTO> updateConfigValue(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ConfigValueDTO configValueDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ConfigValue : {}, {}", id, configValueDTO);
        if (configValueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, configValueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!configValueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        configValueDTO = configValueService.update(configValueDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configValueDTO.getId().toString()))
            .body(configValueDTO);
    }

    /**
     * {@code PATCH  /config-values/:id} : Partial updates given fields of an existing configValue, field will ignore if it is null
     *
     * @param id the id of the configValueDTO to save.
     * @param configValueDTO the configValueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configValueDTO,
     * or with status {@code 400 (Bad Request)} if the configValueDTO is not valid,
     * or with status {@code 404 (Not Found)} if the configValueDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the configValueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ConfigValueDTO> partialUpdateConfigValue(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ConfigValueDTO configValueDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ConfigValue partially : {}, {}", id, configValueDTO);
        if (configValueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, configValueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!configValueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ConfigValueDTO> result = configValueService.partialUpdate(configValueDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configValueDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /config-values} : get all the configValues.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of configValues in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ConfigValueDTO>> getAllConfigValues(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of ConfigValues");
        Page<ConfigValueDTO> page = configValueService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /config-values/:id} : get the "id" configValue.
     *
     * @param id the id of the configValueDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the configValueDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ConfigValueDTO> getConfigValue(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ConfigValue : {}", id);
        Optional<ConfigValueDTO> configValueDTO = configValueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(configValueDTO);
    }

    /**
     * {@code DELETE  /config-values/:id} : delete the "id" configValue.
     *
     * @param id the id of the configValueDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConfigValue(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ConfigValue : {}", id);
        configValueService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
