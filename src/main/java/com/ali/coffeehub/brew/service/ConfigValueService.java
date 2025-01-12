package com.ali.coffeehub.brew.service;

import com.ali.coffeehub.brew.domain.ConfigValueEntity;
import com.ali.coffeehub.brew.repository.ConfigValueRepository;
import com.ali.coffeehub.brew.service.dto.ConfigValueDTO;
import com.ali.coffeehub.brew.service.mapper.ConfigValueMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ali.coffeehub.brew.domain.ConfigValueEntity}.
 */
@Service
@Transactional
public class ConfigValueService {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigValueService.class);

    private final ConfigValueRepository configValueRepository;

    private final ConfigValueMapper configValueMapper;

    public ConfigValueService(ConfigValueRepository configValueRepository, ConfigValueMapper configValueMapper) {
        this.configValueRepository = configValueRepository;
        this.configValueMapper = configValueMapper;
    }

    /**
     * Save a configValue.
     *
     * @param configValueDTO the entity to save.
     * @return the persisted entity.
     */
    public ConfigValueDTO save(ConfigValueDTO configValueDTO) {
        LOG.debug("Request to save ConfigValue : {}", configValueDTO);
        ConfigValueEntity configValueEntity = configValueMapper.toEntity(configValueDTO);
        configValueEntity = configValueRepository.save(configValueEntity);
        return configValueMapper.toDto(configValueEntity);
    }

    /**
     * Update a configValue.
     *
     * @param configValueDTO the entity to save.
     * @return the persisted entity.
     */
    public ConfigValueDTO update(ConfigValueDTO configValueDTO) {
        LOG.debug("Request to update ConfigValue : {}", configValueDTO);
        ConfigValueEntity configValueEntity = configValueMapper.toEntity(configValueDTO);
        configValueEntity = configValueRepository.save(configValueEntity);
        return configValueMapper.toDto(configValueEntity);
    }

    /**
     * Partially update a configValue.
     *
     * @param configValueDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ConfigValueDTO> partialUpdate(ConfigValueDTO configValueDTO) {
        LOG.debug("Request to partially update ConfigValue : {}", configValueDTO);

        return configValueRepository
            .findById(configValueDTO.getId())
            .map(existingConfigValue -> {
                configValueMapper.partialUpdate(existingConfigValue, configValueDTO);

                return existingConfigValue;
            })
            .map(configValueRepository::save)
            .map(configValueMapper::toDto);
    }

    /**
     * Get all the configValues.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ConfigValueDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ConfigValues");
        return configValueRepository.findAll(pageable).map(configValueMapper::toDto);
    }

    /**
     * Get one configValue by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ConfigValueDTO> findOne(Long id) {
        LOG.debug("Request to get ConfigValue : {}", id);
        return configValueRepository.findById(id).map(configValueMapper::toDto);
    }

    /**
     * Delete the configValue by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ConfigValue : {}", id);
        configValueRepository.deleteById(id);
    }
}
