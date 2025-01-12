package com.ali.coffeehub.roaster.web.rest;

import com.ali.coffeehub.roaster.repository.RoasterSocialLinkRepository;
import com.ali.coffeehub.roaster.service.RoasterSocialLinkService;
import com.ali.coffeehub.roaster.service.dto.RoasterSocialLinkDTO;
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
 * REST controller for managing {@link com.ali.coffeehub.roaster.domain.RoasterSocialLinkEntity}.
 */
@RestController
@RequestMapping("/api/roaster-social-links")
public class RoasterSocialLinkResource {

    private static final Logger LOG = LoggerFactory.getLogger(RoasterSocialLinkResource.class);

    private static final String ENTITY_NAME = "roasterSocialLink";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RoasterSocialLinkService roasterSocialLinkService;

    private final RoasterSocialLinkRepository roasterSocialLinkRepository;

    public RoasterSocialLinkResource(
        RoasterSocialLinkService roasterSocialLinkService,
        RoasterSocialLinkRepository roasterSocialLinkRepository
    ) {
        this.roasterSocialLinkService = roasterSocialLinkService;
        this.roasterSocialLinkRepository = roasterSocialLinkRepository;
    }

    /**
     * {@code POST  /roaster-social-links} : Create a new roasterSocialLink.
     *
     * @param roasterSocialLinkDTO the roasterSocialLinkDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new roasterSocialLinkDTO, or with status {@code 400 (Bad Request)} if the roasterSocialLink has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RoasterSocialLinkDTO> createRoasterSocialLink(@Valid @RequestBody RoasterSocialLinkDTO roasterSocialLinkDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save RoasterSocialLink : {}", roasterSocialLinkDTO);
        if (roasterSocialLinkDTO.getId() != null) {
            throw new BadRequestAlertException("A new roasterSocialLink cannot already have an ID", ENTITY_NAME, "idexists");
        }
        roasterSocialLinkDTO = roasterSocialLinkService.save(roasterSocialLinkDTO);
        return ResponseEntity.created(new URI("/api/roaster-social-links/" + roasterSocialLinkDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, roasterSocialLinkDTO.getId().toString()))
            .body(roasterSocialLinkDTO);
    }

    /**
     * {@code PUT  /roaster-social-links/:id} : Updates an existing roasterSocialLink.
     *
     * @param id the id of the roasterSocialLinkDTO to save.
     * @param roasterSocialLinkDTO the roasterSocialLinkDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roasterSocialLinkDTO,
     * or with status {@code 400 (Bad Request)} if the roasterSocialLinkDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the roasterSocialLinkDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RoasterSocialLinkDTO> updateRoasterSocialLink(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RoasterSocialLinkDTO roasterSocialLinkDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update RoasterSocialLink : {}, {}", id, roasterSocialLinkDTO);
        if (roasterSocialLinkDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roasterSocialLinkDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roasterSocialLinkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        roasterSocialLinkDTO = roasterSocialLinkService.update(roasterSocialLinkDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roasterSocialLinkDTO.getId().toString()))
            .body(roasterSocialLinkDTO);
    }

    /**
     * {@code PATCH  /roaster-social-links/:id} : Partial updates given fields of an existing roasterSocialLink, field will ignore if it is null
     *
     * @param id the id of the roasterSocialLinkDTO to save.
     * @param roasterSocialLinkDTO the roasterSocialLinkDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roasterSocialLinkDTO,
     * or with status {@code 400 (Bad Request)} if the roasterSocialLinkDTO is not valid,
     * or with status {@code 404 (Not Found)} if the roasterSocialLinkDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the roasterSocialLinkDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RoasterSocialLinkDTO> partialUpdateRoasterSocialLink(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RoasterSocialLinkDTO roasterSocialLinkDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update RoasterSocialLink partially : {}, {}", id, roasterSocialLinkDTO);
        if (roasterSocialLinkDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roasterSocialLinkDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roasterSocialLinkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RoasterSocialLinkDTO> result = roasterSocialLinkService.partialUpdate(roasterSocialLinkDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roasterSocialLinkDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /roaster-social-links} : get all the roasterSocialLinks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of roasterSocialLinks in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RoasterSocialLinkDTO>> getAllRoasterSocialLinks(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of RoasterSocialLinks");
        Page<RoasterSocialLinkDTO> page = roasterSocialLinkService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /roaster-social-links/:id} : get the "id" roasterSocialLink.
     *
     * @param id the id of the roasterSocialLinkDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the roasterSocialLinkDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoasterSocialLinkDTO> getRoasterSocialLink(@PathVariable("id") Long id) {
        LOG.debug("REST request to get RoasterSocialLink : {}", id);
        Optional<RoasterSocialLinkDTO> roasterSocialLinkDTO = roasterSocialLinkService.findOne(id);
        return ResponseUtil.wrapOrNotFound(roasterSocialLinkDTO);
    }

    /**
     * {@code DELETE  /roaster-social-links/:id} : delete the "id" roasterSocialLink.
     *
     * @param id the id of the roasterSocialLinkDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoasterSocialLink(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete RoasterSocialLink : {}", id);
        roasterSocialLinkService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
