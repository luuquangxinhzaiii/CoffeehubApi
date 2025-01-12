package com.ali.coffeehub.brew.service;

import com.ali.coffeehub.brew.domain.ConfigEntity;
import com.ali.coffeehub.brew.repository.ConfigRepository;
import com.ali.coffeehub.brew.service.dto.ConfigDTO;
import com.ali.coffeehub.brew.service.mapper.ConfigMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ali.coffeehub.brew.domain.ConfigEntity}.
 */
@Service
@Transactional
public class ConfigService {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigService.class);

    private final ConfigRepository configRepository;

    private final ConfigMapper configMapper;

    public ConfigService(ConfigRepository configRepository, ConfigMapper configMapper) {
        this.configRepository = configRepository;
        this.configMapper = configMapper;
    }

    /**
     * Save a config.
     *
     * @param configDTO the entity to save.
     * @return the persisted entity.
     */
    public ConfigDTO save(ConfigDTO configDTO) {
        LOG.debug("Request to save Config : {}", configDTO);
        ConfigEntity configEntity = configMapper.toEntity(configDTO);
        configEntity = configRepository.save(configEntity);
        return configMapper.toDto(configEntity);
    }

    /**
     * Update a config.
     *
     * @param configDTO the entity to save.
     * @return the persisted entity.
     */
    public ConfigDTO update(ConfigDTO configDTO) {
        LOG.debug("Request to update Config : {}", configDTO);
        ConfigEntity configEntity = configMapper.toEntity(configDTO);
        configEntity = configRepository.save(configEntity);
        return configMapper.toDto(configEntity);
    }

    /**
     * Partially update a config.
     *
     * @param configDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ConfigDTO> partialUpdate(ConfigDTO configDTO) {
        LOG.debug("Request to partially update Config : {}", configDTO);

        return configRepository
            .findById(configDTO.getId())
            .map(existingConfig -> {
                configMapper.partialUpdate(existingConfig, configDTO);

                return existingConfig;
            })
            .map(configRepository::save)
            .map(configMapper::toDto);
    }

    /**
     * Get all the configs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ConfigDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Configs");
        return configRepository.findAll(pageable).map(configMapper::toDto);
    }

    /**
     * Get one config by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ConfigDTO> findOne(Long id) {
        LOG.debug("Request to get Config : {}", id);
        return configRepository.findById(id).map(configMapper::toDto);
    }

    /**
     * Delete the config by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Config : {}", id);
        configRepository.deleteById(id);
    }
}
