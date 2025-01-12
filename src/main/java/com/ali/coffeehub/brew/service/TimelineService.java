package com.ali.coffeehub.brew.service;

import com.ali.coffeehub.brew.domain.TimelineEntity;
import com.ali.coffeehub.brew.repository.TimelineRepository;
import com.ali.coffeehub.brew.service.dto.TimelineDTO;
import com.ali.coffeehub.brew.service.mapper.TimelineMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ali.coffeehub.brew.domain.TimelineEntity}.
 */
@Service
@Transactional
public class TimelineService {

    private static final Logger LOG = LoggerFactory.getLogger(TimelineService.class);

    private final TimelineRepository timelineRepository;

    private final TimelineMapper timelineMapper;

    public TimelineService(TimelineRepository timelineRepository, TimelineMapper timelineMapper) {
        this.timelineRepository = timelineRepository;
        this.timelineMapper = timelineMapper;
    }

    /**
     * Save a timeline.
     *
     * @param timelineDTO the entity to save.
     * @return the persisted entity.
     */
    public TimelineDTO save(TimelineDTO timelineDTO) {
        LOG.debug("Request to save Timeline : {}", timelineDTO);
        TimelineEntity timelineEntity = timelineMapper.toEntity(timelineDTO);
        timelineEntity = timelineRepository.save(timelineEntity);
        return timelineMapper.toDto(timelineEntity);
    }

    /**
     * Update a timeline.
     *
     * @param timelineDTO the entity to save.
     * @return the persisted entity.
     */
    public TimelineDTO update(TimelineDTO timelineDTO) {
        LOG.debug("Request to update Timeline : {}", timelineDTO);
        TimelineEntity timelineEntity = timelineMapper.toEntity(timelineDTO);
        timelineEntity = timelineRepository.save(timelineEntity);
        return timelineMapper.toDto(timelineEntity);
    }

    /**
     * Partially update a timeline.
     *
     * @param timelineDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TimelineDTO> partialUpdate(TimelineDTO timelineDTO) {
        LOG.debug("Request to partially update Timeline : {}", timelineDTO);

        return timelineRepository
            .findById(timelineDTO.getId())
            .map(existingTimeline -> {
                timelineMapper.partialUpdate(existingTimeline, timelineDTO);

                return existingTimeline;
            })
            .map(timelineRepository::save)
            .map(timelineMapper::toDto);
    }

    /**
     * Get all the timelines.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TimelineDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Timelines");
        return timelineRepository.findAll(pageable).map(timelineMapper::toDto);
    }

    /**
     * Get one timeline by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TimelineDTO> findOne(Long id) {
        LOG.debug("Request to get Timeline : {}", id);
        return timelineRepository.findById(id).map(timelineMapper::toDto);
    }

    /**
     * Delete the timeline by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Timeline : {}", id);
        timelineRepository.deleteById(id);
    }
}
