package com.ali.coffeehub.service;

import com.ali.coffeehub.domain.SystemConfigEntity;
import com.ali.coffeehub.repository.SystemConfigRepository;
import com.ali.coffeehub.service.dto.SystemConfigDTO;
import com.ali.coffeehub.service.mapper.SystemConfigMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ali.coffeehub.domain.SystemConfigEntity}.
 */
@Service
@Transactional
public class SystemConfigService {

    private static final Logger LOG = LoggerFactory.getLogger(SystemConfigService.class);

    private final SystemConfigRepository systemConfigRepository;

    private final SystemConfigMapper systemConfigMapper;

    public SystemConfigService(SystemConfigRepository systemConfigRepository, SystemConfigMapper systemConfigMapper) {
        this.systemConfigRepository = systemConfigRepository;
        this.systemConfigMapper = systemConfigMapper;
    }

    /**
     * Save a systemConfig.
     *
     * @param systemConfigDTO the entity to save.
     * @return the persisted entity.
     */
    public SystemConfigDTO save(SystemConfigDTO systemConfigDTO) {
        LOG.debug("Request to save SystemConfig : {}", systemConfigDTO);
        SystemConfigEntity systemConfigEntity = systemConfigMapper.toEntity(systemConfigDTO);
        systemConfigEntity = systemConfigRepository.save(systemConfigEntity);
        return systemConfigMapper.toDto(systemConfigEntity);
    }

    /**
     * Update a systemConfig.
     *
     * @param systemConfigDTO the entity to save.
     * @return the persisted entity.
     */
    public SystemConfigDTO update(SystemConfigDTO systemConfigDTO) {
        LOG.debug("Request to update SystemConfig : {}", systemConfigDTO);
        SystemConfigEntity systemConfigEntity = systemConfigMapper.toEntity(systemConfigDTO);
        systemConfigEntity = systemConfigRepository.save(systemConfigEntity);
        return systemConfigMapper.toDto(systemConfigEntity);
    }

    /**
     * Partially update a systemConfig.
     *
     * @param systemConfigDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SystemConfigDTO> partialUpdate(SystemConfigDTO systemConfigDTO) {
        LOG.debug("Request to partially update SystemConfig : {}", systemConfigDTO);

        return systemConfigRepository
            .findById(systemConfigDTO.getId())
            .map(existingSystemConfig -> {
                systemConfigMapper.partialUpdate(existingSystemConfig, systemConfigDTO);

                return existingSystemConfig;
            })
            .map(systemConfigRepository::save)
            .map(systemConfigMapper::toDto);
    }

    /**
     * Get all the systemConfigs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SystemConfigDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all SystemConfigs");
        return systemConfigRepository.findAll(pageable).map(systemConfigMapper::toDto);
    }

    /**
     * Get one systemConfig by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SystemConfigDTO> findOne(Long id) {
        LOG.debug("Request to get SystemConfig : {}", id);
        return systemConfigRepository.findById(id).map(systemConfigMapper::toDto);
    }

    /**
     * Delete the systemConfig by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SystemConfig : {}", id);
        systemConfigRepository.deleteById(id);
    }
}
