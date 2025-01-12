package com.ali.coffeehub.brew.service;

import com.ali.coffeehub.brew.domain.BrewEntity;
import com.ali.coffeehub.brew.repository.BrewRepository;
import com.ali.coffeehub.brew.service.dto.BrewDTO;
import com.ali.coffeehub.brew.service.mapper.BrewMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ali.coffeehub.brew.domain.BrewEntity}.
 */
@Service
@Transactional
public class BrewService {

    private static final Logger LOG = LoggerFactory.getLogger(BrewService.class);

    private final BrewRepository brewRepository;

    private final BrewMapper brewMapper;

    public BrewService(BrewRepository brewRepository, BrewMapper brewMapper) {
        this.brewRepository = brewRepository;
        this.brewMapper = brewMapper;
    }

    /**
     * Save a brew.
     *
     * @param brewDTO the entity to save.
     * @return the persisted entity.
     */
    public BrewDTO save(BrewDTO brewDTO) {
        LOG.debug("Request to save Brew : {}", brewDTO);
        BrewEntity brewEntity = brewMapper.toEntity(brewDTO);
        brewEntity = brewRepository.save(brewEntity);
        return brewMapper.toDto(brewEntity);
    }

    /**
     * Update a brew.
     *
     * @param brewDTO the entity to save.
     * @return the persisted entity.
     */
    public BrewDTO update(BrewDTO brewDTO) {
        LOG.debug("Request to update Brew : {}", brewDTO);
        BrewEntity brewEntity = brewMapper.toEntity(brewDTO);
        brewEntity = brewRepository.save(brewEntity);
        return brewMapper.toDto(brewEntity);
    }

    /**
     * Partially update a brew.
     *
     * @param brewDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BrewDTO> partialUpdate(BrewDTO brewDTO) {
        LOG.debug("Request to partially update Brew : {}", brewDTO);

        return brewRepository
            .findById(brewDTO.getId())
            .map(existingBrew -> {
                brewMapper.partialUpdate(existingBrew, brewDTO);

                return existingBrew;
            })
            .map(brewRepository::save)
            .map(brewMapper::toDto);
    }

    /**
     * Get all the brews.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BrewDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Brews");
        return brewRepository.findAll(pageable).map(brewMapper::toDto);
    }

    /**
     * Get one brew by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BrewDTO> findOne(Long id) {
        LOG.debug("Request to get Brew : {}", id);
        return brewRepository.findById(id).map(brewMapper::toDto);
    }

    /**
     * Delete the brew by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Brew : {}", id);
        brewRepository.deleteById(id);
    }
}
