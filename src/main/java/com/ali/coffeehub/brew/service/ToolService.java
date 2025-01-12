package com.ali.coffeehub.brew.service;

import com.ali.coffeehub.brew.domain.ToolEntity;
import com.ali.coffeehub.brew.repository.ToolRepository;
import com.ali.coffeehub.brew.service.dto.ToolDTO;
import com.ali.coffeehub.brew.service.mapper.ToolMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ali.coffeehub.brew.domain.ToolEntity}.
 */
@Service
@Transactional
public class ToolService {

    private static final Logger LOG = LoggerFactory.getLogger(ToolService.class);

    private final ToolRepository toolRepository;

    private final ToolMapper toolMapper;

    public ToolService(ToolRepository toolRepository, ToolMapper toolMapper) {
        this.toolRepository = toolRepository;
        this.toolMapper = toolMapper;
    }

    /**
     * Save a tool.
     *
     * @param toolDTO the entity to save.
     * @return the persisted entity.
     */
    public ToolDTO save(ToolDTO toolDTO) {
        LOG.debug("Request to save Tool : {}", toolDTO);
        ToolEntity toolEntity = toolMapper.toEntity(toolDTO);
        toolEntity = toolRepository.save(toolEntity);
        return toolMapper.toDto(toolEntity);
    }

    /**
     * Update a tool.
     *
     * @param toolDTO the entity to save.
     * @return the persisted entity.
     */
    public ToolDTO update(ToolDTO toolDTO) {
        LOG.debug("Request to update Tool : {}", toolDTO);
        ToolEntity toolEntity = toolMapper.toEntity(toolDTO);
        toolEntity = toolRepository.save(toolEntity);
        return toolMapper.toDto(toolEntity);
    }

    /**
     * Partially update a tool.
     *
     * @param toolDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ToolDTO> partialUpdate(ToolDTO toolDTO) {
        LOG.debug("Request to partially update Tool : {}", toolDTO);

        return toolRepository
            .findById(toolDTO.getId())
            .map(existingTool -> {
                toolMapper.partialUpdate(existingTool, toolDTO);

                return existingTool;
            })
            .map(toolRepository::save)
            .map(toolMapper::toDto);
    }

    /**
     * Get all the tools.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ToolDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Tools");
        return toolRepository.findAll(pageable).map(toolMapper::toDto);
    }

    /**
     * Get one tool by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ToolDTO> findOne(Long id) {
        LOG.debug("Request to get Tool : {}", id);
        return toolRepository.findById(id).map(toolMapper::toDto);
    }

    /**
     * Delete the tool by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Tool : {}", id);
        toolRepository.deleteById(id);
    }
}
