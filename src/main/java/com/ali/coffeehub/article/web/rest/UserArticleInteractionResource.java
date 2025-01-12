package com.ali.coffeehub.article.web.rest;

import com.ali.coffeehub.article.repository.UserArticleInteractionRepository;
import com.ali.coffeehub.article.service.UserArticleInteractionService;
import com.ali.coffeehub.article.service.dto.UserArticleInteractionDTO;
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
 * REST controller for managing {@link com.ali.coffeehub.article.domain.UserArticleInteractionEntity}.
 */
@RestController
@RequestMapping("/api/user-article-interactions")
public class UserArticleInteractionResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserArticleInteractionResource.class);

    private static final String ENTITY_NAME = "userArticleInteraction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserArticleInteractionService userArticleInteractionService;

    private final UserArticleInteractionRepository userArticleInteractionRepository;

    public UserArticleInteractionResource(
        UserArticleInteractionService userArticleInteractionService,
        UserArticleInteractionRepository userArticleInteractionRepository
    ) {
        this.userArticleInteractionService = userArticleInteractionService;
        this.userArticleInteractionRepository = userArticleInteractionRepository;
    }

    /**
     * {@code POST  /user-article-interactions} : Create a new userArticleInteraction.
     *
     * @param userArticleInteractionDTO the userArticleInteractionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userArticleInteractionDTO, or with status {@code 400 (Bad Request)} if the userArticleInteraction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserArticleInteractionDTO> createUserArticleInteraction(
        @Valid @RequestBody UserArticleInteractionDTO userArticleInteractionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save UserArticleInteraction : {}", userArticleInteractionDTO);
        if (userArticleInteractionDTO.getId() != null) {
            throw new BadRequestAlertException("A new userArticleInteraction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userArticleInteractionDTO = userArticleInteractionService.save(userArticleInteractionDTO);
        return ResponseEntity.created(new URI("/api/user-article-interactions/" + userArticleInteractionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, userArticleInteractionDTO.getId().toString()))
            .body(userArticleInteractionDTO);
    }

    /**
     * {@code PUT  /user-article-interactions/:id} : Updates an existing userArticleInteraction.
     *
     * @param id the id of the userArticleInteractionDTO to save.
     * @param userArticleInteractionDTO the userArticleInteractionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userArticleInteractionDTO,
     * or with status {@code 400 (Bad Request)} if the userArticleInteractionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userArticleInteractionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserArticleInteractionDTO> updateUserArticleInteraction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserArticleInteractionDTO userArticleInteractionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update UserArticleInteraction : {}, {}", id, userArticleInteractionDTO);
        if (userArticleInteractionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userArticleInteractionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userArticleInteractionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userArticleInteractionDTO = userArticleInteractionService.update(userArticleInteractionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userArticleInteractionDTO.getId().toString()))
            .body(userArticleInteractionDTO);
    }

    /**
     * {@code PATCH  /user-article-interactions/:id} : Partial updates given fields of an existing userArticleInteraction, field will ignore if it is null
     *
     * @param id the id of the userArticleInteractionDTO to save.
     * @param userArticleInteractionDTO the userArticleInteractionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userArticleInteractionDTO,
     * or with status {@code 400 (Bad Request)} if the userArticleInteractionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userArticleInteractionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userArticleInteractionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserArticleInteractionDTO> partialUpdateUserArticleInteraction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserArticleInteractionDTO userArticleInteractionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UserArticleInteraction partially : {}, {}", id, userArticleInteractionDTO);
        if (userArticleInteractionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userArticleInteractionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userArticleInteractionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserArticleInteractionDTO> result = userArticleInteractionService.partialUpdate(userArticleInteractionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userArticleInteractionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-article-interactions} : get all the userArticleInteractions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userArticleInteractions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UserArticleInteractionDTO>> getAllUserArticleInteractions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of UserArticleInteractions");
        Page<UserArticleInteractionDTO> page = userArticleInteractionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-article-interactions/:id} : get the "id" userArticleInteraction.
     *
     * @param id the id of the userArticleInteractionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userArticleInteractionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserArticleInteractionDTO> getUserArticleInteraction(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UserArticleInteraction : {}", id);
        Optional<UserArticleInteractionDTO> userArticleInteractionDTO = userArticleInteractionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userArticleInteractionDTO);
    }

    /**
     * {@code DELETE  /user-article-interactions/:id} : delete the "id" userArticleInteraction.
     *
     * @param id the id of the userArticleInteractionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserArticleInteraction(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UserArticleInteraction : {}", id);
        userArticleInteractionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
