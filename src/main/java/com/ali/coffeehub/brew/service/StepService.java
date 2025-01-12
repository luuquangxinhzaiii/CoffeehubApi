package com.ali.coffeehub.brew.service;

import com.ali.coffeehub.brew.domain.StepEntity;
import com.ali.coffeehub.brew.repository.StepRepository;
import com.ali.coffeehub.brew.service.dto.StepDTO;
import com.ali.coffeehub.brew.service.mapper.StepMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ali.coffeehub.brew.domain.StepEntity}.
 */
@Service
@Transactional
public class StepService {

    private static final Logger LOG = LoggerFactory.getLogger(StepService.class);

    private final StepRepository stepRepository;

    private final StepMapper stepMapper;

    public StepService(StepRepository stepRepository, StepMapper stepMapper) {
        this.stepRepository = stepRepository;
        this.stepMapper = stepMapper;
    }

    /**
     * Save a step.
     *
     * @param stepDTO the entity to save.
     * @return the persisted entity.
     */
    public StepDTO save(StepDTO stepDTO) {
        LOG.debug("Request to save Step : {}", stepDTO);
        StepEntity stepEntity = stepMapper.toEntity(stepDTO);
        stepEntity = stepRepository.save(stepEntity);
        return stepMapper.toDto(stepEntity);
    }

    /**
     * Update a step.
     *
     * @param stepDTO the entity to save.
     * @return the persisted entity.
     */
    public StepDTO update(StepDTO stepDTO) {
        LOG.debug("Request to update Step : {}", stepDTO);
        StepEntity stepEntity = stepMapper.toEntity(stepDTO);
        stepEntity = stepRepository.save(stepEntity);
        return stepMapper.toDto(stepEntity);
    }

    /**
     * Partially update a step.
     *
     * @param stepDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<StepDTO> partialUpdate(StepDTO stepDTO) {
        LOG.debug("Request to partially update Step : {}", stepDTO);

        return stepRepository
            .findById(stepDTO.getId())
            .map(existingStep -> {
                stepMapper.partialUpdate(existingStep, stepDTO);

                return existingStep;
            })
            .map(stepRepository::save)
            .map(stepMapper::toDto);
    }

    /**
     * Get all the steps.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<StepDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Steps");
        return stepRepository.findAll(pageable).map(stepMapper::toDto);
    }

    /**
     * Get one step by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StepDTO> findOne(Long id) {
        LOG.debug("Request to get Step : {}", id);
        return stepRepository.findById(id).map(stepMapper::toDto);
    }

    /**
     * Delete the step by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Step : {}", id);
        stepRepository.deleteById(id);
    }
}
