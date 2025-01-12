package com.ali.coffeehub.roaster.service;

import com.ali.coffeehub.roaster.domain.RoasterEntity;
import com.ali.coffeehub.roaster.repository.RoasterRepository;
import com.ali.coffeehub.roaster.service.dto.RoasterDTO;
import com.ali.coffeehub.roaster.service.mapper.RoasterMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ali.coffeehub.roaster.domain.RoasterEntity}.
 */
@Service
@Transactional
public class RoasterService {

    private static final Logger LOG = LoggerFactory.getLogger(RoasterService.class);

    private final RoasterRepository roasterRepository;

    private final RoasterMapper roasterMapper;

    public RoasterService(RoasterRepository roasterRepository, RoasterMapper roasterMapper) {
        this.roasterRepository = roasterRepository;
        this.roasterMapper = roasterMapper;
    }

    /**
     * Save a roaster.
     *
     * @param roasterDTO the entity to save.
     * @return the persisted entity.
     */
    public RoasterDTO save(RoasterDTO roasterDTO) {
        LOG.debug("Request to save Roaster : {}", roasterDTO);
        RoasterEntity roasterEntity = roasterMapper.toEntity(roasterDTO);
        roasterEntity = roasterRepository.save(roasterEntity);
        return roasterMapper.toDto(roasterEntity);
    }

    /**
     * Update a roaster.
     *
     * @param roasterDTO the entity to save.
     * @return the persisted entity.
     */
    public RoasterDTO update(RoasterDTO roasterDTO) {
        LOG.debug("Request to update Roaster : {}", roasterDTO);
        RoasterEntity roasterEntity = roasterMapper.toEntity(roasterDTO);
        roasterEntity = roasterRepository.save(roasterEntity);
        return roasterMapper.toDto(roasterEntity);
    }

    /**
     * Partially update a roaster.
     *
     * @param roasterDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RoasterDTO> partialUpdate(RoasterDTO roasterDTO) {
        LOG.debug("Request to partially update Roaster : {}", roasterDTO);

        return roasterRepository
            .findById(roasterDTO.getId())
            .map(existingRoaster -> {
                roasterMapper.partialUpdate(existingRoaster, roasterDTO);

                return existingRoaster;
            })
            .map(roasterRepository::save)
            .map(roasterMapper::toDto);
    }

    /**
     * Get all the roasters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RoasterDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Roasters");
        return roasterRepository.findAll(pageable).map(roasterMapper::toDto);
    }

    /**
     * Get one roaster by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RoasterDTO> findOne(Long id) {
        LOG.debug("Request to get Roaster : {}", id);
        return roasterRepository.findById(id).map(roasterMapper::toDto);
    }

    /**
     * Delete the roaster by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Roaster : {}", id);
        roasterRepository.deleteById(id);
    }
}
