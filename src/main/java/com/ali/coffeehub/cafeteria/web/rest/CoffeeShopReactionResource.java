package com.ali.coffeehub.cafeteria.web.rest;

import com.ali.coffeehub.cafeteria.repository.CoffeeShopReactionRepository;
import com.ali.coffeehub.cafeteria.service.CoffeeShopReactionService;
import com.ali.coffeehub.cafeteria.service.dto.CoffeeShopReactionDTO;
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
 * REST controller for managing {@link com.ali.coffeehub.cafeteria.domain.CoffeeShopReactionEntity}.
 */
@RestController
@RequestMapping("/api/coffee-shop-reactions")
public class CoffeeShopReactionResource {

    private static final Logger LOG = LoggerFactory.getLogger(CoffeeShopReactionResource.class);

    private static final String ENTITY_NAME = "coffeeShopReaction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CoffeeShopReactionService coffeeShopReactionService;

    private final CoffeeShopReactionRepository coffeeShopReactionRepository;

    public CoffeeShopReactionResource(
        CoffeeShopReactionService coffeeShopReactionService,
        CoffeeShopReactionRepository coffeeShopReactionRepository
    ) {
        this.coffeeShopReactionService = coffeeShopReactionService;
        this.coffeeShopReactionRepository = coffeeShopReactionRepository;
    }

    /**
     * {@code POST  /coffee-shop-reactions} : Create a new coffeeShopReaction.
     *
     * @param coffeeShopReactionDTO the coffeeShopReactionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new coffeeShopReactionDTO, or with status {@code 400 (Bad Request)} if the coffeeShopReaction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CoffeeShopReactionDTO> createCoffeeShopReaction(@Valid @RequestBody CoffeeShopReactionDTO coffeeShopReactionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CoffeeShopReaction : {}", coffeeShopReactionDTO);
        if (coffeeShopReactionDTO.getId() != null) {
            throw new BadRequestAlertException("A new coffeeShopReaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        coffeeShopReactionDTO = coffeeShopReactionService.save(coffeeShopReactionDTO);
        return ResponseEntity.created(new URI("/api/coffee-shop-reactions/" + coffeeShopReactionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, coffeeShopReactionDTO.getId().toString()))
            .body(coffeeShopReactionDTO);
    }

    /**
     * {@code PUT  /coffee-shop-reactions/:id} : Updates an existing coffeeShopReaction.
     *
     * @param id the id of the coffeeShopReactionDTO to save.
     * @param coffeeShopReactionDTO the coffeeShopReactionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated coffeeShopReactionDTO,
     * or with status {@code 400 (Bad Request)} if the coffeeShopReactionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the coffeeShopReactionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CoffeeShopReactionDTO> updateCoffeeShopReaction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CoffeeShopReactionDTO coffeeShopReactionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CoffeeShopReaction : {}, {}", id, coffeeShopReactionDTO);
        if (coffeeShopReactionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, coffeeShopReactionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!coffeeShopReactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        coffeeShopReactionDTO = coffeeShopReactionService.update(coffeeShopReactionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, coffeeShopReactionDTO.getId().toString()))
            .body(coffeeShopReactionDTO);
    }

    /**
     * {@code PATCH  /coffee-shop-reactions/:id} : Partial updates given fields of an existing coffeeShopReaction, field will ignore if it is null
     *
     * @param id the id of the coffeeShopReactionDTO to save.
     * @param coffeeShopReactionDTO the coffeeShopReactionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated coffeeShopReactionDTO,
     * or with status {@code 400 (Bad Request)} if the coffeeShopReactionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the coffeeShopReactionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the coffeeShopReactionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CoffeeShopReactionDTO> partialUpdateCoffeeShopReaction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CoffeeShopReactionDTO coffeeShopReactionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CoffeeShopReaction partially : {}, {}", id, coffeeShopReactionDTO);
        if (coffeeShopReactionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, coffeeShopReactionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!coffeeShopReactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CoffeeShopReactionDTO> result = coffeeShopReactionService.partialUpdate(coffeeShopReactionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, coffeeShopReactionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /coffee-shop-reactions} : get all the coffeeShopReactions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of coffeeShopReactions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CoffeeShopReactionDTO>> getAllCoffeeShopReactions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of CoffeeShopReactions");
        Page<CoffeeShopReactionDTO> page = coffeeShopReactionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /coffee-shop-reactions/:id} : get the "id" coffeeShopReaction.
     *
     * @param id the id of the coffeeShopReactionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the coffeeShopReactionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CoffeeShopReactionDTO> getCoffeeShopReaction(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CoffeeShopReaction : {}", id);
        Optional<CoffeeShopReactionDTO> coffeeShopReactionDTO = coffeeShopReactionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(coffeeShopReactionDTO);
    }

    /**
     * {@code DELETE  /coffee-shop-reactions/:id} : delete the "id" coffeeShopReaction.
     *
     * @param id the id of the coffeeShopReactionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoffeeShopReaction(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CoffeeShopReaction : {}", id);
        coffeeShopReactionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
