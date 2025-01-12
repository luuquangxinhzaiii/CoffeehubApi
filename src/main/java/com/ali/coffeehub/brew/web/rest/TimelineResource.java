package com.ali.coffeehub.brew.web.rest;

import com.ali.coffeehub.brew.repository.TimelineRepository;
import com.ali.coffeehub.brew.service.TimelineService;
import com.ali.coffeehub.brew.service.dto.TimelineDTO;
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
 * REST controller for managing {@link com.ali.coffeehub.brew.domain.TimelineEntity}.
 */
@RestController
@RequestMapping("/api/timelines")
public class TimelineResource {

    private static final Logger LOG = LoggerFactory.getLogger(TimelineResource.class);

    private static final String ENTITY_NAME = "timeline";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TimelineService timelineService;

    private final TimelineRepository timelineRepository;

    public TimelineResource(TimelineService timelineService, TimelineRepository timelineRepository) {
        this.timelineService = timelineService;
        this.timelineRepository = timelineRepository;
    }

    /**
     * {@code POST  /timelines} : Create a new timeline.
     *
     * @param timelineDTO the timelineDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new timelineDTO, or with status {@code 400 (Bad Request)} if the timeline has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TimelineDTO> createTimeline(@Valid @RequestBody TimelineDTO timelineDTO) throws URISyntaxException {
        LOG.debug("REST request to save Timeline : {}", timelineDTO);
        if (timelineDTO.getId() != null) {
            throw new BadRequestAlertException("A new timeline cannot already have an ID", ENTITY_NAME, "idexists");
        }
        timelineDTO = timelineService.save(timelineDTO);
        return ResponseEntity.created(new URI("/api/timelines/" + timelineDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, timelineDTO.getId().toString()))
            .body(timelineDTO);
    }

    /**
     * {@code PUT  /timelines/:id} : Updates an existing timeline.
     *
     * @param id the id of the timelineDTO to save.
     * @param timelineDTO the timelineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated timelineDTO,
     * or with status {@code 400 (Bad Request)} if the timelineDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the timelineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TimelineDTO> updateTimeline(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TimelineDTO timelineDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Timeline : {}, {}", id, timelineDTO);
        if (timelineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, timelineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!timelineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        timelineDTO = timelineService.update(timelineDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, timelineDTO.getId().toString()))
            .body(timelineDTO);
    }

    /**
     * {@code PATCH  /timelines/:id} : Partial updates given fields of an existing timeline, field will ignore if it is null
     *
     * @param id the id of the timelineDTO to save.
     * @param timelineDTO the timelineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated timelineDTO,
     * or with status {@code 400 (Bad Request)} if the timelineDTO is not valid,
     * or with status {@code 404 (Not Found)} if the timelineDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the timelineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TimelineDTO> partialUpdateTimeline(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TimelineDTO timelineDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Timeline partially : {}, {}", id, timelineDTO);
        if (timelineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, timelineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!timelineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TimelineDTO> result = timelineService.partialUpdate(timelineDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, timelineDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /timelines} : get all the timelines.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of timelines in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TimelineDTO>> getAllTimelines(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Timelines");
        Page<TimelineDTO> page = timelineService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /timelines/:id} : get the "id" timeline.
     *
     * @param id the id of the timelineDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the timelineDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TimelineDTO> getTimeline(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Timeline : {}", id);
        Optional<TimelineDTO> timelineDTO = timelineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(timelineDTO);
    }

    /**
     * {@code DELETE  /timelines/:id} : delete the "id" timeline.
     *
     * @param id the id of the timelineDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimeline(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Timeline : {}", id);
        timelineService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
