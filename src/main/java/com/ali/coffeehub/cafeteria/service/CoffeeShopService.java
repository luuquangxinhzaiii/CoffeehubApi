package com.ali.coffeehub.cafeteria.service;

import com.ali.coffeehub.cafeteria.domain.CoffeeShopEntity;
import com.ali.coffeehub.cafeteria.repository.CoffeeShopRepository;
import com.ali.coffeehub.cafeteria.service.dto.CoffeeShopDTO;
import com.ali.coffeehub.cafeteria.service.mapper.CoffeeShopMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ali.coffeehub.cafeteria.domain.CoffeeShopEntity}.
 */
@Service
@Transactional
public class CoffeeShopService {

    private static final Logger LOG = LoggerFactory.getLogger(CoffeeShopService.class);

    private final CoffeeShopRepository coffeeShopRepository;

    private final CoffeeShopMapper coffeeShopMapper;

    public CoffeeShopService(CoffeeShopRepository coffeeShopRepository, CoffeeShopMapper coffeeShopMapper) {
        this.coffeeShopRepository = coffeeShopRepository;
        this.coffeeShopMapper = coffeeShopMapper;
    }

    /**
     * Save a coffeeShop.
     *
     * @param coffeeShopDTO the entity to save.
     * @return the persisted entity.
     */
    public CoffeeShopDTO save(CoffeeShopDTO coffeeShopDTO) {
        LOG.debug("Request to save CoffeeShop : {}", coffeeShopDTO);
        CoffeeShopEntity coffeeShopEntity = coffeeShopMapper.toEntity(coffeeShopDTO);
        coffeeShopEntity = coffeeShopRepository.save(coffeeShopEntity);
        return coffeeShopMapper.toDto(coffeeShopEntity);
    }

    /**
     * Update a coffeeShop.
     *
     * @param coffeeShopDTO the entity to save.
     * @return the persisted entity.
     */
    public CoffeeShopDTO update(CoffeeShopDTO coffeeShopDTO) {
        LOG.debug("Request to update CoffeeShop : {}", coffeeShopDTO);
        CoffeeShopEntity coffeeShopEntity = coffeeShopMapper.toEntity(coffeeShopDTO);
        coffeeShopEntity = coffeeShopRepository.save(coffeeShopEntity);
        return coffeeShopMapper.toDto(coffeeShopEntity);
    }

    /**
     * Partially update a coffeeShop.
     *
     * @param coffeeShopDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CoffeeShopDTO> partialUpdate(CoffeeShopDTO coffeeShopDTO) {
        LOG.debug("Request to partially update CoffeeShop : {}", coffeeShopDTO);

        return coffeeShopRepository
            .findById(coffeeShopDTO.getId())
            .map(existingCoffeeShop -> {
                coffeeShopMapper.partialUpdate(existingCoffeeShop, coffeeShopDTO);

                return existingCoffeeShop;
            })
            .map(coffeeShopRepository::save)
            .map(coffeeShopMapper::toDto);
    }

    /**
     * Get all the coffeeShops.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CoffeeShopDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all CoffeeShops");
        return coffeeShopRepository.findAll(pageable).map(coffeeShopMapper::toDto);
    }

    /**
     * Get one coffeeShop by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CoffeeShopDTO> findOne(Long id) {
        LOG.debug("Request to get CoffeeShop : {}", id);
        return coffeeShopRepository.findById(id).map(coffeeShopMapper::toDto);
    }

    /**
     * Delete the coffeeShop by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CoffeeShop : {}", id);
        coffeeShopRepository.deleteById(id);
    }
}
