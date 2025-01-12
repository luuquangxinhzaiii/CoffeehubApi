package com.ali.coffeehub.cafeteria.service;

import com.ali.coffeehub.cafeteria.domain.CoffeeShopLocationEntity;
import com.ali.coffeehub.cafeteria.repository.CoffeeShopLocationRepository;
import com.ali.coffeehub.cafeteria.service.dto.CoffeeShopLocationDTO;
import com.ali.coffeehub.cafeteria.service.mapper.CoffeeShopLocationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ali.coffeehub.cafeteria.domain.CoffeeShopLocationEntity}.
 */
@Service
@Transactional
public class CoffeeShopLocationService {

    private static final Logger LOG = LoggerFactory.getLogger(CoffeeShopLocationService.class);

    private final CoffeeShopLocationRepository coffeeShopLocationRepository;

    private final CoffeeShopLocationMapper coffeeShopLocationMapper;

    public CoffeeShopLocationService(
        CoffeeShopLocationRepository coffeeShopLocationRepository,
        CoffeeShopLocationMapper coffeeShopLocationMapper
    ) {
        this.coffeeShopLocationRepository = coffeeShopLocationRepository;
        this.coffeeShopLocationMapper = coffeeShopLocationMapper;
    }

    /**
     * Save a coffeeShopLocation.
     *
     * @param coffeeShopLocationDTO the entity to save.
     * @return the persisted entity.
     */
    public CoffeeShopLocationDTO save(CoffeeShopLocationDTO coffeeShopLocationDTO) {
        LOG.debug("Request to save CoffeeShopLocation : {}", coffeeShopLocationDTO);
        CoffeeShopLocationEntity coffeeShopLocationEntity = coffeeShopLocationMapper.toEntity(coffeeShopLocationDTO);
        coffeeShopLocationEntity = coffeeShopLocationRepository.save(coffeeShopLocationEntity);
        return coffeeShopLocationMapper.toDto(coffeeShopLocationEntity);
    }

    /**
     * Update a coffeeShopLocation.
     *
     * @param coffeeShopLocationDTO the entity to save.
     * @return the persisted entity.
     */
    public CoffeeShopLocationDTO update(CoffeeShopLocationDTO coffeeShopLocationDTO) {
        LOG.debug("Request to update CoffeeShopLocation : {}", coffeeShopLocationDTO);
        CoffeeShopLocationEntity coffeeShopLocationEntity = coffeeShopLocationMapper.toEntity(coffeeShopLocationDTO);
        coffeeShopLocationEntity = coffeeShopLocationRepository.save(coffeeShopLocationEntity);
        return coffeeShopLocationMapper.toDto(coffeeShopLocationEntity);
    }

    /**
     * Partially update a coffeeShopLocation.
     *
     * @param coffeeShopLocationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CoffeeShopLocationDTO> partialUpdate(CoffeeShopLocationDTO coffeeShopLocationDTO) {
        LOG.debug("Request to partially update CoffeeShopLocation : {}", coffeeShopLocationDTO);

        return coffeeShopLocationRepository
            .findById(coffeeShopLocationDTO.getId())
            .map(existingCoffeeShopLocation -> {
                coffeeShopLocationMapper.partialUpdate(existingCoffeeShopLocation, coffeeShopLocationDTO);

                return existingCoffeeShopLocation;
            })
            .map(coffeeShopLocationRepository::save)
            .map(coffeeShopLocationMapper::toDto);
    }

    /**
     * Get all the coffeeShopLocations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CoffeeShopLocationDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all CoffeeShopLocations");
        return coffeeShopLocationRepository.findAll(pageable).map(coffeeShopLocationMapper::toDto);
    }

    /**
     * Get one coffeeShopLocation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CoffeeShopLocationDTO> findOne(Long id) {
        LOG.debug("Request to get CoffeeShopLocation : {}", id);
        return coffeeShopLocationRepository.findById(id).map(coffeeShopLocationMapper::toDto);
    }

    /**
     * Delete the coffeeShopLocation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CoffeeShopLocation : {}", id);
        coffeeShopLocationRepository.deleteById(id);
    }
}
