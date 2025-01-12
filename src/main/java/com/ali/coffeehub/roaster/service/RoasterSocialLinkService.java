package com.ali.coffeehub.roaster.service;

import com.ali.coffeehub.roaster.domain.RoasterSocialLinkEntity;
import com.ali.coffeehub.roaster.repository.RoasterSocialLinkRepository;
import com.ali.coffeehub.roaster.service.dto.RoasterSocialLinkDTO;
import com.ali.coffeehub.roaster.service.mapper.RoasterSocialLinkMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ali.coffeehub.roaster.domain.RoasterSocialLinkEntity}.
 */
@Service
@Transactional
public class RoasterSocialLinkService {

    private static final Logger LOG = LoggerFactory.getLogger(RoasterSocialLinkService.class);

    private final RoasterSocialLinkRepository roasterSocialLinkRepository;

    private final RoasterSocialLinkMapper roasterSocialLinkMapper;

    public RoasterSocialLinkService(
        RoasterSocialLinkRepository roasterSocialLinkRepository,
        RoasterSocialLinkMapper roasterSocialLinkMapper
    ) {
        this.roasterSocialLinkRepository = roasterSocialLinkRepository;
        this.roasterSocialLinkMapper = roasterSocialLinkMapper;
    }

    /**
     * Save a roasterSocialLink.
     *
     * @param roasterSocialLinkDTO the entity to save.
     * @return the persisted entity.
     */
    public RoasterSocialLinkDTO save(RoasterSocialLinkDTO roasterSocialLinkDTO) {
        LOG.debug("Request to save RoasterSocialLink : {}", roasterSocialLinkDTO);
        RoasterSocialLinkEntity roasterSocialLinkEntity = roasterSocialLinkMapper.toEntity(roasterSocialLinkDTO);
        roasterSocialLinkEntity = roasterSocialLinkRepository.save(roasterSocialLinkEntity);
        return roasterSocialLinkMapper.toDto(roasterSocialLinkEntity);
    }

    /**
     * Update a roasterSocialLink.
     *
     * @param roasterSocialLinkDTO the entity to save.
     * @return the persisted entity.
     */
    public RoasterSocialLinkDTO update(RoasterSocialLinkDTO roasterSocialLinkDTO) {
        LOG.debug("Request to update RoasterSocialLink : {}", roasterSocialLinkDTO);
        RoasterSocialLinkEntity roasterSocialLinkEntity = roasterSocialLinkMapper.toEntity(roasterSocialLinkDTO);
        roasterSocialLinkEntity = roasterSocialLinkRepository.save(roasterSocialLinkEntity);
        return roasterSocialLinkMapper.toDto(roasterSocialLinkEntity);
    }

    /**
     * Partially update a roasterSocialLink.
     *
     * @param roasterSocialLinkDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RoasterSocialLinkDTO> partialUpdate(RoasterSocialLinkDTO roasterSocialLinkDTO) {
        LOG.debug("Request to partially update RoasterSocialLink : {}", roasterSocialLinkDTO);

        return roasterSocialLinkRepository
            .findById(roasterSocialLinkDTO.getId())
            .map(existingRoasterSocialLink -> {
                roasterSocialLinkMapper.partialUpdate(existingRoasterSocialLink, roasterSocialLinkDTO);

                return existingRoasterSocialLink;
            })
            .map(roasterSocialLinkRepository::save)
            .map(roasterSocialLinkMapper::toDto);
    }

    /**
     * Get all the roasterSocialLinks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RoasterSocialLinkDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all RoasterSocialLinks");
        return roasterSocialLinkRepository.findAll(pageable).map(roasterSocialLinkMapper::toDto);
    }

    /**
     * Get one roasterSocialLink by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RoasterSocialLinkDTO> findOne(Long id) {
        LOG.debug("Request to get RoasterSocialLink : {}", id);
        return roasterSocialLinkRepository.findById(id).map(roasterSocialLinkMapper::toDto);
    }

    /**
     * Delete the roasterSocialLink by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete RoasterSocialLink : {}", id);
        roasterSocialLinkRepository.deleteById(id);
    }
}
