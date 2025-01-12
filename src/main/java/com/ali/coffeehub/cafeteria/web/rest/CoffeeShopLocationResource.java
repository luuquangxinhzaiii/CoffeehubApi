package com.ali.coffeehub.cafeteria.web.rest;

import com.ali.coffeehub.cafeteria.repository.CoffeeShopLocationRepository;
import com.ali.coffeehub.cafeteria.service.CoffeeShopLocationService;
import com.ali.coffeehub.cafeteria.service.dto.CoffeeShopLocationDTO;
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
 * REST controller for managing {@link com.ali.coffeehub.cafeteria.domain.CoffeeShopLocationEntity}.
 */
@RestController
@RequestMapping("/api/coffee-shop-locations")
public class CoffeeShopLocationResource {

    private static final Logger LOG = LoggerFactory.getLogger(CoffeeShopLocationResource.class);

    private static final String ENTITY_NAME = "coffeeShopLocation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CoffeeShopLocationService coffeeShopLocationService;

    private final CoffeeShopLocationRepository coffeeShopLocationRepository;

    public CoffeeShopLocationResource(
        CoffeeShopLocationService coffeeShopLocationService,
        CoffeeShopLocationRepository coffeeShopLocationRepository
    ) {
        this.coffeeShopLocationService = coffeeShopLocationService;
        this.coffeeShopLocationRepository = coffeeShopLocationRepository;
    }

    /**
     * {@code POST  /coffee-shop-locations} : Create a new coffeeShopLocation.
     *
     * @param coffeeShopLocationDTO the coffeeShopLocationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new coffeeShopLocationDTO, or with status {@code 400 (Bad Request)} if the coffeeShopLocation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CoffeeShopLocationDTO> createCoffeeShopLocation(@Valid @RequestBody CoffeeShopLocationDTO coffeeShopLocationDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CoffeeShopLocation : {}", coffeeShopLocationDTO);
        if (coffeeShopLocationDTO.getId() != null) {
            throw new BadRequestAlertException("A new coffeeShopLocation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        coffeeShopLocationDTO = coffeeShopLocationService.save(coffeeShopLocationDTO);
        return ResponseEntity.created(new URI("/api/coffee-shop-locations/" + coffeeShopLocationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, coffeeShopLocationDTO.getId().toString()))
            .body(coffeeShopLocationDTO);
    }

    /**
     * {@code PUT  /coffee-shop-locations/:id} : Updates an existing coffeeShopLocation.
     *
     * @param id the id of the coffeeShopLocationDTO to save.
     * @param coffeeShopLocationDTO the coffeeShopLocationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated coffeeShopLocationDTO,
     * or with status {@code 400 (Bad Request)} if the coffeeShopLocationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the coffeeShopLocationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CoffeeShopLocationDTO> updateCoffeeShopLocation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CoffeeShopLocationDTO coffeeShopLocationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CoffeeShopLocation : {}, {}", id, coffeeShopLocationDTO);
        if (coffeeShopLocationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, coffeeShopLocationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!coffeeShopLocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        coffeeShopLocationDTO = coffeeShopLocationService.update(coffeeShopLocationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, coffeeShopLocationDTO.getId().toString()))
            .body(coffeeShopLocationDTO);
    }

    /**
     * {@code PATCH  /coffee-shop-locations/:id} : Partial updates given fields of an existing coffeeShopLocation, field will ignore if it is null
     *
     * @param id the id of the coffeeShopLocationDTO to save.
     * @param coffeeShopLocationDTO the coffeeShopLocationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated coffeeShopLocationDTO,
     * or with status {@code 400 (Bad Request)} if the coffeeShopLocationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the coffeeShopLocationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the coffeeShopLocationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CoffeeShopLocationDTO> partialUpdateCoffeeShopLocation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CoffeeShopLocationDTO coffeeShopLocationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CoffeeShopLocation partially : {}, {}", id, coffeeShopLocationDTO);
        if (coffeeShopLocationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, coffeeShopLocationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!coffeeShopLocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CoffeeShopLocationDTO> result = coffeeShopLocationService.partialUpdate(coffeeShopLocationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, coffeeShopLocationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /coffee-shop-locations} : get all the coffeeShopLocations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of coffeeShopLocations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CoffeeShopLocationDTO>> getAllCoffeeShopLocations(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of CoffeeShopLocations");
        Page<CoffeeShopLocationDTO> page = coffeeShopLocationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /coffee-shop-locations/:id} : get the "id" coffeeShopLocation.
     *
     * @param id the id of the coffeeShopLocationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the coffeeShopLocationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CoffeeShopLocationDTO> getCoffeeShopLocation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CoffeeShopLocation : {}", id);
        Optional<CoffeeShopLocationDTO> coffeeShopLocationDTO = coffeeShopLocationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(coffeeShopLocationDTO);
    }

    /**
     * {@code DELETE  /coffee-shop-locations/:id} : delete the "id" coffeeShopLocation.
     *
     * @param id the id of the coffeeShopLocationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoffeeShopLocation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CoffeeShopLocation : {}", id);
        coffeeShopLocationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
