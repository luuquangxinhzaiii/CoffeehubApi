package com.ali.coffeehub.cafeteria.web.rest;

import com.ali.coffeehub.cafeteria.repository.CoffeeShopRepository;
import com.ali.coffeehub.cafeteria.service.CoffeeShopService;
import com.ali.coffeehub.cafeteria.service.dto.CoffeeShopDTO;
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
 * REST controller for managing {@link com.ali.coffeehub.cafeteria.domain.CoffeeShopEntity}.
 */
@RestController
@RequestMapping("/api/coffee-shops")
public class CoffeeShopResource {

    private static final Logger LOG = LoggerFactory.getLogger(CoffeeShopResource.class);

    private static final String ENTITY_NAME = "coffeeShop";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CoffeeShopService coffeeShopService;

    private final CoffeeShopRepository coffeeShopRepository;

    public CoffeeShopResource(CoffeeShopService coffeeShopService, CoffeeShopRepository coffeeShopRepository) {
        this.coffeeShopService = coffeeShopService;
        this.coffeeShopRepository = coffeeShopRepository;
    }

    /**
     * {@code POST  /coffee-shops} : Create a new coffeeShop.
     *
     * @param coffeeShopDTO the coffeeShopDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new coffeeShopDTO, or with status {@code 400 (Bad Request)} if the coffeeShop has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CoffeeShopDTO> createCoffeeShop(@Valid @RequestBody CoffeeShopDTO coffeeShopDTO) throws URISyntaxException {
        LOG.debug("REST request to save CoffeeShop : {}", coffeeShopDTO);
        if (coffeeShopDTO.getId() != null) {
            throw new BadRequestAlertException("A new coffeeShop cannot already have an ID", ENTITY_NAME, "idexists");
        }
        coffeeShopDTO = coffeeShopService.save(coffeeShopDTO);
        return ResponseEntity.created(new URI("/api/coffee-shops/" + coffeeShopDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, coffeeShopDTO.getId().toString()))
            .body(coffeeShopDTO);
    }

    /**
     * {@code PUT  /coffee-shops/:id} : Updates an existing coffeeShop.
     *
     * @param id the id of the coffeeShopDTO to save.
     * @param coffeeShopDTO the coffeeShopDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated coffeeShopDTO,
     * or with status {@code 400 (Bad Request)} if the coffeeShopDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the coffeeShopDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CoffeeShopDTO> updateCoffeeShop(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CoffeeShopDTO coffeeShopDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CoffeeShop : {}, {}", id, coffeeShopDTO);
        if (coffeeShopDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, coffeeShopDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!coffeeShopRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        coffeeShopDTO = coffeeShopService.update(coffeeShopDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, coffeeShopDTO.getId().toString()))
            .body(coffeeShopDTO);
    }

    /**
     * {@code PATCH  /coffee-shops/:id} : Partial updates given fields of an existing coffeeShop, field will ignore if it is null
     *
     * @param id the id of the coffeeShopDTO to save.
     * @param coffeeShopDTO the coffeeShopDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated coffeeShopDTO,
     * or with status {@code 400 (Bad Request)} if the coffeeShopDTO is not valid,
     * or with status {@code 404 (Not Found)} if the coffeeShopDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the coffeeShopDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CoffeeShopDTO> partialUpdateCoffeeShop(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CoffeeShopDTO coffeeShopDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CoffeeShop partially : {}, {}", id, coffeeShopDTO);
        if (coffeeShopDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, coffeeShopDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!coffeeShopRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CoffeeShopDTO> result = coffeeShopService.partialUpdate(coffeeShopDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, coffeeShopDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /coffee-shops} : get all the coffeeShops.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of coffeeShops in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CoffeeShopDTO>> getAllCoffeeShops(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of CoffeeShops");
        Page<CoffeeShopDTO> page = coffeeShopService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /coffee-shops/:id} : get the "id" coffeeShop.
     *
     * @param id the id of the coffeeShopDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the coffeeShopDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CoffeeShopDTO> getCoffeeShop(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CoffeeShop : {}", id);
        Optional<CoffeeShopDTO> coffeeShopDTO = coffeeShopService.findOne(id);
        return ResponseUtil.wrapOrNotFound(coffeeShopDTO);
    }

    /**
     * {@code DELETE  /coffee-shops/:id} : delete the "id" coffeeShop.
     *
     * @param id the id of the coffeeShopDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoffeeShop(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CoffeeShop : {}", id);
        coffeeShopService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
